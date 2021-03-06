package in.mitrevels.mitrevels.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.adapters.EventsAdapter;
import in.mitrevels.mitrevels.models.events.EventDetailsModel;
import in.mitrevels.mitrevels.models.events.EventModel;
import in.mitrevels.mitrevels.models.events.EventsListModel;
import in.mitrevels.mitrevels.models.events.ScheduleListModel;
import in.mitrevels.mitrevels.models.events.ScheduleModel;
import in.mitrevels.mitrevels.network.APIClient;
import in.mitrevels.mitrevels.utilities.HandyMan;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 8/12/16.
 */
public class DayFragment extends Fragment {

    private LinearLayout noConnectionLayout;
    private EventsAdapter adapter;
    private Realm mDatabase;
    private ProgressDialog dialog;
    private MenuItem searchItem;
    private List<EventModel> eventsList = new ArrayList<>();
    private List<EventModel> allEvents = new ArrayList<>();
    private List<String> categoriesList = new ArrayList<>();
    private List<String> venueList = new ArrayList<>();
    private List<String> eventTypeList = new ArrayList<>();
    private CoordinatorLayout rootLayout;
    private int filterStartHour = 12;
    private int filterStartMinute = 30;
    private int filterEndHour = 23;
    private int filterEndMinute = 59;
    private String filterCategory = "All";
    private String filterVenue = "All";
    private String filterEventType = "All";
    private static final int LOAD_EVENTS = 0;;
    private boolean eventsLoaded = false;
    private boolean scheduleLoaded = false;

    public DayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = Realm.getDefaultInstance();
        categoriesList.add("All");
        venueList.add("All");
        eventTypeList.add("All");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        rootLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_activity_coordinator_layout);

        noConnectionLayout = (LinearLayout)rootView.findViewById(R.id.day_no_connection_layout);

        TextView retry = (TextView)noConnectionLayout.findViewById(R.id.no_connection_retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandyMan.help().isInternetConnected(getActivity())){
                    prepareData(LOAD_EVENTS);
                    noConnectionLayout.setVisibility(View.GONE);
                }
                else{
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView eventsRecyclerView = (RecyclerView) rootView.findViewById(R.id.events_recycler_view);
        adapter = new EventsAdapter(getActivity(), eventsList, allEvents, mDatabase);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mDatabase.where(ScheduleModel.class).equalTo("day", getArguments().getInt("day", 1)+"").findAll().size() == 0){
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
        else{
            displayData();
        }

        return rootView;
    }

    public void prepareData(final int operation){
        APIClient.APIInterface apiInterface = APIClient.getAPIInterface();
        Call<EventsListModel> eventsCall = apiInterface.getEventsList();
        Call<ScheduleListModel> scheduleCall = apiInterface.getScheduleList();

        if (operation == LOAD_EVENTS){
            dialog = new ProgressDialog(getActivity());

            dialog.setMessage(getResources().getString(R.string.loading_events));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        eventsCall.enqueue(new Callback<EventsListModel>() {
            @Override
            public void onResponse(Call<EventsListModel> call, Response<EventsListModel> response) {
                if (response.body() != null && mDatabase != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(EventDetailsModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getEvents());
                    mDatabase.commitTransaction();
                }
                eventsLoaded = true;

                if (scheduleLoaded){
                    displayData();

                    if (operation == LOAD_EVENTS && dialog != null){
                        if (dialog.isShowing())
                            dialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventsListModel> call, Throwable t) {
                if (operation == LOAD_EVENTS && dialog != null){
                    if (dialog.isShowing())
                        dialog.hide();
                    noConnectionLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        scheduleCall.enqueue(new Callback<ScheduleListModel>() {
            @Override
            public void onResponse(Call<ScheduleListModel> call, Response<ScheduleListModel> response) {
                if (response.body() != null && mDatabase != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(ScheduleModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getData());
                    mDatabase.commitTransaction();
                }
                scheduleLoaded = true;

                if (eventsLoaded){
                    displayData();

                    if (operation == LOAD_EVENTS && dialog != null){
                        if (dialog.isShowing())
                            dialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleListModel> call, Throwable t){
                if (operation == LOAD_EVENTS && dialog != null){
                    if (dialog.isShowing())
                        dialog.hide();
                    noConnectionLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void displayData(){
        if (mDatabase == null) return;

        eventsList.clear();
        String[] sortCriteria = {"startTime", "catName", "eventName"};
        Sort[] sortOrder = {Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING};

        RealmResults<ScheduleModel> scheduleResult = mDatabase.where(ScheduleModel.class).equalTo("day", getArguments().getInt("day", 1)+"").findAllSorted(sortCriteria, sortOrder);

        for (ScheduleModel schedule : scheduleResult){
            EventDetailsModel eventDetails = mDatabase.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();

            if (eventDetails != null && !eventDetails.getCatName().isEmpty() && !categoriesList.contains(eventDetails.getCatName()))
                categoriesList.add(eventDetails.getCatName());

            if (eventDetails != null && !eventDetails.getType().isEmpty() && !eventTypeList.contains(eventDetails.getType()))
                eventTypeList.add(eventDetails.getType());

            String venue = "";
            for (int i = 0; i<schedule.getVenue().length(); i++){
                if (schedule.getVenue().charAt(i) == '-') continue;
                if (!Character.isDigit(schedule.getVenue().charAt(i))){
                    venue += schedule.getVenue().charAt(i);
                }
                else if (i > 0 && schedule.getVenue().charAt(i-1) != ' ' && schedule.getVenue().charAt(i-1) != '-'){
                    venue += schedule.getVenue().charAt(i);
                }
                else break;
            }

            if (!venue.isEmpty() && !venueList.contains(venue))
                venueList.add(venue);

            EventModel event = new EventModel(eventDetails, schedule);
            eventsList.add(event);
        }

        Collections.sort(eventsList, new Comparator<EventModel>() {
            @Override
            public int compare(EventModel o1, EventModel o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);

                try {
                    Date d1 = sdf.parse(o1.getStartTime());
                    Date d2 = sdf.parse(o2.getStartTime());

                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(d1);
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(d2);

                    long diff = c1.getTimeInMillis() - c2.getTimeInMillis();

                    if (diff>0) return 1;
                    else if (diff<0) return -1;
                    else{
                        int catDiff = o1.getCatName().compareTo(o2.getCatName());

                        if (catDiff>0) return 1;
                        else if (catDiff<0) return -1;
                        else {
                            int eventDiff = o1.getEventName().compareTo(o2.getEventName());

                            if (eventDiff>0) return 1;
                            else if (eventDiff<0) return -1;
                            else return 0;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        adapter.notifyDataSetChanged();

        allEvents.clear();
        allEvents.addAll(eventsList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_day, menu);

        searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);

        // Method call for removing left padding on search view
        trimChildMargins(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterResults(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.filterResults("");
                return false;
            }
        });
    }

    private static void trimChildMargins(@NonNull ViewGroup vg) {
        final int childCount = vg.getChildCount();

        // i+=2 is a workaround for removing left padding for only the search icon
        // i++ would remove left padding for the search text as well

        for (int i = 0; i < childCount; i+=2) {
            final View child = vg.getChildAt(i);

            if (child instanceof ViewGroup) {
                trimChildMargins((ViewGroup) child);
            }

            final ViewGroup.LayoutParams lp = child.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).leftMargin = 0;
            }
            child.setBackground(null);
            child.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }

    private void applyFilters(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
        Date startDate = null;
        Date endDate = null;
        List<EventModel> tempList = new ArrayList<>();
        tempList.addAll(allEvents);
        eventsList.clear();

        for (EventModel event : tempList) {
            try{
                if (!filterCategory.equals("All") && !filterCategory.toLowerCase().equals(event.getCatName().toLowerCase()))
                    continue;

                if (!filterVenue.equals("All") && !event.getVenue().toLowerCase().contains(filterVenue.toLowerCase()))
                    continue;

                if (!filterEventType.equals("All") && !filterEventType.toLowerCase().equals(event.getEventType().toLowerCase()))
                    continue;

                startDate = sdf.parse(event.getStartTime());
                endDate = sdf.parse(event.getEndTime());

                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                Calendar c3 = Calendar.getInstance();
                Calendar c4 = Calendar.getInstance();

                c1.setTime(startDate);
                c2.setTime(endDate);

                c3.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), filterStartHour, filterStartMinute, c1.get(Calendar.SECOND));
                c3.set(Calendar.MILLISECOND, c1.get(Calendar.MILLISECOND));
                c4.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE), filterEndHour, filterEndMinute, c2.get(Calendar.SECOND));
                c4.set(Calendar.MILLISECOND, c2.get(Calendar.MILLISECOND));

                if ((c1.getTimeInMillis() >= c3.getTimeInMillis()) && (c2.getTimeInMillis() <= c4.getTimeInMillis())){
                    eventsList.add(event);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if (eventsList.isEmpty()){
            if (rootLayout != null)
                Snackbar.make(rootLayout, "No events found!", Snackbar.LENGTH_SHORT).show();
        }
        else{
            if (rootLayout != null)
                Snackbar.make(rootLayout, "Filters applied for Day "+getArguments().getInt("day", 1)+"!", Snackbar.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }

    private void clearFilters(){
        filterStartHour = 12;
        filterStartMinute = 30;
        filterEndHour = 23;
        filterEndMinute = 59;
        filterCategory = "All";
        filterVenue = "All";
        filterEventType = "All";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_filter:{
                View view = View.inflate(getActivity(), R.layout.dialog_filter, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

                dialog.setContentView(view);

                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                LinearLayout clearFiltersLayout = (LinearLayout)view.findViewById(R.id.clear_filters_layout);
                LinearLayout startTimeLayout = (LinearLayout)view.findViewById(R.id.filter_start_time_layout);
                final TextView startTimeTextView = (TextView)view.findViewById(R.id.start_time_text_view);

                LinearLayout endTimeLayout = (LinearLayout)view.findViewById(R.id.filter_end_time_layout);
                final TextView endTimeTextView = (TextView)view.findViewById(R.id.end_time_text_view);

                TextView cancelTextView = (TextView)view.findViewById(R.id.filter_cancel_text_view);
                TextView applyTextView = (TextView)view.findViewById(R.id.filter_apply_text_view);

                final Spinner categorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
                final Spinner venueSpinner = (Spinner)view.findViewById(R.id.event_venue_spinner);
                final Spinner eventTypeSpinner = (Spinner)view.findViewById(R.id.event_type_spinner);

                ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_custom_spinner, categoriesList);
                categorySpinner.setAdapter(categorySpinnerAdapter);

                categorySpinner.setSelection(categoriesList.indexOf(filterCategory));

                ArrayAdapter<String> venueSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_custom_spinner, venueList);
                venueSpinner.setAdapter(venueSpinnerAdapter);

                venueSpinner.setSelection(venueList.indexOf(filterVenue));

                ArrayAdapter<String> eventTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_custom_spinner, eventTypeList);
                eventTypeSpinner.setAdapter(eventTypeSpinnerAdapter);

                eventTypeSpinner.setSelection(eventTypeList.indexOf(filterEventType));

                String sTime = "";
                String eTime = "";

                if (filterStartHour < 12)
                    sTime = filterStartHour+":"+(filterStartMinute < 10 ? "0"+filterStartMinute : filterStartMinute) +" AM";
                else if (filterStartHour == 12)
                    sTime = filterStartHour+":"+(filterStartMinute < 10 ? "0"+filterStartMinute : filterStartMinute)+" PM";
                else if (filterStartHour > 12)
                    sTime = (filterStartHour-12)+":"+(filterStartMinute < 10 ? "0"+filterStartMinute : filterStartMinute)+" PM";

                if (filterEndHour < 12)
                    eTime = filterEndHour+":"+(filterEndMinute < 10 ? "0"+filterEndMinute : filterEndMinute)+" AM";
                else if (filterEndHour == 12)
                    eTime = filterEndHour+":"+(filterEndMinute < 10 ? "0"+filterEndMinute : filterEndMinute)+" PM";
                else if (filterEndHour > 12)
                    eTime = (filterEndHour-12)+":"+(filterEndMinute < 10 ? "0"+filterEndMinute : filterEndMinute)+" PM";

                startTimeTextView.setText(sTime);
                endTimeTextView.setText(eTime);

                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

                applyTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyFilters();
                        dialog.hide();
                    }
                });

                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        filterCategory = categorySpinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

               venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       filterVenue = venueSpinner.getSelectedItem().toString();
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> adapterView) {
                   }
               });

                eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        filterEventType = eventTypeSpinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                startTimeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog tpDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String startTime = "";
                                filterStartHour = hourOfDay;
                                filterStartMinute = minute;
                                if (hourOfDay < 12)
                                    startTime = hourOfDay+":"+(minute < 10 ? "0"+minute : minute)+" AM";
                                else if (hourOfDay == 12)
                                    startTime = hourOfDay+":"+(minute < 10 ? "0"+minute : minute)+" PM";
                                else if (hourOfDay > 12)
                                    startTime = (hourOfDay-12)+":"+(minute < 10 ? "0"+minute : minute)+" PM";

                                startTimeTextView.setText(startTime);
                            }
                        }, filterStartHour, filterStartMinute, false);

                        tpDialog.show();
                    }
                });

                endTimeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog tpDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String endTime = "";
                                filterEndHour = hourOfDay;
                                filterEndMinute = minute;
                                if (hourOfDay < 12)
                                    endTime = hourOfDay+":"+(minute < 10 ? "0"+minute : minute)+" AM";
                                else if (hourOfDay == 12)
                                    endTime = hourOfDay+":"+(minute < 10 ? "0"+minute : minute)+" PM";
                                else if (hourOfDay > 12)
                                    endTime = (hourOfDay-12)+":"+(minute < 10 ? "0"+minute : minute)+" PM";

                                endTimeTextView.setText(endTime);
                            }
                        }, filterEndHour, filterEndMinute, false);

                        tpDialog.show();
                    }
                });

                clearFiltersLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventsList.clear();
                        eventsList.addAll(allEvents);
                        dialog.hide();
                        adapter.notifyDataSetChanged();
                        clearFilters();

                        if(rootLayout != null)
                            Snackbar.make(rootLayout, "Filters cleared!", Snackbar.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mDatabase = null;
        if (dialog != null && dialog.isShowing())
            dialog.hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}

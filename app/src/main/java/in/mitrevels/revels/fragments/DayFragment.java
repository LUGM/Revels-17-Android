package in.mitrevels.revels.fragments;

import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.EventsAdapter;
import in.mitrevels.revels.models.events.EventDetailsModel;
import in.mitrevels.revels.models.events.EventModel;
import in.mitrevels.revels.models.events.EventsListModel;
import in.mitrevels.revels.models.events.ScheduleListModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import in.mitrevels.revels.network.EventsAPIClient;
import in.mitrevels.revels.network.ScheduleAPIClient;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 8/12/16.
 */
public class DayFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventsAdapter adapter;
    private Realm mDatabase;
    private List<EventModel> eventsList = new ArrayList<>();
    private List<EventModel> allEvents = new ArrayList<>();
    private List<String> categoriesList = new ArrayList<>();
    private List<String> venueList = new ArrayList<>();
    private CoordinatorLayout rootLayout;
    private int filterStartHour = 12;
    private int filterStartMinute = 30;
    private int filterEndHour = 22;
    private int filterEndMinute = 30;
    private String filterCategory = "All";
    private String filterVenue = "All";

    public DayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = Realm.getDefaultInstance();
        categoriesList.add("All");
        venueList.add("All");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        rootLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_activity_coordinator_layout);

        eventsRecyclerView = (RecyclerView)rootView.findViewById(R.id.events_recycler_view);
        adapter = new EventsAdapter(getActivity(), eventsList, allEvents, mDatabase);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        prepareData();

        return rootView;
    }

    private void prepareData(){
        Call<EventsListModel> eventsCall = EventsAPIClient.getAPIInterface().getEventsList();
        Call<ScheduleListModel> scheduleCall = ScheduleAPIClient.getAPIInterface().getScheduleList();

        eventsCall.enqueue(new Callback<EventsListModel>() {
            @Override
            public void onResponse(Call<EventsListModel> call, Response<EventsListModel> response) {
                if (response.body() != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(EventDetailsModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getEvents());
                    mDatabase.commitTransaction();
                }
            }

            @Override
            public void onFailure(Call<EventsListModel> call, Throwable t) {

            }
        });
        scheduleCall.enqueue(new Callback<ScheduleListModel>() {
            @Override
            public void onResponse(Call<ScheduleListModel> call, Response<ScheduleListModel> response) {
                if (response.body() != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(ScheduleModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getData());
                    mDatabase.commitTransaction();
                }
                displayData();
            }

            @Override
            public void onFailure(Call<ScheduleListModel> call, Throwable t) {
                displayData();
            }
        });
    }

    private void displayData(){
        eventsList.clear();
        RealmResults<ScheduleModel> scheduleResult = mDatabase.where(ScheduleModel.class).equalTo("day", getArguments().getInt("day", 1)+"").findAll();

        for (ScheduleModel schedule : scheduleResult){
            EventDetailsModel eventDetails = mDatabase.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();

            if (eventDetails != null && !categoriesList.contains(eventDetails.getCatName()))
                categoriesList.add(eventDetails.getCatName());

            if (!venueList.contains(schedule.getVenue()))
                venueList.add(schedule.getVenue());

            EventModel event = new EventModel(eventDetails, schedule);
            eventsList.add(event);
        }
        adapter.notifyDataSetChanged();

        allEvents.clear();
        allEvents.addAll(eventsList);
    }

    List<EventModel> createList(){
        List<EventModel> eventsList = new ArrayList<>();

        for (int i=0; i<10; i++){
            EventModel em = new EventModel();
            em.setEventName("Battle of the Bands (Round 1)");
            em.setDate("08/03/2017");
            em.setStartTime("12:30 PM");
            em.setEndTime("7:30 PM");
            em.setCatName("Gaming");
            em.setContactName("Gaming Category Head");
            em.setContactNumber("9090909090");
            em.setVenue("NLH 402, 403, 404, 405");
            em.setEventMaxTeamNumber("4");
            em.setDescription("Pretty much everything that happens around us has some sound reason underlying it. The question is are you aware of them?This event pertains to testing your general awareness and how good you are at reasoning. Set aside all your engineering formulae and just stick to basics. Pretty much everything that happens around us has some sound reason underlying it. The question is are you aware of them?This event pertains to testing your general awareness and how good you are at reasoning. Set aside all your engineering formulae and just stick to basics. Pretty much everything that happens around us has some sound reason underlying it. The question is are you aware of them?This event pertains to testing your general awareness and how good you are at reasoning. Set aside all your engineering formulae and just stick to basics. Pretty much everything that happens around us has some sound reason underlying it. The question is are you aware of them?This event pertains to testing your general awareness and how good you are at reasoning. Set aside all your engineering formulae and just stick to basics.");
            eventsList.add(em);
        }

        return eventsList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_day, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
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
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        Date startDate = null;
        Date endDate = null;
        List<EventModel> tempList = new ArrayList<>();
        tempList.addAll(allEvents);
        eventsList.clear();

        try {
            for (EventModel event : tempList) {

                if (!filterCategory.equals("All") && !filterCategory.toLowerCase().equals(event.getCatName().toLowerCase()))
                    continue;

                if (!filterVenue.equals("All") && !filterVenue.toLowerCase().equals(event.getVenue().toLowerCase()))
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if (eventsList.isEmpty()){
            if (rootLayout != null)
                Snackbar.make(rootLayout, "No events found!", Snackbar.LENGTH_SHORT).show();
        }
        else{
            if (rootLayout != null)
                Snackbar.make(rootLayout, "Filters applied!", Snackbar.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                dialog.show();

                LinearLayout clearFiltersLayout = (LinearLayout)view.findViewById(R.id.clear_filters_layout);
                LinearLayout startTimeLayout = (LinearLayout)view.findViewById(R.id.filter_start_time_layout);
                final TextView startTimeTextView = (TextView)view.findViewById(R.id.start_time_text_view);

                LinearLayout endTimeLayout = (LinearLayout)view.findViewById(R.id.filter_end_time_layout);
                final TextView endTimeTextView = (TextView)view.findViewById(R.id.end_time_text_view);

                TextView cancelTextView = (TextView)view.findViewById(R.id.filter_cancel_text_view);
                TextView applyTextView = (TextView)view.findViewById(R.id.filter_apply_text_view);

                final Spinner categorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
                final Spinner venueSpinner = (Spinner)view.findViewById(R.id.event_venue_spinner);

                ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_custom_spinner, categoriesList);
                categorySpinner.setAdapter(categorySpinnerAdapter);

                categorySpinner.setSelection(categoriesList.indexOf(filterCategory));

                ArrayAdapter<String> venueSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_custom_spinner, venueList);
                venueSpinner.setAdapter(venueSpinnerAdapter);

                venueSpinner.setSelection(venueList.indexOf(filterVenue));

                String sTime = "";
                String eTime = "";

                if (filterStartHour < 12)
                    sTime = filterStartHour+":"+(filterStartMinute == 0 ? "00" : filterStartMinute) +" AM";
                else if (filterStartHour == 12)
                    sTime = filterStartHour+":"+(filterStartMinute == 0 ? "00" : filterStartMinute)+" PM";
                else if (filterStartHour > 12)
                    sTime = (filterStartHour-12)+":"+(filterStartMinute == 0 ? "00" : filterStartMinute)+" PM";

                if (filterEndHour < 12)
                    eTime = filterEndHour+":"+(filterEndMinute == 0 ? "00" : filterEndMinute)+" AM";
                else if (filterEndHour == 12)
                    eTime = filterEndHour+":"+(filterEndMinute == 0 ? "00" : filterEndMinute)+" PM";
                else if (filterEndHour > 12)
                    eTime = (filterEndHour-12)+":"+(filterEndMinute == 0 ? "00" : filterEndMinute)+" PM";

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
                                    startTime = hourOfDay+":"+(minute == 0 ? "00" : minute)+" AM";
                                else if (hourOfDay == 12)
                                    startTime = hourOfDay+":"+(minute == 0 ? "00" : minute)+" PM";
                                else if (hourOfDay > 12)
                                    startTime = (hourOfDay-12)+":"+(minute == 0 ? "00" : minute)+" PM";

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
                                    endTime = hourOfDay+":"+(minute == 0 ? "00" : minute)+" AM";
                                else if (hourOfDay == 12)
                                    endTime = hourOfDay+":"+(minute == 0 ? "00" : minute)+" PM";
                                else if (hourOfDay > 12)
                                    endTime = (hourOfDay-12)+":"+(minute == 0 ? "00" : minute)+" PM";

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
                        filterStartHour = 12;
                        filterStartMinute = 30;
                        filterEndHour = 22;
                        filterEndMinute = 30;
                        filterCategory = "All";
                        filterVenue = "All";
                        dialog.hide();
                        adapter.notifyDataSetChanged();

                        if(rootLayout != null)
                            Snackbar.make(rootLayout, "Filters cleared!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}

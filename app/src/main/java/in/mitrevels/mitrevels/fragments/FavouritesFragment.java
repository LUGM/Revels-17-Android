package in.mitrevels.mitrevels.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.adapters.FavouritesAdapter;
import in.mitrevels.mitrevels.models.FavouritesModel;
import in.mitrevels.mitrevels.receivers.NotificationReceiver;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by anurag on 12/12/16.
 */
public class FavouritesFragment extends Fragment {

    private TextView[] removeAll = new TextView[4];
    private TextView[] noEvents = new TextView[4];
    private Realm mRealm;
    private List<FavouritesModel> day1List;
    private List<FavouritesModel> day2List;
    private List<FavouritesModel> day3List;
    private List<FavouritesModel> day4List;
    private FavouritesAdapter[] dayAdapter = new FavouritesAdapter[4];

    public FavouritesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.favourites);
        setHasOptionsMenu(true);

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().findViewById(R.id.toolbar).setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
                getActivity().findViewById(R.id.main_app_bar_layout).setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        removeAll[0] = (TextView)rootView.findViewById(R.id.day_1_remove_all_text_view);
        removeAll[1] = (TextView)rootView.findViewById(R.id.day_2_remove_all_text_view);
        removeAll[2] = (TextView)rootView.findViewById(R.id.day_3_remove_all_text_view);
        removeAll[3] = (TextView)rootView.findViewById(R.id.day_4_remove_all_text_view);

        noEvents[0] = (TextView)rootView.findViewById(R.id.fav_day_1_no_events);
        noEvents[1] = (TextView)rootView.findViewById(R.id.fav_day_2_no_events);
        noEvents[2] = (TextView)rootView.findViewById(R.id.fav_day_3_no_events);
        noEvents[3] = (TextView)rootView.findViewById(R.id.fav_day_4_no_events);

        day1List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "1").findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING));
        favSort(day1List);

        day2List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "2").findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING));
        favSort(day2List);

        day3List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "3").findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING));
        favSort(day3List);

        day4List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "4").findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING));
        favSort(day4List);

        RecyclerView day1RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_1_recycler_view);
        dayAdapter[0] = new FavouritesAdapter(getActivity(), day1List, this, mRealm);
        day1RecyclerView.setAdapter(dayAdapter[0]);
        day1RecyclerView.setNestedScrollingEnabled(false);
        day1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day1List.isEmpty()) {
            removeAll[0].setVisibility(View.GONE);
            day1RecyclerView.setVisibility(View.GONE);
            noEvents[0].setVisibility(View.VISIBLE);
        }

        RecyclerView day2RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_2_recycler_view);
        dayAdapter[1] = new FavouritesAdapter(getActivity(), day2List, this, mRealm);
        day2RecyclerView.setAdapter(dayAdapter[1]);
        day2RecyclerView.setNestedScrollingEnabled(false);
        day2RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day2List.isEmpty()){
            removeAll[1].setVisibility(View.GONE);
            day2RecyclerView.setVisibility(View.GONE);
            noEvents[1].setVisibility(View.VISIBLE);
        }

        RecyclerView day3RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_3_recycler_view);
        dayAdapter[2] = new FavouritesAdapter(getActivity(), day3List, this, mRealm);
        day3RecyclerView.setAdapter(dayAdapter[2]);
        day3RecyclerView.setNestedScrollingEnabled(false);
        day3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day3List.isEmpty()){
            removeAll[2].setVisibility(View.GONE);
            day3RecyclerView.setVisibility(View.GONE);
            noEvents[2].setVisibility(View.VISIBLE);
        }

        RecyclerView day4RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_4_recycler_view);
        dayAdapter[3] = new FavouritesAdapter(getActivity(), day4List, this, mRealm);
        day4RecyclerView.setAdapter(dayAdapter[3]);
        day4RecyclerView.setNestedScrollingEnabled(false);
        day4RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        if (day4List.isEmpty()){
            removeAll[3].setVisibility(View.GONE);
            day4RecyclerView.setVisibility(View.GONE);
            noEvents[3].setVisibility(View.VISIBLE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               removeAllFavourites(view.getId(), false);
            }
        };

        removeAll[0].setOnClickListener(onClickListener);
        removeAll[1].setOnClickListener(onClickListener);
        removeAll[2].setOnClickListener(onClickListener);
        removeAll[3].setOnClickListener(onClickListener);

        return rootView;
    }
    
    private void favSort(List<FavouritesModel> favouritesList){
        Collections.sort(favouritesList, new Comparator<FavouritesModel>() {
            @Override
            public int compare(FavouritesModel o1, FavouritesModel o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

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

    }

    public void removeAllFavourites(String day){
        int id = 0;
        switch(day){
            case "1": id = R.id.day_1_remove_all_text_view;
                break;
            case "2": id = R.id.day_2_remove_all_text_view;
                break;
            case "3": id = R.id.day_3_remove_all_text_view;
                break;
            case "4": id = R.id.day_4_remove_all_text_view;
                break;
        }
        removeAllFavourites(id, false);
    }

    private void removeAllFavourites(final int id, final boolean removeAllEvents){

        final CoordinatorLayout mainLayout  = (CoordinatorLayout)getActivity().findViewById(R.id.main_activity_coordinator_layout);

        View view = View.inflate(getActivity(), R.layout.dialog_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(view);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView alertText = (TextView)view.findViewById(R.id.alert_text_view);
        TextView cancel = (TextView)view.findViewById(R.id.alert_cancel_text_view);
        TextView confirm = (TextView)view.findViewById(R.id.alert_ok_button);

        alertText.setText(R.string.favourite_alert_message);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                String day = "";

                try{
                    if (removeAllEvents || id == removeAll[0].getId()){
                        removeAll[0].setVisibility(View.GONE);
                        if (!removeAllEvents) Snackbar.make(mainLayout, "Day 1 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "1";
                        day1List.clear();
                        dayAdapter[0].notifyDataSetChanged();
                        noEvents[0].setVisibility(View.VISIBLE);
                    }
                    if (removeAllEvents || id == removeAll[1].getId()){
                        removeAll[1].setVisibility(View.GONE);
                        if (!removeAllEvents) Snackbar.make(mainLayout, "Day 2 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "2";
                        day2List.clear();
                        dayAdapter[1].notifyDataSetChanged();
                        noEvents[1].setVisibility(View.VISIBLE);
                    }
                    if (removeAllEvents || id == removeAll[2].getId()){
                        removeAll[2].setVisibility(View.GONE);
                        if (!removeAllEvents) Snackbar.make(mainLayout, "Day 3 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "3";
                        day3List.clear();
                        dayAdapter[2].notifyDataSetChanged();
                        noEvents[2].setVisibility(View.VISIBLE);
                    }
                    if (removeAllEvents || id == removeAll[3].getId()){
                        removeAll[3].setVisibility(View.GONE);
                        if (!removeAllEvents) Snackbar.make(mainLayout, "Day 4 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "4";
                        day4List.clear();
                        dayAdapter[3].notifyDataSetChanged();
                        noEvents[3].setVisibility(View.VISIBLE);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                mRealm.beginTransaction();
                if (!removeAllEvents){
                    removeNotification(mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", day).findAll()));
                    mRealm.where(FavouritesModel.class).equalTo("day", day).findAll().deleteAllFromRealm();
                }
                else{
                    removeNotification(mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).findAll()));
                    mRealm.where(FavouritesModel.class).findAll().deleteAllFromRealm();
                }
                mRealm.commitTransaction();

                if (removeAllEvents) Snackbar.make(mainLayout, "Favourites removed!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void removeNotification(List<FavouritesModel> removalList){

        for (FavouritesModel favourite : removalList){
            Intent intent = new Intent(getActivity(), NotificationReceiver.class);
            intent.putExtra("eventName", favourite.getEventName());
            intent.putExtra("startTime", favourite.getStartTime());
            intent.putExtra("eventVenue", favourite.getVenue());
            intent.putExtra("eventID", favourite.getId());

            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(favourite.getCatID()+favourite.getId()+"0"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(favourite.getCatID()+favourite.getId()+"1"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent1);
            alarmManager.cancel(pendingIntent2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_favourites, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favourites_remove_all:
                if ((day1List != null && !day1List.isEmpty()) || (day2List != null && !day2List.isEmpty()) || (day3List != null && !day3List.isEmpty()) || (day4List != null && !day4List.isEmpty()))
                    removeAllFavourites(0, true);
                else
                    Snackbar.make(getActivity().findViewById(R.id.main_activity_coordinator_layout), "Favourites empty!", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }
}

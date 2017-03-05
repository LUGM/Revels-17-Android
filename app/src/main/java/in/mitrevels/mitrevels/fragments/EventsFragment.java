package in.mitrevels.mitrevels.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.adapters.DayPagerAdapter;
import in.mitrevels.mitrevels.models.events.EventDetailsModel;
import in.mitrevels.mitrevels.models.events.EventsListModel;
import in.mitrevels.mitrevels.models.events.ScheduleListModel;
import in.mitrevels.mitrevels.models.events.ScheduleModel;
import in.mitrevels.mitrevels.network.APIClient;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsFragment extends Fragment {

    private Realm mDatabase;
    private boolean eventsLoaded = false;
    private boolean scheduleLoaded = false;

    public EventsFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.revels));
        setHasOptionsMenu(true);

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().findViewById(R.id.toolbar).setElevation(0);
                AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.main_app_bar_layout);
                appBarLayout.setElevation(0);
                appBarLayout.setTargetElevation(0);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        mDatabase = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        final TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.events_tab_layout);
        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.events_view_pager);

        DayPagerAdapter pagerAdapter = new DayPagerAdapter(getChildFragmentManager());
        final DayFragment[] fragments = new DayFragment[4];
        for (int i=0; i<4; i++){
            fragments[i] = new DayFragment();
        }
        pagerAdapter.add(fragments[0], "Day 1");
        pagerAdapter.add(fragments[1], "Day 2");
        pagerAdapter.add(fragments[2], "Day 3");
        pagerAdapter.add(fragments[3], "Day 4");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout.setupWithViewPager(viewPager);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());

        switch(formattedDate){

            case "08-03-2017":{
                viewPager.setCurrentItem(0);
                break;
            }
            case "09-03-2017":{
                viewPager.setCurrentItem(1);
                break;
            }
            case "10-03-2017":{
                viewPager.setCurrentItem(2);
                break;
            }
            case "11-03-2017":{
                viewPager.setCurrentItem(3);
                break;
            }
            default: viewPager.setCurrentItem(0);

        }

        if (!getActivity().getIntent().getBooleanExtra("dataLoaded", false)){
            try{
                APIClient.APIInterface apiInterface = APIClient.getAPIInterface();
                Call<EventsListModel> eventsCall = apiInterface.getEventsList();
                Call<ScheduleListModel> scheduleCall = apiInterface.getScheduleList();

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
                            fragments[0].displayData();
                            fragments[1].displayData();
                            fragments[2].displayData();
                            fragments[3].displayData();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventsListModel> call, Throwable t) {
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
                            fragments[0].displayData();
                            fragments[1].displayData();
                            fragments[2].displayData();
                            fragments[3].displayData();
                        }
                    }

                    @Override
                    public void onFailure(Call<ScheduleListModel> call, Throwable t){
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mDatabase = null;
    }
}

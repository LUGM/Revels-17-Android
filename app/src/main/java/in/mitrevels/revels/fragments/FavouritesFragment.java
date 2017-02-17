package in.mitrevels.revels.fragments;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.FavouritesAdapter;
import in.mitrevels.revels.models.FavouritesModel;
import in.mitrevels.revels.models.events.EventDetailsModel;
import in.mitrevels.revels.models.events.EventModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anurag on 12/12/16.
 */
public class FavouritesFragment extends Fragment {

    private TextView day1RemoveAll;
    private TextView day2RemoveAll;
    private TextView day3RemoveAll;
    private TextView day4RemoveAll;
    private TextView day1NoEvents;
    private TextView day2NoEvents;
    private TextView day3NoEvents;
    private TextView day4NoEvents;
    private Realm mRealm;
    private List<FavouritesModel> day1List;
    private List<FavouritesModel> day2List;
    private List<FavouritesModel> day3List;
    private List<FavouritesModel> day4List;
    private FavouritesAdapter day1Adapter;
    private FavouritesAdapter day2Adapter;
    private FavouritesAdapter day3Adapter;
    private FavouritesAdapter day4Adapter;

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

        day1RemoveAll = (TextView)rootView.findViewById(R.id.day_1_remove_all_text_view);
        day2RemoveAll = (TextView)rootView.findViewById(R.id.day_2_remove_all_text_view);
        day3RemoveAll = (TextView)rootView.findViewById(R.id.day_3_remove_all_text_view);
        day4RemoveAll = (TextView)rootView.findViewById(R.id.day_4_remove_all_text_view);

        day1NoEvents = (TextView)rootView.findViewById(R.id.fav_day_1_no_events);
        day2NoEvents = (TextView)rootView.findViewById(R.id.fav_day_2_no_events);
        day3NoEvents = (TextView)rootView.findViewById(R.id.fav_day_3_no_events);
        day4NoEvents = (TextView)rootView.findViewById(R.id.fav_day_4_no_events);

        day1List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "1").findAll());
        day2List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "2").findAll());
        day3List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "3").findAll());
        day4List = mRealm.copyFromRealm(mRealm.where(FavouritesModel.class).equalTo("day", "4").findAll());

        RecyclerView day1RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_1_recycler_view);
        day1Adapter = new FavouritesAdapter(getActivity(), day1List, this, mRealm);
        day1RecyclerView.setAdapter(day1Adapter);
        day1RecyclerView.setNestedScrollingEnabled(false);
        day1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day1List.isEmpty()) {
            day1RemoveAll.setVisibility(View.GONE);
            day1RecyclerView.setVisibility(View.GONE);
            day1NoEvents.setVisibility(View.VISIBLE);
        }

        RecyclerView day2RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_2_recycler_view);
        day2Adapter = new FavouritesAdapter(getActivity(), day2List, this, mRealm);
        day2RecyclerView.setAdapter(day2Adapter);
        day2RecyclerView.setNestedScrollingEnabled(false);
        day2RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day2List.isEmpty()){
            day2RemoveAll.setVisibility(View.GONE);
            day2RecyclerView.setVisibility(View.GONE);
            day2NoEvents.setVisibility(View.VISIBLE);
        }

        RecyclerView day3RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_3_recycler_view);
        day3Adapter = new FavouritesAdapter(getActivity(), day3List, this, mRealm);
        day3RecyclerView.setAdapter(day3Adapter);
        day3RecyclerView.setNestedScrollingEnabled(false);
        day3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (day3List.isEmpty()){
            day3RemoveAll.setVisibility(View.GONE);
            day3RecyclerView.setVisibility(View.GONE);
            day3NoEvents.setVisibility(View.VISIBLE);
        }

        RecyclerView day4RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_4_recycler_view);
        day4Adapter = new FavouritesAdapter(getActivity(), day4List, this, mRealm);
        day4RecyclerView.setAdapter(day4Adapter);
        day4RecyclerView.setNestedScrollingEnabled(false);
        day4RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        if (day4List.isEmpty()){
            day4RemoveAll.setVisibility(View.GONE);
            day4RecyclerView.setVisibility(View.GONE);
            day4NoEvents.setVisibility(View.VISIBLE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               removeAllFavourites(view.getId(), false);
            }
        };

        day1RemoveAll.setOnClickListener(onClickListener);
        day2RemoveAll.setOnClickListener(onClickListener);
        day3RemoveAll.setOnClickListener(onClickListener);
        day4RemoveAll.setOnClickListener(onClickListener);

        return rootView;
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

    private void removeAllFavourites(final int id, final boolean removeAll){

        final CoordinatorLayout mainLayout  = (CoordinatorLayout)getActivity().findViewById(R.id.main_activity_coordinator_layout);

        View view = View.inflate(getActivity(), R.layout.dialog_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(view);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

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
                    if (removeAll || id == day1RemoveAll.getId()){
                        day1RemoveAll.setVisibility(View.GONE);
                        if (!removeAll) Snackbar.make(mainLayout, "Day 1 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "1";
                        day1List.clear();
                        day1Adapter.notifyDataSetChanged();
                        day1NoEvents.setVisibility(View.VISIBLE);
                    }
                    if (removeAll || id == day2RemoveAll.getId()){
                        day2RemoveAll.setVisibility(View.GONE);
                        if (!removeAll) Snackbar.make(mainLayout, "Day 2 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "2";
                        day2List.clear();
                        day2Adapter.notifyDataSetChanged();
                        day2NoEvents.setVisibility(View.VISIBLE);
                    }
                    if (removeAll || id == day3RemoveAll.getId()){
                        day3RemoveAll.setVisibility(View.GONE);
                        if (!removeAll) Snackbar.make(mainLayout, "Day 3 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "3";
                        day3List.clear();
                        day3Adapter.notifyDataSetChanged();
                        day3NoEvents.setVisibility(View.VISIBLE);
                    }
                    if (removeAll || id == day4RemoveAll.getId()){
                        day4RemoveAll.setVisibility(View.GONE);
                        if (!removeAll) Snackbar.make(mainLayout, "Day 4 favourites removed!", Snackbar.LENGTH_SHORT).show();
                        day = "4";
                        day4List.clear();
                        day4Adapter.notifyDataSetChanged();
                        day4NoEvents.setVisibility(View.VISIBLE);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                mRealm.beginTransaction();
                if (!removeAll) mRealm.where(FavouritesModel.class).equalTo("day", day).findAll().deleteAllFromRealm();
                else mRealm.where(FavouritesModel.class).findAll().deleteAllFromRealm();
                mRealm.commitTransaction();

                if (removeAll) Snackbar.make(mainLayout, "Favourites removed!", Snackbar.LENGTH_SHORT).show();
            }
        });
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
    }
}

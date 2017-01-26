package in.mitrevels.revels.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.FavouritesAdapter;
import in.mitrevels.revels.models.events.EventModel;

/**
 * Created by anurag on 12/12/16.
 */
public class FavouritesFragment extends Fragment {

    private LinearLayout day1TitleLayout;
    private LinearLayout day2TitleLayout;
    private LinearLayout day3TitleLayout;
    private LinearLayout day4TitleLayout;
    private TextView day1RemoveAll;
    private TextView day2RemoveAll;
    private TextView day3RemoveAll;
    private TextView day4RemoveAll;

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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        day1TitleLayout = (LinearLayout)rootView.findViewById(R.id.day_1_title_layout);
        day2TitleLayout = (LinearLayout)rootView.findViewById(R.id.day_2_title_layout);
        day3TitleLayout = (LinearLayout)rootView.findViewById(R.id.day_3_title_layout);
        day4TitleLayout = (LinearLayout)rootView.findViewById(R.id.day_4_title_layout);

        day1RemoveAll = (TextView)rootView.findViewById(R.id.day_1_remove_all_text_view);
        day2RemoveAll = (TextView)rootView.findViewById(R.id.day_2_remove_all_text_view);
        day3RemoveAll = (TextView)rootView.findViewById(R.id.day_3_remove_all_text_view);
        day4RemoveAll = (TextView)rootView.findViewById(R.id.day_4_remove_all_text_view);

        RecyclerView day1RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_1_recycler_view);
        RecyclerView day2RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_2_recycler_view);
        RecyclerView day3RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_3_recycler_view);
        RecyclerView day4RecyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_day_4_recycler_view);

        FavouritesAdapter adapter = new FavouritesAdapter(getActivity(), createList());
        RecyclerView.LayoutManager day1LayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager day2LayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager day3LayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager day4LayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        day1RecyclerView.setAdapter(adapter);
        day1RecyclerView.setNestedScrollingEnabled(false);
        day1RecyclerView.setLayoutManager(day1LayoutManager);

        day2RecyclerView.setAdapter(adapter);
        day2RecyclerView.setNestedScrollingEnabled(false);
        day2RecyclerView.setLayoutManager(day2LayoutManager);

        day3RecyclerView.setAdapter(adapter);
        day3RecyclerView.setNestedScrollingEnabled(false);
        day3RecyclerView.setLayoutManager(day3LayoutManager);

        day4RecyclerView.setAdapter(adapter);
        day4RecyclerView.setNestedScrollingEnabled(false);
        day4RecyclerView.setLayoutManager(day4LayoutManager);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_favourites, menu);
    }
}

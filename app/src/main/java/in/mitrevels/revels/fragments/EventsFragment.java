package in.mitrevels.revels.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import in.mitrevels.revels.R;
import in.mitrevels.revels.activities.TrendingActivity;
import in.mitrevels.revels.adapters.DayPagerAdapter;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsFragment extends Fragment {

    public EventsFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        final TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.events_tab_layout);
        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.events_view_pager);

        DayPagerAdapter pagerAdapter = new DayPagerAdapter(getChildFragmentManager());
        pagerAdapter.add(new DayFragment(), "Day 1");
        pagerAdapter.add(new DayFragment(), "Day 2");
        pagerAdapter.add(new DayFragment(), "Day 3");
        pagerAdapter.add(new DayFragment(), "Day 4");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Events Fragment", "OnDetach() called");
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }
}

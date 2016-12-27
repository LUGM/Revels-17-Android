package in.mitrevels.revels.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import in.mitrevels.revels.R;
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
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }

}

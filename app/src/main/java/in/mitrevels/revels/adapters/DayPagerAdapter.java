package in.mitrevels.revels.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 8/12/16.
 */
public class DayPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    public DayPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add (Fragment fragment, String title){
        Bundle bundle = new Bundle();

        switch(title){
            case "Day 1": bundle.putInt("day", 1); break;
            case "Day 2": bundle.putInt("day", 2); break;
            case "Day 3": bundle.putInt("day", 3); break;
            case "Day 4": bundle.putInt("day", 4); break;
        }
        fragment.setArguments(bundle);
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

package in.mitrevels.revels.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
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

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.EventsAdapter;
import in.mitrevels.revels.models.EventModel;

/**
 * Created by anurag on 8/12/16.
 */
public class DayFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventsAdapter adapter;

    public DayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        setHasOptionsMenu(true);

        eventsRecyclerView = (RecyclerView)rootView.findViewById(R.id.events_recycler_view);
        adapter = new EventsAdapter(getActivity(), createList());
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    List<EventModel> createList(){
        List<EventModel> eventsList = new ArrayList<>();

        for (int i=0; i<10; i++){
            EventModel em = new EventModel();
            em.setEventName("Battle of the Bands (Round 1)");
            em.setEventDate("08/03/2017");
            em.setStartTime("12:30 PM");
            em.setEndTime("7:30 PM");
            em.setCategory("Gaming");
            em.setContactName("Gaming Category Head");
            em.setContactNumber("9090909090");
            em.setEventVenue("NLH 402, 403, 404, 405");
            em.setTeamSize("4");
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
}

package in.mitrevels.revels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public DayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        eventsRecyclerView = (RecyclerView)rootView.findViewById(R.id.events_recycler_view);
        EventsAdapter adapter = new EventsAdapter(getActivity(), createList());
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    List<EventModel> createList(){
        List<EventModel> eventsList = new ArrayList<>();

        EventModel em = new EventModel();
        em.setEventName("Fifa 14");
        em.setStartTime("12:30 PM");
        em.setEndTime("7:30 PM");
        em.setEventVenue("NLH 402, 403, 404, 405");
        eventsList.add(em);

        for (int i=0; i<10; i++){
            EventModel model = new EventModel();
            model.setEventName("Battle of the Bands");
            model.setStartTime("1:00 PM");
            model.setEndTime("8:00 PM");
            model.setEventVenue("Quadrangle");
            eventsList.add(model);
        }

        return eventsList;
    }
}

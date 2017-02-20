package in.mitrevels.revels.fragments;


import android.os.Build;
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
import in.mitrevels.revels.adapters.ResultsAdapter;
import in.mitrevels.revels.models.results.ResultModel;
import in.mitrevels.revels.models.results.ResultsListModel;
import in.mitrevels.revels.network.ResultsAPIClient;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsFragment extends Fragment {

    private Realm mRealm;
    private List<EventResultModel> resultsList = new ArrayList<>();
    private ResultsAdapter adapter;

    public ResultsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_results);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        RecyclerView resultsRecyclerView = (RecyclerView)rootView.findViewById(R.id.results_recycler_view);
        adapter = new ResultsAdapter(resultsList, getActivity());
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mRealm.where(ResultModel.class).findAll().size() != 0){
            displayData();
            prepareData();
        }
        else{
            prepareData();
        }

        return rootView;
    }

    private void prepareData(){
        Call<ResultsListModel> call = ResultsAPIClient.getAPIInterface().getResults();

        call.enqueue(new Callback<ResultsListModel>() {
            @Override
            public void onResponse(Call<ResultsListModel> call, Response<ResultsListModel> response) {
                if (response.body() != null && mRealm != null){
                    mRealm.beginTransaction();
                    mRealm.where(ResultModel.class).findAll().deleteAllFromRealm();
                    mRealm.copyToRealm(response.body().getData());
                    mRealm.commitTransaction();
                }
                displayData();
            }

            @Override
            public void onFailure(Call<ResultsListModel> call, Throwable t) {
                displayData();
            }
        });
    }

    private void displayData(){
        if (mRealm == null) return;

        RealmResults<ResultModel> results = mRealm.where(ResultModel.class).findAll();

        if (!results.isEmpty()){
            resultsList.clear();

            List<String> eventNamesList = new ArrayList<>();

            for (ResultModel result : results){
                String eventName = result.getEventName();
                if (eventNamesList.contains(eventName)){
                    resultsList.get(eventNamesList.indexOf(eventName)).eventResultsList.add(result);
                }
                else{
                    EventResultModel eventResult = new EventResultModel();
                    eventResult.eventName = eventName;
                    eventResult.eventCategory = result.getCatName();
                    eventResult.eventResultsList.add(result);
                    resultsList.add(eventResult);
                    eventNamesList.add(eventName);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    public class EventResultModel {
        public String eventName;
        public String eventCategory;
        public List<ResultModel> eventResultsList = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }

}

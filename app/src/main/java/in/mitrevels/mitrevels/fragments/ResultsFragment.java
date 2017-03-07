package in.mitrevels.mitrevels.fragments;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.adapters.ResultsAdapter;
import in.mitrevels.mitrevels.models.results.ResultModel;
import in.mitrevels.mitrevels.models.results.ResultsListModel;
import in.mitrevels.mitrevels.network.APIClient;
import in.mitrevels.mitrevels.utilities.HandyMan;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsFragment extends Fragment {

    private LinearLayout noConnectionLayout;
    private LinearLayout noDataLayout;
    private Realm mRealm;
    private List<EventResultModel> resultsList = new ArrayList<>();
    private ResultsAdapter adapter;
    private ProgressDialog dialog;
    private SwipeRefreshLayout swipeRefresh;
    private CoordinatorLayout rootLayout;
    private static final int LOAD_RESULTS = 0;
    private static final int UPDATE_RESULTS = 1;
    private boolean isUpdating = false;

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

        rootLayout = (CoordinatorLayout)getActivity().findViewById(R.id.main_activity_coordinator_layout);

        noDataLayout = (LinearLayout)rootView.findViewById(R.id.results_no_data_layout);

        noConnectionLayout = (LinearLayout)rootView.findViewById(R.id.results_no_connection_layout);
        TextView retry = (TextView)noConnectionLayout.findViewById(R.id.no_connection_retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandyMan.help().isInternetConnected(getActivity())){
                    prepareData(LOAD_RESULTS);
                    noConnectionLayout.setVisibility(View.GONE);
                }
                else{
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.result_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.amber);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isUpdating && HandyMan.help().isInternetConnected(getActivity())){
                    if (noDataLayout.getVisibility() == View.VISIBLE || noConnectionLayout.getVisibility() == View.VISIBLE){
                        noDataLayout.setVisibility(View.GONE);
                        noConnectionLayout.setVisibility(View.GONE);
                        prepareData(LOAD_RESULTS);
                    }
                    else{
                        prepareData(UPDATE_RESULTS);
                    }
                }
                else if (!HandyMan.help().isInternetConnected(getActivity())){
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        RecyclerView resultsRecyclerView = (RecyclerView)rootView.findViewById(R.id.results_recycler_view);
        adapter = new ResultsAdapter(resultsList, getActivity());
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mRealm.where(ResultModel.class).findAll().size() != 0){
            displayData();
            prepareData(UPDATE_RESULTS);
        }
        else{
            if (HandyMan.help().isInternetConnected(getActivity())){
                prepareData(LOAD_RESULTS);
            }
            else{
                noConnectionLayout.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    private void prepareData(final int operation){
        Call<ResultsListModel> call = APIClient.getAPIInterface().getResults();

        if (operation == LOAD_RESULTS && !swipeRefresh.isRefreshing()){
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.loading_results));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        else if (operation == UPDATE_RESULTS){
            isUpdating = true;
        }

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
                if (operation == LOAD_RESULTS && dialog != null){
                    if (dialog.isShowing())
                        dialog.hide();
                }
                isUpdating = false;
                if (swipeRefresh.isRefreshing()){
                    swipeRefresh.setRefreshing(false);
                    Snackbar.make(rootLayout, "Results updated!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultsListModel> call, Throwable t) {
                isUpdating = false;
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                    Snackbar.make(rootLayout, "Failed to update results!", Snackbar.LENGTH_SHORT).show();
                }

                if (operation == LOAD_RESULTS) {
                    if (dialog != null && dialog.isShowing())
                        dialog.hide();

                    if (t!=null && t.getClass() == JsonSyntaxException.class){
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                    else{
                        noConnectionLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void displayData(){
        if (mRealm == null) return;

        RealmResults<ResultModel> results = mRealm.where(ResultModel.class).findAllSorted("eventName", Sort.ASCENDING, "position", Sort.ASCENDING);

        if (!results.isEmpty()){
            resultsList.clear();

            List<String> eventNamesList = new ArrayList<>();

            for (ResultModel result : results){
                String eventName = result.getEventName()+" "+result.getRound();
                if (eventNamesList.contains(eventName)){
                    resultsList.get(eventNamesList.indexOf(eventName)).eventResultsList.add(result);
                }
                else{
                    EventResultModel eventResult = new EventResultModel();
                    eventResult.eventName = result.getEventName();
                    eventResult.eventRound = result.getRound();
                    eventResult.eventCategory = result.getCatName();
                    eventResult.eventResultsList.add(result);
                    resultsList.add(eventResult);
                    eventNamesList.add(eventName);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_results, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.results_menu_refresh:
                if (!isUpdating && HandyMan.help().isInternetConnected(getActivity())){
                    swipeRefresh.setRefreshing(true);
                    if (noDataLayout.getVisibility() == View.VISIBLE || noConnectionLayout.getVisibility() == View.VISIBLE){
                        noDataLayout.setVisibility(View.GONE);
                        noConnectionLayout.setVisibility(View.GONE);
                        prepareData(LOAD_RESULTS);
                    }
                    else{
                        prepareData(UPDATE_RESULTS);
                    }
                }
                else if (!HandyMan.help().isInternetConnected(getActivity())){
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }


    public class EventResultModel {
        public String eventName;
        public String eventRound;
        public String eventCategory;
        public List<ResultModel> eventResultsList = new ArrayList<>();
    }
}

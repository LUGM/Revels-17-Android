package in.mitrevels.revels.fragments;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.CategoriesAdapter;
import in.mitrevels.revels.models.categories.CategoriesListModel;
import in.mitrevels.revels.models.categories.CategoryModel;
import in.mitrevels.revels.network.APIClient;
import in.mitrevels.revels.utilities.HandyMan;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {

    private LinearLayout noConnectionLayout;
    private List<CategoryModel> categoriesList = new ArrayList<>();
    private Realm mDatabase;
    private CategoriesAdapter adapter;
    private ProgressDialog dialog;
    private static final int LOAD_CATEGORIES = 0;
    private static final int UPDATE_CATEGORIES = 1;

    public CategoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.categories);
        mDatabase = Realm.getDefaultInstance();

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().findViewById(R.id.toolbar).setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
                getActivity().findViewById(R.id.main_app_bar_layout).setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        final CoordinatorLayout rootLayout = (CoordinatorLayout)getActivity().findViewById(R.id.main_activity_coordinator_layout);
        noConnectionLayout = (LinearLayout)view.findViewById(R.id.cat_no_connection_layout);

        TextView retry = (TextView)noConnectionLayout.findViewById(R.id.no_connection_retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandyMan.help().isInternetConnected(getActivity())){
                    prepareData(LOAD_CATEGORIES);
                    noConnectionLayout.setVisibility(View.GONE);
                }
                else{
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView categoriesRecyclerView = (RecyclerView)view.findViewById(R.id.categories_recycler_view);
        adapter = new CategoriesAdapter(categoriesList, getActivity());
        categoriesRecyclerView.setAdapter(adapter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mDatabase.where(CategoryModel.class).findAll().size() != 0){
            displayData();
            prepareData(UPDATE_CATEGORIES);
        }
        else{
            if (HandyMan.help().isInternetConnected(getActivity())){
                prepareData(LOAD_CATEGORIES);
            }
            else{
                noConnectionLayout.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private void prepareData(final int operation){
        Call<CategoriesListModel> categoriesCall = APIClient.getAPIInterface().getCategoriesList();

        if (operation == LOAD_CATEGORIES){
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.loading_categories));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        categoriesCall.enqueue(new Callback<CategoriesListModel>() {
            @Override
            public void onResponse(Call<CategoriesListModel> call, Response<CategoriesListModel> response) {
                if (response.body() != null && mDatabase != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(CategoryModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getCategoriesList());
                    mDatabase.commitTransaction();
                }
                displayData();

                if (operation == LOAD_CATEGORIES && dialog != null){
                    if (dialog.isShowing())
                        dialog.hide();
                }
            }

            @Override
            public void onFailure(Call<CategoriesListModel> call, Throwable t) {
                if (operation == LOAD_CATEGORIES && dialog != null){
                    if (dialog.isShowing())
                        dialog.hide();
                    noConnectionLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void displayData(){

        if (mDatabase != null){
            RealmResults<CategoryModel> categoryResults = mDatabase.where(CategoryModel.class).findAllSorted("categoryName");

            if (!categoryResults.isEmpty()){
                categoriesList.clear();
                categoriesList.addAll(categoryResults);
                for (CategoryModel category : categoriesList){
                    Log.d("Cat ID:"+category.getCategoryID(), "Cat Name: "+category.getCategoryName());
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mDatabase = null;
    }

}

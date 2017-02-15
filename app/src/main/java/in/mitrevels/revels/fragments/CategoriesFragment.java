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
import in.mitrevels.revels.adapters.CategoriesAdapter;
import in.mitrevels.revels.models.categories.CategoriesListModel;
import in.mitrevels.revels.models.categories.CategoryModel;
import in.mitrevels.revels.network.CategoriesAPIClient;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {

    private List<CategoryModel> categoriesList = new ArrayList<>();
    private Realm mDatabase;
    private CategoriesAdapter adapter;

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

        RecyclerView categoriesRecyclerView = (RecyclerView)view.findViewById(R.id.categories_recycler_view);
        adapter = new CategoriesAdapter(categoriesList, getActivity());
        categoriesRecyclerView.setAdapter(adapter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        prepareData();
        return view;
    }

    private void prepareData(){
        Call<CategoriesListModel> categoriesCall = CategoriesAPIClient.getAPIInterface().getCategoriesList();

        categoriesCall.enqueue(new Callback<CategoriesListModel>() {
            @Override
            public void onResponse(Call<CategoriesListModel> call, Response<CategoriesListModel> response) {
                if (response.body() != null){
                    mDatabase.beginTransaction();
                    mDatabase.where(CategoryModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getCategoriesList());
                    mDatabase.commitTransaction();
                }
                displayData();
            }

            @Override
            public void onFailure(Call<CategoriesListModel> call, Throwable t) {
                displayData();
            }
        });
    }

    private void displayData(){

        RealmResults<CategoryModel> categoryResults = mDatabase.where(CategoryModel.class).findAll();

        if (!categoryResults.isEmpty()){
            categoriesList.clear();
            categoriesList.addAll(categoryResults);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}

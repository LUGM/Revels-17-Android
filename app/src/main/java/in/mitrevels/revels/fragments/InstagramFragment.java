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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.InstaFeedAdapter;
import in.mitrevels.revels.models.instagram.InstagramFeed;
import in.mitrevels.revels.network.InstaFeedAPIClient;
import in.mitrevels.revels.utilities.HandyMan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 18/12/16.
 */
public class InstagramFragment extends Fragment {

    private RecyclerView instaFeedRecyclerView;
    private LinearLayout noConnectionLayout;

    public InstagramFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.revels_17_hashtag);

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
        View rootView = inflater.inflate(R.layout.fragment_instagram, container, false);

        instaFeedRecyclerView = (RecyclerView)rootView.findViewById(R.id.insta_feed_recycler_view);
        noConnectionLayout = (LinearLayout)rootView.findViewById(R.id.insta_no_connection_layout);
        final CoordinatorLayout rootLayout = (CoordinatorLayout)getActivity().findViewById(R.id.main_activity_coordinator_layout);

        TextView retry = (TextView)noConnectionLayout.findViewById(R.id.no_connection_retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandyMan.help().isInternetConnected(getActivity())){
                    displayData();
                }
                else{
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        if (HandyMan.help().isInternetConnected(getActivity())){
            displayData();
        }
        else{
            noConnectionLayout.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void displayData(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.loading_insta));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Call<InstagramFeed> call = InstaFeedAPIClient.getAPIInterface().getInstagramFeed();

        call.enqueue(new Callback<InstagramFeed>() {
            @Override
            public void onResponse(Call<InstagramFeed> call, Response<InstagramFeed> response) {
                try{
                    InstagramFeed feed = response.body();
                    InstaFeedAdapter adapter = new InstaFeedAdapter(getActivity(), feed);
                    instaFeedRecyclerView.setAdapter(adapter);
                    instaFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    dialog.hide();
                    noConnectionLayout.setVisibility(View.GONE);
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<InstagramFeed> call, Throwable t) {
                dialog.hide();
                noConnectionLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}

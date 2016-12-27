package in.mitrevels.revels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.InstaFeedAdapter;
import in.mitrevels.revels.models.instagram.InstagramFeed;
import in.mitrevels.revels.network.InstaFeedAPIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 18/12/16.
 */
public class InstagramFragment extends Fragment {

    private RecyclerView instaFeedRecyclerView;

    public InstagramFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.revels_17_hashtag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instagram, container, false);

        instaFeedRecyclerView = (RecyclerView)rootView.findViewById(R.id.insta_feed_recycler_view);

        Call<InstagramFeed> call = InstaFeedAPIClient.getAPIInterface().getInstagramFeed();

        call.enqueue(new Callback<InstagramFeed>() {
            @Override
            public void onResponse(Call<InstagramFeed> call, Response<InstagramFeed> response) {
                InstagramFeed feed = response.body();
                InstaFeedAdapter adapter = new InstaFeedAdapter(getActivity(), feed);
                instaFeedRecyclerView.setAdapter(adapter);
                instaFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(Call<InstagramFeed> call, Throwable t) {

            }
        });

        return rootView;
    }
}

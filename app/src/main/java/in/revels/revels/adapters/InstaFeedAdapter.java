package in.revels.revels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.revels.revels.R;
import in.revels.revels.models.instafeed.InstaFeedModel;
import in.revels.revels.models.instafeed.InstaFeedMeta;

/**
 * Created by anurag on 8/6/16.
 */
public class InstaFeedAdapter extends RecyclerView.Adapter<InstaFeedAdapter.InstaFeedViewHolder> {

    InstaFeedMeta feed;
    LayoutInflater inflater;
    Context context;


    public InstaFeedAdapter(Context context, InstaFeedMeta feed) {
        inflater = LayoutInflater.from(context);
        this.feed= feed;
        this.context=context;
    }

    @Override
    public InstaFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_instafeed, viewGroup, false);
        InstaFeedViewHolder holder = new InstaFeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InstaFeedViewHolder holder, int position) {

        InstaFeedModel instaFeedItem = feed.getFeed().get(position);
        holder.instaFeedName.setText(instaFeedItem.getUser().getUsername());
        Picasso.with(context).load(instaFeedItem.getImages().getStandard_resolution().getUrl()).into(holder.instaFeedImage);
        Picasso.with(context).load(instaFeedItem.getUser().getProfile_picture()).into(holder.instaFeedDP);
        holder.instaFeedDescription.setText(instaFeedItem.getCaption().getText());
        holder.instaFeedLikes.setText(Integer.toString(instaFeedItem.getLikes().getCount()) + " likes");
        holder.instaFeedComments.setText(Integer.toString(instaFeedItem.getComments().getCount()) + " comments");
    }


    @Override
    public int getItemCount() {
        return feed.getFeed().size();
    }


    public class InstaFeedViewHolder extends RecyclerView.ViewHolder {

        ImageView instaFeedDP, instaFeedImage;
        TextView instaFeedName, instaFeedDescription, instaFeedLikes, instaFeedComments;

        public InstaFeedViewHolder(View itemView) {
            super(itemView);

            instaFeedDP = (ImageView)itemView.findViewById(R.id.insta_feed_dp_image_view);
            instaFeedName = (TextView)itemView.findViewById(R.id.insta_feed_name_text_view);
            instaFeedImage = (ImageView)itemView.findViewById(R.id.insta_feed_img_image_view);
            instaFeedDescription = (TextView)itemView.findViewById(R.id.insta_feed_description_text_view);
            instaFeedLikes = (TextView)itemView.findViewById(R.id.insta_feed_likes_text_view);
            instaFeedComments = (TextView)itemView.findViewById(R.id.insta_feed_comments_text_view);
        }
    }
}

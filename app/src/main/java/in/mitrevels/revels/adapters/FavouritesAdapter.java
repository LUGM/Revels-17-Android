package in.mitrevels.revels.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.activities.EventActivity;
import in.mitrevels.revels.models.events.EventModel;

/**
 * Created by anurag on 5/1/17.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>{

    private Activity activity;
    private List<EventModel> eventsList;
    boolean flag = false;

    public FavouritesAdapter(Activity activity, List<EventModel> eventsList) {
        this.activity = activity;
        this.eventsList = eventsList;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_favourite, parent, false));
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, int position) {
        EventModel event = eventsList.get(position);

        holder.favouriteName.setText(event.getEventName());
        holder.favouriteTime.setText(event.getStartTime());
        holder.favouriteLogo.setImageResource(flag ? R.drawable.tt_kraftwagen : R.drawable.tt_aero);

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), flag ? R.drawable.tt_kraftwagen : R.drawable.tt_aero);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                holder.bgLayout.setBackgroundColor(palette.getDominantColor(ContextCompat.getColor(activity, R.color.teal)));
            }
        });

        flag = !flag;
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView favouriteLogo;
        TextView favouriteName;
        TextView favouriteTime;
        LinearLayout bgLayout;
        ImageView deleteFavourite;

        public FavouriteViewHolder(View itemView) {
            super(itemView);

            favouriteLogo = (ImageView)itemView.findViewById(R.id.fav_logo_image_view);
            favouriteName = (TextView)itemView.findViewById(R.id.fav_name_text_view);
            favouriteTime = (TextView)itemView.findViewById(R.id.fav_time_text_view);
            bgLayout = (LinearLayout)itemView.findViewById(R.id.fav_card_bg_layout);
            deleteFavourite = (ImageView)itemView.findViewById(R.id.favourite_item_delete);

            itemView.setOnClickListener(this);
            deleteFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == deleteFavourite.getId()){
                eventsList.remove(getLayoutPosition());
                notifyItemRemoved(getLayoutPosition());
            }
            else{
                Intent intent = new Intent(activity, EventActivity.class);
                EventModel event = eventsList.get(getLayoutPosition());
                intent.putExtra("Event Name", event.getEventName());
                intent.putExtra("Event Date", event.getDate());
                intent.putExtra("Event Time", event.getStartTime()+" - "+event.getEndTime());
                intent.putExtra("Event Venue", event.getVenue());
                intent.putExtra("Team Of", event.getEventMaxTeamNumber());
                intent.putExtra("Event Category", event.getCatName());
                intent.putExtra("Contact Number", event.getContactNumber());
                intent.putExtra("Contact Name", "("+event.getContactName()+")");
                intent.putExtra("Event Description", event.getDescription());
                intent.putExtra("Favourite", true);
                intent.putExtra("Category Logo", getLayoutPosition()%2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity, favouriteLogo, activity.getString(R.string.cat_logo_transition));
                    activity.startActivity(intent, options.toBundle());
                }
                else {
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
                }

            }
        }
    }
}

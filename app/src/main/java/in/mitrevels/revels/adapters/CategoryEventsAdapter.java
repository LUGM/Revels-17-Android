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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.activities.EventActivity;
import in.mitrevels.revels.models.events.EventModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import in.mitrevels.revels.models.instagram.image.Image;
import in.mitrevels.revels.utilities.HandyMan;

/**
 * Created by anurag on 15/2/17.
 */
public class CategoryEventsAdapter extends RecyclerView.Adapter<CategoryEventsAdapter.CategoryEventViewHolder> {

    private List<EventModel> eventsList;
    private Activity activity;

    public CategoryEventsAdapter(List<EventModel> eventsList, Activity activity) {
        this.eventsList = eventsList;
        this.activity = activity;
    }

    @Override
    public CategoryEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryEventViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_category_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryEventViewHolder holder, int position) {
        EventModel event = eventsList.get(position);

        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getStartTime());

        int logoID = HandyMan.help().getCategoryLogo(event.getCatId());
        holder.eventLogo.setImageResource(logoID);

        holder.eventRound.setVisibility(View.VISIBLE);

        if (!event.getRound().equals("-")){
            if (event.getRound().toLowerCase().equals("finals") || event.getRound().toLowerCase().equals("f") || event.getRound().toLowerCase().equals("final")){
                holder.eventRound.setText("RF");
            }
            else if(event.getRound().toLowerCase().equals("prelims")) {
                holder.eventRound.setText("RP");
            }
            else{
                holder.eventRound.setText("R"+event.getRound());
            }
        }
        else{
            holder.eventRound.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class CategoryEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView eventLogo;
        TextView eventName;
        TextView eventTime;
        FrameLayout logoFrame;
        TextView eventRound;

        public CategoryEventViewHolder(View itemView) {
            super(itemView);

            eventLogo = (ImageView)itemView.findViewById(R.id.cat_event_logo_image_view);
            eventName = (TextView)itemView.findViewById(R.id.cat_event_name_text_view);
            eventTime = (TextView)itemView.findViewById(R.id.cat_event_time_text_view);
            logoFrame = (FrameLayout)itemView.findViewById(R.id.fav_event_logo_frame);
            eventRound = (TextView)itemView.findViewById(R.id.cat_event_round_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, EventActivity.class);
            EventModel event = eventsList.get(getAdapterPosition());
            intent.putExtra("Event ID", event.getEventId());
            intent.putExtra("Event Name", event.getEventName());
            intent.putExtra("Event Round", event.getRound());
            intent.putExtra("Event Date", event.getDate());
            intent.putExtra("Event Start Time", event.getStartTime());
            intent.putExtra("Event End Time", event.getEndTime());
            intent.putExtra("Event Venue", event.getVenue());
            intent.putExtra("Team Of", event.getEventMaxTeamNumber());
            intent.putExtra("Event Category", event.getCatName());
            intent.putExtra("Category ID", event.getCatId());
            intent.putExtra("Event Day", event.getDay());
            intent.putExtra("Contact Number", event.getContactNumber());
            intent.putExtra("Contact Name", "("+event.getContactName()+")");
            intent.putExtra("Event Description", event.getDescription());
            intent.putExtra("enableFavourite", true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, logoFrame, activity.getString(R.string.cat_logo_transition));
                activity.startActivity(intent, options.toBundle());
            }
            else {
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
            }
        }
    }
}

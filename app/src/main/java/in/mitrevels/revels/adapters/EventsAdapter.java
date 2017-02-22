package in.mitrevels.revels.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mitrevels.revels.R;
import in.mitrevels.revels.activities.EventActivity;
import in.mitrevels.revels.models.FavouritesModel;
import in.mitrevels.revels.models.events.EventModel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<EventModel> eventsList;
    private List<EventModel> allEvents;
    private Activity activity;
    private Realm mDatabase;
    private static final int ADD_FAVOURITE = 0;
    private static final int REMOVE_FAVOURITE = 1;

    public EventsAdapter(Activity activity, List<EventModel> eventsList, List<EventModel> allEvents, Realm mDatabase) {
        this.activity = activity;
        this.eventsList = eventsList;
        this.allEvents = allEvents;
        this.mDatabase = mDatabase;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventModel event = eventsList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventVenue.setText(event.getVenue());
        holder.eventTime.setText(event.getStartTime()+" - "+event.getEndTime());
        if (position%2==0){
            holder.eventLogo.setImageResource(R.drawable.tt_aero);
        }
        else{
            holder.eventLogo.setImageResource(R.drawable.tt_kraftwagen);
        }

        FavouritesModel favourite = mDatabase.where(FavouritesModel.class).equalTo("eventName", event.getEventName()).equalTo("date", event.getDate()).findFirst();

        if(favourite != null) {
            holder.eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.red));
            holder.eventFav.setTag("Selected");
        }
        else{
            holder.eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.fav_deselect));
            holder.eventFav.setTag("Deselected");
        }

    }

    public void addOrRemoveFavourites(EventModel event, int operation){

        if(operation == ADD_FAVOURITE) {
            FavouritesModel favourite = new FavouritesModel();

            favourite.setId(event.getEventId());
            favourite.setCatID(event.getCatId());
            favourite.setEventName(event.getEventName());
            favourite.setVenue(event.getVenue());
            favourite.setDate(event.getDate());
            favourite.setDay(event.getDay());
            favourite.setStartTime(event.getStartTime());
            favourite.setEndTime(event.getEndTime());
            favourite.setParticipants(event.getEventMaxTeamNumber());
            favourite.setContactName(event.getContactName());
            favourite.setContactNumber(event.getContactNumber());
            favourite.setCatName(event.getCatName());
            favourite.setDescription(event.getDescription());

            mDatabase.beginTransaction();
            mDatabase.copyToRealm(favourite);
            mDatabase.commitTransaction();
        }
        else if (operation == REMOVE_FAVOURITE){
            mDatabase.beginTransaction();
            mDatabase.where(FavouritesModel.class).equalTo("eventName", event.getEventName()).equalTo("day", event.getDay()).findAll().deleteAllFromRealm();
            mDatabase.commitTransaction();
        }

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void filterResults(String query){
        eventsList.clear();

        for (EventModel event : allEvents){
            if (event.getEventName().toLowerCase().contains(query.toLowerCase()))
                eventsList.add(event);
        }

        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView eventLogo;
        TextView eventName;
        TextView eventTime;
        TextView eventVenue;
        ImageView eventFav;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventLogo = (ImageView)itemView.findViewById(R.id.event_logo_image_view);
            eventName = (TextView)itemView.findViewById(R.id.event_name_text_view);
            eventTime = (TextView)itemView.findViewById(R.id.event_time_text_view);
            eventVenue = (TextView)itemView.findViewById(R.id.event_venue_text_view);
            eventFav = (ImageView)itemView.findViewById(R.id.event_fav_image_view);

            eventFav.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == eventFav.getId()){
                String name = eventsList.get(getAdapterPosition()).getEventName();
                if (eventFav.getTag().toString().equals("Deselected")){
                    eventFav.setTag("Selected");
                    eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.red));
                    Snackbar.make(activity.findViewById(R.id.main_activity_coordinator_layout), eventName.getText()+" added to favourites!", Snackbar.LENGTH_SHORT).show();
                    
                    addOrRemoveFavourites(eventsList.get(getAdapterPosition()), ADD_FAVOURITE);
                }
                else{
                    eventFav.setTag("Deselected");
                    eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.fav_deselect));
                    Snackbar.make(activity.findViewById(R.id.main_activity_coordinator_layout), eventName.getText()+" removed from favourites!", Snackbar.LENGTH_SHORT).show();
                    
                    addOrRemoveFavourites(eventsList.get(getAdapterPosition()), REMOVE_FAVOURITE);
                }
            }

            if (v.getId() == itemView.getId()){
                Intent intent = new Intent(activity, EventActivity.class);
                EventModel event = eventsList.get(getAdapterPosition());
                intent.putExtra("Event ID", event.getEventId());
                intent.putExtra("Event Name", event.getEventName());
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
                intent.putExtra("Category Logo", getAdapterPosition()%2);
                intent.putExtra("enableFavourite", true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity, eventLogo, activity.getString(R.string.cat_logo_transition));
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

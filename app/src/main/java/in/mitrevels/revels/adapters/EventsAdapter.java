package in.mitrevels.revels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import in.mitrevels.revels.models.EventModel;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    List<EventModel> eventsList;
    List<EventModel> allEvents;
    Context context;
    Map<String, Boolean> favouritesMap;

    public EventsAdapter(Context context, List<EventModel> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
        allEvents = new ArrayList<>();

        for (EventModel event : this.eventsList)
            allEvents.add(event);

        favouritesMap = new HashMap<>();
        for (EventModel event : this.eventsList)
            favouritesMap.put(event.getEventName(), false);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventModel model = eventsList.get(position);
        holder.eventName.setText(model.getEventName());
        holder.eventVenue.setText(model.getEventVenue());
        holder.eventTime.setText(model.getStartTime()+" - "+model.getEndTime());
        if (position%2==0){
            holder.eventLogo.setImageResource(R.drawable.tt_aero);
        }
        else{
            holder.eventLogo.setImageResource(R.drawable.tt_kraftwagen);
        }

        if (favouritesMap.get(model.getEventName())) {
            holder.eventFav.setColorFilter(ContextCompat.getColor(context, R.color.red));
            holder.eventFav.setTag("Selected");
        }
        else {
            holder.eventFav.setColorFilter(ContextCompat.getColor(context, R.color.fav_deselect));
            holder.eventFav.setTag("Deselected");
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
                String name = eventsList.get(getLayoutPosition()).getEventName();
                if (eventFav.getTag().toString().equals("Deselected")){
                    eventFav.setTag("Selected");
                    eventFav.setColorFilter(ContextCompat.getColor(context, R.color.red));
                    Snackbar.make(v, eventName.getText()+" added to favourites!", Snackbar.LENGTH_SHORT).show();

                    if(favouritesMap.containsKey(name))
                        favouritesMap.remove(name);
                    favouritesMap.put(name, true);
                }
                else{
                    eventFav.setTag("Deselected");
                    eventFav.setColorFilter(ContextCompat.getColor(context, R.color.fav_deselect));
                    Snackbar.make(v, eventName.getText()+" removed from favourites!", Snackbar.LENGTH_SHORT).show();

                    if(favouritesMap.containsKey(name))
                        favouritesMap.remove(name);
                    favouritesMap.put(name, false);
                }
            }

            if (v.getId() == itemView.getId()){
                Log.d("Item", "pressed");
                Intent intent = new Intent(context, EventActivity.class);
                EventModel event = eventsList.get(getLayoutPosition());
                intent.putExtra("Event Name", event.getEventName());
                intent.putExtra("Event Date", event.getEventDate());
                intent.putExtra("Event Time", event.getStartTime()+" - "+event.getEndTime());
                intent.putExtra("Event Venue", event.getEventVenue());
                intent.putExtra("Team Of", event.getTeamSize());
                intent.putExtra("Event Category", event.getCategory());
                intent.putExtra("Event Contact", event.getContactNumber()+" ("+event.getContactName()+")");
                intent.putExtra("Event Description", event.getDescription());
                intent.putExtra("Favourite", favouritesMap.get(event.getEventName()));
                intent.putExtra("Category Logo", getLayoutPosition()%2);
                context.startActivity(intent);
            }
        }
    }
}

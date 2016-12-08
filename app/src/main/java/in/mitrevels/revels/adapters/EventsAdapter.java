package in.mitrevels.revels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.models.EventModel;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    List<EventModel> eventsList;
    Context context;

    public EventsAdapter(Context context, List<EventModel> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
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
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}

package in.mitrevels.revels.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.models.events.ScheduleModel;

/**
 * Created by anurag on 15/2/17.
 */
public class CategoryEventsAdapter extends RecyclerView.Adapter<CategoryEventsAdapter.CategoryEventViewHolder> {

    private List<ScheduleModel> eventsList;
    private Activity activity;

    public CategoryEventsAdapter(List<ScheduleModel> eventsList, Activity activity) {
        this.eventsList = eventsList;
        this.activity = activity;
    }

    @Override
    public CategoryEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryEventViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_category_event, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryEventViewHolder holder, int position) {
        ScheduleModel schedule = eventsList.get(position);

        holder.eventName.setText(schedule.getEventName());
        holder.eventTime.setText(schedule.getStartTime());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class CategoryEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventTime;

        public CategoryEventViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView)itemView.findViewById(R.id.cat_event_name_text_view);
            eventTime = (TextView)itemView.findViewById(R.id.cat_event_time_text_view);
        }
    }
}

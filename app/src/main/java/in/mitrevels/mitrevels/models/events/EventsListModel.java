package in.mitrevels.mitrevels.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * Created by Naman on 7/6/2016.
 */
public class EventsListModel {
    @SerializedName("data")
    @Expose
    private List<EventDetailsModel> events;

    public EventsListModel() {
    }

    public List<EventDetailsModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventDetailsModel> events) {
        this.events = events;
    }
}

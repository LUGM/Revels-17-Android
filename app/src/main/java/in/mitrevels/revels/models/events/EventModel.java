package in.mitrevels.revels.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naman on 6/3/2016.
 */
public class EventModel {

    private String eventName;
    private String eventId;
    private String description;
    private String eventMaxTeamNumber;
    private String catName;
    private String catId;
    private String venue;
    private String startTime;
    private String endTime;
    private String day;
    private String date;
    private String contactName;
    private String contactNumber;
    private String hashtag1;
    private String hashtag2;

    public EventModel() {
    }

    public EventModel(EventDetailsModel eventDetails, ScheduleModel schedule){

        if (eventDetails != null){
            eventName = eventDetails.getEventName();
            eventId = eventDetails.getEventID();
            description = eventDetails.getDescription();
            eventMaxTeamNumber = eventDetails.getMaxTeamSize();
            catName = eventDetails.getCatName();
            catId = eventDetails.getCatID();
            contactName = eventDetails.getContactName();
            contactNumber = eventDetails.getContactNo();
            hashtag1 = eventDetails.getHs1();
            hashtag2 = eventDetails.getHs2();
        }

        if (schedule != null){
            venue = schedule.getVenue();
            startTime = schedule.getStartTime();
            endTime = schedule.getEndTime();
            day = schedule.getDay();
            date = schedule.getDate();
        }
    }

    public String getHashtag1() {
        return hashtag1;
    }

    public void setHashtag1(String hashtag1) {
        this.hashtag1 = hashtag1;
    }

    public String getHashtag2() {
        return hashtag2;
    }

    public void setHashtag2(String hashtag2) {
        this.hashtag2 = hashtag2;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getEventMaxTeamNumber() {
        return eventMaxTeamNumber;
    }

    public void setEventMaxTeamNumber(String eventMaxTeamNumber) {
        this.eventMaxTeamNumber = eventMaxTeamNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

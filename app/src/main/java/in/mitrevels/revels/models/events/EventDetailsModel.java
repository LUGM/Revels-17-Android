package in.mitrevels.revels.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by anurag on 1/10/16.
 */
public class EventDetailsModel extends RealmObject {

    @SerializedName("ename")
    @Expose
    private String eventName;
    @SerializedName("eid")
    @Expose
    private String eventID;
    @SerializedName("edesc")
    @Expose
    private String description;
    @SerializedName("emaxteamsize")
    @Expose
    private String maxTeamSize;
    @SerializedName("cid")
    @Expose
    private String catID;
    @SerializedName("cname")
    @Expose
    private String catName;
    @SerializedName("cntctname")
    @Expose
    private String contactName;
    @SerializedName("cntctno")
    @Expose
    private String contactNo;
    @SerializedName("hs1")
    @Expose
    private String hs1;
    @SerializedName("hs2")
    @Expose
    private String hs2;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(String maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    public String getHs1() {
        return hs1;
    }

    public void setHs1(String hs1) {
        this.hs1 = hs1;
    }

    public String getHs2() {
        return hs2;
    }

    public void setHs2(String hs2) {
        this.hs2 = hs2;
    }

}

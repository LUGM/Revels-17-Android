package in.revels.revels.models.instafeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naman on 8/31/2016.
 */
public class InstaFeedMeta {

    @SerializedName("pagination")
    @Expose

    private Pagination pagination;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    @SerializedName("data")
    @Expose
    private List<InstaFeedModel> feed;

    public InstaFeedMeta() {
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<InstaFeedModel> getFeed() {
        return feed;
    }

    public void setFeed(List<InstaFeedModel> feed) {
        this.feed = feed;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}

package in.mitrevels.mitrevels.models.sports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by anurag on 1/3/17.
 */
public class SportsListModel {
    @SerializedName("data")
    @Expose
    private List<SportsModel> data = new ArrayList<>();

    public List<SportsModel> getData() {
        return data;
    }

    public void setData(List<SportsModel> data) {
        this.data = data;
    }
}

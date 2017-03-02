package in.revels.revels.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 29/6/16.
 */
public class CategoriesListModel {

    @SerializedName("data")
    @Expose
    private List<CategoryModel> categoriesList = new ArrayList<>();

    public CategoriesListModel() {
    }

    public List<CategoryModel> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<CategoryModel> categoriesList) {
        this.categoriesList = categoriesList;
    }
}

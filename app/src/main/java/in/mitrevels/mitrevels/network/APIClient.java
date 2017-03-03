package in.mitrevels.mitrevels.network;

import in.mitrevels.mitrevels.models.categories.CategoriesListModel;
import in.mitrevels.mitrevels.models.events.EventsListModel;
import in.mitrevels.mitrevels.models.events.ScheduleListModel;
import in.mitrevels.mitrevels.models.results.ResultsListModel;
import in.mitrevels.mitrevels.models.sports.SportsListModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by anurag on 18/1/17.
 */
public class APIClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://api.mitportals.in/";

    public static APIInterface getAPIInterface(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(APIInterface.class);
    }

    public interface APIInterface{
        @GET("events")
        Call<EventsListModel> getEventsList();

        @GET("categories")
        Call<CategoriesListModel> getCategoriesList();

        @GET("results")
        Call<ResultsListModel> getResults();

        @GET("schedule")
        Call<ScheduleListModel> getScheduleList();

        @GET("sports")
        Call<SportsListModel> getSportsResults();
    }
}

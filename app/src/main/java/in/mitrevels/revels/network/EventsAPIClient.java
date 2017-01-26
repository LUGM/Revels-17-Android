package in.mitrevels.revels.network;

import in.mitrevels.revels.models.events.EventsListModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by anurag on 18/1/17.
 */
public class EventsAPIClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.myjson.com/";

    public static APIInterface getAPIInterface(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(APIInterface.class);
    }

    public interface APIInterface{
        @GET("bins/13s847")
        Call<EventsListModel> getEventsList();
    }
}

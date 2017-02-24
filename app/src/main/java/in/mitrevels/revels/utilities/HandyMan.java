package in.mitrevels.revels.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by anurag on 23/2/17.
 */
public class HandyMan {

    public HandyMan() {
    }

    public static HandyMan help(){
        return new HandyMan();
    }

    public boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

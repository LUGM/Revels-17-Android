package in.mitrevels.mitrevels.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.mitrevels.mitrevels.R;

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

    public int getCategoryLogo(String catID){
        if (catID == null) return R.mipmap.ic_launcher;
        switch(catID){
            case "2": return R.drawable.crescendo;
            case "3": return R.drawable.haute_couture;
            case "4": return R.drawable.kalakriti;
            case "5": return R.drawable.footloose;
            case "6": return R.drawable.dramebaaz;
            case "7": return R.drawable.xventure;
            case "8": return R.drawable.anubhuti;
            case "9": return R.drawable.lensation;
            case "10": return R.drawable.animania;
            case "11": return R.drawable.eq_iq;
            case "12": return R.drawable.iridescent;
            case "13": return R.drawable.paradigm_shift;
            case "14": return R.drawable.ergo;
            case "15": return R.drawable.psychus;
            case "16": return R.drawable.gaming;
            case "17": return R.drawable.sports;
            default: return  R.mipmap.ic_launcher;
        }
    }

    public int getCategoryLogoByName(String catName){
        if (catName == null) return R.mipmap.ic_launcher;
        switch(catName){
            case "crescendo": return R.drawable.crescendo;
            case "haute couture": return R.drawable.haute_couture;
            case "kalakriti": return R.drawable.kalakriti;
            case "footloose": return R.drawable.footloose;
            case "dramebaaz": return R.drawable.dramebaaz;
            case "xventure": return R.drawable.xventure;
            case "anubhuti": return R.drawable.anubhuti;
            case "lensation": return R.drawable.lensation;
            case "animania": return R.drawable.animania;
            case "eq-iq": return R.drawable.eq_iq;
            case "eq iq": return R.drawable.eq_iq;
            case "iridescent": return R.drawable.iridescent;
            case "paradigm shift": return R.drawable.paradigm_shift;
            case "ergo": return R.drawable.ergo;
            case "psychus": return R.drawable.psychus;
            case "gaming": return R.drawable.gaming;
            case "sports": return R.drawable.sports;
            default: return  R.mipmap.ic_launcher;
        }
    }
}

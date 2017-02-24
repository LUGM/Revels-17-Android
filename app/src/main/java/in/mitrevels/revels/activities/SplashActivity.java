package in.mitrevels.revels.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import in.mitrevels.revels.R;
import in.mitrevels.revels.models.events.EventDetailsModel;
import in.mitrevels.revels.models.events.EventsListModel;
import in.mitrevels.revels.models.events.ScheduleListModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import in.mitrevels.revels.network.APIClient;
import in.mitrevels.revels.utilities.HandyMan;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private Realm mDatabase;
    private boolean eventsLoaded = false;
    private boolean scheduleLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDatabase = Realm.getDefaultInstance();

        final ImageView icon = (ImageView)findViewById(R.id.splash_revels_icon);
        final ImageView text = (ImageView)findViewById(R.id.splash_revels_text);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.splash_progress_bar);

        icon.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_icon_anim));
        text.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_text_anim));

        if (mDatabase.where(ScheduleModel.class).findAll().size() == 0 && HandyMan.help().isInternetConnected(this)){
            progressBar.setVisibility(View.VISIBLE);

            APIClient.APIInterface apiInterface = APIClient.getAPIInterface();
            Call<EventsListModel> eventsCall = apiInterface.getEventsList();
            Call<ScheduleListModel> scheduleCall = apiInterface.getScheduleList();

            eventsCall.enqueue(new Callback<EventsListModel>() {
                @Override
                public void onResponse(Call<EventsListModel> call, Response<EventsListModel> response) {
                    if (response.body() != null && mDatabase != null){
                        mDatabase.beginTransaction();
                        mDatabase.where(EventDetailsModel.class).findAll().deleteAllFromRealm();
                        mDatabase.copyToRealm(response.body().getEvents());
                        mDatabase.commitTransaction();
                    }
                    eventsLoaded = true;
                    if (scheduleLoaded){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMain();
                            }
                        }, 1000);
                    }
                }

                @Override
                public void onFailure(Call<EventsListModel> call, Throwable t) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMain();
                        }
                    }, 1000);
                }
            });
            scheduleCall.enqueue(new Callback<ScheduleListModel>() {
                @Override
                public void onResponse(Call<ScheduleListModel> call, Response<ScheduleListModel> response) {
                    if (response.body() != null && mDatabase != null){
                        mDatabase.beginTransaction();
                        mDatabase.where(ScheduleModel.class).findAll().deleteAllFromRealm();
                        mDatabase.copyToRealm(response.body().getData());
                        mDatabase.commitTransaction();
                    }
                    scheduleLoaded = true;
                    if (eventsLoaded){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMain();
                            }
                        }, 1000);
                    }
                }

                @Override
                public void onFailure(Call<ScheduleListModel> call, Throwable t){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMain();
                        }
                    }, 1000);
                }
            });
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMain();
                }
            }, 2000);
        }

    }

    private void loadMain(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}

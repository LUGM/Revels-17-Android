package in.mitrevels.revels.activities;

import android.os.Build;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.CategoryEventsAdapter;
import in.mitrevels.revels.models.events.EventDetailsModel;
import in.mitrevels.revels.models.events.EventModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private String catName;
    private String catID;
    private String catDesc;
    private Realm mRealm;
    private TextView day1NoEvents;
    private TextView day2NoEvents;
    private TextView day3NoEvents;
    private TextView day4NoEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar)findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        catName = getIntent().getStringExtra("catName");
        if (catName == null) catName = "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(catName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        catID = getIntent().getStringExtra("catID");
        if (catID == null) catID = "";

        catDesc = getIntent().getStringExtra("catDesc");
        if (catDesc == null) catDesc = "";

        mRealm = Realm.getDefaultInstance();
        displayEvents();
    }

    private void displayEvents(){

        List<EventModel> day1List = new ArrayList<>();
        List<EventModel> day2List = new ArrayList<>();
        List<EventModel> day3List = new ArrayList<>();
        List<EventModel> day4List = new ArrayList<>();

        day1NoEvents = (TextView)findViewById(R.id.cat_day_1_no_events);
        day2NoEvents = (TextView)findViewById(R.id.cat_day_2_no_events);
        day3NoEvents = (TextView)findViewById(R.id.cat_day_3_no_events);
        day4NoEvents = (TextView)findViewById(R.id.cat_day_4_no_events);

        RealmResults<ScheduleModel> scheduleResults = mRealm.where(ScheduleModel.class).equalTo("catID", catID).findAllSorted("startTime");

        for (ScheduleModel schedule : scheduleResults){
            EventDetailsModel eventDetails = mRealm.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();

            EventModel event = new EventModel(eventDetails, schedule);
            switch(event.getDay()){
                case "1": day1List.add(event);
                    break;
                case "2": day2List.add(event);
                    break;
                case "3": day3List.add(event);
                    break;
                case "4": day4List.add(event);
                    break;
            }

        }

        RecyclerView day1RecyclerView = (RecyclerView)findViewById(R.id.category_day_1_recycler_view);

        if (day1List.isEmpty()){
            day1NoEvents.setVisibility(View.VISIBLE);
            day1RecyclerView.setVisibility(View.GONE);
        }
        else {
            day1RecyclerView.setAdapter(new CategoryEventsAdapter(day1List, this));
            day1RecyclerView.setNestedScrollingEnabled(false);
            day1RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView day2RecyclerView = (RecyclerView)findViewById(R.id.category_day_2_recycler_view);

        if (day2List.isEmpty()){
            day2NoEvents.setVisibility(View.VISIBLE);
            day2RecyclerView.setVisibility(View.GONE);
        }
        else{
            day2RecyclerView.setAdapter(new CategoryEventsAdapter(day2List, this));
            day2RecyclerView.setNestedScrollingEnabled(false);
            day2RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView day3RecyclerView = (RecyclerView)findViewById(R.id.category_day_3_recycler_view);

        if (day3List.isEmpty()){
            day3NoEvents.setVisibility(View.VISIBLE);
            day3RecyclerView.setVisibility(View.GONE);
        }
        else{
            day3RecyclerView.setAdapter(new CategoryEventsAdapter(day3List, this));
            day3RecyclerView.setNestedScrollingEnabled(false);
            day3RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView day4RecyclerView = (RecyclerView)findViewById(R.id.category_day_4_recycler_view);

        if (day4List.isEmpty()){
            day4NoEvents.setVisibility(View.VISIBLE);
            day4RecyclerView.setVisibility(View.GONE);
        }
        else{
            day4RecyclerView.setAdapter(new CategoryEventsAdapter(day4List, this));
            day4RecyclerView.setNestedScrollingEnabled(false);
            day4RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
                break;

            case R.id.about_category:
                View view = View.inflate(this, R.layout.dialog_about_category, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);

                TextView catNameTextView = (TextView)view.findViewById(R.id.cat_about_name_text_view);
                TextView catDescTextView = (TextView)view.findViewById(R.id.cat_about_description_text_view);

                catNameTextView.setText(catName);
                catDescTextView.setText(catDesc);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}


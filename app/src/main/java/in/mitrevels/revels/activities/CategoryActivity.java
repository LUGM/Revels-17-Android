package in.mitrevels.revels.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.adapters.CategoryEventsAdapter;
import in.mitrevels.revels.models.categories.CategoryModel;
import in.mitrevels.revels.models.events.ScheduleModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private String catName;
    private String catID;
    private Realm mRealm;

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

        mRealm = Realm.getDefaultInstance();
        displayEvents();
    }

    private void displayEvents(){

        List<ScheduleModel> day1Results = mRealm.copyFromRealm(mRealm.where(ScheduleModel.class).equalTo("catID", catID).equalTo("day", "1").findAll());
        List<ScheduleModel> day2Results = mRealm.copyFromRealm(mRealm.where(ScheduleModel.class).equalTo("catID", catID).equalTo("day", "2").findAll());
        List<ScheduleModel> day3Results = mRealm.copyFromRealm(mRealm.where(ScheduleModel.class).equalTo("catID", catID).equalTo("day", "3").findAll());
        List<ScheduleModel> day4Results = mRealm.copyFromRealm(mRealm.where(ScheduleModel.class).equalTo("catID", catID).equalTo("day", "4").findAll());
        
        RecyclerView day1RecyclerView = (RecyclerView)findViewById(R.id.category_day_1_recycler_view);
        day1RecyclerView.setAdapter(new CategoryEventsAdapter(day1Results, this));
        day1RecyclerView.setNestedScrollingEnabled(false);
        day1RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView day2RecyclerView = (RecyclerView)findViewById(R.id.category_day_2_recycler_view);
        day2RecyclerView.setAdapter(new CategoryEventsAdapter(day2Results, this));
        day2RecyclerView.setNestedScrollingEnabled(false);
        day2RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView day3RecyclerView = (RecyclerView)findViewById(R.id.category_day_3_recycler_view);
        day3RecyclerView.setAdapter(new CategoryEventsAdapter(day3Results, this));
        day3RecyclerView.setNestedScrollingEnabled(false);
        day3RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView day4RecyclerView = (RecyclerView)findViewById(R.id.category_day_4_recycler_view);
        day4RecyclerView.setAdapter(new CategoryEventsAdapter(day4Results, this));
        day4RecyclerView.setNestedScrollingEnabled(false);
        day4RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }
}


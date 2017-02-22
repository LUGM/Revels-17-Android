package in.mitrevels.revels.activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import in.mitrevels.revels.R;
import in.mitrevels.revels.models.FavouritesModel;
import io.realm.Realm;

public class EventActivity extends AppCompatActivity {

    private boolean isFavourited;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int primaryColor, darkColor;
    private LinearLayout headerLayout;
    private ImageView logo;
    private AppBarLayout appBarLayout;
    private FloatingActionMenu fabMenu;
    private FloatingActionButton favFab;
    private Realm mRealm;
    private String title;
    private String id;
    private String catID;
    private String venue;
    private String date;
    private String day;
    private String startTime;
    private String endTime;
    private String teamOf;
    private String contactName;
    private String contactNumber;
    private String category;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.event_collapsing_toolbar);
        appBarLayout = (AppBarLayout)findViewById(R.id.event_app_bar_layout);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.event_coordinator_layout);
        headerLayout = (LinearLayout)findViewById(R.id.event_header_layout);

        logo = (ImageView)findViewById(R.id.event_cat_logo);

        fabMenu = (FloatingActionMenu)findViewById(R.id.event_fab_menu);
        fabMenu.setClosedOnTouchOutside(true);

        favFab = (FloatingActionButton)findViewById(R.id.fav_fab);

        mRealm = Realm.getDefaultInstance();

        isFavourited = getIntent().getBooleanExtra("Favourite", false);

        id = getIntent().getStringExtra("Event ID");
        title = getIntent().getStringExtra("Event Name");
        date = getIntent().getStringExtra("Event Date");
        day = getIntent().getStringExtra("Event Day");
        startTime = getIntent().getStringExtra("Event Start Time");
        endTime = getIntent().getStringExtra("Event End Time");
        venue = getIntent().getStringExtra("Event Venue");
        teamOf = getIntent().getStringExtra("Team Of");
        category = getIntent().getStringExtra("Event Category");
        catID = getIntent().getStringExtra("Category ID");
        contactNumber = getIntent().getStringExtra("Contact Number");
        contactName = getIntent().getStringExtra("Contact Name");
        description = getIntent().getStringExtra("Event Description");

        Boolean enableFavourite = getIntent().getBooleanExtra("enableFavourite", true);
        if (!enableFavourite) fabMenu.setVisibility(View.GONE);

        if (title!=null && !title.equals(""))
            collapsingToolbarLayout.setTitle(title);
        else  collapsingToolbarLayout.setTitle("");

        TextView eventDate = (TextView)findViewById(R.id.event_date_text_view);
        if (date!=null) eventDate.setText(date);

        TextView eventTime = (TextView)findViewById(R.id.event_time_text_view);
        if (startTime!=null && endTime!=null) eventTime.setText(startTime+" - "+endTime);

        TextView eventVenue = (TextView)findViewById(R.id.event_venue_text_view);
        if (venue!=null) eventVenue.setText(venue);

        TextView eventTeamOf = (TextView)findViewById(R.id.event_team_of_text_view);
        if (teamOf!=null) eventTeamOf.setText(teamOf);

        TextView eventCategory = (TextView)findViewById(R.id.event_category_text_view);
        if (category!=null) eventCategory.setText(category);

        TextView eventContactNumber = (TextView)findViewById(R.id.event_contact_number_text_view);
        if (contactNumber!=null) eventContactNumber.setText(contactNumber);

        TextView eventContactName = (TextView)findViewById(R.id.event_contact_name_text_view);
        if (contactName!=null) eventContactName.setText(contactName);

        TextView eventDescription = (TextView)findViewById(R.id.event_description_text_view);
        if (description!=null) eventDescription.setText(description);

        Bitmap bitmap = null;

        if (getIntent().getIntExtra("Category Logo", 0) == 0) {
            logo.setImageResource(R.drawable.tt_aero);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tt_aero);
        }
        else{
            logo.setImageResource(R.drawable.tt_kraftwagen);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tt_kraftwagen);
        }

        // Code for dynamic colouring of toolbar, status bar and navigation bar

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                primaryColor = palette.getDominantColor(ContextCompat.getColor(EventActivity.this, R.color.teal));

                int a = Color.alpha(primaryColor);
                int r = Math.round(Color.red(primaryColor) * 0.8f);
                int g = Math.round(Color.green(primaryColor) * 0.8f);
                int b = Math.round(Color.blue(primaryColor) * 0.8f);
                darkColor = Color.argb(a, Math.min(r,255), Math.min(g,255), Math.min(b,255));

                collapsingToolbarLayout.setContentScrimColor(primaryColor);
                collapsingToolbarLayout.setStatusBarScrimColor(darkColor);
                headerLayout.setBackgroundColor(primaryColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.setStatusBarColor(darkColor);
                    window.setNavigationBarColor(primaryColor);
                }
            }
        });

        if (mRealm.where(FavouritesModel.class).equalTo("id", id).findAll().size() != 0){
            favFab.setImageResource(R.drawable.ic_fav_deselected);
            favFab.setColorNormal(ContextCompat.getColor(this, R.color.grey));
            favFab.setColorPressed(ContextCompat.getColor(this, R.color.grey_medium));
            favFab.setColorRipple(ContextCompat.getColor(this, R.color.grey_dark));
            favFab.setLabelText(getString(R.string.remove_from_favourites));
        }
        else{
            favFab.setImageResource(R.drawable.ic_fav_selected);
            favFab.setColorNormal(ContextCompat.getColor(this, R.color.red));
            favFab.setColorPressed(ContextCompat.getColor(this, R.color.red_medium));
            favFab.setColorRipple(ContextCompat.getColor(this, R.color.red_dark));
            favFab.setLabelText(getString(R.string.add_to_favourites));
        }

        favFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrRemoveFavourite();
            }
        });

    }

    private void addOrRemoveFavourite(){
        if (mRealm == null) return;

        mRealm.beginTransaction();
        if (mRealm.where(FavouritesModel.class).equalTo("id", id).findAll().size() == 0){
            favFab.setImageResource(R.drawable.ic_fav_deselected);
            favFab.setLabelText(getString(R.string.remove_from_favourites));
            favFab.setColorNormal(ContextCompat.getColor(this, R.color.grey));
            favFab.setColorPressed(ContextCompat.getColor(this, R.color.grey_medium));
            favFab.setColorRipple(ContextCompat.getColor(this, R.color.grey_dark));

            FavouritesModel favourite = new FavouritesModel();
            favourite.setId(id);
            favourite.setEventName(title);
            favourite.setCatID(catID);
            favourite.setCatName(category);
            favourite.setDate(date);
            favourite.setDay(day);
            favourite.setVenue(venue);
            favourite.setStartTime(startTime);
            favourite.setEndTime(endTime);
            favourite.setDescription(description);
            favourite.setParticipants(teamOf);
            favourite.setContactName(contactName);
            favourite.setContactNumber(contactNumber);
            mRealm.copyToRealm(favourite);

            Snackbar.make(coordinatorLayout, title+" added to favourites!", Snackbar.LENGTH_SHORT).show();

        }
        else{
            favFab.setImageResource(R.drawable.ic_fav_selected);
            favFab.setColorNormal(ContextCompat.getColor(this, R.color.red));
            favFab.setColorPressed(ContextCompat.getColor(this, R.color.red_medium));
            favFab.setColorRipple(ContextCompat.getColor(this, R.color.red_dark));
            favFab.setLabelText(getString(R.string.add_to_favourites));

            mRealm.where(FavouritesModel.class).equalTo("id", id).findAll().deleteAllFromRealm();

            Snackbar.make(coordinatorLayout, title+" removed from favourites!", Snackbar.LENGTH_SHORT).show();
        }
        mRealm.commitTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //This enables shared element transition on Up Navigation
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isOpened()) fabMenu.close(true);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.onBackPressed();
        else{
            finish();
            overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }
}

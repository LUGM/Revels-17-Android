package in.mitrevels.revels.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import in.mitrevels.revels.R;

public class EventActivity extends AppCompatActivity {

    private boolean isFavourited;
    private String title;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int primaryColor, darkColor;
    private LinearLayout headerLayout;
    private ImageView logo;
    private LinearLayout favouriteLayout;
    private TextView favouriteTextView;
    private ImageView favouriteIcon;
    private AppBarLayout appBarLayout;
    private FloatingActionButton favouriteFab;
    private FloatingActionMenu fabMenu;

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

        //favouriteFab = (FloatingActionButton)findViewById(R.id.event_favourite_fab);

        /*favouriteLayout = (LinearLayout)findViewById(R.id.event_favourite_layout);
        favouriteTextView = (TextView)findViewById(R.id.event_fav_text_view);
        favouriteIcon = (ImageView)findViewById(R.id.event_fav_image_view);*/

        isFavourited = getIntent().getBooleanExtra("Favourite", false);

        /*if (isFavourited){
            favouriteTextView.setText(getResources().getString(R.string.remove_from_favourites));
            favouriteIcon.setImageResource(R.drawable.ic_fav_deselected);

            favouriteFab.setImageResource(R.drawable.ic_fav_deselected);
            favouriteFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
            favouriteFab.setRippleColor(ContextCompat.getColor(this, R.color.grey_dark));
        }
        else{
            favouriteTextView.setText(getResources().getString(R.string.add_to_favourites));
            favouriteIcon.setImageResource(R.drawable.ic_fav_selected);

            favouriteFab.setImageResource(R.drawable.ic_fav_selected);
            favouriteFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            favouriteFab.setRippleColor(ContextCompat.getColor(this, R.color.red_dark));
        }*/


        title = getIntent().getStringExtra("Event Name");
        String date = getIntent().getStringExtra("Event Date");
        String time = getIntent().getStringExtra("Event Time");
        String venue = getIntent().getStringExtra("Event Venue");
        String teamOf = getIntent().getStringExtra("Team Of");
        String category = getIntent().getStringExtra("Event Category");
        String contactNumber = getIntent().getStringExtra("Contact Number");
        String contactName = getIntent().getStringExtra("Contact Name");
        String description = getIntent().getStringExtra("Event Description");

        if (title!=null && !title.equals(""))
            collapsingToolbarLayout.setTitle(title);
        else  collapsingToolbarLayout.setTitle("");

        TextView eventDate = (TextView)findViewById(R.id.event_date_text_view);
        if (date!=null) eventDate.setText(date);

        TextView eventTime = (TextView)findViewById(R.id.event_time_text_view);
        if (time!=null) eventTime.setText(time);

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

        /*favouriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourited){
                    favouriteFab.setImageResource(R.drawable.ic_fav_selected);
                    favouriteFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(EventActivity.this, R.color.red)));
                    favouriteFab.setRippleColor(ContextCompat.getColor(EventActivity.this, R.color.red_dark));
                    isFavourited = false;
                    if (title!=null) Snackbar.make(coordinatorLayout, title+" removed from favourites!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    favouriteFab.setImageResource(R.drawable.ic_fav_deselected);
                    favouriteFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(EventActivity.this, R.color.grey)));
                    favouriteFab.setRippleColor(ContextCompat.getColor(EventActivity.this, R.color.grey_dark));
                    isFavourited = true;
                    if (title!=null) Snackbar.make(coordinatorLayout, title+" added to favourites!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });*/

        /*favouriteLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isFavourited){
                    favouriteTextView.setText(getResources().getString(R.string.add_to_favourites));
                    favouriteIcon.setImageResource(R.drawable.ic_fav_selected);
                    isFavourited = false;
                    if (title!=null) Snackbar.make(coordinatorLayout, title+" removed from favourites!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    favouriteTextView.setText(getResources().getString(R.string.remove_from_favourites));
                    favouriteIcon.setImageResource(R.drawable.ic_fav_deselected);
                    isFavourited = true;
                    if (title!=null) Snackbar.make(coordinatorLayout, title+" added to favourites!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });*/

        /*appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //Collapsed
                    favouriteLayout.setVisibility(View.GONE);
                }
                else
                {
                    //Expanded
                   favouriteLayout.setVisibility(View.VISIBLE);
                }
            }
        });*/

    }

}

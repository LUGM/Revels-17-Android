package in.mitrevels.revels.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.mitrevels.revels.R;

public class EventActivity extends AppCompatActivity {

    private boolean isFavourited;
    private String title;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int primaryColor, darkColor;
    private LinearLayout headerLayout;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.event_collapsing_toolbar);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.event_coordinator_layout);
        headerLayout = (LinearLayout)findViewById(R.id.event_header_layout);

        logo = (ImageView)findViewById(R.id.event_cat_logo);

        isFavourited = getIntent().getBooleanExtra("Favourite", false);

        title = getIntent().getStringExtra("Event Name");
        String date = getIntent().getStringExtra("Event Date");
        String time = getIntent().getStringExtra("Event Time");
        String venue = getIntent().getStringExtra("Event Venue");
        String teamOf = getIntent().getStringExtra("Team Of");
        String category = getIntent().getStringExtra("Event Category");
        String contact = getIntent().getStringExtra("Event Contact");
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

        TextView eventContact = (TextView)findViewById(R.id.event_contact_text_view);
        if (contact!=null) eventContact.setText(contact);

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

    }

}

package in.mitrevels.revels.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mitrevels.revels.R;
import in.mitrevels.revels.fragments.CategoriesFragment;
import in.mitrevels.revels.fragments.EventsFragment;
import in.mitrevels.revels.fragments.FavouritesFragment;
import in.mitrevels.revels.fragments.InstagramFragment;
import in.mitrevels.revels.fragments.ResultsFragment;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private int checkedItem;
    private static final String EVENTS_TAG = "Events Fragment";
    private static final String FAVOURITES_TAG = "Favourites Fragment";
    private static final String INSTAGRAM_TAG = "InstaFeed Fragment";
    private static final String CATEGORIES_TAG = "Categories Fragment";
    private static final String RESULTS_TAG = "Results Fragment";
    private int CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout)findViewById(R.id.main_app_bar_layout);

        if (getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                toolbar.setElevation(0);
                appBarLayout.setElevation(0);
                appBarLayout.setTargetElevation(0);
            }
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fm = getSupportFragmentManager();

        if (fm.findFragmentByTag(EVENTS_TAG) == null){
            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new EventsFragment(), EVENTS_TAG).commit();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.drawer_menu_events).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        drawer.closeDrawers();
        if (item.getItemId() == checkedItem)
            return false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                switch(item.getItemId()){
                    case R.id.drawer_menu_events:

                        if (fm.findFragmentByTag(EVENTS_TAG) == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new EventsFragment(), EVENTS_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(EVENTS_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_events);
                        break;

                    case R.id.drawer_menu_favourites:

                        if (fm.findFragmentByTag(FAVOURITES_TAG) == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new FavouritesFragment(), FAVOURITES_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(FAVOURITES_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_favourites);
                        break;

                    case R.id.drawer_menu_insta:
                        if (fm.findFragmentByTag(INSTAGRAM_TAG) == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new InstagramFragment(), INSTAGRAM_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(INSTAGRAM_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_insta);
                        break;

                    case R.id.drawer_menu_categories:
                        if (fm.findFragmentByTag(CATEGORIES_TAG) == null){
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new CategoriesFragment(), CATEGORIES_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(CATEGORIES_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_categories);
                        break;

                    case R.id.drawer_menu_results:
                        if (fm.findFragmentByTag(RESULTS_TAG) == null){
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new ResultsFragment(), RESULTS_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(RESULTS_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_results);
                        break;

                    case R.id.drawer_menu_developers:
                        Intent developersIntent = new Intent(MainActivity.this, DevelopersActivity.class);
                        startActivity(developersIntent);
                        break;

                    case R.id.drawer_menu_about:
                        Intent aboutIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(aboutIntent);
                        break;
                }
            }
        }, 320);

        return false;
    }

    private void setCheckedItem(int id){
        if (navigationView == null) return;
        checkedItem = id;

        for (int i=0; i<navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        navigationView.getMenu().findItem(id).setChecked(true);
    }
}

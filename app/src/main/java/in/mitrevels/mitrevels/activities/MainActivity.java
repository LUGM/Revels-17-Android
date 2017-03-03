package in.mitrevels.mitrevels.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.fragments.CategoriesFragment;
import in.mitrevels.mitrevels.fragments.EventsFragment;
import in.mitrevels.mitrevels.fragments.FavouritesFragment;
import in.mitrevels.mitrevels.fragments.ResultsFragment;
import in.mitrevels.mitrevels.fragments.RevelsCupFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private int checkedItem;
    private static final String EVENTS_TAG = "Events Fragment";
    private static final String FAVOURITES_TAG = "Favourites Fragment";
    private static final String CATEGORIES_TAG = "Categories Fragment";
    private static final String RESULTS_TAG = "Results Fragment";
    private static final String REVELS_CUP_TAG = "Revels Cup Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.revels));

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_app_bar_layout);

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
            fm.beginTransaction().replace(R.id.main_container, new EventsFragment(), EVENTS_TAG).commit();
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
            if (navigationView.getMenu().getItem(0).isChecked()){
                super.onBackPressed();
            }
            else{
                if (fm.findFragmentByTag(EVENTS_TAG) == null) {
                    fm.beginTransaction().replace(R.id.main_container, new EventsFragment(), EVENTS_TAG).commit();
                }
                else{
                    fm.beginTransaction().replace(R.id.main_container, fm.findFragmentByTag(EVENTS_TAG)).commit();
                }

                setCheckedItem(R.id.drawer_menu_events);
            }
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

                    case R.id.drawer_menu_revels_cup:
                        if (fm.findFragmentByTag(REVELS_CUP_TAG) == null){
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new RevelsCupFragment(), REVELS_CUP_TAG).commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag(REVELS_CUP_TAG)).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_revels_cup);
                        break;

                    case R.id.drawer_menu_proshow:
                        Intent proshowIntent = new Intent(MainActivity.this, ProshowActivity.class);
                        startActivity(proshowIntent);
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

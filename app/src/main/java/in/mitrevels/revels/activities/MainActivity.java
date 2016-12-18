package in.mitrevels.revels.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.mitrevels.revels.R;
import in.mitrevels.revels.fragments.DevelopersFragment;
import in.mitrevels.revels.fragments.EventsFragment;
import in.mitrevels.revels.fragments.FavouritesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

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

        if (fm.findFragmentByTag("Events Fragment") == null){
            fm.beginTransaction().replace(R.id.main_container, new EventsFragment(), "Events Fragment").commit();
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayout.setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
                    toolbar.setElevation((4 * getResources().getDisplayMetrics().density + 0.5f));
                }

                switch(item.getItemId()){
                    case R.id.drawer_menu_events:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            toolbar.setElevation(0);
                            appBarLayout.setElevation(0);
                            appBarLayout.setTargetElevation(0);
                        }

                        if (fm.findFragmentByTag("Events Fragment") == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new EventsFragment(), "Events Fragment").commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag("Events Fragment")).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_events);
                        break;

                    case R.id.drawer_menu_favourites:
                        if (fm.findFragmentByTag("Events Fragment") != null)
                            fm.findFragmentByTag("Events Fragment").setMenuVisibility(false);

                        if (fm.findFragmentByTag("Favourites Fragment") == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new FavouritesFragment(), "Favourites Fragment").commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag("Favourites Fragment")).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_favourites);
                        break;

                    case R.id.drawer_menu_developers:
                        if (fm.findFragmentByTag("Events Fragment") != null)
                            fm.findFragmentByTag("Events Fragment").setMenuVisibility(false);

                        if (fm.findFragmentByTag("Developers Fragment") == null) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, new DevelopersFragment(), "Developers Fragment").commit();
                        }
                        else{
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, R.anim.blank).replace(R.id.main_container, fm.findFragmentByTag("Developers Fragment")).commit();
                        }

                        setCheckedItem(R.id.drawer_menu_developers);
                        break;
                }
            }
        }, 280);

        return false;
    }

    private void setCheckedItem(int id){
        if (navigationView == null) return;

        for (int i=0; i<navigationView.getMenu().size(); i++)
            navigationView.getMenu().getItem(i).setChecked(false);

        navigationView.getMenu().findItem(id).setChecked(true);
    }


}

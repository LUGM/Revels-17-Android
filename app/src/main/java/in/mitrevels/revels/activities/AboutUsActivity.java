package in.mitrevels.revels.activities;

import android.animation.Animator;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.github.clans.fab.FloatingActionMenu;

import in.mitrevels.revels.R;

public class AboutUsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TableLayout linksTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.about_collapsing_toolbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.about_toolbar);

        fab = (FloatingActionButton) findViewById(R.id.about_fab);
        linksTableLayout = (TableLayout)findViewById(R.id.about_links_table_layout);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getResources().getString(R.string.about_revels));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        int cx = linksTableLayout.getWidth()/2;
                        int cy = linksTableLayout.getHeight()/2;

                        float finalRadius = (float) Math.hypot(cx, cy);

                        Animator anim = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            anim = ViewAnimationUtils.createCircularReveal(linksTableLayout, cx, cy, 0, finalRadius);

                        if (anim!=null)
                            anim.start();
                        linksTableLayout.setVisibility(View.VISIBLE);
                    }
                });


            }
        });

    }
}

package in.mitrevels.revels.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionMenu;

import in.mitrevels.revels.R;
import in.mitrevels.revels.fragments.SnapchatFragment;

public class AboutUsActivity extends AppCompatActivity {

    private static final String FB_URL = "http://www.facebook.com/mitrevels";
    private static final String INSTA_URL = "http://www.instagram.com/revelsmit";
    private static final String YOUTUBE_URL = "http://www.youtube.com/channel/UC9gwWd47a0q042qwEgutjWw";
    private static final String TWITTER_URL = "http://twitter.com/revelsmit";
    private FloatingActionMenu fabMenu;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private LinearLayout linksToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.about_collapsing_toolbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.about_toolbar);
        appBarLayout = (AppBarLayout)findViewById(R.id.about_app_bar_layout);

        fabMenu = (FloatingActionMenu)findViewById(R.id.about_fab_menu);

        linksToolbar = (LinearLayout)findViewById(R.id.about_links_linear_layout);

        ImageView fbLink = (ImageView)findViewById(R.id.fb_link_image_view);
        ImageView instaLink = (ImageView)findViewById(R.id.insta_link_image_view);
        ImageView snapchatLink = (ImageView)findViewById(R.id.snapchat_link_image_view);
        ImageView youtubeLink = (ImageView)findViewById(R.id.youtube_link_image_view);
        ImageView twitterLink = (ImageView)findViewById(R.id.twitter_link_image_view);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getResources().getString(R.string.about_us));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= (appBarLayout.getTotalScrollRange()/2)){
                    //Half collapsed
                    if (!fabMenu.isMenuHidden())
                        fabMenu.hideMenu(true);
                }
                else{
                    if (fabMenu.isMenuHidden())
                        fabMenu.showMenu(true);
                }
            }
        });

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened){
                    linksToolbar.setVisibility(View.VISIBLE);
                    linksToolbar.startAnimation(AnimationUtils.loadAnimation(AboutUsActivity.this, R.anim.slide_up_from_bottom));
                }
                else{
                    linksToolbar.startAnimation(AnimationUtils.loadAnimation(AboutUsActivity.this, R.anim.slide_out_down));
                    linksToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

        fbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(FB_URL);
            }
        });

        instaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(INSTA_URL);
            }
        });

        youtubeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(YOUTUBE_URL);
            }
        });

        twitterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(TWITTER_URL);
            }
        });

        snapchatLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment =new SnapchatFragment();
                fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                fragment.show(getSupportFragmentManager(),"fragment_snapchat");
            }
        });

    }

    private void openLink(String url){
        Intent socialIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(socialIntent);
        fabMenu.close(true);
    }
}

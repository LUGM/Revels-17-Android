package in.mitrevels.revels.activities;

import android.animation.Animator;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import in.mitrevels.revels.R;

public class TrendingActivity extends AppCompatActivity {

    private CoordinatorLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.hold, R.anim.hold);
        setContentView(R.layout.activity_trending);

        rootLayout = (CoordinatorLayout) findViewById(R.id.trending_root_layout);

    }
}

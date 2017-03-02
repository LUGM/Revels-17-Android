package in.revels.revels.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.revels.revels.R;
import in.revels.revels.utilities.HandyMan;

public class ProshowActivity extends AppCompatActivity {

    private static final String PROSHOW_PORTAL_URL = "http://proshow.mitrevels.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proshow);

        Toolbar toolbar = (Toolbar)findViewById(R.id.proshow_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(R.string.drawer_proshow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.proshow_coordinator_layout);

        final WebView proshowWebView = (WebView)findViewById(R.id.proshow_portal_web_view);
        WebSettings mWebSettings = proshowWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        proshowWebView.setWebChromeClient(new WebChromeClient());

        WebViewClient mWebViewClient = new WebViewClient();
        proshowWebView.setWebViewClient(mWebViewClient);

        LinearLayout noConnectionLayout = (LinearLayout)findViewById(R.id.proshow_no_connection_layout);
        TextView retry = (TextView)noConnectionLayout.findViewById(R.id.no_connection_retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandyMan.help().isInternetConnected(ProshowActivity.this)){
                    proshowWebView.loadUrl(PROSHOW_PORTAL_URL);
                }
                else{
                    Snackbar.make(rootLayout, "Check connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        if (HandyMan.help().isInternetConnected(this)){
            proshowWebView.loadUrl(PROSHOW_PORTAL_URL);
        }
        else{
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }
}

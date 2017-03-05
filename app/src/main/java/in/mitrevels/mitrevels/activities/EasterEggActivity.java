package in.mitrevels.mitrevels.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import in.mitrevels.mitrevels.R;

public class EasterEggActivity extends AppCompatActivity {

    private static final String QNAX_ZRZF = "http://www.facebook.com/qnaxzrzrf";
    private static final String LUG_LINK = "http://www.lugm.xyz";
    private static final String LUG_FB = "http://www.facebook.com/LUGManipal";
    private static final String LUG_GITHUB = "http://www.github.com/LUGM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        ViewFlipper viewFlipper = (ViewFlipper)findViewById(R.id.easter_egg_view_flipper);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(8000);
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);

        LinearLayout qnaxZrzfLayout = (LinearLayout)findViewById(R.id.qnax_zrzf_layout);
        qnaxZrzfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(QNAX_ZRZF);
            }
        });

        ImageView lugTux = (ImageView)findViewById(R.id.lug_tux);
        TextView lugLink = (TextView)findViewById(R.id.lug_link);

        View.OnClickListener lugLinkOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(LUG_LINK);
            }
        };

        lugLink.setOnClickListener(lugLinkOnClickListener);
        lugTux.setOnClickListener(lugLinkOnClickListener);
    }

    private void openLink(String url){
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(urlIntent);
    }
}

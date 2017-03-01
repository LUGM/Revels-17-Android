package in.mitrevels.revels.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import in.mitrevels.revels.R;

/**
 * Created by anurag on 14/12/16.
 */
public class DevelopersActivity extends AppCompatActivity {

    public DevelopersActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        Toolbar toolbar = (Toolbar)findViewById(R.id.developers_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getResources().getString(R.string.drawer_developers));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}

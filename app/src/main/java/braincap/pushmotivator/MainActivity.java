package braincap.pushmotivator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Add ripple in Wall Button

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);

        Button btnWall = (Button) findViewById(R.id.btn_wall);
        if (btnWall != null) {
            btnWall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    QuoteWallFragment quoteWallFragment = new QuoteWallFragment();
                    fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.myfrag, quoteWallFragment, "FRAGWALL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });
        }

        /*
        RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();
        */
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
package braincap.pushmotivator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity implements AuthorFragment.GiveAuthorToActivityListener {
    private static final String TAG = "JT";

    public static QuoteWallFragment newInstance(String mAuthorName) {
        QuoteWallFragment quoteWallFragment = new QuoteWallFragment();

        Bundle args = new Bundle();
        args.putString("1", mAuthorName);
        quoteWallFragment.setArguments(args);
        Log.d(TAG, "newInstance: " + quoteWallFragment.getArguments().getString("1", null));
        return quoteWallFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Add ripple in Wall Button

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);

        Button btnWall = (Button) findViewById(R.id.btn_wall);
        Button btnAuth = (Button) findViewById(R.id.btn_auth);
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

        if (btnAuth != null) {
            btnAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AuthorFragment authorFragment = new AuthorFragment();
                    authorFragment.setGiveAuthorToActivityListener(MainActivity.this);
                    fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.myfrag, authorFragment, "FRAGAUTH");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void giveAuthorToActivity(String mAuthorName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuoteWallFragment quoteWallFragment = newInstance(mAuthorName);
        fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.myfrag, quoteWallFragment, "FRAGWALL");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
package braincap.pushmotivator;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.notifier.NotifierService;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity implements AuthorFragment.GiveAuthorToActivityListener
        , TopicFragment.GiveTopicToActivityListener
        , View.OnClickListener
//        , FragmentManager.OnBackStackChangedListener


{
    private static final long FLIP_DELAY = 5000;
    private static final String TAG = "JT";
    Button btnTopic;
    Button btnAuthor;
    Button btnWall;
    ArrayList<Integer> authorImageIds;
    ArrayList<Integer> topicImageIds;
    Animation outAnimationAuth;
    Animation inAnimationAuth;
    Animation outAnimationTopic;
    Animation inAnimationTopic;
    ImageView ivTopic;
    ImageView ivAuthor;
    ImageView ivWall;
    Button stopButton;
    FragmentManager fragmentManager;

    public static QuoteWallFragment newInstance(String inputType, String inputName) {
        QuoteWallFragment quoteWallFragment = new QuoteWallFragment();

        Bundle args = new Bundle();
        args.putString(inputType, inputName);
        quoteWallFragment.setArguments(args);
        return quoteWallFragment;
    }

    @Override
    protected void onPause() {
        super.onPause();
        btnAuthor.removeCallbacks(null);
        btnTopic.removeCallbacks(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (getIntent() != null && getIntent().hasExtra("Quote") && getIntent().hasExtra("Author")) {
            onQuoteClicked(getIntent().getStringExtra("Quote"), getIntent().getStringExtra("Author"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged: LOL");
            }
        });
//        fragmentManager.addOnBackStackChangedListener(this);

        outAnimationAuth = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        inAnimationAuth = AnimationUtils.loadAnimation(this, R.anim.fadein);
        outAnimationTopic = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        inAnimationTopic = AnimationUtils.loadAnimation(this, R.anim.fadein);

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
        authorImageIds = new ArrayList<>();
        authorImageIds.add(R.drawable.img_author_1);
        authorImageIds.add(R.drawable.img_author_2);
        authorImageIds.add(R.drawable.img_author_3);
        authorImageIds.add(R.drawable.img_author_4);
        authorImageIds.add(R.drawable.img_author_5);
        authorImageIds.add(R.drawable.img_author_6);

        topicImageIds = new ArrayList<>();
        topicImageIds.add(R.drawable.img_topic_1);
        topicImageIds.add(R.drawable.img_topic_2);
        topicImageIds.add(R.drawable.img_topic_3);
        topicImageIds.add(R.drawable.img_topic_4);
        topicImageIds.add(R.drawable.img_topic_5);
        topicImageIds.add(R.drawable.img_topic_6);
        topicImageIds.add(R.drawable.img_topic_7);
        topicImageIds.add(R.drawable.img_topic_8);

        btnWall = (Button) findViewById(R.id.btn_wall);
        btnAuthor = (Button) findViewById(R.id.btn_author);
        btnTopic = (Button) findViewById(R.id.btn_topic);

        ivTopic = (ImageView) findViewById(R.id.iv_topic);
        ivAuthor = (ImageView) findViewById(R.id.iv_author);
        ivWall = (ImageView) findViewById(R.id.iv_wall);
        stopButton = (Button) findViewById(R.id.btn_stop);

        if (stopButton != null) {
            stopButton.setOnClickListener(this);
        }

        Glide.with(this).load(topicImageIds.get(0)).fitCenter().into(ivTopic);
        Glide.with(this).load(authorImageIds.get(0)).fitCenter().into(ivAuthor);
        Glide.with(this).load(R.drawable.img_wall).centerCrop().into(ivWall);

        btnTopic.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateAuthButtonImage();
                btnTopic.postDelayed(this, FLIP_DELAY);
            }
        }, FLIP_DELAY);


        if (btnWall != null) {
            btnWall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuoteWallFragment quoteWallFragment = new QuoteWallFragment();
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.myfrag, quoteWallFragment, "FRAGWALL")
                            .addToBackStack(null)
                            .commit();
                    Log.d(TAG, "onClick: " + fragmentManager.getBackStackEntryCount());
                }
            });
        }

        if (btnAuthor != null) {
            btnAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorFragment authorFragment = new AuthorFragment();
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.myfrag, authorFragment, "FRAGAUTH")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (btnTopic != null) {
            btnTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicFragment topicFragment = new TopicFragment();
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.myfrag, topicFragment, "FRAGTOPIC")
                            .addToBackStack(null)
                            .commit();
                    Log.d(TAG, "onClick: " + fragmentManager.getBackStackEntryCount());
                }
            });
        }
    }


    private void updateAuthButtonImage() {
        try {
            int prevAuthorId = authorImageIds.get(0);
            int prevTopicId = topicImageIds.get(0);
            Collections.shuffle(authorImageIds);
            while (prevAuthorId == authorImageIds.get(0)) {
                Collections.shuffle(authorImageIds);
            }
            outAnimationAuth.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Glide.with(MyApplication.getContext()).load(authorImageIds.get(0)).fitCenter().dontAnimate().into(ivAuthor);
                    ivAuthor.startAnimation(inAnimationAuth);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            ivAuthor.startAnimation(outAnimationAuth);

            Collections.shuffle(topicImageIds);
            while (prevTopicId == topicImageIds.get(0)) {
                Collections.shuffle(topicImageIds);
            }
            outAnimationTopic.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Glide.with(MyApplication.getContext()).load(topicImageIds.get(0)).fitCenter().dontAnimate().into(ivTopic);
                    ivTopic.startAnimation(inAnimationTopic);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            ivTopic.startAnimation(outAnimationTopic);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void giveAuthorToActivity(String mAuthorName) {
        QuoteWallFragment quoteWallFragment = newInstance("AUTHOR", mAuthorName);
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.myfrag, quoteWallFragment, "FRAGWALL")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void giveTopicToActivity(String mTopicName) {
        QuoteWallFragment quoteWallFragment = newInstance("TOPIC", mTopicName);
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.myfrag, quoteWallFragment, "FRAGWALL")
                .addToBackStack(null)
                .commit();
    }

    public void onQuoteClicked(String quote, String author) {
        Log.d(TAG, "onQuoteClicked: " + quote);
        QuoteDetailsFragment quoteDetailsFragment = new QuoteDetailsFragment();
        quoteDetailsFragment.setQuote(quote);
        quoteDetailsFragment.setAuthor(author);
        quoteDetailsFragment.show(fragmentManager, "QUOTEDETAILSFRAG");
    }

    @Override
    public void onClick(View v) {
//        stopButton.setVisibility(View.GONE);
//        MyApplication.writeViewVisibility(View.GONE);
        Log.d(TAG, "onClick: " + fragmentManager.getBackStackEntryCount());
        PendingIntent.getService(this, 2, new Intent(this, NotifierService.class), PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }

    

/*    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "onBackStackChanged: ");
        if (fragmentManager.getBackStackEntryCount() == 0) {
            Log.d(TAG, "onBackStackChanged: 1");
            //noinspection ResourceType
            stopButton.setVisibility(MyApplication.readViewVisibility());
        }
    }
*/

}

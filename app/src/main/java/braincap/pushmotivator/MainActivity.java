package braincap.pushmotivator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity implements AuthorFragment.GiveAuthorToActivityListener, TopicFragment.GiveTopicToActivityListener {
    private static final long FLIP_DELAY = 3000;
    private static final String TAG = "JT";
    ImageButton btnWall;
    ImageButton btnAuth;
    ImageButton btnTopic;
    ArrayList<Integer> authorImageIds;
    ArrayList<Integer> topicImageIds;
    Animation outAnimationAuth;
    Animation inAnimationAuth;
    Animation outAnimationTopic;
    Animation inAnimationTopic;

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
        btnAuth.removeCallbacks(null);
        btnTopic.removeCallbacks(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO: Add ripple in Wall Button

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

        btnWall = (ImageButton) findViewById(R.id.btn_wall);
        btnAuth = (ImageButton) findViewById(R.id.btn_auth);
        btnTopic = (ImageButton) findViewById(R.id.btn_topic);

        Glide.with(this).load(authorImageIds.get(0)).fitCenter().into(btnAuth);
        Glide.with(this).load(topicImageIds.get(0)).fitCenter().into(btnTopic);
        Glide.with(this).load(R.drawable.img_wall).centerCrop().into(btnWall);


        btnAuth.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateAuthButtonImage();
                btnAuth.postDelayed(this, FLIP_DELAY);
            }
        }, FLIP_DELAY);


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

        if (btnTopic != null) {
            btnTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TopicFragment topicFragment = new TopicFragment();
                    topicFragment.setGiveTopicToActivityListener(MainActivity.this);
                    fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.myfrag, topicFragment, "FRAGTOPIC");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
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
                    Glide.with(MyApplication.getContext()).load(authorImageIds.get(0)).fitCenter().dontAnimate().into(btnAuth);
                    btnAuth.startAnimation(inAnimationAuth);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            btnAuth.startAnimation(outAnimationAuth);

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
                    Glide.with(MyApplication.getContext()).load(topicImageIds.get(0)).fitCenter().dontAnimate().into(btnTopic);
                    btnTopic.startAnimation(inAnimationTopic);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            btnTopic.startAnimation(outAnimationTopic);

        } catch (IllegalArgumentException e) {
            e.toString();
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
        QuoteWallFragment quoteWallFragment = newInstance("AUTHOR", mAuthorName);
        fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.myfrag, quoteWallFragment, "FRAGWALL");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void giveTopicToActivity(String mTopicName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuoteWallFragment quoteWallFragment = newInstance("TOPIC", mTopicName);
        Log.d(TAG, "giveTopicToActivity: " + mTopicName);
        fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.myfrag, quoteWallFragment, "FRAGWALL");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
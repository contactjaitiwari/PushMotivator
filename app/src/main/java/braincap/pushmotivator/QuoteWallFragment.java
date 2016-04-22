package braincap.pushmotivator;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.adapters.WallRecyclerViewAdapter;
import braincap.pushmotivator.beans.Quote;
import braincap.pushmotivator.beans.ResultQuote;
import braincap.pushmotivator.notifier.NotifierService;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteWallFragment extends Fragment implements SearchView.OnQueryTextListener, WallRecyclerViewAdapter.QuoteClickListener {

    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Quote> mResults;
    SearchView searchView;
    Activity context;
    String mFilterInputReceived;
    String mFilterInputType;
    WallRecyclerViewAdapter rcAdapter;
    ArrayList<ResultQuote> description = new ArrayList<>();
    ArrayList<ResultQuote> filteredDescriptionList = new ArrayList<>();
    Thread realmToArray = new Thread(new Runnable() {
        public void run() {
            if (mFilterInputType != null) {
                RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
                mRealm = Realm.getInstance(config0);
                if (mFilterInputType == "AUTHOR") {
                    mResults = mRealm.where(Quote.class).equalTo("AUTH_TITLE", mFilterInputReceived).findAll();
                } else if (mFilterInputType == "TOPIC") {
                    mResults = mRealm.where(Quote.class).equalTo("CAT_TITLE", mFilterInputReceived).findAll();
                }
                Log.d(TAG, "run: Start Loop");
                for (int i = 0; i < mResults.size(); i++) {
                    description.add(new ResultQuote(mResults.get(i).getPOST_DESCRIPTION(), mResults.get(i).getAUTH_TITLE()));
                }
            }
            if (mFilterInputReceived == null) {
                description = MyApplication.getDescription();
            }
            Collections.shuffle(description);
            rcAdapter.passData(description);
            Log.d(TAG, "run: Data Passed " + description.size());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rcAdapter.notifyDataSetChanged();
                }
            });
        }
    });

    public QuoteWallFragment() {
    }

    public static QuoteWallFragment newInstance(String inputType, String inputName) {
        QuoteWallFragment quoteWallFragment = new QuoteWallFragment();

        Bundle args = new Bundle();
        args.putString(inputType, inputName);
        quoteWallFragment.setArguments(args);
        return quoteWallFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getString("AUTHOR") != null) {
                mFilterInputType = "AUTHOR";
                mFilterInputReceived = getArguments().getString("AUTHOR", null);
            } else if (getArguments().getString("TOPIC") != null) {
                mFilterInputType = "TOPIC";
                mFilterInputReceived = getArguments().getString("TOPIC", null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_quote_wall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.recycler_view);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.requestFocus();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) context.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeView.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rcAdapter = new WallRecyclerViewAdapter(getActivity());
        rcAdapter.setQuoteClickListener(this);
        recyclerView.setAdapter(rcAdapter);
        realmToArray.start();
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        recyclerView.addItemDecoration(decoration);

    }

    private void show() {
        final Dialog d = new Dialog(context);
        d.setTitle("Select repeat interval hour");
        d.setContentView(R.layout.number_picker);
        final Button b1 = (Button) d.findViewById(R.id.startButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                startRocket();
                MyApplication.writeViewVisibility(View.VISIBLE);
                Intent intent = new Intent(context, NotifierService.class);
                intent.putParcelableArrayListExtra("list", mFilterInputType != null ? description : new ArrayList<>(description.subList(0, 999)));
                intent.putExtra("hours", np.getValue());
                context.startService(intent);
            }
        });
        d.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.clearFocus();
        searchView.setFocusable(false);
        searchView.setQuery("", true);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(false);


        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                rcAdapter.setFilter(description);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredDescriptionList = filter(description, newText);
        rcAdapter.setFilter(filteredDescriptionList);
        return true;
    }

    private ArrayList<ResultQuote> filter(ArrayList<ResultQuote> models, String query) {
        query = query.toLowerCase();
        final ArrayList<ResultQuote> filteredModelList = new ArrayList<>();
        for (ResultQuote model : models) {
            final String text = model.getPOST_DESCRIPTION().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void onQuoteClicked(String quote, String author) {
        FragmentManager fragmentManager = getFragmentManager();
        QuoteDetailsFragment quoteDetailsFragment = new QuoteDetailsFragment();
        quoteDetailsFragment.setQuote(quote);
        quoteDetailsFragment.setAuthor(author);
        quoteDetailsFragment.show(fragmentManager, "QUOTEDETAILSFRAG");
    }

    public void startRocket() {
        final ImageView imageView = (ImageView) context.findViewById(R.id.animated_rocket);
        imageView.setVisibility(View.VISIBLE);
        imageView.setY(0 - imageView.getHeight());
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, context.findViewById(R.id.frag_wall_parent).getHeight(), 0 - imageView.getHeight());
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(context, android.R.anim.accelerate_interpolator);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ding);
                final float volume = (float) (1 - (Math.log(50 - 25) / Math.log(50)));
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.start();
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(translateAnimation);
    }
}



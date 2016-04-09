package braincap.pushmotivator;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.adapters.WallRecyclerViewAdapter;
import braincap.pushmotivator.beans.Quote;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteWallFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Quote> mResults;
    Activity context;
    String mAuthorNameReceived;
    WallRecyclerViewAdapter rcAdapter;
    private ArrayList<String> description;
    Thread realmToArray = new Thread(new Runnable() {
        public void run() {
            if (mAuthorNameReceived != null) {
                RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
                mRealm = Realm.getInstance(config0);
                mResults = mRealm.where(Quote.class).equalTo("AUTH_TITLE", mAuthorNameReceived).findAll();
                Log.d(TAG, "run: Start Loop");
                for (int i = 0; i < mResults.size(); i++) {
                    description.add(mResults.get(i).getPOST_DESCRIPTION());
                }
            }
            if (mAuthorNameReceived == null) {
                description = MyApplication.getDescription();
                Log.d(TAG, "run: Call " + description.size());
            }
            Collections.shuffle(description);
            rcAdapter.passData(description);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rcAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    );

    public QuoteWallFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthorNameReceived = getArguments().getString("1", null);
        }
        description = new ArrayList<>();
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
        recyclerView.setAdapter(rcAdapter);
        realmToArray.start();
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        recyclerView.addItemDecoration(decoration);
    }

    private void show() {
        final Dialog d = new Dialog(context);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_picker);
        Button b1 = (Button) d.findViewById(R.id.startButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setMaxValue(10);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Set Value", Toast.LENGTH_SHORT).show();
            }
        });
        d.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

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
        final ArrayList<String> filteredDescriptionList = filter(description, newText);
        rcAdapter.setFilter(filteredDescriptionList);
        return true;
    }

    private ArrayList<String> filter(ArrayList<String> models, String query) {
        query = query.toLowerCase();
        final ArrayList<String> filteredModelList = new ArrayList<>();
        for (String model : models) {
            final String text = model.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}



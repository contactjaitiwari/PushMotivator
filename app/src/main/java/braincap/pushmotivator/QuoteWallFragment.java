package braincap.pushmotivator;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.adapters.WallRecyclerViewAdapter;
import braincap.pushmotivator.beans.Quote;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteWallFragment extends Fragment {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Quote> mResults;
    Activity context;
    List<Integer> indices;

    public QuoteWallFragment() {
        // Required empty public constructor
        Log.d(TAG, "QuoteWallFragment: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        context = getActivity();

        RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
        mRealm = Realm.getInstance(config0);

        mResults = mRealm.where(Quote.class).findAll();
        indices = new ArrayList<>();
        for (int i = 0; i < mResults.size(); i++) {
            indices.add(i);
        }
        Log.d(TAG, "onCreateView: "+mRealm.isEmpty());
        return inflater.inflate(R.layout.fragment_quote_wall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.recycler_view);
        Log.d(TAG, "onViewCreated: ");

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

        //Trying to read default_realm.realm.realm file

        WallRecyclerViewAdapter rcAdapter = new WallRecyclerViewAdapter(context, null, mResults, indices, getActivity());
        recyclerView.setAdapter(rcAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);

    }

}

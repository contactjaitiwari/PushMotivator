package braincap.pushmotivator;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import braincap.pushmotivator.adapters.AuthorRecyclerViewAdapter;
import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.beans.Quote;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends Fragment {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Quote> mResults;
    Activity context;

    public AuthorFragment() {
        // Required empty public constructor
        Log.d(TAG, "AuthorFragment: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        context = getActivity();
        RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default").build();
        mRealm = Realm.getInstance(config0);
        mResults = mRealm.where(Quote.class).findAll(); //TODO : GET AUTHOR WITH QUOTE COUNT
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.author_view);
        Log.d(TAG, "onViewCreated: ");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AuthorRecyclerViewAdapter rcAdapter = new AuthorRecyclerViewAdapter(context, null, mResults, context);
        recyclerView.setAdapter(rcAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }
}

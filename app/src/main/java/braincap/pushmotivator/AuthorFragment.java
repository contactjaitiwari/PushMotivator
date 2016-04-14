package braincap.pushmotivator;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import braincap.pushmotivator.adapters.AuthorRecyclerViewAdapter;
import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.beans.Author;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends Fragment implements AuthorRecyclerViewAdapter.OnAuthorSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Author> mResultsAuth;
    Activity context;
    AuthorRecyclerViewAdapter rcAdapter;
    private ArrayList<Author> authorList;
    private GiveAuthorToActivityListener giveAuthorToActivityListener;

    public AuthorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Author tempAuthor = new Author();
        authorList = new ArrayList<>();
        context = getActivity();

        RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
        mRealm = Realm.getInstance(config0);
        mResultsAuth = mRealm.where(Author.class).findAll(); //TODO : GET AUTHOR WITH QUOTE COUNT
        for (int i = 0; i < mResultsAuth.size(); i++) {
            tempAuthor = new Author();
            tempAuthor.setAUTH_NAME(mResultsAuth.get(i).getAUTH_NAME());
            tempAuthor.setCOUNT(mResultsAuth.get(i).getCOUNT());
            authorList.add(tempAuthor);
        }
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.author_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rcAdapter = new AuthorRecyclerViewAdapter(context, authorList);
        rcAdapter.setOnAuthorSelectedListener(this);
        recyclerView.setAdapter(rcAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAuthorSelected(View view) {
        if (giveAuthorToActivityListener != null) {
            giveAuthorToActivityListener.giveAuthorToActivity((String) ((TextView) view.findViewById(R.id.tv_author)).getText());
        }
    }

    public void setGiveAuthorToActivityListener(GiveAuthorToActivityListener giveAuthorToActivityListener) {
        this.giveAuthorToActivityListener = giveAuthorToActivityListener;
    }

    //Search Listener
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
                rcAdapter.setFilter(authorList);
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
        final ArrayList<Author> filteredDescriptionList = filter(authorList, newText);
        rcAdapter.setFilter(filteredDescriptionList);
        return true;
    }

    private ArrayList<Author> filter(ArrayList<Author> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Author> filteredModelList = new ArrayList<>();
        for (Author model : models) {
            final String text = model.getAUTH_NAME().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public interface GiveAuthorToActivityListener {
        void giveAuthorToActivity(String mAuthorName);
    }
}

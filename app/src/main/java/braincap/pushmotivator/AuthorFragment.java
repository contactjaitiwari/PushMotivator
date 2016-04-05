package braincap.pushmotivator;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

import braincap.pushmotivator.adapters.AuthorRecyclerViewAdapter;
import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.beans.Author;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends Fragment implements AuthorRecyclerViewAdapter.OnAuthorSelectedListener {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Author> mResultsAuth;
    Activity context;
    private GiveAuthorToActivityListener giveAuthorToActivityListener;


    public AuthorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(context).build())
                        .build());

        RealmInspectorModulesProvider.builder(context)
                .withFolder(context.getCacheDir())
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();

        RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
        mRealm = Realm.getInstance(config0);
        mResultsAuth = mRealm.where(Author.class).findAll(); //TODO : GET AUTHOR WITH QUOTE COUNT
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.author_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AuthorRecyclerViewAdapter rcAdapter = new AuthorRecyclerViewAdapter(context, null, mResultsAuth, context);
        rcAdapter.setOnAuthorSelectedListener(this);
        recyclerView.setAdapter(rcAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
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

    public interface GiveAuthorToActivityListener {
        void giveAuthorToActivity(String mAuthorName);
    }
}

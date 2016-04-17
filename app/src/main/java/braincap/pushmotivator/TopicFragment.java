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

import braincap.pushmotivator.adapters.SpacesItemDecoration;
import braincap.pushmotivator.adapters.TopicRecyclerViewAdapter;
import braincap.pushmotivator.beans.Topic;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends Fragment implements TopicRecyclerViewAdapter.OnTopicSelectedListener {
    private static final String TAG = "JT";
    Realm mRealm;
    RealmResults<Topic> mResultsTopic;
    Activity context;
    private GiveTopicToActivityListener giveTopicToActivityListener;


    public TopicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();

        RealmConfiguration config0 = new RealmConfiguration.Builder(context).name("default_realm").build();
        mRealm = Realm.getInstance(config0);
        mResultsTopic = mRealm.where(Topic.class).findAll(); //TODO : GET TOPIC WITH QUOTE COUNT
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.topic_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        TopicRecyclerViewAdapter rcAdapter = new TopicRecyclerViewAdapter(context, mResultsTopic);
        rcAdapter.setOnTopicSelectedListener(this);
        recyclerView.setAdapter(rcAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onTopicSelected(View view) {
        if (giveTopicToActivityListener != null) {
            giveTopicToActivityListener.giveTopicToActivity((String) ((TextView) view.findViewById(R.id.tv_topic)).getText());
        }
    }

    public void setGiveTopicToActivityListener(GiveTopicToActivityListener giveTopicToActivityListener) {
        this.giveTopicToActivityListener = giveTopicToActivityListener;
    }

    public interface GiveTopicToActivityListener {
        void giveTopicToActivity(String mTopicName);
    }
}

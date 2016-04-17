package braincap.pushmotivator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.Topic;
import io.realm.RealmResults;

/**
 * Created by Jai on 4/2/2016.
 */
public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    private LayoutInflater mInflater;
    private RealmResults<Topic> mResults;
    private OnTopicSelectedListener onTopicSelectedListener;

    public TopicRecyclerViewAdapter(Context context, RealmResults<Topic> results) {
        mInflater = LayoutInflater.from(context);
        mResults = results;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.topic, parent, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopicHolder) {
            TopicHolder topicHolder = (TopicHolder) holder;
            Topic topic = mResults.get(position);
            topicHolder.mTopic.setText(topic.getTOPIC_NAME());
            topicHolder.mCount.setText(topic.getCOUNT() + "");
        }
    }

    public void setOnTopicSelectedListener(OnTopicSelectedListener onTopicSelectedListener) {
        this.onTopicSelectedListener = onTopicSelectedListener;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public interface OnTopicSelectedListener {
        void onTopicSelected(View view);
    }

    public class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTopic;
        TextView mCount;

        public TopicHolder(View itemView) {
            super(itemView);
            mTopic = (TextView) itemView.findViewById(R.id.tv_topic);
            mCount = (TextView) itemView.findViewById(R.id.tv_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onTopicSelectedListener != null) {
                Log.d(TAG, "onClick: ");
                onTopicSelectedListener.onTopicSelected(itemView);
            }
        }
    }

}

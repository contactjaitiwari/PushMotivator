package braincap.pushmotivator.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.Quote;
import io.realm.RealmResults;

/**
 * Created by Jai on 3/9/2016.
 */
public class WallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    private Context mContext;
    private LayoutInflater mInflater;
    private RealmResults<Quote> mResults;
    private List<Integer> mIndices;


    public WallRecyclerViewAdapter(Context context, RealmResults<Quote> results, List<Integer> indices, FragmentActivity activity) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResults = results;
        mIndices = indices;

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        shuffleIndex();
                        notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

    }

    private void shuffleIndex() {
        Collections.shuffle(mIndices);
        Toast.makeText(mContext, "Index Shuffled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.quote, parent, false);
        return new QuoteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuoteHolder) {
            QuoteHolder quoteHolder = (QuoteHolder) holder;
            Quote quote = mResults.get(mIndices.get(position));
            quoteHolder.mQuote.setText(quote.getPOST_DESCRIPTION());
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class QuoteHolder extends RecyclerView.ViewHolder {
        TextView mQuote;

        public QuoteHolder(View itemView) {
            super(itemView);
            mQuote = (TextView) itemView.findViewById(R.id.tv_quote);
        }
    }
}

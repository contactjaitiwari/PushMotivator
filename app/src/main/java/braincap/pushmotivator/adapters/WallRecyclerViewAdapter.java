package braincap.pushmotivator.adapters;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.R;

public class WallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    ArrayList<Integer> colorIds = new ArrayList<>();
    private LayoutInflater mInflater;
    private ArrayList<String> description;

    public WallRecyclerViewAdapter(FragmentActivity activity) {
        description = new ArrayList<>();
        mInflater = LayoutInflater.from(activity);
        colorIds.add(R.color.col1);
        colorIds.add(R.color.col2);
        colorIds.add(R.color.col3);
        colorIds.add(R.color.col4);
        colorIds.add(R.color.col5);
        colorIds.add(R.color.col6);
        colorIds.add(R.color.col7);
        colorIds.add(R.color.col8);
        colorIds.add(R.color.col9);


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
        Collections.shuffle(description);
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
            String quote = description.get(position);
            quoteHolder.mQuote.setText(quote);
        }
    }


    public void passData(ArrayList<String> description) {
        this.description = description;
    }

    @Override
    public int getItemCount() {
        return description.size();
    }

    public void setFilter(ArrayList<String> descriptionNew) {
        description = new ArrayList<>();
        description.addAll(descriptionNew);
        notifyDataSetChanged();
    }

    public class QuoteHolder extends RecyclerView.ViewHolder {
        TextView mQuote;

        public QuoteHolder(View itemView) {
            super(itemView);
            mQuote = (TextView) itemView.findViewById(R.id.tv_quote);
            Collections.shuffle(colorIds);
            mQuote.setBackgroundColor(itemView.getResources().getColor(colorIds.get(0)));
        }
    }
}

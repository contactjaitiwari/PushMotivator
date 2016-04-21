package braincap.pushmotivator.adapters;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.ResultQuote;

public class WallRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    public QuoteClickListener quoteClickListener;
    ArrayList<Integer> colorIds = new ArrayList<>();
    String quote;
    String author;
    private LayoutInflater mInflater;
    private ArrayList<ResultQuote> description;
    private int lastPosition = -1;
    private FragmentActivity activity;

    public WallRecyclerViewAdapter(FragmentActivity activity) {
        this.activity = activity;
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
        lastPosition = -1;
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
            quote = description.get(position).getPOST_DESCRIPTION();
            author = description.get(position).getAUTH_TITLE();
            quoteHolder.mQuote.setText(quote);
            quoteHolder.mAuthor.setText(author);
            setAnimation(((QuoteHolder) holder).container, position);
        }
    }

    public void setQuoteClickListener(QuoteClickListener quoteClickListener) {
        this.quoteClickListener = quoteClickListener;
    }

    public void passData(ArrayList<ResultQuote> description) {
        this.description = description;
    }

    @Override
    public int getItemCount() {
        return description.size();
    }

    public void setFilter(ArrayList<ResultQuote> descriptionNew) {
        description = new ArrayList<>();
        description.addAll(descriptionNew);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.custom_slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((QuoteHolder) holder).clearAnimation();
    }

    public interface QuoteClickListener {
        void onQuoteClicked(String quote, String author);
    }

    public class QuoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mQuote;
        TextView mAuthor;
        FrameLayout container;

        public QuoteHolder(View itemView) {
            super(itemView);
            container = (FrameLayout) itemView.findViewById(R.id.card_view);
            mQuote = (TextView) itemView.findViewById(R.id.tv_quote);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            Collections.shuffle(colorIds);
            mQuote.setBackgroundColor(itemView.getResources().getColor(colorIds.get(0)));
            mQuote.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            quoteClickListener.onQuoteClicked((String) mQuote.getText(), (String) mAuthor.getText());
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }

}

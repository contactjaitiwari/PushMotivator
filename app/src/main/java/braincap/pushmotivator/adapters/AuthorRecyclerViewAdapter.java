package braincap.pushmotivator.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.Quote;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jai on 4/2/2016.
 */
public class AuthorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    private Context mContext;
    private LayoutInflater mInflater;
    private Realm mRealm;
    private RealmResults<Quote> mResults;
    private Activity mActivity;


    public AuthorRecyclerViewAdapter(Context context, Realm realm, RealmResults<Quote> results, Activity activity) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        mResults = results;
        mActivity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.author, parent, false);
        return new QuoteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuoteHolder) {
            QuoteHolder quoteHolder = (QuoteHolder) holder;
            Quote quote = mResults.get(position);
            quoteHolder.mAuthor.setText(quote.getPOST_DESCRIPTION());
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class QuoteHolder extends RecyclerView.ViewHolder {
        TextView mAuthor;

        public QuoteHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        }

    }


}

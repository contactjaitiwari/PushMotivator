package braincap.pushmotivator.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.Author;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jai on 4/2/2016.
 */
public class AuthorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JT";
    private Context mContext;
    private LayoutInflater mInflater;
    private RealmResults<Author> mResults;
    private OnAuthorSelectedListener onAuthorSelectedListener;

    public AuthorRecyclerViewAdapter(Context context, Realm realm, RealmResults<Author> results, Activity activity) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResults = results;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.author, parent, false);
        return new AuthorHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AuthorHolder) {
            AuthorHolder authorHolder = (AuthorHolder) holder;
            Author author = mResults.get(position);
            authorHolder.mAuthor.setText(author.getAUTH_NAME());
            authorHolder.mCount.setText(author.getCOUNT() + "");
        }
    }

    public void setOnAuthorSelectedListener(OnAuthorSelectedListener onAuthorSelectedListener) {
        this.onAuthorSelectedListener = onAuthorSelectedListener;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public interface OnAuthorSelectedListener {
        void onAuthorSelected(View view);
    }

    public class AuthorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAuthor;
        TextView mCount;

        public AuthorHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mCount = (TextView) itemView.findViewById(R.id.tv_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onAuthorSelectedListener != null) {
                onAuthorSelectedListener.onAuthorSelected(itemView);
            }
        }
    }

}

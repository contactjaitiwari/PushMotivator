package braincap.pushmotivator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class QuoteDetailsFragment extends DialogFragment implements View.OnTouchListener, View.OnLongClickListener {

    private static final String TAG = "JT";
    private String quote;
    private String author;

    public QuoteDetailsFragment() {
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quote_details, container, false);
        rootView.setOnTouchListener(this);
        rootView.setOnLongClickListener(this);
        getDialog().setTitle("About this Quote");
        getDialog().setCanceledOnTouchOutside(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView quoteTextView = (TextView) view.findViewById(R.id.tv_quote_text);
        TextView authorTextView = (TextView) view.findViewById(R.id.tv_author_text);
        authorTextView.setText(author);
        quoteTextView.setText(quote);
        Toast.makeText(view.getContext(), "Tap to copy\nLong press to share", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch: ");
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("quote", quote + "\n\n-" + author);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(v.getContext(), "Quote copied!", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick: ");
        getDialog().dismiss();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, quote + "\n\n-" + author);
        sendIntent.setType("text/plain");
        Toast.makeText(v.getContext(), "Share it!", Toast.LENGTH_SHORT).show();
        startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_using)));
        return false;
    }
}

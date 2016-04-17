package braincap.pushmotivator.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jai on 4/16/2016.
 */

public class ResultQuote implements Parcelable {
    public static final Creator<ResultQuote> CREATOR = new Creator<ResultQuote>() {
        @Override
        public ResultQuote createFromParcel(Parcel in) {
            return new ResultQuote(in);
        }

        @Override
        public ResultQuote[] newArray(int size) {
            return new ResultQuote[size];
        }
    };
    private String POST_DESCRIPTION;
    private String AUTH_TITLE;

    public ResultQuote(String POST_DESCRIPTION, String AUTH_TITLE) {
        this.AUTH_TITLE = AUTH_TITLE;
        this.POST_DESCRIPTION = POST_DESCRIPTION;
    }

    protected ResultQuote(Parcel in) {
        POST_DESCRIPTION = in.readString();
        AUTH_TITLE = in.readString();
    }

    public String getAUTH_TITLE() {
        return AUTH_TITLE;
    }

    public String getPOST_DESCRIPTION() {
        return POST_DESCRIPTION;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(POST_DESCRIPTION);
        dest.writeString(AUTH_TITLE);
    }
}
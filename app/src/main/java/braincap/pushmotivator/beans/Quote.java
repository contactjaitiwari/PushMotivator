package braincap.pushmotivator.beans;

import io.realm.RealmObject;

/**
 * Created by Jai on 3/12/2016.
 */
public class Quote extends RealmObject {
    private int POST_id;
    private String POST_TITLE;
    private String POST_DESCRIPTION;
    private String AUTH_TITLE;
    private String CAT_TITLE;

    public Quote() {
    }

    public String getAUTH_TITLE() {
        return AUTH_TITLE;
    }

    public void setAUTH_TITLE(String AUTH_TITLE) {
        this.AUTH_TITLE = AUTH_TITLE;
    }

    public String getCAT_TITLE() {
        return CAT_TITLE;
    }

    public void setCAT_TITLE(String CAT_TITLE) {
        this.CAT_TITLE = CAT_TITLE;
    }

    public String getPOST_DESCRIPTION() {
        return POST_DESCRIPTION;
    }

    public void setPOST_DESCRIPTION(String POST_DESCRIPTION) {
        this.POST_DESCRIPTION = POST_DESCRIPTION;
    }

    public int getPOST_id() {
        return POST_id;
    }

    public void setPOST_id(int POST_id) {
        this.POST_id = POST_id;
    }

    public String getPOST_TITLE() {
        return POST_TITLE;
    }

    public void setPOST_TITLE(String POST_TITLE) {
        this.POST_TITLE = POST_TITLE;
    }
}

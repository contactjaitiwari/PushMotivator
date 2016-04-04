package braincap.pushmotivator.beans;

import io.realm.RealmObject;

/**
 * Created by Jai on 3/12/2016.
 */
public class Author extends RealmObject {
    private String AUTH_NAME;
    private int COUNT;

    public Author() {
    }

    public String getAUTH_NAME() {
        return AUTH_NAME;
    }

    public void setAUTH_NAME(String AUTH_NAME) {
        this.AUTH_NAME = AUTH_NAME;
    }

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }
}

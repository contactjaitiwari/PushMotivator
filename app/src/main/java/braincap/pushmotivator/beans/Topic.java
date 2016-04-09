package braincap.pushmotivator.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jai on 3/12/2016.
 */
public class Topic extends RealmObject {
    @PrimaryKey
    private String TOPIC_NAME;
    private int COUNT;

    public Topic() {

    }

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }

    public String getTOPIC_NAME() {
        return TOPIC_NAME;
    }

    public void setTOPIC_NAME(String TOPIC_NAME) {
        this.TOPIC_NAME = TOPIC_NAME;
    }

}


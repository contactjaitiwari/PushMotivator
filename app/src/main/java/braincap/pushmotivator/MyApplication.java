package braincap.pushmotivator;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import braincap.pushmotivator.beans.Quote;
import braincap.pushmotivator.beans.ResultQuote;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Jai on 3/21/2016.
 */
public class MyApplication extends Application {
    private static final String TAG = "JT";
    public static ArrayList<ResultQuote> description = new ArrayList<>();
    private static Application sApplication;
    static Thread realmToArray = new Thread(new Runnable() {
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            RealmConfiguration config0 = new RealmConfiguration.Builder(MyApplication.getContext()).name("default_realm").build();
            Realm mRealm = Realm.getInstance(config0);
            RealmResults<Quote> mResults = mRealm.where(Quote.class).findAll();
            Log.d(TAG, "run: " + mResults.size());
            Log.d(TAG, "run: Start Loop");
            for (int i = 0; i < mResults.size(); i++) {
                description.add(new ResultQuote(mResults.get(i).getPOST_DESCRIPTION(), mResults.get(i).getAUTH_TITLE()));
            }
            Collections.shuffle(description);
            Log.d(TAG, "run: " + description.size());
        }
    });

    public static ArrayList<ResultQuote> getDescription() {
        if (description.size() == 0) {
            realmToArray.run();
        }
        return description;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        Log.d(TAG, "MyApplication onCreate: ");
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default_realm), "default_realm");
        RealmConfiguration config0 = new RealmConfiguration.Builder(this).name("default_realm").build();
        Realm.setDefaultConfiguration(config0);
        realmToArray.start();
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
package braincap.pushmotivator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.util.Log;
import android.view.View;

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

public class MyApplication extends Application {
    private static final String TAG = "JT";
    public static ArrayList<ResultQuote> description = new ArrayList<>();
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
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

    public static void writeViewVisibility(int view_status) {
        editor = sharedPref.edit();
        Log.d(TAG, "writeViewVisibility: " + view_status);
        editor.putInt("view_status", view_status);
        editor.apply();
    }

    public static int readViewVisibility() {
        Log.d(TAG, "readViewVisibility: " + sharedPref.getInt("view_status", 0));
        return sharedPref.getInt("view_status", View.GONE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sharedPref = getContext().getSharedPreferences("view", MODE_PRIVATE);
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
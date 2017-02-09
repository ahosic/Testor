package at.fhooe.mc.hosic.mobilelearningapp;

import android.app.Application;
import android.content.Context;

/**
 * Defines the context of the application
 */

public class TestorApplication extends Application {

    private static Context mContext;

    /**
     * Returns the context of the application.
     *
     * @return the application's context
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }
}

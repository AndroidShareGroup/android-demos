package me.pexcn.demos;

import android.app.Application;

import me.pexcn.simpleutils.Utils;

/**
 * Created by pexcn on 2016-09-25.
 */
public class DemosApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}

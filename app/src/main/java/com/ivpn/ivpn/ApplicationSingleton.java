package com.ivpn.ivpn;

import android.app.Application;

import com.ivpn.ivpn.network.Server;

import java.util.ArrayList;

/**
 * application representation
 */
public class ApplicationSingleton extends Application {

    private static ApplicationSingleton sApplication;
    private ArrayList<Server> mServers = new ArrayList<>();

    /**
     * @return instance of current app context
     */
    public static ApplicationSingleton getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    /**
     * @return loaded servers list
     */
    public ArrayList<Server> getServers() {
        return mServers;
    }

    /**
     * sets loaded servers list
     *
     * @param mServers loaded servers list
     */
    public void setServers(ArrayList<Server> mServers) {
        this.mServers = mServers;
    }
}

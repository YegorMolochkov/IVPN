package com.ivpn.ivpn.framework;

import com.ivpn.ivpn.network.Server;

import java.util.ArrayList;

/**
 * Event fired on servers list update loaded
 */
public class UpdatesLoadedEvent {

    private ArrayList<Server> mServers;

    UpdatesLoadedEvent(ArrayList<Server> servers) {
        mServers = servers;
    }

    public ArrayList<Server> getServers() {
        return mServers;
    }
}

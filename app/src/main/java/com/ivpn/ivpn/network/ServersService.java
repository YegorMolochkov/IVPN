package com.ivpn.ivpn.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface with server methods
 */
public interface ServersService {

    /**
     * @return available vpn servers
     */
    @GET("servers.json")
    Call<ArrayList<Server>> getServersList();
}

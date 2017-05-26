package com.ivpn.ivpn.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Representation of server object
 */
public class Server implements Serializable{

    public String gateway;

    @SerializedName("country_code")
    public String countryCode;

    public String country;

    public String city;

    @SerializedName("ip_addresses")
    public ArrayList<String> ipAddresses;
}

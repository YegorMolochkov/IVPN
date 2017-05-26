package com.ivpn.ivpn.network;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Services factory
 */
public class ServiceProvider {

    private static final String sBaseURL = "https://api.ivpn.net/v1/";

    /**
     * creates service of given type
     *
     * @param serviceClass service class
     * @param <S>          service type
     * @return service instance
     */
    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(sBaseURL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
        Retrofit retrofit = builder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }
}

package com.ivpn.ivpn.framework;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ivpn.ivpn.network.Server;
import com.ivpn.ivpn.network.ServersService;
import com.ivpn.ivpn.network.ServiceProvider;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * class with helper methods
 */
public class Utils {

    private static final String sDefaultString = "";
    private static final String sServersKey = "SERVERS_KEY";
    private static final String sSelectedCity = "SERVER_CITY";
    private static final String sSelectedCountry = "SERVER_COUNTRY";
    private static final long sMinWait = TimeUnit.MINUTES.toMillis(15);
    private static final long sMaxWait = TimeUnit.MINUTES.toMillis(20);

    /**
     * schedule load job for each 15min if network connection is available
     *
     * @param context context
     */
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, UpdateJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(sMinWait);
        builder.setOverrideDeadline(sMaxWait);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    private static void storeServers(Context context, ArrayList<Server> servers) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(sServersKey, serversToJson(servers)).apply();
    }

    /**
     * read servers list from preferences
     *
     * @param context context
     * @return servers list
     */
    public static ArrayList<Server> readServers(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(sServersKey, sDefaultString);
        if (json.equals(sDefaultString)) {
            return null;
        }
        ArrayList<Server> servers = null;
        try {
            servers = jsonToServers(json);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return servers;
    }

    /**
     * store selected server info
     *
     * @param server  selected server
     * @param context context
     */
    public static void setSelectedServer(Server server, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(sSelectedCity, server.city).putString(sSelectedCountry, server.countryCode).apply();
    }

    /**
     * get last selected server
     *
     * @param context context
     * @return last selected server
     */
    public static Server getSelectedServer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String city = preferences.getString(sSelectedCity, sDefaultString);
        String country = preferences.getString(sSelectedCountry, sDefaultString);

        if (city.equals(sDefaultString) || country.equals(sDefaultString)) {
            return null;
        }
        Server server = new Server();
        server.city = city;
        server.countryCode = country;
        return server;
    }

    private static String serversToJson(ArrayList<Server> servers) {
        return new Gson().toJson(servers);
    }

    private static ArrayList<Server> jsonToServers(String json) throws JsonSyntaxException {
        Type type = new TypeToken<List<Server>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    /**
     * load servers list from web
     *
     * @param context  context (warning! to avoid pass application context here or {@link java.lang.ref.WeakReference<Context>}
     * @param callback will be triggered on load events
     */
    public static void loadServers(final Context context, final Callback<ArrayList<Server>> callback) {
        ServersService service = ServiceProvider.createService(ServersService.class);
        Call<ArrayList<Server>> call = service.getServersList();
        call.enqueue(new Callback<ArrayList<Server>>() {
            @Override
            public void onResponse(Call<ArrayList<Server>> call, Response<ArrayList<Server>> response) {
                ArrayList<Server> servers = response.body();
                Utils.storeServers(context, servers);
                EventBus.getDefault().post(new UpdatesLoadedEvent(servers));
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Server>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    /**
     * load servers list from web
     *
     * @param context context
     */
    static void loadServers(final Context context) {
        loadServers(context, null);
    }

    /**
     * checks network connection
     *
     * @param context context
     * @return is network available?
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

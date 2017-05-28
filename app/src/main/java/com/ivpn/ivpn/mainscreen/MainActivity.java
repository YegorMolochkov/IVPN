package com.ivpn.ivpn.mainscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.ivpn.ivpn.R;
import com.ivpn.ivpn.framework.Utils;
import com.ivpn.ivpn.framework.BaseActivity;
import com.ivpn.ivpn.listscreen.ListActivity;
import com.ivpn.ivpn.network.Server;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.server_name)
    TextView serverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        loadUpdatesIfNeed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isNetworkAvailable(this)) {
            showNoNetworkDialog();
        }
        Server selectedServer = Utils.getSelectedServer(this);
        if (selectedServer != null) {
            setSelectedServer(selectedServer);
        }
    }

    private void loadUpdatesIfNeed() {
        ArrayList<Server> servers = Utils.readServers(this);
        if (servers == null) {
            showWaitDialog();
            Utils.loadServers(getApplicationContext(), new Callback<ArrayList<Server>>() {
                @Override
                public void onResponse(Call<ArrayList<Server>> call, Response<ArrayList<Server>> response) {
                    Server firstServer = response.body().get(0);
                    Utils.scheduleJob(getApplicationContext());
                    if (isAlive) {
                        Utils.setSelectedServer(firstServer, MainActivity.this);
                        setSelectedServer(firstServer);
                        hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Server>> call, Throwable t) {
                    Utils.scheduleJob(getApplicationContext());
                    if (isAlive) {
                        hideDialog();
                    }
                }
            });
        }
    }

    @OnClick(R.id.list_button)
    void goToList() {
        startActivity(new Intent(this, ListActivity.class));
    }

    private void setSelectedServer(Server server) {
        serverName.setText(getString(R.string.list_text, server.city, server.countryCode));
    }

    private void showNoNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_connection)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(settingsIntent);
                    }
                });
        builder.create().show();
    }
}

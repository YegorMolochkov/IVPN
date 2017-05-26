package com.ivpn.ivpn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ivpn.ivpn.ApplicationSingleton;
import com.ivpn.ivpn.R;
import com.ivpn.ivpn.network.Server;
import com.ivpn.ivpn.network.ServersService;
import com.ivpn.ivpn.network.ServiceProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final int sListRequestCode = 1;

    @BindView(R.id.server_name)
    TextView serverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        showWaitDialog();
        ServersService service = ServiceProvider.createService(ServersService.class);
        Call<ArrayList<Server>> call = service.getServersList();
        call.enqueue(new Callback<ArrayList<Server>>() {
            @Override
            public void onResponse(Call<ArrayList<Server>> call, Response<ArrayList<Server>> response) {
                ArrayList<Server> servers = response.body();
                ApplicationSingleton.getInstance().setServers(servers);
                setSelectedServer(servers.get(0));
                hideDialog();
            }

            @Override
            public void onFailure(Call<ArrayList<Server>> call, Throwable t) {
                Log.d("TAGG", t.getLocalizedMessage());
                hideDialog();
            }
        });
    }

    @OnClick(R.id.list_button)
    void goToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivityForResult(intent, sListRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == sListRequestCode && resultCode == RESULT_OK) {
            Server selectedServer = (Server) data.getSerializableExtra(ListActivity.SELECTED_SERVER);
            setSelectedServer(selectedServer);
        }
    }

    private void setSelectedServer(Server server) {
        serverName.setText(getString(R.string.list_text, server.city, server.countryCode));
    }
}

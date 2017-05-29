package com.ivpn.ivpn.listscreen;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ivpn.ivpn.R;
import com.ivpn.ivpn.framework.BaseActivity;
import com.ivpn.ivpn.framework.UpdatesLoadedEvent;
import com.ivpn.ivpn.framework.Utils;
import com.ivpn.ivpn.network.Server;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity with list of servers
 */
public class ListActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        unbinder = ButterKnife.bind(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new ServersListAdapter(this, Utils.readServers(this), new ServersListAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Server server) {
                onListItemSelected(server);
            }
        }));
    }

    private void onListItemSelected(Server server) {
        Utils.setSelectedServer(server, this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onEvent(UpdatesLoadedEvent event) {
        listView.setAdapter(new ServersListAdapter(this, event.getServers(), new ServersListAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Server server) {
                onListItemSelected(server);
            }
        }));
    }
}

package com.ivpn.ivpn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ivpn.ivpn.R;
import com.ivpn.ivpn.ServersListAdapter;
import com.ivpn.ivpn.network.Server;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity with list of servers
 */
public class ListActivity extends BaseActivity {

    static String SELECTED_SERVER = "SELECTED_SERVER";

    @BindView(R.id.list)
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        unbinder = ButterKnife.bind(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new ServersListAdapter(this, new ServersListAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Server server) {
                onListItemSelected(server);
            }
        }));
    }

    private void onListItemSelected(Server server) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_SERVER, server);
        setResult(RESULT_OK, intent);
        finish();
    }
}

package com.ivpn.ivpn.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivpn.ivpn.R;
import com.ivpn.ivpn.framework.Utils;
import com.ivpn.ivpn.listscreen.ListActivity;
import com.ivpn.ivpn.network.Server;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    @BindView(R.id.server_name)
    TextView serverName;
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        loadUpdatesIfNeed();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Server selectedServer = Utils.getSelectedServer(getActivity());
        if (selectedServer != null) {
            setSelectedServer(selectedServer);
        }
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @OnClick(R.id.list_button)
    void goToList() {
        startActivity(new Intent(getActivity(), ListActivity.class));
    }

    private void setSelectedServer(Server server) {
        serverName.setText(getString(R.string.list_text, server.city, server.countryCode));
    }

    private void loadUpdatesIfNeed() {
        ArrayList<Server> servers = Utils.readServers(getContext());
        if (servers == null) {
            loadUpdates();
        }
    }

    private void loadUpdates() {
        MainActivity activity = (MainActivity) getActivity();
        activity.showWaitDialog();
        Utils.loadServers(getActivity(), new Callback<ArrayList<Server>>() {
            @Override
            public void onResponse(Call<ArrayList<Server>> call, Response<ArrayList<Server>> response) {
                if (isAdded()) {
                    MainActivity activity = (MainActivity) getActivity();
                    Server firstServer = response.body().get(0);
                    Utils.scheduleJob(activity.getApplicationContext());
                    Utils.setSelectedServer(firstServer, activity);
                    setSelectedServer(firstServer);
                    activity.hideDialog();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Server>> call, Throwable t) {
                if (isAdded()) {
                    loadUpdatesIfNeed();
                }
            }
        });
    }
}

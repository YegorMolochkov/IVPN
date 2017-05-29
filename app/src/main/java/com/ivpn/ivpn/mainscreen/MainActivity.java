package com.ivpn.ivpn.mainscreen;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.ivpn.ivpn.R;
import com.ivpn.ivpn.framework.BaseActivity;
import com.ivpn.ivpn.framework.Utils;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String sTagRetainedFragment = "RetainedFragment";
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentByTag(sTagRetainedFragment);
        if (fragment == null) {
            fragment = new MainFragment();
            fm.beginTransaction().replace(R.id.frame, fragment, sTagRetainedFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isNetworkAvailable(this)) {
            showNoNetworkDialog();
        }
    }

    private void showNoNetworkDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_connection)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(settingsIntent);
                    }
                })
                .create()
                .show();
    }

    void showWaitDialog() {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}

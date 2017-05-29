package com.ivpn.ivpn.framework;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;

/**
 * Base activity for all activities
 */
public class BaseActivity extends AppCompatActivity {

    protected Unbinder unbinder;

    protected boolean isAlive;

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAlive = false;
    }
}

package com.ivpn.ivpn.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;

/**
 * Base activity for all activities
 */
public class BaseActivity extends AppCompatActivity {

    Unbinder unbinder;
    private ProgressDialog mDialog;

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * shows loading dialog
     */
    void showWaitDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    /**
     * hides loading dialog
     */
    void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}

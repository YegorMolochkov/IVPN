package com.ivpn.ivpn.framework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * receiver to schedule updates when device restart
 */
public class BootDoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.scheduleJob(context);
    }
}

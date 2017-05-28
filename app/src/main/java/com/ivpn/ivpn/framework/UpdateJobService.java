package com.ivpn.ivpn.framework;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * repeating loa task
 */
public class UpdateJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Utils.loadServers(getApplicationContext());
        Utils.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}

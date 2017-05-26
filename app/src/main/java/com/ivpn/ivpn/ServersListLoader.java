package com.ivpn.ivpn;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by ymo on 5/26/2017.
 */

public class ServersListLoader extends AsyncTaskLoader {

    public ServersListLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        return null;
    }
}

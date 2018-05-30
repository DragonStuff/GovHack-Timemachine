package com.timemachine.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Jeri on 3/9/14.
 */
public class DBManager {
    private DBHelper databaseHelper = null;

    //gets a helper once one is created ensures it doesnt create a new one
    public DBHelper getHelper(Context context)
    {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(context, DBHelper.class);
        }
        return databaseHelper;
    }

    //releases the helper once usages has ended
    public void releaseHelper(DBHelper helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}

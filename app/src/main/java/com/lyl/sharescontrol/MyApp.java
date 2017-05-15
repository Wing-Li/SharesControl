package com.lyl.sharescontrol;

import android.app.Application;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

/**
 * Created by lyl on 2017/5/15.
 */

public class MyApp extends Application {

    public static LiteOrm liteOrm;

    @Override
    public void onCreate() {
        super.onCreate();

        initSQL();
    }

    private void initSQL() {
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(this, "shares.db");
            config.dbVersion = 1;
            config.onUpdateListener = null; //升级
            liteOrm = LiteOrm.newSingleInstance(config);
        }
        liteOrm.setDebugged(true);
    }
}

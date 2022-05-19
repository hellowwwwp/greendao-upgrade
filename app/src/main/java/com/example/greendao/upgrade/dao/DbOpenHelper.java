package com.example.greendao.upgrade.dao;

import android.content.Context;
import android.util.Log;

import com.example.dbupgrade.GreenDaoUpgradeHelper;

import org.greenrobot.greendao.database.Database;

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/5/19
 */
public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.d("tag", "onUpgrade oldVersion: " + oldVersion + " newVersion: " + newVersion);
        GreenDaoUpgradeHelper.upgrade(db, GreenDaoCollector.getAllDaoClasses());
    }

}

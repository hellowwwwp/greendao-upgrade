package com.example.greendao.upgrade.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.greendao.upgrade.App;
import com.example.greendao.upgrade.beans.User;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/5/19
 */
public class DbHelper {

    private static volatile DbHelper sInstance = null;

    private static DaoSession sDaoSession;

    private final Object mUserLock = new Object();

    private DbHelper() {
        DbOpenHelper openHelper = new DbOpenHelper(App.getApp(), "greendao_demo");
        SQLiteDatabase database = openHelper.getWritableDatabase();
        sDaoSession = new DaoMaster(database).newSession();
    }

    public static DbHelper get() {
        if (sInstance == null) {
            synchronized (DbHelper.class) {
                if (sInstance == null) {
                    sInstance = new DbHelper();
                }
            }
        }
        return sInstance;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    public void addUser(@Nullable User user) {
        if (user == null) {
            return;
        }
        UserDao userDao = getDaoSession().getUserDao();
        synchronized (mUserLock) {
            userDao.insertInTx(user);
        }
    }

    @Nullable
    public List<User> getUsers() {
        UserDao userDao = getDaoSession().getUserDao();
        synchronized (mUserLock) {
            return userDao.loadAll();
        }
    }

}

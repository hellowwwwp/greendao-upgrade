package com.example.greendao.upgrade;

import android.app.Application;

import com.example.greendao.upgrade.dao.DbHelper;

import androidx.annotation.NonNull;

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/5/19
 */
public class App extends Application {

    private static App sApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        DbHelper.get();
    }

    @NonNull
    public static App getApp() {
        return sApp;
    }

}

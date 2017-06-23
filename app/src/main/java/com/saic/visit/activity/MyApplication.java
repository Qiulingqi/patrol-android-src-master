package com.saic.visit.activity;

import android.app.Application;
import android.util.Log;

import com.saic.visit.utils.excelutil.Order;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2017/3/17.
 */

public class MyApplication extends Application {

    public static String path = "/sdcard/凯迪拉克/凯迪拉克评估/";
    public static String path1 = "";
    public static String path2 = "/sdcard/凯迪拉克/";
    public static String path3 = "/sdcard/凯迪拉克缩略图/";
    public static String JingXiaoShang = "";
    public static String JingXiaoCode = "";
    public  static String filePath = "/sdcard/Cadillac/";

    public static List<Order> excelList = new ArrayList<>();
    public static List<Order> excelist3=new ArrayList<>();

    //这个集合存在的意义在于判断二次取证是否重复
    public static List<List<String>> excelList2 = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
        // 初始化数据库
        initDbSqlite();

    }

    public static DbManager initDbSqlite() {
        /**
         * 初始化DaoConfig配置
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName("myexceldata.db")
                //设置数据库路径，默认存储在app的私有目录
                .setDbDir(new File("/sdcard/"))
                //设置数据库的版本号
                .setDbVersion(2)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });
        //设置是否允许事务，默认true
        //.setAllowTransaction(true)

        DbManager db = x.getDb(daoConfig);
        return db;
    }
}

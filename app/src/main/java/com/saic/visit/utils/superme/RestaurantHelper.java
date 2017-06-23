package com.saic.visit.utils.superme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Super-me on 2017/5/24.
 */

public class RestaurantHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Excelldata.db";//数据库名称
    private static final int SCHEMA_VERSION = 2;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    public RestaurantHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建的是一个午餐订餐的列表,id,菜名,地址等等
        db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, xuhao TEXT, pointcode TEXT, type TEXT, isswith TEXT,name TEXT,contont TEXT,beizhu TEXT, phone TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            db.execSQL("ALTER TABLE restaurants ADD phone TEXT;");
        }
    }

    public Cursor getAll(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf = new StringBuilder("SELECT _id, xuhao , pointcode, type, isswith, name, contont, beizhu,phone FROM restaurants");

        if (where != null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy != null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }

        return (getReadableDatabase().rawQuery(buf.toString(), null));
    }

    public Cursor getById(String id) {//根据点击事件获取id,查询数据库
        String[] args = {id};

        return (getReadableDatabase()
                .rawQuery("SELECT _id, xuhao , pointcode, type, isswith,name,contont ,beizhu，phone FROM restaurants WHERE _ID=?",
                        args));
    }

    public void insert(String xuhao , String pointcode, String type, String isswith, String name,String contont,String beizhu,String phone) {
        ContentValues cv = new ContentValues();

        cv.put("xuhao ", xuhao );
        cv.put("pointcode", pointcode);
        cv.put("type", type);
        cv.put("isswith", isswith);
        cv.put("name",name);
        cv.put("contont",contont);
        cv.put("beizhu",beizhu);
        cv.put("phone", phone);

        getWritableDatabase().insert("restaurants", "xuhao ", cv);
    }

    public void update(String id, String xuhao , String pointcode,
                       String type, String isswith,String name,String contont,String beizhu, String phone) {
        ContentValues cv = new ContentValues();
        String[] args = {id};

        cv.put("xuhao ", xuhao );
        cv.put("pointcode", pointcode);
        cv.put("type", type);
        cv.put("isswith", isswith);
        cv.put("name",name);
        cv.put("contont",contont);
        cv.put("beizhu",beizhu);
        cv.put("phone", phone);

        getWritableDatabase().update("restaurants", cv, "_ID=?",
                args);
    }

    public String getxuhao (Cursor c) {
        return (c.getString(1));
    }

    public String getpointcode(Cursor c) {
        return (c.getString(2));
    }

    public String gettype(Cursor c) {
        return (c.getString(3));
    }

    public String getisswith(Cursor c) {
        return (c.getString(4));
    }

    public String getname(Cursor c){
        return (c.getString(5));
    }
    public String getcontont(Cursor c){
        return (c.getString(6));
    }
    public String getbeizhu(Cursor c){
        return (c.getString(7));
    }
    public String getPhone(Cursor c) {
        return (c.getString(8));
    }
}
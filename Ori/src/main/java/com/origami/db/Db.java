package com.origami.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @by: origami
 * @date: {2021-06-07}
 * @info:
 **/
public class Db extends SQLiteOpenHelper {

    public static final String TAG = "Db";

    public static String DB_MANE = "opendoor.db";

    public static final String FACE_IOC = "face_ioc";

    //
    private final static String CREATE_FACE_IOC = "create table if not exists face_ioc("
            + "id integer primary key autoincrement,"
            + "time varchar(255) default \"\","                       //bid
            + "userName varchar(255) default \"\","                           //时间戳  -- 存放去掉后三位!
            + "personId varchar(255) default \"\","
            + "personName varchar(255) default \"\","
            + "sex integer default 0,"
            + "personImage varchar(255) default \"\","
            + "cardNo varchar(255) default \"\","
            + "startTime varchar(255) default \"\","
            + "limitType integer default 0,"                       //添加方式
            + "endTime varchar(255) default \"\","
            + "content text default \"\","
            + "status integer default 0)";              //状态字段


    private static Db DBHelper;

    public static void init(Context context) {
        DBHelper = new Db(context);
    }

    private Db(@Nullable Context context) {
        super(context, DB_MANE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FACE_IOC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (oldVersion) { }
    }

    public static void doWriteSql(String sql,Object[] ...s){
        if(s.length != 0){
            DBHelper.getWritableDatabase().execSQL(sql,s[0]);
        }else {
            DBHelper.getWritableDatabase().execSQL(sql);
        }
    }

    public static Cursor doReadSql(String sql,String[] prams){
        return DBHelper.getReadableDatabase().rawQuery(sql,prams);
    }

    public static void closeDb() {
        DBHelper.getWritableDatabase().close();
    }

    /**
     * 方法1：检查某表列是否存在
     *
     * @param db         database
     * @param tableName  表名
     * @param columnName 列名
     * @return true已存在，false不存在
     */
    private boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }


}

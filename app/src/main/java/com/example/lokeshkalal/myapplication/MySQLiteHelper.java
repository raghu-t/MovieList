package com.example.lokeshkalal.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "warranty.db";
    private static final int DATABASE_VERSION = 4;
    private final static String TAG = "MySQLiteHelper";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + DataContract.DATA_TABLE_NAME + "(" + DataContract.COLUMN_ID + " integer primary key not null, "
            + DataContract.COLUMN_NAME + " text, "
            + DataContract.COLUMN_PRODUCT_IMAGE_PATH + " text, "
            + DataContract.COLUMN_BILL_IMAGES_PATH + " text not null, "
            + DataContract.COLUMN_PRODUCT_TYPE + " text, "
            + DataContract.COLUMN_BRAND_NAME + " text, "
            + DataContract.COLUMN_TEXT + " text, "
            + DataContract.COLUMN_PURCHASE_DATE + " text, "
            + DataContract.COLUMN_EXPIRY_DATE + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, DATABASE_CREATE);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("ls2", "onCreate");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.DATA_TABLE_NAME);
        onCreate(db);
    }

}

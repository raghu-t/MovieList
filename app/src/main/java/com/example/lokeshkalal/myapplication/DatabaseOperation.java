package com.example.lokeshkalal.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseOperation {

    private SQLiteDatabase database;
    private MySQLiteHelper sqLiteHelper;
    private final static String TAG = "DatabaseOperation";

    public DatabaseOperation(Context context) {
        sqLiteHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = sqLiteHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteHelper.close();
    }

    public void dummyAdd() {
        ContentValues values = new ContentValues();
        values.put(DataContract.COLUMN_ID, System.currentTimeMillis());
        values.put(DataContract.COLUMN_NAME, "abc");
        values.put(DataContract.COLUMN_PRODUCT_IMAGE_PATH, "xyz");
        values.put(DataContract.COLUMN_BILL_IMAGES_PATH, "pqr");
        values.put(DataContract.COLUMN_PRODUCT_TYPE, "abc");
        values.put(DataContract.COLUMN_BRAND_NAME, "pqr");
        values.put(DataContract.COLUMN_TEXT, "lmn");
        values.put(DataContract.COLUMN_PURCHASE_DATE, "1000");
        values.put(DataContract.COLUMN_EXPIRY_DATE, "2000");
        database.insert(DataContract.DATA_TABLE_NAME, null,
                values);
    }

    public long insert(Data data) {
        ContentValues values = new ContentValues();
        values.put(DataContract.COLUMN_ID, data.id);
        values.put(DataContract.COLUMN_NAME, data.name);
        values.put(DataContract.COLUMN_PRODUCT_IMAGE_PATH, data.productImagePath);
        values.put(DataContract.COLUMN_BILL_IMAGES_PATH, data.billImagePath);
        values.put(DataContract.COLUMN_PRODUCT_TYPE, data.productType);
        values.put(DataContract.COLUMN_BRAND_NAME, data.brandName);
        values.put(DataContract.COLUMN_TEXT, data.text);
        values.put(DataContract.COLUMN_PURCHASE_DATE, data.date);
        values.put(DataContract.COLUMN_EXPIRY_DATE, data.expiryDate);
        return database.insert(DataContract.DATA_TABLE_NAME, null,
                values);
    }

    public int delete(Data data) {
        return database.delete(DataContract.DATA_TABLE_NAME,
                DbHelper.buildQuery(DbHelper.DELETE_ITEM, data.id), null);
    }

    public int deleteAll() {
        return database.delete(DataContract.DATA_TABLE_NAME,
                null, null);
    }

    public Data getDataByPurchaseDate(long purchaseDate) {
        Data data = new Data();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.ALL_COLUMNS,
                DbHelper.buildQuery(DbHelper.GET_ITEM_BY_PURCHASE_DATE, purchaseDate), null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
            data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
            data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
            data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
            data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
            data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
            data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
            data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
            data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));
        }
        if (cursor != null) {
            cursor.close();
        }
        return data;
    }

    public Data getDataByExpiryDate(long expiryDate) {
        Data data = new Data();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.ALL_COLUMNS,
                DbHelper.buildQuery(DbHelper.GET_ITEM_BY_EXPIRY_DATE, expiryDate), null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
            data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
            data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
            data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
            data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
            data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
            data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
            data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
            data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));
        }
        if (cursor != null) {
            cursor.close();
        }
        return data;
    }

    public ArrayList<Data> getAllDataList() {
        ArrayList<Data> allDatas = new ArrayList<>();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.ALL_COLUMNS, null, null, null, null, DbHelper.SORT_BY_NAME_ASCENDING);
        if (cursor != null && cursor.moveToFirst()) {

            do {
                Data data = new Data();
                data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
                data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
                data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
                data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
                data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
                data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
                data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
                data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
                data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));
                Log.i("SONI", data.toString());
                allDatas.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return allDatas;
    }

    public ArrayList<Data> getDataListByProductType(String productType) {
        ArrayList<Data> allDatas = new ArrayList<>();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.ALL_COLUMNS,
                DbHelper.buildQuery(DbHelper.GET_ITEMS_BY_PRODUCT_TYPE, productType), null,
                null, null, DbHelper.SORT_BY_PRODUCT_TYPE_ASCENDING);
        if (cursor != null && cursor.moveToFirst()) {
            Data data = new Data();
            do {
                data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
                data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
                data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
                data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
                data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
                data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
                data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
                data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
                data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));

                allDatas.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return allDatas;
    }

    public ArrayList<Data> getDataListByBrandName(String brandName) {
        ArrayList<Data> allDatas = new ArrayList<>();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.ALL_COLUMNS,
                DbHelper.buildQuery(DbHelper.GET_ITEMS_BY_BRAND_NAME, brandName), null,
                null, null, DbHelper.SORT_BY_PRODUCT_TYPE_ASCENDING);
        if (cursor != null && cursor.moveToFirst()) {
            Data data = new Data();
            do {
                data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
                data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
                data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
                data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
                data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
                data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
                data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
                data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
                data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));

                allDatas.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return allDatas;
    }

    public ArrayList<Data> getDataListByText(String text) {
        ArrayList<Data> allDatas = new ArrayList<>();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME,
                DbHelper.ALL_COLUMNS, DbHelper.buildQuery(DbHelper.GET_ITEMS_BY_TEXT, text), null,
                null, null, DbHelper.SORT_BY_NAME_ASCENDING);
        if (cursor != null && cursor.moveToFirst()) {
            Data data = new Data();
            do {
                data.id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.COLUMN_ID));
                data.name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_NAME));
                data.productImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_IMAGE_PATH));
                data.billImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BILL_IMAGES_PATH));
                data.productType = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE));
                data.brandName = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_BRAND_NAME));
                data.text = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_TEXT));
                data.date = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PURCHASE_DATE));
                data.expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_EXPIRY_DATE));

                allDatas.add(data);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return allDatas;
    }

    public ArrayList<String> getAllProductType() {
        ArrayList<String> allProductTypes = new ArrayList<>();
        Cursor cursor = database.query(DataContract.DATA_TABLE_NAME, DbHelper.PRODUCT_TYPE, null, null,
                null, null, DbHelper.SORT_BY_PRODUCT_TYPE_ASCENDING);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                allProductTypes.add(cursor.getString(cursor.getColumnIndexOrThrow(DataContract.COLUMN_PRODUCT_TYPE)));
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }
        return allProductTypes;
    }
}
package com.example.lokeshkalal.myapplication;

public class DbHelper {

    public static final String[] ALL_COLUMNS = {DataContract.COLUMN_ID,
            DataContract.COLUMN_NAME, DataContract.COLUMN_PRODUCT_IMAGE_PATH, DataContract.COLUMN_BILL_IMAGES_PATH, DataContract.COLUMN_PRODUCT_TYPE,
            DataContract.COLUMN_BRAND_NAME, DataContract.COLUMN_TEXT, DataContract.COLUMN_PURCHASE_DATE, DataContract.COLUMN_EXPIRY_DATE};
    public static final String[] PRODUCT_TYPE = {DataContract.COLUMN_PRODUCT_TYPE};


    public static final int GET_ITEM = 0;
    public static final int GET_ALL_ITEMS = 1;
    public static final int GET_ITEMS_BY_PRODUCT_TYPE = 2;
    public static final int GET_ITEMS_BY_NAME = 3;
    public static final int GET_ITEMS_BY_BRAND_NAME = 4;
    public static final int GET_ITEMS_BY_TEXT = 5;
    public static final int GET_ITEM_BY_EXPIRY_DATE = 6;
    public static final int GET_ITEM_BY_PURCHASE_DATE = 7;
    public static final int DELETE_ITEM = 8;


    public static final String SORT_ORDER_ASCENDING = "ASC";
    public static final String SORT_ORDER_DESCENDING = "DESC";
    public static final String SORT_BY_NAME_ASCENDING = DataContract.COLUMN_NAME + " " + SORT_ORDER_ASCENDING;
    public static final String SORT_BY_PRODUCT_TYPE_ASCENDING = DataContract.COLUMN_PRODUCT_TYPE + " " + SORT_ORDER_ASCENDING;
    public static final String SORT_BY_TEXT_ASCENDING = DataContract.COLUMN_TEXT + " " + SORT_ORDER_ASCENDING;
    public static final String SORT_BY_NAME_DESCENDING = DataContract.COLUMN_NAME + " " + SORT_ORDER_DESCENDING;


    public static String buildQuery(int type, String arg) {
        String query = null;
        switch (type) {
            case GET_ITEMS_BY_PRODUCT_TYPE:
                query = DataContract.COLUMN_PRODUCT_TYPE + " LIKE " + "\"%" + arg + "%\"";
                break;
            case GET_ITEMS_BY_NAME:
                query = DataContract.COLUMN_NAME + " LIKE " + "\"%" + arg + "%\"";
                break;
            case GET_ITEMS_BY_BRAND_NAME:
                query = DataContract.COLUMN_BRAND_NAME + " LIKE " + "\"%" + arg + "%\"";
                break;
            case GET_ITEMS_BY_TEXT:
                query = DataContract.COLUMN_TEXT + " LIKE " + "\"%" + arg + "%\"";
            default:
                break;
        }
        return query;
    }


    public static String buildQuery(int type, long arg) {
        String query = null;
        switch (type) {
            case GET_ITEM:
            case DELETE_ITEM:
                query = DataContract.COLUMN_ID + " = " + arg;
                break;
            case GET_ITEM_BY_PURCHASE_DATE:
                query = DataContract.COLUMN_PURCHASE_DATE + " = " + arg;
                break;
            case GET_ITEM_BY_EXPIRY_DATE:
                query = DataContract.COLUMN_EXPIRY_DATE + " = " + arg;
                break;
            default:
                break;
        }
        return query;
    }
}

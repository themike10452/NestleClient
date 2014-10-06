package com.themike10452.nestleclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Mike on 6/8/2014.
 */
public class mSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    public final static int DBversion = 1;
    public final static String DBname = "database1";

    public final static String NESTLE_TABLE_NAME = "Nescafe";
    public final static String NESTLE_PRIMARY_KEY = "_id";
    public final static String NESTLE_COLUMN_1 = "date";
    public final static String NESTLE_COLUMN_2 = "first";
    public final static String NESTLE_COLUMN_3 = "last";
    public final static String NESTLE_COLUMN_4 = "difference";

    public final static String TIPS_TABLE_NAME = "Tips";
    public final static String TIPS_PRIMARY_KEY = "_id";
    public final static String TIPS_COLUMN_1 = "name";
    public final static String TIPS_COLUMN_2 = "position";

    public static mSQLiteOpenHelper instance = null;

    public mSQLiteOpenHelper(Context context) {
        super(context, DBname, null, DBversion);
    }

    public static boolean insertIntoTable(SQLiteDatabase database, String table, String[] data) throws SQLiteConstraintException {
        String values = "";
        for (int i = 0; i < data.length; i++) {
            if (data[i].trim().length() != 0) {
                if (i == data.length - 1)
                    values += quote(data[i]);
                else
                    values += quote(data[i]) + ", ";
            } else {
                if (i == data.length - 1)
                    values += "null";
                else
                    values += "null ,";
            }
        }
        String query = String.format("insert into %s values (%s);", table, values);
        database.execSQL(query);
        return true;
    }

    public static void updateTable(SQLiteDatabase database, String table, String id, String[] newValues) {
        ContentValues cv = new ContentValues();
        if (table.equals(NESTLE_TABLE_NAME)) {
            try {
                cv.put(NESTLE_COLUMN_1, newValues[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                cv.put(NESTLE_COLUMN_2, newValues[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                cv.put(NESTLE_COLUMN_3, newValues[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                cv.put(NESTLE_COLUMN_4, newValues[3]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            database.update(table, cv, NESTLE_PRIMARY_KEY + " =" + quote(id), null);
        } else if (table.equals(TIPS_TABLE_NAME)) {

        }
    }

    public static String quote(String s) {
        return "\'" + s + "\'";
    }

    public static ArrayList<String> selectFromTable(SQLiteDatabase db, String table, String sample) {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = db.query(table, new String[]{NESTLE_COLUMN_1, NESTLE_COLUMN_2, NESTLE_COLUMN_3, NESTLE_COLUMN_4}, NESTLE_PRIMARY_KEY + " LIKE ?", new String[]{sample}, null, null, null);
        while (cursor.moveToNext()) {
            String s = null;
            StringBuffer buffer = new StringBuffer();
            for (String col : new String[]{NESTLE_COLUMN_2, NESTLE_COLUMN_3, NESTLE_COLUMN_4}) {
                s = cursor.getString(cursor.getColumnIndex(col));
                if (s != null && s.length() > 0)
                    buffer.append(s + ":");
                else
                    buffer.append("null:");
            }
            list.add(buffer.toString());
        }
        return list;
    }

    public static ArrayList<String> getListOfDays(SQLiteDatabase db) {
        Cursor cursor = db.query(true, NESTLE_TABLE_NAME, new String[]{NESTLE_COLUMN_1}, null, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<String>();
        while (cursor.moveToNext())
            list.add(cursor.getString(cursor.getColumnIndex(NESTLE_COLUMN_1)));
        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        instance = this;

        final String createTable1 = String.format("create table %s (" +
                "%s VARCHAR(10) PRIMARY KEY," +
                "%s VARCHAR(8)," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER" +
                ");", NESTLE_TABLE_NAME, NESTLE_PRIMARY_KEY, NESTLE_COLUMN_1, NESTLE_COLUMN_2, NESTLE_COLUMN_3, NESTLE_COLUMN_4);

        db.execSQL(createTable1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}

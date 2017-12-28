package com.todo.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todo.simpletodo.FeedReaderContract.FeedEntry;

import static com.todo.simpletodo.FeedReaderContract.FeedEntry.COLUMN_NAME_ITEM_ID;
import static com.todo.simpletodo.FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE;
import static com.todo.simpletodo.FeedReaderContract.FeedEntry.PROJECTION;
import static com.todo.simpletodo.FeedReaderContract.FeedEntry.TABLE_NAME;

/**
 * Created by Grant on 12/26/17.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_ITEM_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoItem.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void read(String[] args) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, PROJECTION, args[0], args, null, null, null);

        //TODO: parse the cursor to useable data
    }

    public void delete(long rowID) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_NAME_ITEM_ID + "LIKE ?";
        String[] selectionArgs ={ String.valueOf(rowID) };

        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void update(long rowID, String[] updatedValues){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = parseToContentValue(updatedValues);
        String selection = COLUMN_NAME_ITEM_ID + "LIKE ?";
        String[] selectionArgs ={ String.valueOf(rowID) };
        int count = db.update(TABLE_NAME, values, selection, selectionArgs);

    }

    /**
     * Insert into database
     * @param values
     */
    public void insert(String[] values) {
        //Check values passed match parameter for database
        if (values.length == PROJECTION.length) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues content = parseToContentValue(values);

            long newRowId;
            newRowId = db.insert(TABLE_NAME, COLUMN_NAME_NULLABLE, content);
            //TODO: Add check for successful insertion and log
        }
    }

    public ContentValues parseToContentValue(String[] values) {
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < values.length; i++) {
            contentValues.put(PROJECTION[i], values[i]);
        }

        return contentValues;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

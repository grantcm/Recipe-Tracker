package com.todo.simpletodo;

import android.provider.BaseColumns;

/**
 * Created by Grant on 12/26/17.
 */

public final class FeedReaderContract {
    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_NULLABLE = "null";

        public static final String[] PROJECTION = {
                COLUMN_NAME_ITEM_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_CONTENT
        };
    }
}

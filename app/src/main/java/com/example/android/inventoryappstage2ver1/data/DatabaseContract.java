package com.example.android.inventoryappstage2ver1.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryappstage2ver1";
    public static final String PATH_GAMES = "games";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private DatabaseContract() {
    }

    public static final class GameEntry implements BaseColumns {


        //public static final string for ear row of table that we need to refer - inside and or outside this class
        public final static String TABLE_NAME = "games";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GAMES);

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_CATEGORY = "category";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";

        public final static String COLUMN_SUPPLIER = "supplier";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";
        public final static String COLUMN_SUPPLIER_EMAIL = "email";

        //public static final string for game's category
        public final static int CATEGORY_RPG = 1;
        public final static int CATEGORY_SIMULATOR = 2;
        public final static int CATEGORY_1PERSON = 3;
        public final static int CATEGORY_ADVENTURE = 4;
        public final static int CATEGORY_RTS = 5;
        public final static int CATEGORY_PUZZLE = 6;
        public final static int CATEGORY_ACTION = 7;
        public final static int CATEGORY_COMBAT = 8;
        public final static int CATEGORY_EDU = 9;
        public final static int CATEGORY_UNKNOWN = 0;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of game.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single game.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        /**
         * Returns whether or not the given category is
         * {@link #CATEGORY_RPG},
         * {@link #CATEGORY_SIMULATOR},
         * {@link #CATEGORY_1PERSON},
         * {@link #CATEGORY_ADVENTURE},
         * {@link #CATEGORY_RTS},
         * {@link #CATEGORY_PUZZLE},
         * {@link #CATEGORY_ACTION},
         * {@link #CATEGORY_COMBAT},
         * {@link #CATEGORY_EDU},
         * or {@link #CATEGORY_UNKNOWN}.
         */
        public static boolean isValidCategory(int category) {
            return category == CATEGORY_RPG || category == CATEGORY_SIMULATOR || category == CATEGORY_1PERSON
                    || category == CATEGORY_ADVENTURE || category == CATEGORY_RTS || category == CATEGORY_PUZZLE
                    || category == CATEGORY_ACTION || category == CATEGORY_COMBAT || category == CATEGORY_EDU
                    || category == CATEGORY_UNKNOWN;
        }
    }
}

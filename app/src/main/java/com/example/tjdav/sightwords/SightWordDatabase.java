package com.example.tjdav.sightwords;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tjdav on 1/19/2018.
 */

public class SightWordDatabase {

        // Name of database
        private SQLiteDatabase db;

        // Helper strings to make constructing SQL queries more straight forward
        // inside and outside of this class.
        private static final String TABLE_ROWID = "_id";
        private static final String TABLE_COLUMN_ID = "level";
        private static final String TABLE_COLUMN_SIGHTWORD = "sightword";
        private static final String TABLE_COLUMN_ALT1 = "alt1";
        private static final String TABLE_COLUMN_ALT2 = "alt2";

        // private static final variables for internal methods
        private static final String DB_NAME = "sightword";
        private static final String TABLE = "words";
        private static final int DB_VERSION = 1;

        private CustomSQLiteOpenHelper helper;

        SightWordDatabase(Context context){

            // Creates instance of our internal CustomSQLiteOpenHelper class
            helper = new CustomSQLiteOpenHelper(context);

            // Get a writable database
            db = helper.getWritableDatabase();
        }

        void insert(int id, String sightWord, String alt1, String alt2){
            // Adds a new row in to database containing the title and tts text
            String insert = "INSERT INTO " + TABLE + " (" +
                    TABLE_COLUMN_ID + ", " +
                    TABLE_COLUMN_SIGHTWORD + ", " +
                    TABLE_COLUMN_ALT1 + ", " +
                    TABLE_COLUMN_ALT2 + ") " +
                    "VALUES ('" + id + "', " +
                            "'" + sightWord + "', " +
                            "'" + alt1 + "', " +
                            "'" + alt2 + "');";

            db.execSQL(insert);
        }

        void dropTable(){
            String delete = "DROP TABLE IF EXISTS '" + TABLE + "';";
            db.execSQL(delete);
            helper.onCreate(db);

        }

        Cursor selectAll() {
            // Gets all records
            Cursor c = db.rawQuery("SELECT * from " + TABLE, null);

            return c;
        }

        private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

            CustomSQLiteOpenHelper(Context context) {
                super(context, DB_NAME, null, DB_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                // Create a table for the tts strings and titles
                String newTable =
                        "create table " + TABLE + " (" +
                                TABLE_ROWID + " integer primary key autoincrement not null, " +
                                TABLE_COLUMN_ID + " text not null, " +
                                TABLE_COLUMN_SIGHTWORD + " text not null, " +
                                TABLE_COLUMN_ALT1 + " text not null, " +
                                TABLE_COLUMN_ALT2 + " text not null);";

                db.execSQL(newTable);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        }
 }


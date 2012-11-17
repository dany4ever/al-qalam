/**
 * Copyright 2012 (c) Al-Qalam Project
 *
 * This file is part of Al-Qalam (uz.efir.alqalam) package.
 *
 * Al-Qalam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Al-Qalam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uz.efir.alqalam;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlQalamDatabase {
    private static final String TAG = "DatabaseHelper";
    // Database info
    private static final String DATABASE_NAME = "alqalam.db";
    private static final int DATABASE_VERSION = 1; // Note: reset to 1 in app version 4.0
    // Tables
    private static final String TABLE_ARABIC = "quran";
    private static final String TABLE_RUSSIAN = "russian";
    private static final String TABLE_TURKISH = "turkish";
    private static final String TABLE_UZBEK_CYRILLIC = "uzbek_cyrillic";
    private static final String TABLE_UZBEK_LATIN = "uzbek_latin";
    private static final String TABLE_BOOKMARKS = "bookmarks";
    private static final String[] TRANSLATION_TABLES =
    {
        TABLE_UZBEK_CYRILLIC,
        TABLE_UZBEK_LATIN,
        TABLE_RUSSIAN,
        TABLE_TURKISH
    };
    public static final String COLUMN_SURAHNO = "surah_no";
    public static final String COLUMN_AYATNO = "ayat_no";
    public static final String COLUMN_AYAT = "ayat";
    public static final String COLUMNT_DATE = "date_col";

    private SQLiteDatabase db;
    private Context mContext;
    private AlQalamDbHelper dbHelper;

    public AlQalamDatabase(Context context) {
        mContext = context;
        dbHelper = new AlQalamDbHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Open database from SQLite */
    public boolean openFromFile(File file) {
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
        return db.isOpen();
    }

    /* Open database in read-only. */
    public AlQalamDatabase openReadable() {
        try {
            db = dbHelper.getReadableDatabase();
            Log.i(TAG, "Readable open");
        } catch(SQLException e) {
            Log.e(TAG, "Could not open for reading");
        }

        return this;
    }

    /* Open database.  */
    public AlQalamDatabase openWritable() {
        try {
            db = dbHelper.getWritableDatabase();
            Log.i(TAG, "Writable open");
        }
        catch (SQLException e){
            Log.e(TAG, "Can't open database for writing");
        }

        return this;
    }

    /*  Close database */
    public void close() {
        Log.i(TAG, "Close");
        db.close();
    }

    /* is database open */
    public boolean isOpen() {
        if (db == null || !db.isOpen())
        {
            return false;
        }
        return true;
    }

    public class AlQalamDbHelper extends SQLiteOpenHelper {

        public AlQalamDbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // Create Qur'an database
            createTable(db, TABLE_ARABIC);
            createTable(db, TABLE_UZBEK_CYRILLIC);
            //createTable(db, TABLE_RUSSIAN);
            //createTable(db, TABLE_TURKISH);
            //createTable(db, TABLE_UZBEK_LATIN);

            // Bookmarks table
            db.execSQL("CREATE TABLE " + TABLE_BOOKMARKS +" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SURAHNO + " INTEGER NOT NULL," +
                    COLUMN_AYATNO + " INTEGER NOT NULL, date_col DEFAULT CURRENT_TIMESTAMP);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
            Log.i(TAG, "Upgrade Database");

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARABIC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUSSIAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURKISH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UZBEK_CYRILLIC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UZBEK_LATIN);

            onCreate(db);

        }

        private void createTable(SQLiteDatabase db, String table) {
            /** CREATE TABLE table(
             *                   _id INTEGER PRIMARY KEY AUTOINCREMENT,
             *                   surah_no INTEGER NOT NULL,
             *                   ayat_no INTEGER NOT NULL,
             *                   ayat TEXT COLLATE UNICODE);
             */
            db.execSQL("CREATE TABLE " + table +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SURAHNO + " INTEGER NOT NULL," +
                    COLUMN_AYATNO + " INTEGER NOT NULL," +
                    COLUMN_AYAT + " TEXT COLLATE UNICODE);");
            try {
                // Insert Qur'an ayats
                insertAyats(db, table);
            } catch (IOException e) {
                Log.e(TAG, "IOException while creating table " + table);
            }
        }

        private void insertAyats(SQLiteDatabase db, String which) throws IOException {
            Log.i(TAG, "Inserting translations... " + which);

            final Resources resources = mContext.getResources();
            InputStream inputStream = null;
            /*if (TABLE_UZBEK_LATIN.equals(which)) {
                inputStream = resources.openRawResource(R.raw.uzbek_latin);
            } else if (TABLE_RUSSIAN.equals(which)) {
                inputStream = resources.openRawResource(R.raw.russian);
            } else if (TABLE_TURKISH.equals(which)) {
                inputStream = resources.openRawResource(R.raw.turkish);
            } else */if (TABLE_ARABIC.equals(which)) {
                inputStream = resources.openRawResource(R.raw.quran);
            } else {
                inputStream = resources.openRawResource(R.raw.uzbek_cyrillic);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line;
                int surahNumber = 1;
                int ayatNumber = 1;
                while ((line = reader.readLine()) != null) {
                    ContentValues values = new ContentValues();

                    // Let's collect the values
                    values.put(COLUMN_SURAHNO, surahNumber);
                    values.put(COLUMN_AYATNO, ayatNumber);
                    values.put(COLUMN_AYAT, line);

                    // Now insert the values to the table
                    db.insert(which, COLUMN_AYAT, values);

                    // Routine to populate proper surah and ayat numbers
                    if (ayatNumber == Utils.SURAH_NUMBER_OF_AYATS[surahNumber - 1]) {
                        surahNumber++;
                        ayatNumber = 1;
                    } else {
                        ayatNumber++;
                    }
                }
            }
            finally {
                reader.close();
            }
        }
    }

    public Cursor getArabicVerses(int index) {
        String selection = COLUMN_SURAHNO + "=" + index;
        Cursor c = db.query(TABLE_ARABIC, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO, COLUMN_AYAT}, selection, null, null, null, null);

        return c;
    }

    public Cursor getVerses(int index, int language) {
        String selection = COLUMN_SURAHNO + "=" + index;
        String table = TRANSLATION_TABLES[language];

        Cursor c = db.query(table, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO, COLUMN_AYAT}, selection, null, null, null, null);

        return c;
    }

    public Cursor getWordMatches(String query, int language) {
        String selection = COLUMN_AYAT + " MATCH ? " + query+"*";
        String table = TRANSLATION_TABLES[language];

        Cursor c = db.query(table, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO, COLUMN_AYAT}, selection, null, null, null, null);

        return c;
    }

    // bookmark or unbookmark the ayat
    public boolean bookmarkOperation(int chapter, int verse) {
        String selection = COLUMN_SURAHNO + "="  + chapter + " AND " + COLUMN_AYATNO + "=" + verse;

        // aready exist, we have to unbookmark
        if (db.delete(TABLE_BOOKMARKS, selection, null) != 0 ){
            return false;
        }
        // does not exist, bookmark it;
        else {
            ContentValues values = new ContentValues();

            values.put(COLUMN_SURAHNO, chapter);
            values.put(COLUMN_AYATNO, verse);
            db.insert(TABLE_BOOKMARKS, null, values);
            return true;
        }
    }


    public boolean isInBookmark(int chapter, int verse) {

        String selection = COLUMN_SURAHNO + "="  + chapter + " AND " + COLUMN_AYATNO + "=" + verse;

        Cursor c = db.query(TABLE_BOOKMARKS, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO}, selection, null, null, null, null);

        if (c.moveToFirst())
            return true;
        else
            return false;
    }

    //get bookmarks for surah
    public Cursor getBookmarksForSurah(int chapter) {
        String selection = COLUMN_SURAHNO + "="  + chapter ;

        return db.query(TABLE_BOOKMARKS, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO}, selection, null, null, null, COLUMN_AYATNO);

    }

    //get all bookmarks
    public Cursor getAllBookmarks() {

        return db.query(TABLE_BOOKMARKS, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO}, null, null, null, null, COLUMN_SURAHNO);

    }
}

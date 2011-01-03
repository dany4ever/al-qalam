/*
 * Copyright 2010 (c) Al-Qalam Project
 *
 * This file is part of Al-Qalam (com.uzislam.alqalam) package.
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

package com.uzislam.alqalam.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.uzislam.alqalam.CONSTANTS;
import com.uzislam.alqalam.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class Helper extends SQLiteOpenHelper {

	private static final String TAG = "Helper";

	private static final String COLUMN_SURAHNO = "surah_no";
	private static final String COLUMN_AYATNO = "ayat_no";
	private static final String COLUMN_AYAT = "ayat";
	//private static final String TABLE_ARABIC = "arabic";
	private static final String TABLE_RUSSIAN = "russian";
	private static final String TABLE_TURKISH = "turkish";
	private static final String TABLE_UZBEK_CYRILLIC = "uzbek_cyrillic";
	private static final String TABLE_UZBEK_LATIN = "uzbek_latin";

	private static final int DATABASE_VERSION = 1;

	private final Context mContext;

	public Helper (Context context) {
		super(context, CONSTANTS.DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create Qur'an database
		//createTable(db, TABLE_ARABIC);
		createTable(db, TABLE_RUSSIAN);
		createTable(db, TABLE_TURKISH);
		createTable(db, TABLE_UZBEK_CYRILLIC);
		createTable(db, TABLE_UZBEK_LATIN);
		
		/** CREATE TABLE bookmarks(
		 *                        _id INTEGER PRIMARY KEY AUTOINCREMENT,
		 *                        surah_no INTEGER NOT NULL,
		 *                        ayat_no INTEGER NOT NULL,
		 *                        date_col DATETIME NOT NULL);
		 */
		db.execSQL("CREATE TABLE bookmarks(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_SURAHNO + " INTEGER NOT NULL," +
				COLUMN_AYATNO + " INTEGER NOT NULL,date_col DATETIME NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		// TODO Auto-generated method stub
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
		InputStream inputStream = resources.openRawResource(R.raw.uzbek_cyrillic);
		if (which.equals(TABLE_UZBEK_LATIN))
			inputStream = resources.openRawResource(R.raw.uzbek_latin);
		else if (which.equals(TABLE_RUSSIAN))
			inputStream = resources.openRawResource(R.raw.russian);
		else if (which.equals(TABLE_TURKISH))
			inputStream = resources.openRawResource(R.raw.turkish);
		/* TODO: Enable when Arabic text is supported by Android
		else if (which.equals(TABLE_ARABIC))
			inputStream = resources.openRawResource(R.raw.arabic);
		*/
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
				if (ayatNumber == CONSTANTS.SurahNumberOfAyats[surahNumber - 1]) {
					surahNumber++;
					ayatNumber = 1;
				} else {
					ayatNumber++;
				}
			}
		} finally {
			reader.close();
		}
	}
	
	public Cursor getWordMatches(String query, String[] columns) {
		String selection = COLUMN_AYAT + " MATCH ?";
		String[] selectionArgs = new String[] {query+"*"};
		
		return query(selection, selectionArgs, columns);
	}

	private Cursor query(String selection, String[] selectionArgs,
			String[] columns) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(/*TABLE_ARABIC
				+ "," +*/TABLE_RUSSIAN
				+ "," + TABLE_TURKISH
				+ "," + TABLE_UZBEK_CYRILLIC
				+ "," + TABLE_UZBEK_LATIN); 
		return null;
	}
}

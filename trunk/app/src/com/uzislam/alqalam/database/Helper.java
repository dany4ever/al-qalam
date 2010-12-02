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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Helper extends SQLiteOpenHelper {
	
	private static final String TAG = "Helper";
	
	private static final String DATABASE_NAME = "quran.db";
	private static final String TABLE_SURAHNO_COLUMN = "surah_no";
	private static final String TABLE_AYATNO_COLUMN = "ayat_no";
	private static final String TABLE_AYAT_COLUMN = "ayat";
	
	private static final String UZBEK_CYRILLIC = "uzbek_cyrillic";
	private static final String UZBEK_LATIN = "uzbek_latin";
	private static final String RUSSIAN = "russian";
	private static final String ARABIC = "arabic";
	
	private static final int DATABASE_VERSION = 1;
	
	private final Context mContext;
	
	public Helper (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create Qur'an database
		createTable(db, UZBEK_CYRILLIC);
		createTable(db, UZBEK_LATIN);
		createTable(db, RUSSIAN);
		//createTable(db, ARABIC);
		
		/** CREATE TABLE bookmarks(
		 *                        _id INTEGER PRIMARY KEY AUTOINCREMENT,
		 *                        surah_no INTEGER NOT NULL,
		 *                        ayat_no INTEGER NOT NULL,
		 *                        date_col DATETIME NOT NULL);
		 */
		db.execSQL("CREATE TABLE bookmarks(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				TABLE_SURAHNO_COLUMN + " INTEGER NOT NULL," +
				TABLE_AYATNO_COLUMN + " INTEGER NOT NULL,date_col DATETIME NOT NULL);");
		
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
				TABLE_SURAHNO_COLUMN + " INTEGER NOT NULL," +
				TABLE_AYATNO_COLUMN + " INTEGER NOT NULL," +
				TABLE_AYAT_COLUMN + " TEXT COLLATE UNICODE);");
		
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
		if (which.equals(UZBEK_LATIN))
			inputStream = resources.openRawResource(R.raw.uzbek_latin);
		else if (which.equals(RUSSIAN))
			inputStream = resources.openRawResource(R.raw.russian);
		else if (which.equals(ARABIC))
			inputStream = resources.openRawResource(R.raw.arabic);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line;
			int surahNumber = 1;
			int ayatNumber = 1;
			while ((line = reader.readLine()) != null) {
				ContentValues values = new ContentValues();
				
				// Let's collect the values
				values.put(TABLE_SURAHNO_COLUMN, surahNumber);
				values.put(TABLE_AYATNO_COLUMN, ayatNumber);
				values.put(TABLE_AYAT_COLUMN, line);
				
				// Now insert the values to the table
				db.insert(which, TABLE_AYAT_COLUMN, values);
				
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
}

package com.uzislam.alqalam.database;

/* 
 * Copyright 2010 (c) Shuhrat Dehkanov and Elmurod Talipov
 *  
 * This file is part of Al-Qalam (com.uzislam.alqalam package).
 * 
 * Al-Qalam  is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Al-Qalam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private static final int DATABASE_VERSION = 1;
	public static final String TRANS_COLUMN = "translation";
	
	private final Context mContext;
	
	public Helper (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db, "uzbek_cyrillic");
		//createTable(db, "uzbek_latin");
		//createTable(db, "russian");
		
		try {
			insertTranslation(db, "uzbek_cyrillic");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		// TODO Auto-generated method stub
		
	}
	
	private void createTable(SQLiteDatabase db, String table) {
		db.execSQL("CREATE TABLE " + table +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				TRANS_COLUMN + " TEXT COLLATE UNICODE);");
	}
	
	private void insertTranslation(SQLiteDatabase db, String which) throws IOException {
		Log.i(TAG, "Inserting translations...");
		final Resources resources = mContext.getResources(); 
		InputStream inputStream = resources.openRawResource(R.raw.uzbek_cyrillic);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				//db.execSQL("INSERT INTO uzbek_cyrillic('" + TRANS_COLUMN + "') values('" + line + "');");
				ContentValues values = new ContentValues();
				Log.i("YAY", "line: " + line);
				values.put(TRANS_COLUMN, line);
				db.insert(which, TRANS_COLUMN, values);
			}
		} finally {
			reader.close();
		}
	}
}

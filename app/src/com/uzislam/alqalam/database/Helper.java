package com.uzislam.alqalam.database;

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

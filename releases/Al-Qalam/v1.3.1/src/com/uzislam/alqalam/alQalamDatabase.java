package com.uzislam.alqalam;

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
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class alQalamDatabase {

	private static final String TAG = "alQalam Database";

	// Database Info
	private static final String 	DATABASE_NAME = "alQalam.db";
	private static final int 		DATABASE_VERSION = 2; 		
	
	// Tables
	private static final String 	TABLE_RUSSIAN = "russian";
	private static final String 	TABLE_TURKISH = "turkish";
	private static final String 	TABLE_UZBEK_CYRILLIC = "uzbek_cyrillic";
	private static final String 	TABLE_UZBEK_LATIN = "uzbek_latin";
	private static final String 	TABLE_BOOKMARKS = "bookmarks";

	private static final String[] 	TABLES = {"uzbek_cyrillic", "uzbek_latin","russian", "turkish"};
	
	
	public static final String COLUMN_SURAHNO = "surah_no";
	public static final String COLUMN_AYATNO = "ayat_no";
	public static final String COLUMN_AYAT = "ayat";	
	public static final String COLUMNT_DATE = "date_col";
	
	private SQLiteDatabase 		db;
	private Context 			context;
	private alQalamDbHelper 	dbHelper;

	public alQalamDatabase(Context _context) {
		context = _context;
		dbHelper = new alQalamDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/*  Open database from SQLite 	 */
	public boolean openFromFile(File file) {
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		return db.isOpen();
	}
	
	/* Open database in read-only. */
	public alQalamDatabase openReadable() {
		try { 
			db = dbHelper.getReadableDatabase();
			Log.i(TAG, "Readable open");
		}
		catch(SQLException e) {
			Log.e(TAG, "Can't open for reading ");
		}
		
		return this;
	}
	
	/* Open database.  */
	public alQalamDatabase openWritable() {
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
	
	public class alQalamDbHelper extends SQLiteOpenHelper {
		
		public alQalamDbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			// Create Qur'an database
			createTable(db, TABLE_RUSSIAN);
			createTable(db, TABLE_TURKISH);
			createTable(db, TABLE_UZBEK_CYRILLIC);
			createTable(db, TABLE_UZBEK_LATIN);
			
		
			db.execSQL("CREATE TABLE " + TABLE_BOOKMARKS +" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					COLUMN_SURAHNO + " INTEGER NOT NULL," +
					COLUMN_AYATNO + " INTEGER NOT NULL, date_col DEFAULT CURRENT_TIMESTAMP);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
			Log.i(TAG, "Upgrade Database");
			
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
			final Resources resources = context.getResources(); 
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
					if (ayatNumber == CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber - 1]) {
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
	
	public Cursor getVerses(int index, int language) {
		String selection = COLUMN_SURAHNO + "=" + index;
		String table = TABLES[language];
		
		Cursor c = db.query(table, new String[] {COLUMN_SURAHNO, COLUMN_AYATNO, COLUMN_AYAT}, selection, null, null, null, null);
		
		return c;
	}
	
	public Cursor getWordMatches(String query, int language) {
		String selection = COLUMN_AYAT + " MATCH ? " + query+"*";
		String table = TABLES[language];
				
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

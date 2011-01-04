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

package com.uzislam.alqalam;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class BookmarksActivity extends Activity {
	private ListView			BookmarkList; 	
	private String[] 			gSurahTitles;
	private int[][]				bookmarks;
	private String[]			bookmarksString;
		
	private final String		TAG = "Al-Qalam Bookmarks";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);
        
        BookmarkList = (ListView)findViewById(R.id.BookmarkList);
        BookmarkList.setVerticalScrollBarEnabled(false);
        BookmarkList.setHorizontalScrollBarEnabled(false);
       
        gSurahTitles = getResources().getStringArray(R.array.SurahTitle);
        
        alQalamDatabase db = new alQalamDatabase(this);
		db.openReadable();
		Cursor cursor = db.getAllBookmarks();
		
		if(cursor.moveToFirst())
		{
			bookmarks = new int[cursor.getCount()][2];
			bookmarksString = new String[cursor.getCount()];
			
			int index = 0, sr = 0, ay = 0;
			do
			{
				sr = cursor.getInt(cursor.getColumnIndex(alQalamDatabase.COLUMN_SURAHNO));
				ay = cursor.getInt(cursor.getColumnIndex(alQalamDatabase.COLUMN_AYATNO));
				bookmarks[index][0] = sr; 
				bookmarks[index][1] = ay; 
				bookmarksString[index] = gSurahTitles[sr-1]  + " : " + ay;
				index++;
			} while(cursor.moveToNext());
			
			BookmarkList.setAdapter(new ArrayAdapter<String>(this, R.layout.bookmark_row, bookmarksString));
			
		}
		
		db.close();
       
        BookmarkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					final int index, long order) {
				
						Intent quranIntent = new Intent(BookmarksActivity.this, SurahActivity.class);
						quranIntent.putExtra("sNumber", bookmarks[index][0]);
						quranIntent.putExtra("aNumber", bookmarks[index][1]);
						startActivity(quranIntent);
			}
        });
        
 	}
}

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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SurahActivity extends Activity {
	private static final String TAG = "al-Qalam SurahActivity";
	private String []		AYATSTRANS;
	private String []		AYATSARABIC;
	private int[]			Bookmarks;
	private int				surahNumber = 0;
	private int				currentAyat = 0;
	private int				selectedAyat = 0;

	private ImageView 			surahTitle;
	private SurahAdapter 		surahAdapter;
	private ListView			ayatList;

	private final int 			DIALOG_TRANSLATION = 0x01;
	private final int 			DIALOG_AYAT_CLICK_OPTION = 0x03;

	private SharedPreferences			commonPrefs;
	private SharedPreferences.Editor 	preferenceEditor = null;
	private int 						TranslationType = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
        	surahNumber = extras.getInt("sNumber");
        	currentAyat = extras.getInt("aNumber");
        }

        commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, 0);
        preferenceEditor = commonPrefs.edit();

		// Get Translation Type from shared preferences, default is 0 (uzbek-cyr)
        TranslationType = commonPrefs.getInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, 0);

        commonPrefs.getInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, 0);

        // Set chapter image title
        surahTitle = (ImageView) findViewById(R.id.suraName);

        // create new chapter adapter
        surahAdapter = new SurahAdapter(this);

        // get AyatList View
        ayatList = (ListView)findViewById(R.id.AyaList);

        ayatList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, final int index, long order) {
						selectedAyat = index + 1;
						showDialog(DIALOG_AYAT_CLICK_OPTION);
						return true;
			}
        });
        // Display surah
        showData();

        final ImageButton  imgBtnPrevious = (ImageButton) findViewById(R.id.headerPrev);
        final ImageButton  imgBtnNext = (ImageButton) findViewById(R.id.headerNext);

        imgBtnPrevious.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			surahNumber--;
    			if (surahNumber < 0) {
    				surahNumber = 0;
    				return;
    			}
    			// we are moving to previous  surah, reset and show;
    			currentAyat = 0;
    			showData();
    		}
        });

        imgBtnNext.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			surahNumber++;
    			if (surahNumber > 113) {
    				surahNumber = 113;
    				return;
    			}
    			// we are moving to next surah, reset and show;
    			currentAyat = 0;
    			showData();
    		}
        });
 	}

	private void resetData() {

		surahTitle.setImageResource(CONSTANTS.TITLE_OF_SURAHS[surahNumber]);

        // create string arrays for verses
		AYATSTRANS = new String[CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber]];
        AYATSARABIC = new String [CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber]];


    	AlQalamDatabase db = new AlQalamDatabase(this);
		db.openReadable();

		Cursor cursor;
		int index = 0;

        // if translation is enabled
		if (TranslationType != CONSTANTS.NUMBER_OF_TRANSLATIONS) {
			cursor = db.getVerses(surahNumber + 1, TranslationType);
			index = 0;
			if (cursor.moveToFirst()) {
				do
				{
					AYATSTRANS[index] = cursor.getString(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYAT));
					index++;
				} while(cursor.moveToNext());
			}

			cursor.close();

		}

		// Now read Bookmarked ayats to Array
		cursor = db.getBookmarksForSurah(surahNumber+1);
		if(cursor.moveToFirst())
		{
			Bookmarks = new int [cursor.getCount()];

			do
			{
				Bookmarks[index] = cursor.getInt(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYATNO));
				index++;

			} while(cursor.moveToNext());
		}
		else {
			Bookmarks =  null;
		}
		cursor.close();

        // Put verses
        cursor = db.getArabicVerses(surahNumber + 1);
        index = 0;
        if (cursor.moveToFirst()) {
            do {
                AYATSARABIC[index] = cursor.getString(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYAT));
                index++;
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
	}

	private void showData() {

		AyatIconifiedText	ait;
		Drawable  			iconBismillah =  null, bookmarkImage = null;
		int 				AyatBackground = Color.TRANSPARENT;

		surahAdapter.clear();

		resetData();

        for (int i=0; i < CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber] ; i++) {

        	// check if BISMILLAH must be shown
        	if (i == 0 && surahNumber != 0 && surahNumber != 8)
                iconBismillah = getResources().getDrawable(R.drawable.bismillah);

        	if (Bookmarks != null && Arrays.binarySearch(Bookmarks, i+1) >= 0) {
        		bookmarkImage = getResources().getDrawable(R.drawable.bookmark_icon);
        		AyatBackground = Color.rgb(243, 255, 140);
        	}

        	ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATSTRANS[i]);

    		ait.setAyatSpecialImage(getSpecialImage(surahNumber+1, i+1));
    		ait.setAyatBookmarkImage(bookmarkImage);
    		ait.setAyatBismillah(iconBismillah);
    		ait.setAyatBackground(AyatBackground);

        	surahAdapter.addItem(ait);

        	iconBismillah =  null;
        	bookmarkImage = null;
        	AyatBackground = Color.TRANSPARENT;
        }

        // make verses appear in list view
        ayatList.setAdapter(surahAdapter);
        ayatList.setCacheColorHint(00000000);
        ayatList.setDivider(null);

        // Jump to Specific Ayat (if defined);
        if (currentAyat != 0) {
        	ayatList.setSelectionFromTop(currentAyat - 1, 0);
        }
	}

	private Drawable getSpecialImage(int surah, int ayat) {

		for (int i=0; i <CONSTANTS.NUMBER_OF_SAJDAS; i++) {
			if (CONSTANTS.SAJDA_INDEXES[i][0] == surah &&  CONSTANTS.SAJDA_INDEXES[i][1] == ayat)
				return getResources().getDrawable(R.drawable.sajdah);
		}

		for (int i=0; i<CONSTANTS.NUMBER_OF_JUZZ; i++) {
			if (CONSTANTS.JUZZ_INDEXES[i][0] == surah && CONSTANTS.JUZZ_INDEXES[i][1] == ayat)
				return getResources().getDrawable(R.drawable.juzz);
		}

		return null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.surah_activity, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		//final int selectedItem;

    	switch (menuItem.getItemId()) {
    		case R.id.translation: //MENU_ITEM_TRANSLATION :
    			showDialog(DIALOG_TRANSLATION);
    			return true;
    	}

    	return false;
   }


	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);

		if (id == DIALOG_TRANSLATION){
			ab.setTitle(R.string.translation_language);

			ab.setSingleChoiceItems(R.array.TranslationOptions, TranslationType , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	Log.i(TAG, "Item " + item);
	            	changeTranslation(item);
	            }
	        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
	            @SuppressWarnings("deprecation")
				public void onClick(DialogInterface dialog, int item) {
	            	removeDialog(DIALOG_TRANSLATION);
	            }
	        });

		} else if (id == DIALOG_AYAT_CLICK_OPTION) {

			ab.setTitle(R.string.options);

			int options = R.array.AyatClickOptions;

			if (Bookmarks != null && Arrays.binarySearch(Bookmarks, selectedAyat) >= 0) {
				options = R.array.AyatClickOptions2;
        	}

			ab.setSingleChoiceItems(options, 0 , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	Log.i(TAG, "Item " + item);
	            	doAyatClickTask(item);
	            }

	        })
	        .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
	            @SuppressWarnings("deprecation")
				public void onClick(DialogInterface dialog, int item) {
	            	removeDialog(DIALOG_AYAT_CLICK_OPTION);
	            }
	        });
		}


		AlertDialog alert = ab.create();

		return alert;

	}

	@SuppressWarnings("deprecation")
	private void doAyatClickTask(int item) {
		removeDialog(DIALOG_AYAT_CLICK_OPTION);
		if (item == 2) {
			AlQalamDatabase db = new AlQalamDatabase(this);
			db.openWritable();

			AyatIconifiedText ait = (AyatIconifiedText)surahAdapter.getItem(selectedAyat-1);

			if (db.bookmarkOperation(surahNumber + 1, selectedAyat)) {
				ait.setAyatBackground(Color.rgb(243, 255, 140));
				ait.setAyatBookmarkImage(getResources().getDrawable(R.drawable.bookmark_icon));

			} else  {
				ait.setAyatBackground(Color.TRANSPARENT);
				ait.setAyatBookmarkImage(null);
			}

			surahAdapter.notifyDataSetChanged();


			// Now read Bookmarked ayats to Array
			Cursor cursor = db.getBookmarksForSurah(surahNumber+1);

			int  index = 0;

			if(cursor.moveToFirst())
			{
				Bookmarks = new int [cursor.getCount()];

				do
				{
					Bookmarks[index] = cursor.getInt(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYATNO));
					index++;

				} while(cursor.moveToNext());
			}
			else {
				Bookmarks =  null;
			}

			cursor.close();
			db.close();


		}
	}

	@SuppressWarnings("deprecation")
	private void changeTranslation(int lng) {
		removeDialog(DIALOG_TRANSLATION);
		TranslationType = lng;
		preferenceEditor.putInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, lng);
		preferenceEditor.commit();

		showData();
	}
}

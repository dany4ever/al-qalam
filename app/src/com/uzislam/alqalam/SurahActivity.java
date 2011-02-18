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
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
		
	public static DisplayMetrics displaymetrics = new DisplayMetrics(); 
    
	private ImageView 			surahTitle, playButton, pauseButton;
	private SurahAdapter 		surahAdapter;
	private ListView			ayatList; 
	
	private final int 			DIALOG_TRANSLATION = 0x01;
	private final int			DIALOG_RECITER = 0x02;
	private final int 			DIALOG_AYAT_CLICK_OPTION = 0x03;
	
	private int					audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
	

	private SharedPreferences			commonPrefs;
	private SharedPreferences.Editor 	preferenceEditor = null;
	private int 						TranslationType = 0;
	private int							ReciterType = 0;
	private LinearLayout 				audioControlPanel;
	private MediaPlayer					quranPlayer;
	private boolean						isAudioControlShown = false;
	
	private PowerManager 				powerManager;
	private WakeLock 					wakeLockPlayMode;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.surah);
        
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
       
        Bundle extras = getIntent().getExtras();
                
        if (extras != null) {
        	surahNumber = extras.getInt("sNumber");
        	currentAyat = extras.getInt("aNumber");
        }
        
        commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, 0);
        preferenceEditor = commonPrefs.edit();
        
		// Get Translation Type from shared preferences, default is 0 (uzbek-cyr)
        TranslationType = commonPrefs.getInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, 0);
		 
        ReciterType = commonPrefs.getInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, 0);
        
        // Set chapter image title 
        surahTitle = (ImageView) findViewById(R.id.suraName);
         
        // create new chapter adapter
        surahAdapter = new SurahAdapter(this); 	
        
        // get AyatList View 
        ayatList = (ListView)findViewById(R.id.AyaList);  
        
        ayatList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, final int index, long order) {
						selectedAyat = index + 1;
						showDialog(DIALOG_AYAT_CLICK_OPTION);
						return true;
			}
        });
        
        audioControlPanel = (LinearLayout) findViewById(R.id.audioPlayerControl);
        audioControlPanel.setVisibility(View.GONE);
        
        
        // Display surah
        showData();
   
        // Init player controller
        playerController();
        
        ImageView	surahName = (ImageView) findViewById(R.id.suraName);
        
        surahName.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			if (isAudioControlShown) {
    				isAudioControlShown = false;
    				audioControlPanel.setVisibility(View.GONE);
    			}
    			else {
    				isAudioControlShown = true;
    				audioControlPanel.setVisibility(View.VISIBLE);
    			}
    		}
        });
        
        final ImageButton  imgBtnPrevious = (ImageButton) findViewById(R.id.headerPrev);
        final ImageButton  imgBtnNext = (ImageButton) findViewById(R.id.headerNext);
 		
        imgBtnPrevious.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			surahNumber--;
    			if (surahNumber < 0) {
    				surahNumber = 0;
    				return;
    			}
    			
    			stopAudio();

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
    			
    			stopAudio();
    			
    			// we are moving to next surah, reset and show;
    			currentAyat = 0;
    			showData();
    		}
        });
 		
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLockPlayMode = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
 	}
	
	
	private void playerController() {
		playButton = (ImageView) findViewById(R.id.audioPlayButton);
		pauseButton = (ImageView) findViewById(R.id.audioPauseButton);
		
		pauseButton.setVisibility(View.GONE);
		
		playButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			pauseButton.setVisibility(View.VISIBLE);
    			playButton.setVisibility(View.GONE);
    			playAudio();
    		}
    	});
		
		pauseButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			pauseButton.setVisibility(View.GONE);
    			playButton.setVisibility(View.VISIBLE);
    			pauseAudio();
    		}
    	});
		
	}
	
	private void resetData() {
		
		surahTitle.setImageResource(CONSTANTS.TITLE_OF_SURAHS[surahNumber]);
		  
        // create string arrays for verses
		AYATSTRANS = new String[CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber]];
        AYATSARABIC = new String [CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber]];
            
        
    	alQalamDatabase db = new alQalamDatabase(this);
		db.openReadable();
		
		Cursor 	cursor;
		
        // if translation is enabled
		if (TranslationType != CONSTANTS.NUMBER_OF_TRANSLATIONS) {
			
			cursor = db.getVerses(surahNumber + 1, TranslationType);
			
			int  	index = 0;
			
			if(cursor.moveToFirst())
			{
				do
				{
					AYATSTRANS[index] = cursor.getString(cursor.getColumnIndex(alQalamDatabase.COLUMN_AYAT));
					index++;
				} while(cursor.moveToNext());
			}
			
			cursor.close();
			
		}
		
		// Now read Bookmarked ayats to Array
		cursor = db.getBookmarksForSurah(surahNumber+1);
		
		int  index = 0;
		
		if(cursor.moveToFirst())
		{
			Bookmarks = new int [cursor.getCount()];
			
			do
			{
				Bookmarks[index] = cursor.getInt(cursor.getColumnIndex(alQalamDatabase.COLUMN_AYATNO));
				index++;
				
			} while(cursor.moveToNext());
		}
		else {
			Bookmarks =  null;
		}
		
		cursor.close();
		db.close();

		// Put Arabic Image Links to Array
		String  snm, anm;
		
		snm = CONSTANTS.numberToString(surahNumber + 1);
		
		for (index=0;index<CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber];index++) {
		
			anm = CONSTANTS.numberToString(index);
			AYATSARABIC[index]  = CONSTANTS.FOLDER_QURAN_ARABIC + (surahNumber+1)+ "/" + snm + anm +".jdw";
			//AYATSARABIC[index]  = ArabicUtilities.reshapeSentence(arLine);
		}
		
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
		
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		//final int selectedItem;
		
    	switch (menuItem.getItemId()) {	
    		
    		case R.id.reciters: //MENU_ITEM_RECITER :
    			showDialog(DIALOG_RECITER);
    			return true;
    			
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
	            public void onClick(DialogInterface dialog, int item) {
	            	removeDialog(DIALOG_TRANSLATION);
	            }
	        });
			
		}
		
		if (id == DIALOG_RECITER) {
			ab.setTitle(R.string.reciters);
			
			ab.setSingleChoiceItems(R.array.ReciterOptions, ReciterType , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	Log.i(TAG, "Item " + item);
	            	changeReciter(item);
	            }
	        })
	        .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	removeDialog(DIALOG_RECITER);
	            }
	        });
		}
		
		if (id == DIALOG_AYAT_CLICK_OPTION) {
			
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
	            public void onClick(DialogInterface dialog, int item) {
	            	removeDialog(DIALOG_AYAT_CLICK_OPTION);
	            }
	        });	
		}
			
		
		AlertDialog alert = ab.create();

		return alert;
		
	}
	
	private void doAyatClickTask(int item) {
		removeDialog(DIALOG_AYAT_CLICK_OPTION);
		
		if (item == 0) {
			currentAyat = 0;
			isAudioControlShown = true;
			audioControlPanel.setVisibility(View.VISIBLE);
			playButton.setVisibility(View.GONE);
			pauseButton.setVisibility(View.VISIBLE);
			playAudio();
		}
		else if (item == 1) {
			currentAyat = selectedAyat;
			isAudioControlShown = true;
			audioControlPanel.setVisibility(View.VISIBLE);
			playButton.setVisibility(View.GONE);
			pauseButton.setVisibility(View.VISIBLE);
			playAudio();
		}
		else if (item == 2) {
			alQalamDatabase db = new alQalamDatabase(this);
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
					Bookmarks[index] = cursor.getInt(cursor.getColumnIndex(alQalamDatabase.COLUMN_AYATNO));
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
	
	private void changeTranslation(int lng) {
		removeDialog(DIALOG_TRANSLATION);
		TranslationType = lng;
		preferenceEditor.putInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, lng);
		preferenceEditor.commit();
		
		showData();
	}
	
	private void changeReciter(int rct) {
		removeDialog(DIALOG_RECITER);
		preferenceEditor.putInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, rct);
    	preferenceEditor.commit();
		ReciterType = rct;
		
		stopAudio();
	}
	
	private void playAudio() {
		
		String AudioPath = CONSTANTS.FOLDER_QURAN_AUDIO + CONSTANTS.RECITER_DIRECTORY[ReciterType] + "/";
			
		if (audioState == CONSTANTS.AUDIO_PLAYING )
			return;
		else if (audioState == CONSTANTS.AUDIO_PAUSED) {
			audioState = CONSTANTS.AUDIO_PLAYING;
			quranPlayer.start();
			return;
		}
		else if (audioState == CONSTANTS.AUDIO_NOT_INTIALIZED) {
			quranPlayer = new MediaPlayer();
			wakeLockPlayMode.acquire();
		}
						
		try {
			
			if (currentAyat == 0 && (surahNumber == 0 || surahNumber == 8))
				currentAyat = 1;
			
			if (currentAyat == 0) {
				Log.i(TAG, AudioPath + "bismillah.mp3");

				quranPlayer.setDataSource(AudioPath + "bismillah.mp3");
				quranPlayer.prepare();
				quranPlayer.start();
				
			} else {
				final String  SNM = CONSTANTS.numberToString(surahNumber+1), ANM = CONSTANTS.numberToString(currentAyat);
								
				Log.i(TAG, AudioPath + SNM+"/" + SNM+ANM+".mp3");
				
				quranPlayer.setDataSource(AudioPath + SNM+"/" + SNM+ANM+".mp3");
				quranPlayer.prepare();
				quranPlayer.start();
				
				ayatList.setSelectionFromTop(currentAyat - 1, 0);
				
			}
			
			quranPlayer.setOnCompletionListener(
					new OnCompletionListener() {
						 
                        public void onCompletion(MediaPlayer arg0) {
                        		audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
                        		currentAyat++; 
                        		
                        		if (currentAyat > CONSTANTS.SURAH_NUMBER_OF_AYATS[surahNumber]) {
                        			currentAyat = 0;
                        			
                        			if (quranPlayer != null)
                        				quranPlayer.stop();
                        		}
                        		else {
                        			playAudio();
                        		}
                        }
 
                });

			audioState = CONSTANTS.AUDIO_PLAYING;
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	
	private void pauseAudio() {
		audioState = CONSTANTS.AUDIO_PAUSED;
		quranPlayer.pause();
	}
	
	private void stopAudio() {
		audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
		
		if (wakeLockPlayMode.isHeld()) {
			wakeLockPlayMode.release();
		}
		
		if (quranPlayer != null && quranPlayer.isPlaying()) {
			quranPlayer.stop();
			quranPlayer.release();
			quranPlayer = null;
		}
	}

	@Override
	public void onPause() {
		stopAudio();
		super.onPause();
	}
	
	@Override
	public void onStop() {
		stopAudio();
		super.onStop();
	}
}

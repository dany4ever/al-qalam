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

import java.io.File;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class startQalam extends Activity {

	private static final String TAG = "al-Qalam startQalam";
	private Handler 			aqHandler;
	private ImageView 			frontSplash;
	private LinearLayout 		mainView;
	private LinearLayout 		btnView;

	private SharedPreferences	commonPrefs;
	//private int 				TranslationType = 0;
	//private int				ReciterType = 0;
	public 						startQalam() {
		// This is here to call updateUiLocale() method
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/* Initialize interface language
         * This should be done before setContentView() is called
         */
    	commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, MODE_PRIVATE);
    	String[] availableLocales = getResources().getStringArray(R.array.locale_values);
    	// The index of default language is 2 (Uzbek)
    	int index = commonPrefs.getInt(CONSTANTS.SETTINGS_UI_LOCALE_TITLE, 2);
    	updateUiLocale(this, availableLocales[index]);

    	super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);

        frontSplash = (ImageView) findViewById(R.id.splashimage);
        mainView = (LinearLayout) findViewById(R.id.mainview);
        btnView = (LinearLayout) findViewById(R.id.buttons);
        btnView.setVisibility(View.GONE);
      
        //Initialize the database
        initializeDb();
        
        aqHandler = new Handler();
        aqHandler.postDelayed(Splash, 1500);
        
        // Check if arabic is downloaded
        checkDownloadedSurahs();
        
        // Get Translation Type from shared preferences, default is 0 (uzbek-cyr)        
        //TranslationType = commonPrefs.getInt(CONSTANTS.SETTINGS_TRANLATION_OPTION_TITLE, 0);
        //ReciterType = commonPrefs.getInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, 0);
    }

    @Override
    protected void onStop() {
    	super.onStop();
    }

    private Runnable Splash = new Runnable() {
	 	   public void run() {
	 		frontSplash.setVisibility(View.GONE);
	 		btnView.setVisibility(View.VISIBLE);
	 		mainView.setBackgroundResource(R.drawable.background);
	 		
	 		final ImageButton  imgBtnQuran = (ImageButton) findViewById(R.id.imgBtnQuran);
	 		imgBtnQuran.setClickable(true);
	 		imgBtnQuran.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			 startActivity(new Intent(startQalam.this, QuranActivity.class));
	    		}
	        });
	        
	        final ImageButton imgBukhari = (ImageButton) findViewById(R.id.imgBtnBukhari);
	        imgBukhari.setAlpha(80);
	    	
	        
	        final ImageButton imgMuslim = (ImageButton) findViewById(R.id.imgBtnMuslim);
	        imgMuslim.setAlpha(80);
	    		        
	        final ImageButton imgTirmidi = (ImageButton) findViewById(R.id.imgBtnTirmidi);
	        imgTirmidi.setAlpha(80);
	    		        
	        final ImageButton imgAbudavud  = (ImageButton) findViewById(R.id.imgBtnAbudovud);
	    	imgAbudavud.setAlpha(80);
	 	   }
	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear(); // Need to *refresh* menu names when UI locale is changed */
		getMenuInflater().inflate(R.menu.main, menu);

		return true;		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

    	switch (menuItem.getItemId()) {	

    	case R.id.info:
    		return true;

    	case R.id.help:
    		return true;

    	case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;

    	}

    	return false;
   }

	private boolean isFileExists(String _file) {
		  File file = new File(_file);
		  return file.exists();
	}

	public void checkDownloadedSurahs() {
		String arabicFileName;
		//String audioFileName;

		for (int i=0; i<CONSTANTS.numberOfSurahs; i++) {
				arabicFileName = CONSTANTS.FOLDER_QURAN_ARABIC + (i+1);
				CONSTANTS.gSurahIsDownloaded[i] = isFileExists(arabicFileName);

				// audioFileName = CONSTANTS.FOLDER_QURAN_AUDIO + CONSTANTS.ReciterDirectory[ReciterType]+ "/" + CONSTANTS.numberToString(i+1) ;
				//CONSTANTS.gSurahIsAudioDownloaded[i] = isFileExists(audioFileName);
		}
	}

	public void updateUiLocale(Context context, String locale) {
		Configuration config = new Configuration();
		if (locale != null) {
			config.locale = new Locale(locale);
		} else {
			config.locale = new Locale("uz");
		}
		context.getResources().updateConfiguration(config, null);
	}

	protected void initializeDb() {
		final alQalamDatabase db;
		db = new alQalamDatabase(this);
		
    	Thread mThread = new Thread() {
    		public void run() {
    			db.openWritable();
    			db.close();
    		}
    	};
    	
    	mThread.start();
    }
}
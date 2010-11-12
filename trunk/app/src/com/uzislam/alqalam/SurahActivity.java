package com.uzislam.alqalam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SurahActivity extends Activity {

	private String []		AYATS ; 
	private String []		AYATSARABIC;
	private int				surahNumber = 0;
	
	public static DisplayMetrics displaymetrics = new DisplayMetrics(); 
    
	private ImageView 			surahTitle;
	private SurahAdapter 		surahAdapter;
	private AyatIconifiedText	ait;
	private Drawable 			iconBismillah;
	private ListView			ayatList; 

	private final int	MENU_ITEM_PLAY = 0x01;
	private final int	MENU_ITEM_PAUSE = 0x02;
	private final int	MENU_ITEM_TRANSLATION = 0x03;
	private final int	MENU_ITEM_RECITER = 0x04;
	private final int	MENU_ITEM_HELP = 0x05;
	
	private boolean		isAudioPlaying = false;

	private SharedPreferences			commonPrefs;
	private SharedPreferences.Editor 	preferenceEditor = null;
	private int 						TranslationType = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.surah);
        
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
       
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
        	surahNumber = extras.getInt("sNumber");
        }
        
        commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, 0);
        preferenceEditor = commonPrefs.edit();
        
		// Get Translation Type from shared preferences, default is 0 (uzbek-cyr)
        TranslationType = commonPrefs.getInt("TransOption", 0);
		 
        // Set chapter image title 
        surahTitle = (ImageView) findViewById(R.id.suraName);
        
        //get Bismillah image
        iconBismillah = getResources().getDrawable(R.drawable.bismillah);
        
        // create new chapter adapter
        surahAdapter = new SurahAdapter(this); 	
        
        // get AyatList View 
        ayatList = (ListView)findViewById(R.id.AyaList);  
        
        // Display Surah
        showSurah();
        
        final ImageButton  imgBtnPrevious = (ImageButton) findViewById(R.id.headerPrev);
        final ImageButton  imgBtnNext = (ImageButton) findViewById(R.id.headerNext);
 		
        imgBtnPrevious.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			surahNumber--;
    			if (surahNumber < 0) {
    				surahNumber = 0;
    				return;
    			}
    			 showSurah();
    		}
        });
        
        imgBtnNext.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			surahNumber++;
    			if (surahNumber > 113) {
    				surahNumber = 113;
    				return;
    			}
    			 showSurah();
    		}
        });
 		
 	}
	
	private void showSurah() {
		
		surahAdapter.clear();
		
		surahTitle.setImageResource(CONSTANTS.SurahTitles[surahNumber]);
	  
        // create string arrays for verses
        AYATS = new String[CONSTANTS.SurahNumberOfAyats[surahNumber]];
        AYATSARABIC = new String [CONSTANTS.SurahNumberOfAyats[surahNumber]];
        
        
        // if  Surah is shown with translation
        if (TranslationType != 3) { 
	    
        	// get file link to Translation (in assets)
	        String surahFileLink = + (surahNumber + 1) + ".txt";
	       
	        // reads string from file and puts AYATS array, and reads image  to put AYATSARABIC
	        readFileToArray(surahFileLink);

        }
        // else {
        // 	Arrays.fill(AYATS, " ");
        // }
        
        readArabicToArray();
        
        for (int i=0; i < CONSTANTS.SurahNumberOfAyats[surahNumber] ; i++) {
        	
        	ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATS[i], getSpecialImage(surahNumber+1, i+1), null, getAyatBackgroundColor());
        	
        	// check if BISMILLAH must be shown 
        	if (i == 0 && surahNumber != 0 && surahNumber != 8)  
        	     ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATS[i], getSpecialImage(surahNumber+1, i+1), iconBismillah, getAyatBackgroundColor());
       
        	surahAdapter.addItem(ait);
        }
        
        // make verses appear in list view   
        ayatList.setAdapter(surahAdapter);
        ayatList.setCacheColorHint(00000000); 
        ayatList.setDivider(null);
	}
	
	private void readArabicToArray() {
		
		String  SNM, ANM;
		
		if (surahNumber+1 < 10 )
			SNM = "00"+(surahNumber+1);
		else if (surahNumber+1 < 100 )
			SNM = "0"+(surahNumber+1);
		else 
			SNM = ""+(surahNumber+1);
		
		for (int index=0;index<CONSTANTS.SurahNumberOfAyats[surahNumber];index++) {
		
				if (index < 10 )
					ANM = "00"+index;
				else if (index < 100)
					ANM = "0"+index;
				else 
					ANM = ""+index;
				
				// server : http://al-qalam.googlecode.com/svn/trunk/assets/arabic/1/001000.gdw
				AYATSARABIC[index]  = CONSTANTS.FOLDER_QURAN_ARABIC +(surahNumber+1)+"/"+SNM+ANM+".gdw";
				//AYATSARABIC[index]  = ArabicUtilities.reshapeSentence(arLine);
		}
	}
	
	private void readFileToArray(String SurahFileName) {
	
		// Add Language Directory
		SurahFileName = CONSTANTS.LanguageDirectory[TranslationType] + "/" + SurahFileName;
		
		Log.i("al-Qalam SurahActivity", "DIR : "+ SurahFileName + " VAL: " + TranslationType);
		
		BufferedReader udis = null;
		//BufferedReader adis = null;
		try {
			udis = new BufferedReader(new InputStreamReader(getAssets().open(SurahFileName))); 
			//adis = new BufferedReader(new InputStreamReader(getAssets().open("arabic/"+SurahFileName), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int  	index = 0;
		String  trLine = ""; 
		
		try {
			while((trLine = udis.readLine()) != null) {
				
				AYATS[index] = trLine;
				index++;		
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Drawable getSpecialImage(int surah, int ayat) {
		
		for (int i=0; i <CONSTANTS.numberOfSajdaAyats; i++) {
			if (CONSTANTS.SajdaAyats[i][0] == surah &&  CONSTANTS.SajdaAyats[i][1] == ayat)
				return getResources().getDrawable(R.drawable.sajdah);
		}
		
		for (int i=0; i<= CONSTANTS.numberOfJuzs; i++) {
			if (CONSTANTS.JuzNumbers[i][0] == surah && CONSTANTS.JuzNumbers[i][1] == ayat)
				return getResources().getDrawable(R.drawable.juzz);
		}
				
		return null;
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu mainMenu) { 	
    
  	
    	mainMenu.clear();
    	
    	MenuItem subitem;
    	
		mainMenu.setQwertyMode(true);
		
		if (!isAudioPlaying) {
			subitem = mainMenu.add(0, MENU_ITEM_PLAY, 0 ,"Тинглаш");
			subitem.setIcon(android.R.drawable.ic_media_play);
		}
		
		else {
			subitem = mainMenu.add(0, MENU_ITEM_PAUSE, 0 ,"Пауза");
			subitem.setIcon(android.R.drawable.ic_media_pause);
		}
		
		subitem = mainMenu.add(0, MENU_ITEM_TRANSLATION, 0 ,"Таржима");
		subitem.setIcon(android.R.drawable.ic_menu_agenda);
		
		subitem = mainMenu.add(0, MENU_ITEM_RECITER, 0 ,"Қори");
		subitem.setIcon(android.R.drawable.ic_menu_recent_history);
		
		subitem = mainMenu.add(0, MENU_ITEM_HELP, 0, "Ёрдам");
		subitem.setIcon(android.R.drawable.ic_menu_help);
		
    	return true;
    }	

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		
    	switch (menuItem.getItemId()) {	

    		case MENU_ITEM_PLAY :
    			isAudioPlaying = true;
    			return true;
    			
    		case MENU_ITEM_PAUSE :
    			isAudioPlaying = false;
    			return true;
    		
    		case MENU_ITEM_RECITER :
    			return true;
    			
    		case MENU_ITEM_TRANSLATION :
    			return true;
    			
    		case MENU_ITEM_HELP:
    			//startActivity(new Intent(this, helpActivity.class));
    			return true;

    	}
	   
    	return false;
   }
	
	
	private int  getAyatBackgroundColor() {
		//TODO: differentiate the color of bookmarked or playing verse background.
		
		return Color.TRANSPARENT;
	}
	
}

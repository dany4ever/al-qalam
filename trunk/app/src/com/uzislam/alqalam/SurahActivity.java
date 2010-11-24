package com.uzislam.alqalam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
	private static final String TAG = "SurahActivity";
	private String []		AYATS; 
	private String []		AYATSARABIC;
	private int				surahNumber = 0;
	private int				currentAyat = 0;
	
	public static DisplayMetrics displaymetrics = new DisplayMetrics(); 
    
	private ImageView 			surahTitle;
	private SurahAdapter 		surahAdapter;
	private AyatIconifiedText	ait;
	private Drawable 			iconBismillah;
	private ListView			ayatList; 

	/*private final int	MENU_ITEM_PLAY = 0x01;
	private final int	MENU_ITEM_PAUSE = 0x02;
	private final int	MENU_ITEM_TRANSLATION = 0x03;
	private final int	MENU_ITEM_RECITER = 0x04;
	private final int	MENU_ITEM_HELP = 0x05;*/
	
	private final int 	DIALOG_TRANSLATION = 0x01;
	private final int	DIALOG_RECITER = 0x02;
	
	private int			audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
	

	private SharedPreferences			commonPrefs;
	private SharedPreferences.Editor 	preferenceEditor = null;
	private int 						TranslationType = 0;
	private int							ReciterType = 0;
	
	private MediaPlayer					quranPlayer;
	
	
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
		 
        ReciterType = commonPrefs.getInt("ReciterOption", 0);
        
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
    			
    			stopAudioPlay();
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
    			stopAudioPlay();
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
		
		Log.i(TAG, "DIR : "+ SurahFileName + " VAL: " + TranslationType);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.surah_activity, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isAudioPlaying = audioState == CONSTANTS.AUDIO_PLAYING;
		
		menu.findItem(R.id.play).setVisible(!isAudioPlaying);
		menu.findItem(R.id.pause).setVisible(isAudioPlaying);
		
		return super.onPrepareOptionsMenu(menu);
	}
	/*
	@Override
	public boolean onPrepareOptionsMenu(Menu mainMenu) { 	
    
    	mainMenu.clear();
    	
    	MenuItem subitem;
    	
		mainMenu.setQwertyMode(true);
		
		if (audioState != CONSTANTS.AUDIO_PLAYING) {
			subitem = mainMenu.add(0, MENU_ITEM_PLAY, 0 , R.string.play);
			subitem.setIcon(R.drawable.menu_icon_play);
		}
		
		else {
			subitem = mainMenu.add(0, MENU_ITEM_PAUSE, 0 , R.string.pause);
			subitem.setIcon(R.drawable.menu_icon_pause);
		}
		
		subitem = mainMenu.add(0, MENU_ITEM_TRANSLATION, 0 , R.string.translation);
		subitem.setIcon(R.drawable.menu_icon_translation);
		
		subitem = mainMenu.add(0, MENU_ITEM_RECITER, 0 , R.string.reciters);
		subitem.setIcon(R.drawable.menu_icon_reciter);
		
		subitem = mainMenu.add(0, MENU_ITEM_HELP, 0, R.string.help);
		subitem.setIcon(R.drawable.menu_icon_help);
		
    	return true;
    }*/	

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		//final int selectedItem;
		
    	switch (menuItem.getItemId()) {	

    		case R.id.play: //MENU_ITEM_PLAY :
    			playAudio();
    			return true;
    			
    		case R.id.pause: //MENU_ITEM_PAUSE :
    			pauseAudio();
    			return true;
    		
    		case R.id.reciters: //MENU_ITEM_RECITER :
    			showDialog(DIALOG_RECITER);
    			return true;
    			
    		case R.id.translation: //MENU_ITEM_TRANSLATION :
    			showDialog(DIALOG_TRANSLATION);
    			return true;
    			
    		case R.id.help: //MENU_ITEM_HELP:
    			//startActivity(new Intent(this, helpActivity.class));
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
		
		AlertDialog alert = ab.create();

		return alert;
		
	}
	
	private void changeTranslation(int lng) {
		removeDialog(DIALOG_TRANSLATION);
		TranslationType = lng;
		preferenceEditor.putInt("TransOption", lng);
		preferenceEditor.commit();
		showSurah();
	}
	
	private void changeReciter(int rct) {
		removeDialog(DIALOG_RECITER);
		preferenceEditor.putInt("ReciterOption", rct);
    	preferenceEditor.commit();
		ReciterType = rct;
	}
	
	private void playAudio() {
		
		if (audioState == CONSTANTS.AUDIO_PLAYING )
			return;
		else if (audioState == CONSTANTS.AUDIO_PAUSED) {
			audioState = CONSTANTS.AUDIO_PLAYING;
			quranPlayer.start();
			return;
		}
		else if (audioState == CONSTANTS.AUDIO_NOT_INTIALIZED) {
			quranPlayer = new MediaPlayer();
		}
						
		try {
			
			if (currentAyat == 0 && (surahNumber == 0 || surahNumber == 8))
				currentAyat = 1;
			
			if (currentAyat == 0) {
				Log.i(TAG, CONSTANTS.FOLDER_QURAN_AUDIO+CONSTANTS.ReciterDirectory[1] + "/bismillah.mp3");

				quranPlayer.setDataSource(CONSTANTS.FOLDER_QURAN_AUDIO+CONSTANTS.ReciterDirectory[1] + "/bismillah.mp3");
				quranPlayer.prepare();
				quranPlayer.start();
				
			} else {
				String  SNM, ANM;
				
				if (surahNumber+1 < 10 )
					SNM = "00"+(surahNumber+1);
				else if (surahNumber+1 < 100 )
					SNM = "0"+(surahNumber+1);
				else 
					SNM = ""+(surahNumber+1);
				
				if (currentAyat < 10 )
					ANM = "00" + currentAyat;
				else if (currentAyat < 100)
					ANM = "0" + currentAyat;
				else 
					ANM = "" + currentAyat;
				
				Log.i(TAG, CONSTANTS.FOLDER_QURAN_AUDIO+CONSTANTS.ReciterDirectory[1] + "/" + SNM+"/" + SNM+ANM+".mp3");
				
				quranPlayer.setDataSource(CONSTANTS.FOLDER_QURAN_AUDIO+CONSTANTS.ReciterDirectory[1] + "/" + SNM+"/" + SNM+ANM+".mp3");
				quranPlayer.prepare();
				quranPlayer.start();
				
				ayatList.setSelectionFromTop(currentAyat - 1, 0);
				
			}
			
			quranPlayer.setOnCompletionListener(
					new OnCompletionListener() {
						 
                        public void onCompletion(MediaPlayer arg0) {
                        		audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
                        		currentAyat++; 
                        		
                        		if (currentAyat > CONSTANTS.SurahNumberOfAyats[surahNumber]) {
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
		
 		
	private int  getAyatBackgroundColor() {
		//TODO: differentiate the color of bookmarked or playing verse background.
		
		return Color.TRANSPARENT;
	}
	
	@Override
	public void onPause() {
		stopAudioPlay();
		super.onPause();
	}
	
	@Override
	public void onStop() {
		stopAudioPlay();
		super.onStop();
	}
	
	
	private void stopAudioPlay() {
		currentAyat = 0;
		audioState = CONSTANTS.AUDIO_NOT_INTIALIZED;
		
		if (quranPlayer != null && quranPlayer.isPlaying()) {
			quranPlayer.stop();
			quranPlayer.release();
			quranPlayer = null;
		}
	}
}

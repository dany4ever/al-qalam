package com.uzislam.alqalam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SurahActivity extends Activity {
	private static ListView		AyatList; 
	private static  String []	AYATS ; 
	private static  String []	AYATSARABIC;
	private static int			surahNumber = 0;
	
	public static DisplayMetrics displaymetrics = new DisplayMetrics(); 
    		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.surah);
        
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        
       
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
        	surahNumber = extras.getInt("sNumber");
        }
        
        // Set chapter image title 
        ImageView 		surahTitle = (ImageView) findViewById(R.id.suraName);
        surahTitle.setImageResource(CONSTANTS.SurahTitles[surahNumber]);
            	
        // create string arrays for verses
        AYATS = new String[CONSTANTS.SurahNumberOfAyats[surahNumber]];
        AYATSARABIC = new String [CONSTANTS.SurahNumberOfAyats[surahNumber]];
        
        // get file link to Uzbek translation (in assets)
        String surahFileLink = + (surahNumber + 1) + ".txt";
       
        // reads string from file and puts AYATS array, and reads image  to put AYATSARABIC
        readFileToArray(surahFileLink);
        
        // create new chapter adapter
        SurahAdapter		surahAdapter = new SurahAdapter(this); 	
        AyatIconifiedText	ait;
        Drawable 			iconBismillah = getResources().getDrawable(R.drawable.bismillah);
        
        for (int i=0; i < CONSTANTS.SurahNumberOfAyats[surahNumber] ; i++) {
        	
        	ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATS[i], getSpecialImage(surahNumber+1, i+1), null, getAyatBackgroundColor());
        	
        	// check if BISMILLAH must be shown 
        	if (i == 0 && surahNumber != 0 && surahNumber != 8)  
        	     ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATS[i], getSpecialImage(surahNumber+1, i+1), iconBismillah, getAyatBackgroundColor());
       
        	surahAdapter.addItem(ait);
        }
        
        // make verses appear in list view
        AyatList = (ListView)findViewById(R.id.AyaList);     
        AyatList.setAdapter(surahAdapter);
        AyatList.setCacheColorHint(00000000); 
        AyatList.setDivider(null);
 	}
	
	public void readFileToArray(String SurahFileName) {
				
		BufferedReader udis = null;
		//BufferedReader adis = null;
		try {
			udis = new BufferedReader(new InputStreamReader(getAssets().open("uzbek-cyr/"+SurahFileName))); 
			//adis = new BufferedReader(new InputStreamReader(getAssets().open("arabic/"+SurahFileName), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int  	index = 0;
		String  uzLine = ""; 
		
		String  SNM, ANM;
		
		if (surahNumber+1 < 10 )
			SNM = "00"+(surahNumber+1);
		else if (surahNumber+1 < 100 )
			SNM = "0"+(surahNumber+1);
		else 
			SNM = ""+(surahNumber+1);
		
		try {
			while((uzLine = udis.readLine()) != null) {
				
				if (index < 10 )
					ANM = "00"+index;
				else if (index < 100)
					ANM = "0"+index;
				else 
					ANM = ""+index;
				
				
				AYATS[index] = uzLine;
				// server : http://al-qalam.googlecode.com/svn/trunk/assets/arabic/1/001000.gdw
				AYATSARABIC[index]  = CONSTANTS.FOLDER_QURAN_ARABIC +(surahNumber+1)+"/"+SNM+ANM+".gdw";
				//AYATSARABIC[index]  = ArabicUtilities.reshapeSentence(arLine);

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
	
	private int  getAyatBackgroundColor() {
		//TODO: differentiate the color of bookmarked or playing verse background.
		
		return Color.TRANSPARENT;
	}
	
}

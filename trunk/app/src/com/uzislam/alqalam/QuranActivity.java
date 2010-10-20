package com.uzislam.alqalam;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuranActivity extends Activity {
	private ListView			SurahList; 	
	private static String[] 	SurahTitles;
	private static boolean[]	SurahIsDownloaded;
	//private static boolean[]	SurahIsAudioDownloaded;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quran);
        SurahList = (ListView)findViewById(R.id.SurahList);
        SurahTitles = getResources().getStringArray(R.array.SurahTitle);
        SurahIsDownloaded = new boolean [CONSTANTS.numberOfSurahs];
                                            
        QuranAdapter		quranAdapter = new QuranAdapter(this);
        QuranIconifiedText	qit;
    	Drawable			stateIcon = getResources().getDrawable(R.drawable.index_sound_get);
    	Drawable			placeIcon = null;
    	
    	// check which Surah's Arabic Text is already downloaded
    	checkDownloadedSurahs();
    	
        for (int i = 0; i < CONSTANTS.numberOfSurahs ; i++) {
        	qit = new QuranIconifiedText(i, SurahTitles[i], i+1, CONSTANTS.SurahNumberOfAyats[i], SurahIsDownloaded[i] ,stateIcon, placeIcon);
        	quranAdapter.addItem(qit);
        }
        
        SurahList.setAdapter(quranAdapter);
        SurahList.setCacheColorHint(00000000); 
        SurahList.setDivider(null);
        
        SurahList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					final int index, long order) {
					if (SurahIsDownloaded[index]) {
						Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
						quranIntent.putExtra("sNumber", index);
						startActivity(quranIntent);
					} 
					else {
						// TODO: request file download, and perform download
					}
			}
        });
 	}
	
	private void checkDownloadedSurahs() {
		String fileName;//, surahNumber, AyatNumber;
		
		for (int i = 0; i < CONSTANTS.numberOfSurahs; i++) {
			fileName = CONSTANTS.FOLDER_QURAN_ARABIC + (i + 1);
			SurahIsDownloaded[i] = isFileExists(fileName);
		}

	}
	
	private boolean isFileExists(String _file) {
		  File file = new File(_file);
		  return file.exists();
	}

}

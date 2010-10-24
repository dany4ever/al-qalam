package com.uzislam.alqalam;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuranActivity extends Activity {
	private ListView			gSurahList; 	
	private static String[] 	gSurahTitles;
	private static boolean[]	gSurahIsDownloaded;
	private String				LOG_MAIN = "Al-Qalam Quran Activity";
	private static final String ARABIC_DATA_PACK_INSTALLER_MARKET_LINK =
		"market://search?q=pname:com.uzislam.alqalam.arabicinstaller";
	//private static boolean[]	SurahIsAudioDownloaded;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran);
        gSurahList = (ListView)findViewById(R.id.SurahList);
        gSurahTitles = getResources().getStringArray(R.array.SurahTitle);
        gSurahIsDownloaded = new boolean [CONSTANTS.numberOfSurahs];
                                            
        QuranAdapter		quranAdapter = new QuranAdapter(this);
        QuranIconifiedText	qit;
    	Drawable			stateIcon = getAudioIcon();
    	Drawable			placeIcon = getPlaceIcon();
    	
    	// check which Surah's Arabic Text is already downloaded
    	checkDownloadedSurahs();
    	
        for (int i=0; i < CONSTANTS.numberOfSurahs ; i++) {
        	qit = new QuranIconifiedText(i, gSurahTitles[i], i+1, CONSTANTS.SurahNumberOfAyats[i], gSurahIsDownloaded[i] ,stateIcon, placeIcon);
        	quranAdapter.addItem(qit);
        }
        
        
        gSurahList.setAdapter(quranAdapter);
        gSurahList.setCacheColorHint(00000000); 
        gSurahList.setDivider(null);
        
        gSurahList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					final int index, long order) {
				
					if (isSdCardAccessible() == false) {
						showDialog(CONSTANTS.SURAH_DIALOG_NO_SDCARD);
					}
					else if (gSurahIsDownloaded[index]) {
						Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
						quranIntent.putExtra("sNumber", index);
						startActivity(quranIntent);
					} 
					else {
						showDialog(CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST);
					}
			}
        });
 	}
	
	private void checkDownloadedSurahs() {
		String fileName;//, surahNumber, AyatNumber;
		
		for (int i=0; i<CONSTANTS.numberOfSurahs; i++) {
			fileName = CONSTANTS.FOLDER_QURAN_ARABIC +(i+1);
				gSurahIsDownloaded[i] = isFileExists(fileName);
		}

	}
	
	private boolean isFileExists(String _file) {
		  File file=new File(_file);
		  return file.exists();
	}
	
	private Drawable getAudioIcon() {
		// TODO: differentiate audio icon based on download, playing, or get
		return getResources().getDrawable(R.drawable.index_sound_get);
	}

	private Drawable getPlaceIcon() {
		// TODO: differentiate Surah revealed place: Makkah or Madina 
		return null; //getResources().getDrawable(R.drawable.mecca);
	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.v(LOG_MAIN, "Create Dialog");
		 switch (id) {
			 case CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST:
				 return new AlertDialog.Builder(QuranActivity.this)
	             .setTitle(R.string.sura_need_download)
	             .setCancelable(false)
	             .setItems(R.array.DownloadOptions, new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {
	                     switch (which) {
                          case 0:
                        	  dismissDialog(CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST);
                        	  Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                                      Uri.parse(ARABIC_DATA_PACK_INSTALLER_MARKET_LINK));
                              startActivity(marketIntent);
                              finish();
                              break;
                              
                          default:
                              dismissDialog(CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST);
	                     }	                             
	                 }
	             })
	             .create();
				 
			 case CONSTANTS.SURAH_DIALOG_NO_SDCARD:
				 return new AlertDialog.Builder(QuranActivity.this)
	                .setIcon(R.drawable.alert_dialog_icon)
	                .setTitle(R.string.no_sd_card)
					.setPositiveButton(R.string.btn_confirm, null)
					.create();
				 
			 case CONSTANTS.SURAH_DIALOG_DOWNLOAD_PROGRESS:
				 return null;
				 
		}
		 
		return null;
	}
	
	
	public static boolean isSdCardAccessible() {  
	    return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);  
	} 
}

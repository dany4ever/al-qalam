package com.uzislam.alqalam;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuranActivity extends Activity {
	private ListView			gSurahList; 	
	private static String[] 	gSurahTitles;
	private static boolean[]	gSurahIsDownloaded;
	private String				LOG_MAIN = "Al-Qalam Quran Activity";

	private static final String ARABIC_DATA_PACK_INSTALLER_MARKET_LINK =
		"market://details?id=com.uzislam.alqalam.arabicinstaller";
	//private static boolean[]	SurahIsAudioDownloaded;
	
	private final int	MENU_ITEM_BOOKMARKS = 0x01;
	private final int	MENU_ITEM_SETTINGS = 0x02;
	
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
				
					if (!isSdCardAccessible()) {
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
                        	  downloadSurah();
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
	
	private void downloadSurah() {
		try {
			PackageManager pm = getPackageManager();
			@SuppressWarnings("unused")
			ApplicationInfo appInfo = pm.getApplicationInfo(/*Android Market*/"com.android.vending", 0);
		} catch (NameNotFoundException nnfe) {
			// The user device has no Android Market
			Toast.makeText(this, R.string.no_android_market, Toast.LENGTH_LONG);
			return;
		}
		Intent marketIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(ARABIC_DATA_PACK_INSTALLER_MARKET_LINK));
		startActivity(marketIntent);
		finish();
	}
	
	
	public static boolean isSdCardAccessible() {  
	    String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equalsIgnoreCase(state);  
	} 
	
	@Override
	public boolean onPrepareOptionsMenu(Menu mainMenu) { 	
  	
    	mainMenu.clear();
    	
    	MenuItem subitem;
    	
		mainMenu.setQwertyMode(true);
		
		subitem = mainMenu.add(0, MENU_ITEM_BOOKMARKS, 0 , R.string.bookmarks);
		subitem.setIcon(R.drawable.menu_icon_bookmarks);
		
		subitem = mainMenu.add(0, MENU_ITEM_SETTINGS, 0 , R.string.settings);
		subitem.setIcon(R.drawable.menu_icon_settings);
		
		
		
    	return true;
    }	

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		
    	switch (menuItem.getItemId()) {	

    		case MENU_ITEM_BOOKMARKS :
    			return true;
    		
    		case MENU_ITEM_SETTINGS :
    			startActivity(new Intent(this, SettingsActivity.class));
    			return true;

    	}
	   
    	return false;
   }

}

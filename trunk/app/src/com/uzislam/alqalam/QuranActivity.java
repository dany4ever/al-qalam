package com.uzislam.alqalam;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuranActivity extends Activity {
	private ListView			SurahList; 	
	private static String[] 	SurahTitles;
	private static boolean[]	SurahIsDownloaded;
	//private static boolean[]	SurahIsAudioDownloaded;
	private static final String ARABIC_DATA_PACK_INSTALLER_MARKET_LINK =
		"market://search?q=pname:com.uzislam.alqalam.arabicinstaller";
	private AlertDialog mAlertDialog;
	
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
						displayNoArabicPack();
					}
			}
        });
 	}
	
	@Override
    protected void onStop() {
        super.onStop();
        // TODO: Find better implementation to handle Window Leak bug
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
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
	
	private void displayNoArabicPack() {
        try {
            PackageManager pm = getPackageManager();
            @SuppressWarnings("unused")
			ApplicationInfo info = pm.getApplicationInfo("com.android.vending", 0);
        } catch (NameNotFoundException e) {
            // The user does not have Android Market
        	Toast.makeText(this, R.string.android_maket_app_missing, Toast.LENGTH_LONG);
            return;
        }

        AlertDialog.Builder noArabicPackAlert = new AlertDialog.Builder(this);
        noArabicPackAlert.setTitle(R.string.no_arabic_pack_alert_title);
        noArabicPackAlert.setMessage(R.string.no_arabic_pack_alert_message);

        noArabicPackAlert.setPositiveButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                			Uri.parse(ARABIC_DATA_PACK_INSTALLER_MARKET_LINK));
                    startActivity(marketIntent);
                }
            });

        noArabicPackAlert.setNegativeButton(android.R.string.cancel,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        mAlertDialog = noArabicPackAlert.create();
        mAlertDialog.show();
    }

}

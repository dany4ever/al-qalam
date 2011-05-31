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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuranActivity extends Activity {
	private ListView			gSurahList; 	
	private ListView			gJuzzList;
	private String[] 			gSurahTitles, gSurahInfos;
		
	private final String		TAG = "Al-Qalam";

	private final String ARABIC_DATA_PACK_INSTALLER_MARKET_LINK =
		"market://details?id=com.uzislam.alqalam.arabicinstaller";
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran);
        gSurahList = (ListView)findViewById(R.id.SurahList);
        gSurahList.setVerticalScrollBarEnabled(false);
        gSurahList.setHorizontalScrollBarEnabled(false);
        
        gJuzzList = (ListView)findViewById(R.id.JuzList);
        gJuzzList.setVerticalScrollBarEnabled(false);
        gJuzzList.setHorizontalScrollBarEnabled(false);
        String [] juzz_array = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
        		"21","22","23","24","25","26","27","28","29","30"};
                          
        // By using setAdpater method in listview we an add string array in list.
        gJuzzList.setAdapter(new ArrayAdapter<String>(this,R.layout.quran_juzz, juzz_array));
        gJuzzList.setCacheColorHint(00000000); 
        gJuzzList.setDivider(null);
                
        
        gSurahTitles = getResources().getStringArray(R.array.SurahTitle);
        gSurahInfos  = getResources().getStringArray(R.array.SurahInfo);
        QuranAdapter		quranAdapter = new QuranAdapter(this);
        QuranIconifiedText	qit;
       	        
    	// check which Surah's Arabic Text is already downloaded
    	
        for (int i=0; i < CONSTANTS.NUMBER_OF_SURAHS ; i++) {
        	qit = new QuranIconifiedText(i, gSurahTitles[i], i+1, gSurahInfos[i], CONSTANTS.SURAH_IS_ARABIC_DOWNLOADED[i]);
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
					else if (CONSTANTS.SURAH_IS_ARABIC_DOWNLOADED[index]) {
						Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
						quranIntent.putExtra("sNumber", index);
						quranIntent.putExtra("aNumber", 0);
						startActivity(quranIntent);
					} 
					else {
						showDialog(CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST);
					}
			}
        });
        
        gJuzzList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					final int index, long order) {
				
					if (!isSdCardAccessible()) {
						showDialog(CONSTANTS.SURAH_DIALOG_NO_SDCARD);
					}
					else if (CONSTANTS.SURAH_IS_ARABIC_DOWNLOADED[index]) {
						Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
						quranIntent.putExtra("sNumber", CONSTANTS.JUZZ_INDEXES[index][0]-1);
						quranIntent.putExtra("aNumber", CONSTANTS.JUZZ_INDEXES[index][1]);
						startActivity(quranIntent);
					} 
					else {
						showDialog(CONSTANTS.SURAH_DIALOG_DOWNLOAD_REQUEST);
					}
			}
        });
        
 	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.v(TAG, "Create Dialog");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.quran_activity, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		
    	switch (menuItem.getItemId()) {

    		case R.id.bookmarks:
    			startActivity(new Intent(QuranActivity.this, BookmarksActivity.class));
    			return true;
    		
    		case R.id.search:
    			return true;

    	}
	   
    	return false;
   }

}

package com.uzislam.alqalam;

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
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quran);
        SurahList = (ListView)findViewById(R.id.SurahList);
        SurahTitles = getResources().getStringArray(R.array.SurahTitle);
        
        QuranAdapter		quranAdapter = new QuranAdapter(this);
        QuranIconifiedText	qit;
    	Drawable			stateIcon = getResources().getDrawable(R.drawable.index_sound_get);
    	Drawable			placeIcon = null;
    	
        for (int i=0; i < CONSTANTS.numberOfSurahs ; i++) {
        	qit = new QuranIconifiedText(i, SurahTitles[i], i+1, CONSTANTS.SurahNumberOfAyats[i], stateIcon, placeIcon);
        	quranAdapter.addItem(qit);
        }
        
        SurahList.setAdapter(quranAdapter);
        SurahList.setCacheColorHint(00000000); 
        SurahList.setDivider(null);
        
        SurahList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
					Intent quranIntent = new Intent(QuranActivity.this, SuraActivity.class);
					quranIntent.putExtra("sNumber", arg2);
					startActivity(quranIntent);
			}
        });
 	}

}

/**
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
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class QuranActivity extends Activity {
    private ListView            gSurahList;
    private ListView            gJuzzList;
    private String[]            gSurahTitles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initialize();
        SharedPreferences prefs = getSharedPreferences(Utils.SETTINGS_FILE, MODE_PRIVATE);
        String[] availableLocales = getResources().getStringArray(R.array.locale_values);
        // The index of default language is 2 (Uzbek)
        int index = prefs.getInt(Utils.SETTINGS_UI_LOCALE_TITLE, 2);
        Utils.updateUiLocale(this, availableLocales[index]);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quran);

        gJuzzList = (ListView)findViewById(R.id.JuzList);
        gJuzzList.setVerticalScrollBarEnabled(false);
        gJuzzList.setHorizontalScrollBarEnabled(false);
        String [] juzz_array = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30"};
        // By using setAdpater method in listview we an add string array in list.
        gJuzzList.setAdapter(new ArrayAdapter<String>(this,R.layout.quran_juzz, juzz_array));
        gJuzzList.setCacheColorHint(00000000);
        gJuzzList.setDivider(null);
        gJuzzList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    final int index, long order) {
                Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
                quranIntent.putExtra("sNumber", Utils.JUZZ_INDEXES[index][0]-1);
                quranIntent.putExtra("aNumber", Utils.JUZZ_INDEXES[index][1]);
                startActivity(quranIntent);
            }
        });

        gSurahTitles = getResources().getStringArray(R.array.surah_titles);
        QuranAdapter        quranAdapter = new QuranAdapter(this);
        QuranIconifiedText  qit;

        // check which Surah's Arabic Text is already downloaded
        int j = 0;
        for (int i=0; i < Utils.NUMBER_OF_SURAHS ; i++) {
            if (i+1 == Utils.MADANI_SURAH_INDEX[j]) {
                qit = new QuranIconifiedText(i, gSurahTitles[i], i+1, /*gSurahInfos[i]*/getString(R.string.surah_madani, Utils.SURAH_NUMBER_OF_AYATS[i]));
                j++;
            } else {
                qit = new QuranIconifiedText(i, gSurahTitles[i], i+1, /*gSurahInfos[i]*/getString(R.string.surah_makki, Utils.SURAH_NUMBER_OF_AYATS[i]));
            }
            quranAdapter.addItem(qit);
        }

        gSurahList = (ListView)findViewById(R.id.SurahList);
        gSurahList.setVerticalScrollBarEnabled(false);
        gSurahList.setHorizontalScrollBarEnabled(false);
        gSurahList.setAdapter(quranAdapter);
        gSurahList.setCacheColorHint(00000000);
        gSurahList.setDivider(null);
        gSurahList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    final int index, long order) {
                Intent quranIntent = new Intent(QuranActivity.this, SurahActivity.class);
                quranIntent.putExtra("sNumber", index);
                quranIntent.putExtra("aNumber", 0);
                startActivity(quranIntent);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.i(Utils.GTAG, "onOptionsItemSelected: " + menuItem);

        switch (menuItem.getItemId()) {

            case R.id.bookmarks:
                startActivity(new Intent(this, BookmarksActivity.class));
                return true;

            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

        }

        return false;
   }*/

   private void initialize() {
        Log.i(Utils.GTAG, "initialize the db...");
        Thread thread = new Thread() {
            public void run() {
                new AlQalamDatabase(getApplicationContext());
                /*AlQalamDatabase db = new AlQalamDatabase(getApplicationContext());
                // Creates the DB if it is the first time
                try {
                    db.openWritable();
                } catch (Exception e) {
                    Log.e(Utils.GTAG, "Exception while trying to open Al-Qalam database");
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }*/
            }
        };
        thread.start();
    }

}

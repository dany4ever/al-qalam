/*
 * Copyright 2012 (c) Al-Qalam Project
 *
 * This file is part of Al-Qalam (uz.efir.alqalam) package.
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
package uz.efir.alqalam;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class SurahActivity extends Activity {
    //private static final String TAG = "SurahActivity";
    private String [] AYATSTRANS;
    private String [] AYATSARABIC;
    private int[] mBookmarks;
    private int mSurahNumber;
    private int mCurrentAyat;
    //private int selectedAyat = 0;

    private String[] mTitles;
    private SurahAdapter surahAdapter;
    private ListView ayatList;

    //private final int DIALOG_TRANSLATION = 0x01;
    //private final int DIALOG_AYAT_CLICK_OPTION = 0x03;

    private SharedPreferences mSharedPrefs;
    //private SharedPreferences.Editor preferenceEditor;
    private int TranslationType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah);

        Intent intent = getIntent();
        mSurahNumber = intent.getIntExtra("sNumber", 0);
        mCurrentAyat = intent.getIntExtra("aNumber", 0);
        // TODO: Use Arabic titles
        mTitles = getResources().getStringArray(R.array.surah_titles);

        mSharedPrefs = getSharedPreferences(Utils.SETTINGS_FILE, 0);
        //preferenceEditor = mSharedPrefs.edit();

        // Get Translation Type from shared preferences, default is 0 (uzbek-cyr)
        TranslationType = mSharedPrefs.getInt(Utils.SETTINGS_TRANSLATION_OPTION_TITLE, 0);

        // create new chapter adapter
        surahAdapter = new SurahAdapter(this);

        // get AyatList View
        ayatList = (ListView)findViewById(R.id.AyaList);
        /*ayatList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, final int index, long order) {
                        selectedAyat = index + 1;
                        showDialog(DIALOG_AYAT_CLICK_OPTION);
                        return true;
            }
        });*/
        // Display surah
        showData();
    }

    private void resetData() {
        setTitle(mTitles[mSurahNumber]);

        // create string arrays for verses
        AYATSTRANS = new String[Utils.SURAH_NUMBER_OF_AYATS[mSurahNumber]];
        AYATSARABIC = new String [Utils.SURAH_NUMBER_OF_AYATS[mSurahNumber]];


        AlQalamDatabase db = new AlQalamDatabase(this);
        db.openReadable();

        Cursor cursor;
        int index = 0;

        // if translation is enabled
        if (TranslationType != Utils.NUMBER_OF_TRANSLATIONS) {
            cursor = db.getVerses(mSurahNumber + 1, TranslationType);
            index = 0;
            if (cursor.moveToFirst()) {
                do {
                    AYATSTRANS[index] = cursor.getString(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYAT));
                    index++;
                } while(cursor.moveToNext());
            }
            cursor.close();
        }

        // Now read Bookmarked ayats to Array
        cursor = db.getBookmarksForSurah(mSurahNumber+1);
        if (cursor.moveToFirst()) {
            mBookmarks = new int [cursor.getCount()];
            do {
                mBookmarks[index] = cursor.getInt(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYATNO));
                index++;

            } while(cursor.moveToNext());
        } else {
            mBookmarks =  null;
        }
        cursor.close();

        // Put verses
        cursor = db.getArabicVerses(mSurahNumber + 1);
        index = 0;
        if (cursor.moveToFirst()) {
            do {
                AYATSARABIC[index] = cursor.getString(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYAT));
                index++;
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private void showData() {
        AyatIconifiedText ait;
        final Drawable iconBismillah = getResources().getDrawable(R.drawable.bismillah);
        final Drawable bookmarkImage = getResources().getDrawable(R.drawable.bookmark_icon);

        surahAdapter.clear();
        resetData();

        for (int i = 0; i < Utils.SURAH_NUMBER_OF_AYATS[mSurahNumber]; i++) {
            ait = new AyatIconifiedText(i, i+1, AYATSARABIC[i], AYATSTRANS[i]);
            // check if BISMILLAH must be shown
            if (i == 0 && mSurahNumber != 0 && mSurahNumber != 8) {
                ait.setBismillahImage(iconBismillah);
            }
            // Put bookmarks
            if (mBookmarks != null && Arrays.binarySearch(mBookmarks, i + 1) >= 0) {
                ait.setAyatBookmarkImage(bookmarkImage);
                ait.setAyatBackground(Color.rgb(243, 255, 140));
            }
            // Put juzz and/or sajda icons
            ait.setAyatSpecialImage(getSpecialImage(mSurahNumber + 1, i + 1));
            surahAdapter.addItem(ait);
        }

        // make verses appear in list view
        ayatList.setAdapter(surahAdapter);
        ayatList.setDivider(null);

        // Jump to specific Ayat (if defined);
        if (mCurrentAyat > 0) {
            ayatList.setSelectionFromTop(mCurrentAyat - 1, 0);
        }
    }

    private Drawable getSpecialImage(int surah, int ayat) {
        // Sajda icon
        for (int i = 0; i < Utils.NUMBER_OF_SAJDAS; i++) {
            if (Utils.SAJDA_INDEXES[i][0] == surah &&  Utils.SAJDA_INDEXES[i][1] == ayat)
                return getResources().getDrawable(R.drawable.sajdah);
        }
        // Juzz icon
        for (int i = 0; i < Utils.NUMBER_OF_JUZZ; i++) {
            if (Utils.JUZZ_INDEXES[i][0] == surah && Utils.JUZZ_INDEXES[i][1] == ayat)
                return getResources().getDrawable(R.drawable.juzz);
        }

        return null;

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.translation: //MENU_ITEM_TRANSLATION :
                showDialog(DIALOG_TRANSLATION);
                return true;
        }

        return false;
   }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        if (id == DIALOG_TRANSLATION){
            ab.setTitle(R.string.translation_language);

            ab.setSingleChoiceItems(R.array.translation_options, TranslationType , new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.i(TAG, "Item " + item);
                    changeTranslation(item);
                }
            }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(DialogInterface dialog, int item) {
                    removeDialog(DIALOG_TRANSLATION);
                }
            });

        } else if (id == DIALOG_AYAT_CLICK_OPTION) {

            ab.setTitle(R.string.options);

            int options = R.array.ayat_click_options_add;

            if (Bookmarks != null && Arrays.binarySearch(Bookmarks, selectedAyat) >= 0) {
                options = R.array.ayat_click_options_del;
            }

            ab.setSingleChoiceItems(options, 0 , new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.i(TAG, "Item " + item);
                    doAyatClickTask(item);
                }

            })
            .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(DialogInterface dialog, int item) {
                    removeDialog(DIALOG_AYAT_CLICK_OPTION);
                }
            });
        }


        AlertDialog alert = ab.create();

        return alert;

    }

    @SuppressWarnings("deprecation")
    private void doAyatClickTask(int item) {
        removeDialog(DIALOG_AYAT_CLICK_OPTION);
        if (item == 2) {
            AlQalamDatabase db = new AlQalamDatabase(this);
            db.openWritable();

            AyatIconifiedText ait = (AyatIconifiedText)surahAdapter.getItem(selectedAyat-1);

            if (db.bookmarkOperation(surahNumber + 1, selectedAyat)) {
                ait.setAyatBackground(Color.rgb(243, 255, 140));
                ait.setAyatBookmarkImage(getResources().getDrawable(R.drawable.bookmark_icon));

            } else  {
                ait.setAyatBackground(Color.TRANSPARENT);
                ait.setAyatBookmarkImage(null);
            }

            surahAdapter.notifyDataSetChanged();


            // Now read Bookmarked ayats to Array
            Cursor cursor = db.getBookmarksForSurah(surahNumber+1);

            int  index = 0;

            if(cursor.moveToFirst())
            {
                Bookmarks = new int [cursor.getCount()];

                do
                {
                    Bookmarks[index] = cursor.getInt(cursor.getColumnIndex(AlQalamDatabase.COLUMN_AYATNO));
                    index++;

                } while(cursor.moveToNext());
            }
            else {
                Bookmarks =  null;
            }

            cursor.close();
            db.close();


        }
    }

    @SuppressWarnings("deprecation")
    private void changeTranslation(int lng) {
        removeDialog(DIALOG_TRANSLATION);
        TranslationType = lng;
        preferenceEditor.putInt(Utils.SETTINGS_TRANSLATION_OPTION_TITLE, lng);
        preferenceEditor.commit();

        showData();
    }*/
}

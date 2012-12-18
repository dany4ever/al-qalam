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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;


public class SettingsActivity extends PreferenceActivity {

	private static final String TAG = "SettingsActivity";
	private SharedPreferences		commonPrefs = null;
	public SharedPreferences.Editor preferenceEditor = null;
	
	private ListPreference		translationOption, reciterOption, uiLocale;
	private startQalam mStartQalam = new startQalam();
	private Context mContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Log.i(TAG, "Create Settings");
	        mContext = getApplicationContext();
	        
	        addPreferencesFromResource(R.layout.settings);
	        
	        commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, MODE_PRIVATE);
	        preferenceEditor = commonPrefs.edit();
	        
	        translationOption = (ListPreference) findPreference("TransOption");
	        translationOption.setNegativeButtonText(R.string.btn_cancel);
	        translationOption.setValueIndex(commonPrefs.getInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, 0));
	        translationOption.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
	            public boolean onPreferenceChange(Preference preference, Object newValue) {  
	                int index = translationOption.findIndexOfValue(newValue.toString());  
	                if (index != -1) {  
	                	preferenceEditor.putInt(CONSTANTS.SETTINGS_TRANSLATION_OPTION_TITLE, index);
	                	preferenceEditor.commit();
	                }  
	                return true;  
	            } 
	        });  
	        
	        reciterOption = (ListPreference) findPreference("ReciterOption");
	        reciterOption.setNegativeButtonText(R.string.btn_cancel);
	        reciterOption.setValueIndex(commonPrefs.getInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, 0));
	        reciterOption.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
	            public boolean onPreferenceChange(Preference preference, Object newValue) {  
	                int index = reciterOption.findIndexOfValue(newValue.toString());  
	                if (index != -1) {  
	                	preferenceEditor.putInt(CONSTANTS.SETTINGS_RECITER_OPTION_TITLE, index);
	                	preferenceEditor.commit();
	                }  
	                return true;  
	            }  
	        });
	        
	        uiLocale = (ListPreference) findPreference("ui_locale");
	        uiLocale.setNegativeButtonText(R.string.btn_cancel);
	        // The index of default language is 2 (Uzbek)
	        uiLocale.setValueIndex(commonPrefs.getInt(CONSTANTS.SETTINGS_UI_LOCALE_TITLE, 2));
	        uiLocale.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	            public boolean onPreferenceChange(Preference preference, Object newValue) {
	            	String locale = newValue.toString();
	            	int index = uiLocale.findIndexOfValue(locale);
	                if (index != -1) {
	                	preferenceEditor.putInt(CONSTANTS.SETTINGS_UI_LOCALE_TITLE, index);
	                	preferenceEditor.commit();
	                	mStartQalam.updateUiLocale(mContext, locale);
	                }
	                finish();
	                return true;
	            }
	        });
	        
	 }

}
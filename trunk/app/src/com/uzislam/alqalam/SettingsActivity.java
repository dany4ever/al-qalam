package com.uzislam.alqalam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;


public class SettingsActivity extends PreferenceActivity {

	private String LOG_SETUP = "al-Qalam Settings";
	private SharedPreferences		commonPrefs = null;
	public SharedPreferences.Editor preferenceEditor = null;
	
	private ListPreference		translationOption, reciterOption;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Log.i(LOG_SETUP, "Creat Settings");
	        
	        addPreferencesFromResource(R.layout.settings);
	        
	        commonPrefs = getSharedPreferences(CONSTANTS.SETTINGS_FILE, 0);
	        preferenceEditor = commonPrefs.edit();
	        
	        translationOption = (ListPreference) findPreference("TransOption");
	               
	        translationOption.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
	            public boolean onPreferenceChange(Preference preference, Object newValue) {  
	                int index = translationOption.findIndexOfValue(newValue.toString());  
	                if (index != -1) {  
	                	preferenceEditor.putInt("TransOption", index);
	                	preferenceEditor.commit();
	                }  
	                return true;  
	            }  
	        });  
	        
	        reciterOption = (ListPreference) findPreference("ReciterOption");
            
	        reciterOption.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
	            public boolean onPreferenceChange(Preference preference, Object newValue) {  
	                int index = reciterOption.findIndexOfValue(newValue.toString());  
	                if (index != -1) {  
	                	preferenceEditor.putInt("ReciterOption", index);
	                	preferenceEditor.commit();
	                }  
	                return true;  
	            }  
	        });  
	        
	 }

}
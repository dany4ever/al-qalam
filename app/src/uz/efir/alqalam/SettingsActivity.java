/**
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;


public class SettingsActivity extends SherlockPreferenceActivity {

    //private SharedPreferences commonPrefs;

    //private ListPreference translationOption;
    //private ListPreference uiLocale;
    //private Context mContext;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mContext = getApplicationContext();

        addPreferencesFromResource(R.xml.settings);

        /*commonPrefs = getSharedPreferences(Utils.SETTINGS_FILE, MODE_PRIVATE);

        translationOption = (ListPreference) findPreference("key_translations");
        translationOption.setNegativeButtonText(R.string.btn_cancel);
        translationOption.setValueIndex(commonPrefs.getInt(Utils.SETTINGS_TRANSLATION_OPTION_TITLE, 0));
        translationOption.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int index = translationOption.findIndexOfValue(newValue.toString());
                if (index != -1) {
                    SharedPreferences.Editor editor = commonPrefs.edit();
                    editor.putInt(Utils.SETTINGS_TRANSLATION_OPTION_TITLE, index);
                    editor.commit();
                }
                return true;
            }
        });

        uiLocale = (ListPreference) findPreference("ui_locale");
        uiLocale.setNegativeButtonText(R.string.btn_cancel);
        // The index of default language is 2 (Uzbek)
        uiLocale.setValueIndex(commonPrefs.getInt(Utils.SETTINGS_UI_LOCALE_TITLE, 2));
        uiLocale.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String locale = newValue.toString();
                int index = uiLocale.findIndexOfValue(locale);
                if (index != -1) {
                    SharedPreferences.Editor editor = commonPrefs.edit();
                    editor.putInt(Utils.SETTINGS_UI_LOCALE_TITLE, index);
                    editor.commit();
                    Utils.updateUiLocale(mContext, locale);
                }
                finish();
                return true;
            }
        });*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, QuranActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
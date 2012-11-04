/**
 * Copyright 2012 (c) Al-Qalam Project
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

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

public class Utils {
    // Global application log tag
    public static final String GTAG = "Al-Qalam";

    public static final int NUMBER_OF_SURAHS = 114;
    public static final int NUMBER_OF_JUZZ = 30;
    public static final int[] SURAH_NUMBER_OF_AYATS = new int[] {7,286,200,176,120,165,206,75,129,
            109,123,111,43,52,99,128,111,110,98,135,112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,
            83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,13,14,11,11,18,
            12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,
            8,8,19,5,8,8,11,11,8,3,9,5,4,7,3,6,3,5,4,5,6};
    /**
     * Madani Surah numbers; note that they correspond to actual Surah order
     * The list is not verified, may not be 100% accurate.
     * There is a disagreement regarding Surah 13 (Ra'd),
     * is it possible to put and asterisk (*) with a footnote?
     */
    public static final int[] MADANI_SURAH_INDEX = new int[] {2,3,4,5,8,9,13,22,24,33,47,48,49,57,
            58,59,60,61,62,63,64,65,66,76,98,99,110,113,114};

    public static final int NUMBER_OF_SAJDAS = 14;
    public static final int [][] SAJDA_INDEXES = new int [][] {
            {7,206}, {13, 15}, {16, 50}, {17,109}, {19, 58}, {22, 18}, {25, 60},
            {27, 26}, {32, 15}, {38, 24}, {41,38}, {53, 62}, {84,21}, {96, 19}
        };
    public static final int [][] JUZZ_INDEXES = new int [][] {
            {1, 1}, {2, 142}, {2, 253}, {3, 93}, {4, 24}, {4, 148}, {5, 82}, {6,111}, {7, 88},
            {8, 41}, {9, 93}, {11, 6}, {12, 53}, {15, 1}, {17, 1}, {18, 75}, {21, 1}, {23, 1},
            {25, 21}, {27,56}, {29, 46}, {33, 31}, {36, 28}, {39, 32}, {41, 47}, {46, 1}, {51, 31},
            {58, 1}, {67, 1}, {78, 1}
        };

    public static final String SETTINGS_FILE = "alQalam.set";
    public static final String SETTINGS_TRANSLATION_OPTION_TITLE ="TransOption";
    public static final String SETTINGS_UI_LOCALE_TITLE = "UiLocale";
    public static final int NUMBER_OF_TRANSLATIONS = 4;

    public static final String numberToString(int a) {
        String rTxt;
        if (a < 10) {
            rTxt = "00" + a;
        } else if (a < 100) {
            rTxt = "0" + a;
        } else {
            rTxt = "" + a;
        }

        return rTxt;
    }

    // Overrides the system locale and sets user selected one
    public static void updateUiLocale(Context context, String locale) {
        Configuration config = new Configuration();
        if (TextUtils.isEmpty(locale)) {
            config.locale = new Locale("uz");
        } else {
            config.locale = new Locale(locale);
        }
        context.getResources().updateConfiguration(config, null);
    }
}
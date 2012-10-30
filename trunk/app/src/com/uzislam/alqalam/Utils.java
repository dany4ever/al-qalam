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
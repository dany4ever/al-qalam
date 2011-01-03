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

package com.uzislam.alqalam.database;

import com.uzislam.alqalam.CONSTANTS;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class Provider extends ContentProvider {
	
	protected Helper mOpenHelper;
	
	private static final int SEARCH = 0;
	private static final int GET_AYATS = 1;
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sURIMatcher.addURI(CONSTANTS.AUTHORITY, CONSTANTS.DATABASE_NAME, SEARCH);
		sURIMatcher.addURI(CONSTANTS.AUTHORITY, CONSTANTS.DATABASE_NAME, GET_AYATS);
	}
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new Helper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		/* This method is intentionally left unimplemented.
		 * Number of Qur'an Ayats are fixed, hence we do not want to *delete* any Ayat.
		 */
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int match = sURIMatcher.match(uri);
		switch(match) {
		case SEARCH:
			return "something";
		case GET_AYATS:
			return "something else";
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		/* This method is intentionally left unimplemented.
		 * Number of Qur'an Ayats are fixed, hence we do not want to *insert* any new Ayat.
		 */
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] select, String where, String[] whereArgs,
			String sort) {
		switch (sURIMatcher.match(uri)) {
		case SEARCH:
			if (whereArgs == null) {
				throw new IllegalArgumentException("whereArgs must be provided for the Uri: " + uri);
			}
			return search(whereArgs[0]);
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}
	
	private Cursor search(String query) {
		query = query.toLowerCase();
		String[] columns = new String[] {
				BaseColumns._ID,
				"surah_no",
				"ayat_no",
				"ayat"
		};
		
		return mOpenHelper.getWordMatches(query, columns);
	}

	@Override
	public int update(Uri uri, ContentValues initialValues, String where, String[] whereArgs) {
		// TODO Should be implemented in the future releases to fix possible typos in translations
		return 0;
	}

}

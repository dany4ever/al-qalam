package com.uzislam.alqalam.database;

/* 
 * Copyright 2010 (c) Shuhrat Dehkanov and Elmurod Talipov
 *  
 * This file is part of Al-Qalam (com.uzislam.alqalam package).
 * 
 * Al-Qalam  is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Al-Qalam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Provider extends ContentProvider {
	
	protected Helper mOpenHelper;
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new Helper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] select, String where, String[] whereArgs,
			String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues initialValues, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}

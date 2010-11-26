package com.uzislam.alqalam.database;

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

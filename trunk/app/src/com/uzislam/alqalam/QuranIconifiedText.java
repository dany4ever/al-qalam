package com.uzislam.alqalam;

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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import android.graphics.drawable.Drawable;

public class QuranIconifiedText {
	private long		id = -1 ;
	private String		surahTitle = null;
	private int			surahOrder = 0;
	private int			surahNumberOfAyats = 0;
	private boolean		surahIsDownloaded = false;
	private Drawable	surahState = null;
	private Drawable	surahRevelationPlace = null;
		
	public QuranIconifiedText(long _id, String _title, int _order, int _numberOfAyats, boolean _downloaded ,Drawable _state, Drawable _place) {
		this.id = _id;
		this.surahTitle = _title;
		this.surahOrder = _order;
		this.surahNumberOfAyats = _numberOfAyats;
		this.surahIsDownloaded = _downloaded;
		this.surahState = _state;
		this.surahRevelationPlace = _place;
	}
	
	public void setId (long _id) {
		this.id = _id;
	}
	
	public void setSurahTitle(String _title) {
		this.surahTitle = _title;
	}
	
	public void setSurahOrder(int _order) {
		this.surahOrder = _order;
	}
	
	public void setSurahNumberOfAyats(int _ayats) {
		this.surahNumberOfAyats = _ayats;
	}

	public void setSurahtate(Drawable _state) {
		this.surahState = _state;
	}
	
	public void setSurahRevelationPlace(Drawable _place) {
		this.surahRevelationPlace = _place;
	}
	
	public void setSurahIsDownloaded(boolean _downloaded) {
		this.surahIsDownloaded = _downloaded;
	}
	
	public long getId () {
		return this.id;
	}
	
	public String getSurahTitle() {
		return this.surahTitle ;
	}
	
	public int getSurahOrder() {
		return this.surahOrder ;
	}
	
	public int getSurahNumberOfAyats() {
		return this.surahNumberOfAyats ;
	}

	public boolean getSurahIsDownloaded() {
		return this.surahIsDownloaded;
	}
	
	public Drawable getSurahState() {
		return this.surahState;
	}
	
	public Drawable getSurahRevelationPlace() {
		return this.surahRevelationPlace;
	}
	
}

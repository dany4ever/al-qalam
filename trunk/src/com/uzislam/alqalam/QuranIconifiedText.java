package com.uzislam.alqalam;

import android.graphics.drawable.Drawable;

public class QuranIconifiedText {
	private long		id = -1 ;
	private String		surahTitle = null;
	private int			surahOrder = 0;
	private int			surahNumberOfAyats = 0;
	private Drawable	surahState = null;
	private Drawable	surahRevelationPlace = null;
		
	public QuranIconifiedText(long _id, String _title, int _order, int _numberOfAyats, Drawable _state, Drawable _place) {
		this.id = _id;
		this.surahTitle = _title;
		this.surahOrder = _order;
		this.surahNumberOfAyats = _numberOfAyats;
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

	public Drawable getSurahState() {
		return this.surahState;
	}
	
	public Drawable getSurahRevelationPlace() {
		return this.surahRevelationPlace;
	}
	
}

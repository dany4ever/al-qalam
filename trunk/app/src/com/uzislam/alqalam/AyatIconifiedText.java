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

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class AyatIconifiedText {
	private long		id = -1 ;
	private int			ayatOrder = 0;
	private String		ayatArabicText = null;
	private String		ayatUzbekText = null;
	private Drawable	ayatBismillah = null;
	private Drawable	ayatSpecialImage = null;
	private int			ayatBackground = Color.TRANSPARENT;
		
	public AyatIconifiedText(long _id, int _order, String _arabic, String _uzbek, Drawable _spimage, Drawable _bismillah, int _ayatBackground) {
		this.id = _id;
		this.ayatOrder = _order;
		this.ayatArabicText = _arabic;
		this.ayatUzbekText = _uzbek;
		this.ayatBismillah  = _bismillah;
		this.ayatBackground = _ayatBackground;
		this.ayatSpecialImage = _spimage;
	}
	
	public long getId () {
		return this.id;
	}
	
	public int getAyatOrder() {
		return this.ayatOrder;
	}
	
	public String getAyatUzbekText() {
		return this.ayatUzbekText ;
	}
	
	public String getAyatArabicText() {
		return this.ayatArabicText ;
	}
	
	public Drawable getAyatBismillah() {
		return this.ayatBismillah ;
	}

	public Drawable getAyatSpecialImage() {
		return this.ayatSpecialImage;
	}
	
	public int getAyatBackground() {
		return this.ayatBackground;
	}
	
}

/*
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

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class AyatIconifiedText {
    private long mId = -1 ;
    private int mOrder = 0;
    private String mArabicText = null;
    private String mTranslationText = null;
    private Drawable mBismillahImage = null;
    private Drawable mSpecialImage = null;
    private Drawable mBookmarkImage = null;
    private int mBackgroundColor = Color.TRANSPARENT;

    public AyatIconifiedText(long id, int order, String arabic, String translation) {
        mId = id;
        mOrder = order;
        mArabicText = arabic;
        mTranslationText = translation;
    }

    public long getId () {
        return mId;
    }

    public int getAyatOrder() {
        return mOrder;
    }

    public String getAyatUzbekText() {
        return mTranslationText ;
    }

    public String getAyatArabicText() {
        return mArabicText ;
    }

    public Drawable getBismillahImage() {
        return mBismillahImage;
    }

    public void setBismillahImage (Drawable img) {
        mBismillahImage  = img;
    }

    public Drawable getAyatSpecialImage() {
        return mSpecialImage;
    }

    public int getAyatBackground() {
        return mBackgroundColor;
    }

    public Drawable getAyatBookmarkImage () {
        return mBookmarkImage;
    }

    public void setAyatBackground (int color) {
        mBackgroundColor  = color;
    }

    public void setAyatSpecialImage (Drawable img) {
        mSpecialImage = img;
    }

    public void setAyatBookmarkImage (Drawable img) {
        mBookmarkImage = img;
    }
}

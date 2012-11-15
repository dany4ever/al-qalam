/*
 * Copyright 2010 (c) Al-Qalam Project
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

public class QuranIconifiedText {
    private long id = -1 ;
    private String surahTitle = null;
    private int surahOrder = 0;
    private String surahInfo = null;
    public QuranIconifiedText(long _id, String _title, int _order, String _info) {
        this.id = _id;
        this.surahTitle = _title;
        this.surahOrder = _order;
        this.surahInfo = _info;
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

    public void setSurahInfo(String _info) {
        this.surahInfo = _info;
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

    public String getSurahInfo() {
        return this.surahInfo ;
    }
}

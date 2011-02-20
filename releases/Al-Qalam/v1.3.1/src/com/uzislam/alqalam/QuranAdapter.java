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

package com.uzislam.alqalam;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QuranAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<QuranIconifiedText> mItems = new ArrayList<QuranIconifiedText>(); 
    
	public QuranAdapter(Context context) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return mItems.get(arg0).getId();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder vHolder;
               
         if (convertView == null) {
         	convertView = mInflater.inflate(R.layout.quran_row, null);

         	vHolder = new ViewHolder();
             
         	vHolder.SurahTitle = (TextView) convertView.findViewById(R.id.srTitle);
         	vHolder.SurahOrder = (TextView) convertView.findViewById(R.id.srOrder);
         	vHolder.SurahAyats = (TextView) convertView.findViewById(R.id.srAyats);
         	vHolder.SurahDwnState = (ImageView) convertView.findViewById(R.id.srDown);             
            convertView.setTag(vHolder);
         } else {
         	vHolder = (ViewHolder) convertView.getTag();
         }

         vHolder.SurahTitle.setText(mItems.get(position).getSurahTitle());
         vHolder.SurahOrder.setText(mItems.get(position).getSurahOrder()+ "");
         vHolder.SurahAyats.setText(mItems.get(position).getSurahNumberOfAyats() + "");
       	 vHolder.SurahDwnState.setImageDrawable(mItems.get(position).getSurahState());
         
         /* if (mItems.get(position).getSurahIsDownloaded() == false) {
        	 vHolder.SurahTitle.setTextColor(R.color.lightblack);
        	 vHolder.SurahOrder.setTextColor(R.color.lightblack);
        	 vHolder.SurahAyats.setTextColor(R.color.lightblack);
        	 
         }*/

         return convertView;
	}
	
     static class ViewHolder {
         TextView 		SurahTitle;
         TextView 		SurahOrder;
         TextView 		SurahAyats;
         ImageView 		SurahDwnState;
         ImageView 		SurahPlace;
    }
	
	
	public void addItem(QuranIconifiedText it) {
		mItems.add(it); 
	}
	
	public void clear(){
		mItems.clear();
	}

}

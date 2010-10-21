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
	
	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int arg0) {
		return mItems.get(arg0);
	}

	public long getItemId(int arg0) {
		return mItems.get(arg0).getId();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder vHolder;
               
         if (convertView == null) {
         	convertView = mInflater.inflate(R.layout.surahindex, null);

         	vHolder = new ViewHolder();
             
         	vHolder.SurahTitle = (TextView) convertView.findViewById(R.id.srTitle);
         	vHolder.SurahOrder = (TextView) convertView.findViewById(R.id.srOrder);
         	vHolder.SurahAyats = (TextView) convertView.findViewById(R.id.srAyats);
         	vHolder.SurahDwnState = (ImageView) convertView.findViewById(R.id.srDown);
         	vHolder.SurahPlace = (ImageView) convertView.findViewById(R.id.srPlace);
             
            convertView.setTag(vHolder);
         } else {
         	vHolder = (ViewHolder) convertView.getTag();
         }

         vHolder.SurahTitle.setText(mItems.get(position).getSurahTitle());
         vHolder.SurahOrder.setText(mItems.get(position).getSurahOrder()+ "");
         vHolder.SurahAyats.setText(mItems.get(position).getSurahNumberOfAyats() + "");
       	 vHolder.SurahDwnState.setImageDrawable(mItems.get(position).getSurahState());
         vHolder.SurahPlace.setImageDrawable(mItems.get(position).getSurahRevelationPlace());
         
         if (mItems.get(position).getSurahIsDownloaded() == false) {
        	 vHolder.SurahTitle.setTextColor(R.color.lightblack);
        	 vHolder.SurahOrder.setTextColor(R.color.lightblack);
        	 vHolder.SurahAyats.setTextColor(R.color.lightblack);
        	 
         }

        	 
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

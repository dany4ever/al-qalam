package com.uzislam.alqalam;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SurahAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<AyatIconifiedText> mItems = new ArrayList<AyatIconifiedText>(); 
    private Context _context;
	
	public SurahAdapter(Context context) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this._context = context;
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
         	convertView = mInflater.inflate(R.layout.ayat, null);

         	vHolder = new ViewHolder();
             
         	vHolder.AyatGroup = (LinearLayout) convertView.findViewById(R.id.ayatGroup);
         	vHolder.AyatOrder = (TextView) convertView.findViewById(R.id.ayatOrder);
         	vHolder.AyatBismillah = (ImageView)convertView.findViewById(R.id.ayatBismillah);
        	vHolder.AyatUzbek = (TextView) convertView.findViewById(R.id.ayatUzbekText);
        	vHolder.AyatArabic = (ImageView) convertView.findViewById(R.id.ayatArabicText);
        
        	
        	//Typeface tf = Typeface.createFromAsset(_context.getAssets(),"fonts/IslamicFont.ttf");
    	    //vHolder.AyatUzbek.setTypeface(tf);
    	    
        	vHolder.AyatSpecialImage = (ImageView) convertView.findViewById(R.id.ayatSpecialImage);
             
    	    convertView.setTag(vHolder);
            
         } else {
        	 
         	vHolder = (ViewHolder) convertView.getTag();
         	
         }
       
         if (mItems.get(position).getAyatBackground() != 0)
        	 vHolder.AyatGroup.setBackgroundColor(mItems.get(position).getAyatBackground());
         
         vHolder.AyatOrder.setText(mItems.get(position).getAyatOrder()+ "");
         vHolder.AyatUzbek.setText(mItems.get(position).getAyatUzbekText());
         //vHolder.AyatArabic.setText(mItems.get(position).getAyatArabicText());
     
         Bitmap arabicAyat = BitmapFactory.decodeFile(mItems.get(position).getAyatArabicText());
         
         if (SurahActivity.displaymetrics.densityDpi == DisplayMetrics.DENSITY_HIGH)
        	 arabicAyat.setDensity((int)(SurahActivity.displaymetrics.densityDpi/1.45));
         
         vHolder.AyatArabic.setImageBitmap(arabicAyat);
       	 
         vHolder.AyatSpecialImage.setImageDrawable(mItems.get(position).getAyatSpecialImage());
         vHolder.AyatBismillah.setImageDrawable(mItems.get(position).getAyatBismillah());      
         
         return convertView;
	}
	
     static class ViewHolder {
    	 TextView		AyatOrder;
    	 ImageView		AyatBismillah;
    	 ImageView		AyatArabic;
         TextView		AyatUzbek;
         ImageView		AyatSpecialImage;
         LinearLayout	AyatGroup; 
    }
	
	
	public void addItem(AyatIconifiedText it) {
		mItems.add(it); 
	}
	
	public void clear(){
		mItems.clear();
	}

}

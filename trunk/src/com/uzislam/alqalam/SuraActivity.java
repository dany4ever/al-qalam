package com.uzislam.alqalam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SuraActivity extends Activity {
	private ListView AyatList; 
	
	static int surahNumber = 0;
	 
	static int[] SURAHAYATS = new int[] {7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,112,78,118,64,77,227,93,88,69,60,
		34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,13,14,11,11,18,12,12,30,52,52,44,28,
		28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,8,3,9,5,4,7,3,6,3,5,4,5,6};
	
	static int[] SUHRAHTITLES = {R.drawable.sname_1, R.drawable.sname_2, R.drawable.sname_3, R.drawable.sname_4, R.drawable.sname_5, R.drawable.sname_6,
		R.drawable.sname_7, R.drawable.sname_8, R.drawable.sname_9, R.drawable.sname_10, R.drawable.sname_11, R.drawable.sname_12, R.drawable.sname_13, 
		R.drawable.sname_114,R.drawable.sname_15, R.drawable.sname_16, R.drawable.sname_17, R.drawable.sname_18, R.drawable.sname_19, R.drawable.sname_20, 
		R.drawable.sname_21,R.drawable.sname_22,R.drawable.sname_23, R.drawable.sname_24, R.drawable.sname_25, R.drawable.sname_26, R.drawable.sname_27, 
		R.drawable.sname_28, R.drawable.sname_29, R.drawable.sname_30, R.drawable.sname_31, R.drawable.sname_32, R.drawable.sname_33, R.drawable.sname_34, 
		R.drawable.sname_35, R.drawable.sname_36, R.drawable.sname_37, R.drawable.sname_38, R.drawable.sname_39, R.drawable.sname_40, R.drawable.sname_41,
		R.drawable.sname_42, R.drawable.sname_43, R.drawable.sname_44, R.drawable.sname_45, R.drawable.sname_46, R.drawable.sname_47, R.drawable.sname_48,
		R.drawable.sname_49, R.drawable.sname_50, R.drawable.sname_51, R.drawable.sname_52, R.drawable.sname_53, R.drawable.sname_54, R.drawable.sname_55, 
		R.drawable.sname_56, R.drawable.sname_57, R.drawable.sname_58, R.drawable.sname_59, R.drawable.sname_60, R.drawable.sname_61, R.drawable.sname_62,
		R.drawable.sname_63, R.drawable.sname_64, R.drawable.sname_65, R.drawable.sname_66, R.drawable.sname_67, R.drawable.sname_68, R.drawable.sname_69, 
		R.drawable.sname_70, R.drawable.sname_71, R.drawable.sname_72, R.drawable.sname_73, R.drawable.sname_74, R.drawable.sname_75, R.drawable.sname_76,
		R.drawable.sname_77, R.drawable.sname_78, R.drawable.sname_79, R.drawable.sname_80, R.drawable.sname_81, R.drawable.sname_82, R.drawable.sname_83,
		R.drawable.sname_84, R.drawable.sname_85, R.drawable.sname_86, R.drawable.sname_87, R.drawable.sname_88, R.drawable.sname_89, R.drawable.sname_90,
		R.drawable.sname_91, R.drawable.sname_92, R.drawable.sname_93, R.drawable.sname_94, R.drawable.sname_95, R.drawable.sname_96, R.drawable.sname_97, 
		R.drawable.sname_98, R.drawable.sname_99, R.drawable.sname_100, R.drawable.sname_101, R.drawable.sname_102, R.drawable.sname_103, R.drawable.sname_104,
		R.drawable.sname_105, R.drawable.sname_106, R.drawable.sname_107, R.drawable.sname_108, R.drawable.sname_109, R.drawable.sname_110, 
		R.drawable.sname_111, R.drawable.sname_112, R.drawable.sname_113, R.drawable.sname_114};
	
	static String [] AYATS ; 
	
	private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Bitmap mArabicText;
        private Bitmap mSpecialIcon;
        Context mycontext;
          
        public EfficientAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //LayoutInflater.from(context);
            mycontext = context;
        }

        public int getCount() {
            return AYATS.length;
        }
       
        
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vHolder;
            int srOrder;

            Toast.makeText(mycontext, "Get View Called", Toast.LENGTH_SHORT);	
            
            if (convertView == null) {        
            	convertView = mInflater.inflate(R.layout.ayat, null);

            	vHolder = new ViewHolder();
                
            	vHolder.AyatNumber = (TextView) convertView.findViewById(R.id.ayaNumber);
            	vHolder.AyatUzbek = (TextView) convertView.findViewById(R.id.uzbekText);
            	vHolder.AyatArabic = (ImageView) convertView.findViewById(R.id.arabicText);
            	vHolder.AyatSpImg = (ImageView) convertView.findViewById(R.id.spcImg);
                                
                convertView.setTag(vHolder);
            } else {
            	vHolder = (ViewHolder) convertView.getTag();
            }

            srOrder = position + 1;
            vHolder.AyatNumber.setText(srOrder+"");
            vHolder.AyatUzbek.setText(AYATS[position]);        
            vHolder.AyatArabic.setImageBitmap(mArabicText);
            
            /*
			ImageView	 AyatBismillah = (ImageView) convertView.findViewById(R.id.Bismillah);;
            if (position == 0) AyatBismillah.setImageResource(R.drawable.bismillah);
            */
            
            //vHolder.AyatSpImg.setImageBitmap(mSpecialIcon); 
            
            return convertView;
        }
        
        static class ViewHolder {
        	TextView 	 AyatNumber;
        	TextView 	 AyatUzbek;
            ImageView 	 AyatArabic;
            ImageView 	 AyatSpImg;
       }
        
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
                
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
        	surahNumber = extras.getInt("sNumber");
        }
        
        String sfn = "uzbek/" + (surahNumber + 1) + ".dat";
       
        AYATS = new String[SURAHAYATS[surahNumber]];
        
        readFiletoString(sfn);
        
        setContentView(R.layout.surah);
        
        AyatList = (ListView)findViewById(R.id.AyaList);
        AyatList.setAdapter(new EfficientAdapter(this));
        AyatList.setCacheColorHint(00000000); 
        AyatList.setDivider(null);
               
        ImageView surahTitle = (ImageView) findViewById(R.id.suraName);
        surahTitle.setImageResource(SUHRAHTITLES[surahNumber]);
 	}
	
	public void readFiletoString(String SurahFileName) {
				
		BufferedReader dis = null;
		
		try {
			dis = new BufferedReader(new InputStreamReader(getAssets().open(SurahFileName))); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int  	index = 0;
		String  line = "";
	
		try {
			while((line = dis.readLine()) != null) {
				AYATS[index] = line;
				index++;				
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

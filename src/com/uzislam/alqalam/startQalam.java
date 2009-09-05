package com.uzislam.alqalam;

import com.uzislam.alqalam.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class startQalam extends Activity {
	
	private Handler aqHandler;
	private ImageView frontSplash;
	private LinearLayout mainView;
	private ImageView tmpImg;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main);
        
        frontSplash = (ImageView) findViewById(R.id.splashimage);
        mainView = (LinearLayout) findViewById(R.id.mainview);
        
        tmpImg = (ImageView) findViewById(R.id.imgQuran);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageView) findViewById(R.id.imgBukhari);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageView) findViewById(R.id.imgMuslim);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageView) findViewById(R.id.imgTirmidi);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageView) findViewById(R.id.imgAbudovud);
        tmpImg.setVisibility(View.GONE);
        
        aqHandler = new Handler ();
        aqHandler.postDelayed(Splash, 2500);
    }
    
    private Runnable Splash = new Runnable() {
	 	   public void run() {
	 		frontSplash.setVisibility(View.GONE);
	 		mainView.setBackgroundResource(R.drawable.background) ;
	 		
	 		final ImageView  imgQuran = (ImageView) findViewById(R.id.imgQuran);
	 		imgQuran.setVisibility(View.VISIBLE);
	 		imgQuran.setClickable(true);
	 		imgQuran.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
    				imgQuran.setAlpha(80);
	    		}
	        });
	        
	        final ImageView imgBukhari = (ImageView) findViewById(R.id.imgBukhari);
	        imgBukhari.setVisibility(View.VISIBLE);
	        imgBukhari.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
    				imgBukhari.setAlpha(80);
	    		}
		        });
	        
	        final ImageView imgMuslim = (ImageView) findViewById(R.id.imgMuslim);
	        imgMuslim.setVisibility(View.VISIBLE);
	        imgMuslim.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			imgMuslim.setAlpha(80);
	    		}
		        });
	        
	        final ImageView imgTirmidi = (ImageView) findViewById(R.id.imgTirmidi);
	        imgTirmidi.setVisibility(View.VISIBLE);
	        imgTirmidi.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			imgTirmidi.setAlpha(80);
	    		}
		        });
	        
	        final ImageView imgAbudavud  = (ImageView) findViewById(R.id.imgAbudovud);
	        imgAbudavud.setVisibility(View.VISIBLE);
	        imgAbudavud.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			imgAbudavud.setAlpha(80);
	    		}
		        });
	 	   }
	};   
    
}
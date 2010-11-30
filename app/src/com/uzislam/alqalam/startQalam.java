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

import com.uzislam.alqalam.database.Helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class startQalam extends Activity {

	private Handler aqHandler;
	private ImageView frontSplash;
	private LinearLayout mainView;
	private LinearLayout btnView;
	/*private ImageButton tmpImg;

	private final int	MENU_ITEM_ABOUT = 0x01;
	private final int	MENU_ITEM_HELP = 0x02;
	*/
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        frontSplash = (ImageView) findViewById(R.id.splashimage);
        mainView = (LinearLayout) findViewById(R.id.mainview);
        btnView = (LinearLayout) findViewById(R.id.buttons);
        btnView.setVisibility(View.GONE);
        /*
        tmpImg = (ImageButton) findViewById(R.id.imgBtnQuran);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageButton) findViewById(R.id.imgBtnBukhari);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageButton) findViewById(R.id.imgBtnMuslim);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageButton) findViewById(R.id.imgBtnTirmidi);
        tmpImg.setVisibility(View.GONE);
        tmpImg = (ImageButton) findViewById(R.id.imgBtnAbudovud);
        tmpImg.setVisibility(View.GONE);
        */
        aqHandler = new Handler ();
        aqHandler.postDelayed(Splash, 1500);
        
        // Initialize the database
        Helper mHelper = new Helper(this);
        mHelper.getWritableDatabase();
        
    }
    
    private Runnable Splash = new Runnable() {
	 	   public void run() {
	 		frontSplash.setVisibility(View.GONE);
	 		btnView.setVisibility(View.VISIBLE);
	 		mainView.setBackgroundResource(R.drawable.background);
	 		
	 		final ImageButton  imgBtnQuran = (ImageButton) findViewById(R.id.imgBtnQuran);
	 		//imgBtnQuran.setVisibility(View.VISIBLE);
	 		imgBtnQuran.setClickable(true);
	 		imgBtnQuran.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			 startActivity(new Intent(startQalam.this, QuranActivity.class));
	    		}
	        });
	        
	        final ImageButton imgBukhari = (ImageButton) findViewById(R.id.imgBtnBukhari);
	        //imgBukhari.setVisibility(View.VISIBLE);
	        imgBukhari.setAlpha(80);
	    	
	        
	        final ImageButton imgMuslim = (ImageButton) findViewById(R.id.imgBtnMuslim);
	        //imgMuslim.setVisibility(View.VISIBLE);
	        imgMuslim.setAlpha(80);
	    		        
	        final ImageButton imgTirmidi = (ImageButton) findViewById(R.id.imgBtnTirmidi);
	        //imgTirmidi.setVisibility(View.VISIBLE);
	        imgTirmidi.setAlpha(80);
	    		        
	        final ImageButton imgAbudavud  = (ImageButton) findViewById(R.id.imgBtnAbudovud);
	        //imgAbudavud.setVisibility(View.VISIBLE);
	    	imgAbudavud.setAlpha(80);
	 	   }
	};   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	@Override
	public boolean onPrepareOptionsMenu(Menu mainMenu) { 	
  	
    	mainMenu.clear();
    	
    	MenuItem subitem;
    	
		mainMenu.setQwertyMode(true);
		
		subitem = mainMenu.add(0, MENU_ITEM_ABOUT, 0 , R.string.info);
		subitem.setIcon(R.drawable.menu_icon_info);
	
		subitem = mainMenu.add(0, MENU_ITEM_HELP, 0, R.string.help);
		subitem.setIcon(R.drawable.menu_icon_help);	
		
    	return true;
    }
	*/

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		
    	switch (menuItem.getItemId()) {	

    		/*case MENU_ITEM_ABOUT :
    			return true;
    			
    		case MENU_ITEM_HELP:
    			//startActivity(new Intent(this, helpActivity.class));
    			return true;
    		*/
    	case R.id.info:
    		return true;
    	
    	case R.id.help:
    		return true;

    	}
	   
    	return false;
   }
		
}
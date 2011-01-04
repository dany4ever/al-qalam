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

import android.net.Uri;

public class CONSTANTS {
	
		public static int	numberOfSurahs = 114;
		public static int	numberOfSajdaAyats = 14;
		public static int	numberOfJuzs = 30;
		
		public static int[]	SurahNumberOfAyats = new int[] {7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,112,78,118,64,77,227,93,88,69,60,
			34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,13,14,11,11,18,12,12,30,52,52,44,28,
			28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,8,3,9,5,4,7,3,6,3,5,4,5,6};
	
		public static int [][] SajdaAyats = new int [][]  { {7,206}, {13, 15}, {16, 50}, {17,109}, {19, 58}, {22, 18}, {25, 60}, {27, 26},
			{32, 15}, {38, 24}, {41,38}, {53, 62}, {84,21}, {96, 19} };
		
		public static int [][]  JuzNumbers = new int [][] { {1, 1}, {2, 142}, {2, 253}, {3, 93}, {4, 24}, {4, 148}, {5, 82}, {5, 82}, {6,111}, {7, 88}, {8, 41}, 
			{9, 93}, {11, 6}, {12, 53}, {15, 1}, {17, 1}, {18, 75}, {21, 1}, {23, 1}, {25, 21}, {27,56}, {29, 46}, {33, 31}, {36, 28}, {39, 32}, 
			{41, 47}, {46, 1}, {51, 31}, {58, 1}, {67, 1}, {78, 1} };
		
		public static int[] SurahTitles = {R.drawable.sname_1, R.drawable.sname_2, R.drawable.sname_3, R.drawable.sname_4, R.drawable.sname_5, R.drawable.sname_6,
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
		
		public static final int		SURAH_DIALOG_DOWNLOAD_REQUEST = 0x01;
		public static final int		SURAH_DIALOG_DOWNLOAD_PROGRESS = 0x02;
		public static final int		SURAH_DIALOG_DOWNLOAD_NOCONNECTION = 0x03;
		public static final int		SURAH_DIALOG_NO_SDCARD = 0x04;
		
		public static final String  FOLDER_QURAN_ARABIC ="/sdcard/alQalam/arabic/";
		public static final String  FOLDER_QURAN_AUDIO ="/sdcard/alQalam/audio/";
		
		public static final String 	SETTINGS_FILE = "alQalam.set";
		public static final String	SETTINGS_TRANSLATION_OPTION_TITLE ="TransOption";
		public static final String	SETTINGS_RECITER_OPTION_TITLE ="ReciterOption";
		public static final String SETTINGS_UI_LOCALE_TITLE = "UiLocale";
		
		
		public static String[] LanguageDirectory = {"uzbek-cyr", "uzbek-lat", "russian"};
		public static String[] ReciterDirectory =  {"shatri", "gamidi"};
		
		public static final int 	AUDIO_NOT_INTIALIZED = 0x01;
		public static final int 	AUDIO_PLAYING = 0x02;
		public static final int 	AUDIO_PAUSED = 0x03;
		public static final int 	AUDIO_STOPPED = 0x04;
		
		public static final String		numberToString(int a) {
			String rTxt;
			
			if (a < 10 )
				rTxt = "00" + a; 
			else if (a < 100 )
				rTxt = "0" + a;
			else 
				rTxt = "" + a;
			
			return rTxt;
			
		}
		
		public static boolean[]			gSurahIsDownloaded = new boolean [numberOfSurahs];
		public static boolean[]			gSurahIsAudioDownloaded = new boolean [numberOfSurahs];
		
		// The database stuff
		// public static final String DATABASE_NAME = "quran.db";
		// public static final String AUTHORITY = "com.uzislam.alqalam.database.Provider";
		// public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DATABASE_NAME);

	}
 

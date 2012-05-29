package com.mrtaxi.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class localStorage{
	
	private static final String LOCALSP_INFO = "MrTaxi";
	
	public static final String APP_STATUS 	= "APP_STATUS";
	public static final String PHOTO 		= "PHOTO";
	public static final String NAME 		= "NAME";
	public static final String PHONE 		= "PHONE";
	public static final String PASSANGER_ID = "PASSANGER_ID";
	
	private SharedPreferences localSP;
	private Context masterActivityCTX;
	
	public localStorage(Context CTX){
		masterActivityCTX = CTX;		
		localSP = masterActivityCTX.getSharedPreferences(LOCALSP_INFO, 0);
	}
	
	//Create
	public void create(String Tag, String Data)
	{
	    Editor editor = localSP.edit();
	    editor.putString(Tag, Data);
	    editor.commit();
	}
	
	//Search
	public String search(String Tag)
	{
		return localSP.getString(Tag, null);
	}
	
	//Delete
	public void delete(String Tag)
	{
	    Editor editor = localSP.edit();
	    editor.remove(Tag);
	    editor.commit();
	}
	
	//Clear Register Data
	public void clearRegData()
	{
	    Editor editor = localSP.edit();
	    
	    editor.remove(PHOTO);
	    editor.remove(NAME);
	    editor.remove(PHONE);
	    editor.remove(PASSANGER_ID);
	    
	    editor.commit();
	}
	
	//Check Register Data
	public Boolean isExistRegData()
	{
		if(
				localSP.getString(NAME, null) == null 
				|| localSP.getString(PHONE, null) == null 
				|| localSP.getString(PHOTO, null) == null
				//|| localSP.getString(PASSANGER_ID, null) == null
		)
			return false;
		else
			return true;
	}
}
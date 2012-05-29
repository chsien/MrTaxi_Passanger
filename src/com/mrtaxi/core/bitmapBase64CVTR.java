package com.mrtaxi.core;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

public class bitmapBase64CVTR{
	
	public static String Bitmap2Base64(Bitmap bmp){
	
		byte[] buff;
		
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
	    buff = baos.toByteArray();
	    
		return Base64.encodeToString(buff, Base64.DEFAULT);
	
	}
	
	public static Bitmap Base642Bitmap(String base64String){
    
		byte[] buff = Base64.decode( base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(buff, 0, buff.length);
	
	}
	
}
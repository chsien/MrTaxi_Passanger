package com.mrtaxi.net.implement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;


import com.google.android.maps.GeoPoint;
import com.mrtaxi.net.core.asycTask_todo;
import com.mrtaxi.net.core.netMission;
import com.mrtaxi.passanger.R;

public class geoCoding extends netMission{
	
	//Google geocode service url imformation
	private final String serviceURL= "http://maps.googleapis.com/maps/api/geocode/";
	private final String getPartAddressInitial = "json?address=";
	private final String getPartLatLngInitial = "json?latlng=";
	private final String getPartEnd = "&sensor=false";
	
	private String resultJSON;
	
	public void request(String RawString, final Context masterActivityCTX, final asycTask_todo asycTask_todoList )  
	{	
		String getPartPre = "";
		getPartPre +=getPartAddressInitial;
		
		/* TODO:¸ÑªR»y·N */
		
		try {
			getPartPre+= URLEncoder.encode(RawString, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			Log.v("UTF-8 trans error", e1.getMessage(),e1);
		}
		
		getPartPre+=getPartEnd;
		final String getPart = getPartPre;
		
		final FrameLayout baseLayout = (FrameLayout)((Activity) masterActivityCTX).findViewById(R.id.baseLayout);	
		final FrameLayout freezeLayout = getFreezeLayout(masterActivityCTX);
		
		class asyncTransmit extends unimplementAsyncTransmit {
			
			@Override
			protected Void doInBackground(Void... arg0) {

				JSONObject json = null;
				
				try {
					json = new JSONObject(httpGetImplement(serviceURL, getPart));
				} catch (IOException e) {
					
					Log.v("GET error", e.getMessage(),e);
				} catch (JSONException e) {
					Log.v("JSON construct error", e.getMessage(),e);
				}
				
			    try {
			    	if(json.getString("status").equals("OK"))	    	
			    		resultJSON = ((json.getJSONArray("results")).getJSONObject(0)).toString();
			    	else
			    		resultJSON = null;

				} catch (JSONException e) {
					resultJSON = null;
					Log.v("JSON translate error", e.getMessage(),e);
				}
			    
				return null;
			}
			
			@Override
			protected void onPreExecute() {
				baseLayout.addView(freezeLayout);					
			}
			
			@Override
			protected void onPostExecute(Void v) {
				baseLayout.removeView(freezeLayout);
				asycTask_todoList.onPostExecute();					
			}
			
		}
		
		asyncTransmit task = new asyncTransmit();			
		task.execute();			
	}
	
	public void request(Double lat, Double lng, final Context masterActivityCTX, final asycTask_todo asycTask_todoList )  
	{	
		String getPartPre = "";
		getPartPre +=getPartLatLngInitial;
		
		getPartPre+=Double.toString(lat);
		getPartPre+=",";
		getPartPre+=Double.toString(lng);
		getPartPre+=getPartEnd;
		
		final String getPart = getPartPre;
		
		final FrameLayout baseLayout = (FrameLayout)((Activity) masterActivityCTX).findViewById(R.id.baseLayout);	
		final FrameLayout freezeLayout = getFreezeLayout(masterActivityCTX);
		
		class asyncTransmit extends unimplementAsyncTransmit {
			
			@Override
			protected Void doInBackground(Void... arg0) {

				JSONObject json = null;
				
				try {
					json = new JSONObject(httpGetImplement(serviceURL, getPart));
				} catch (IOException e) {
					
					Log.v("GET error", e.getMessage(),e);
				} catch (JSONException e) {
					Log.v("JSON construct error", e.getMessage(),e);
				}
				
			    try {
			    	if(json.getString("status").equals("OK"))	    	
			    		resultJSON = ((json.getJSONArray("results")).getJSONObject(0)).toString();
			    	else
			    		resultJSON = null;

				} catch (JSONException e) {
					resultJSON = null;
					Log.v("JSON translate error", e.getMessage(),e);
				}
			    
				return null;
			}
			
			@Override
			protected void onPreExecute() {
				baseLayout.addView(freezeLayout);					
			}
			
			@Override
			protected void onPostExecute(Void v) {
				baseLayout.removeView(freezeLayout);
				asycTask_todoList.onPostExecute();					
			}
			
		}
		
		asyncTransmit task = new asyncTransmit();			
		task.execute();			
	}
	public void set(String importJSON)
	{
		resultJSON = importJSON;
	}
	
	public String getResultJSON()
	{
		return resultJSON;
	}	
	public String getFormattedAddress()
	{
		if(resultJSON == null)
			return null;
		
		String formattedAddress;
		JSONObject json = null;
		
		try {
			json = new JSONObject(resultJSON);
			
			formattedAddress = json.getString("formatted_address");
		} catch (JSONException e) {
			formattedAddress = null;
			Log.v("JSON construct error", e.getMessage(),e);
		}		
		return formattedAddress;

	}	
	public GeoPoint getGeoPoint()
	{
		if(resultJSON == null)
			return null;
		
		GeoPoint geoPoint;
		double lat;
		double lon;			
		JSONObject json = null;
		int latE6, lonE6;
		try {
			json = new JSONObject(resultJSON);
			lat = ((json.getJSONObject("geometry")).getJSONObject("location")).getDouble("lat");
			lon = ((json.getJSONObject("geometry")).getJSONObject("location")).getDouble("lng");
			
			latE6 = (int) (lat * 1e6);
			lonE6 = (int) (lon * 1e6);
			
			geoPoint = new GeoPoint(latE6, lonE6);
		} catch (JSONException e) {
			geoPoint = null;
			Log.v("JSON construct error", e.getMessage(),e);
		}

		return geoPoint;
	}	
}
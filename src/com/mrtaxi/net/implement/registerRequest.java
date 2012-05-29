package com.mrtaxi.net.implement;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.mrtaxi.net.core.asycTask_todo;
import com.mrtaxi.net.core.netMission;
import com.mrtaxi.passanger.R;

public class registerRequest extends netMission{
	
	private final String serviceURL = "";
	private final String requestType = "register";
	
	private String resultJSON;
	
	public void request(String name, String phone, String photo, final Context masterActivityCTX, final asycTask_todo asycTask_todoList )  
	{
		JSONObject json = new JSONObject();
		
		final FrameLayout baseLayout = (FrameLayout)((Activity) masterActivityCTX).findViewById(R.id.baseLayout);	
		final FrameLayout freezeLayout = getFreezeLayout(masterActivityCTX);
		
		try {
			json.put("requestType", requestType);
			json.put("name", name);
			json.put("phone", phone);
			json.put("photo", photo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v("JSON construct error", e.getMessage(),e);
		}
		final String JSONData = json.toString();
		
		class asyncTransmit extends unimplementAsyncTransmit {
			
			@Override
			protected Void doInBackground(Void... arg0) {
				
				try {
					resultJSON = httpsPostImplement(serviceURL, JSONData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.v("https POST faild", e.getMessage(),e);
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
	public String getResultJSON()
	{
		return resultJSON;
	}
	public String getID()
	{
		if(resultJSON == null)
			return null;
		
		String ID;
		JSONObject json = null;
		
		try {
			json = new JSONObject(resultJSON);
			
			ID = json.getString("formatted_address");
		} catch (JSONException e) {
			ID = null;
			Log.v("JSON construct error", e.getMessage(),e);
		}		
		return ID;
	}
}
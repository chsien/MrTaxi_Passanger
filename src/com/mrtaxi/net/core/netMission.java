package com.mrtaxi.net.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

//JSON library
/*
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
*/

public class netMission{
	
	// AsyncTask Template
	protected class unimplementAsyncTransmit extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// should be override again
			return null;
		}
		@Override
		
		protected void onPreExecute() {
			// should be override again				
		}
		
		@Override
		protected void onPostExecute(Void v) {
			// should be override again				
		}
		
	}
	
	//example of dynamic Layout & using asycTask
	/*
	public void doSomething( YOUR DATA , final Context masterActivityCTX, final asycTask_todo asycTask_todoList )  
	{		
		
		//link master activity which called this method
		final FrameLayout baseLayout = (FrameLayout)((Activity) masterActivityCTX).findViewById(R.id.baseLayout);
		
		//generate a new freezelayout in
		final FrameLayout freezeLayout = getFreezeLayout(masterActivityCTX);
		
		class asyncTransmit extends unimplementAsyncTransmit {
			
			@Override
			protected Void doInBackground(Void... arg0) {
				
				PROCESS YOUR DATA IN BACKGROUND
				
				return null;
			}
			
			@Override
			protected void onPreExecute() {
			
				//before initializing Background Task, lock the screen
				baseLayout.addView(freezeLayout);
									
			}
			
			@Override
			protected void onPostExecute(Void v) {
				
				//after Background Task complete, free the screen
				baseLayout.removeView(freezeLayout);
				
				//after Background Task complete, Do something which assigned by master activity
				asycTask_todoList.onPostExecute();
			
				//do garbege collection to avoid leak
				System.gc();
			}
			
		}
		
		asyncTransmit newTask = new asyncTransmit();			
		newTask.execute();			
	}
	*/


	protected static String httpGetImplement(String serviceURL, String getPart) throws IOException {
    	
    	String dataBuff = "";
        String getURL = serviceURL + getPart;
        URL getUrl = new URL(getURL);

        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3"); 
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String lines;
        while ((lines = reader.readLine()) != null) {
        	dataBuff = dataBuff+lines;
        }
        reader.close();
        
        connection.disconnect();
        return dataBuff;
    }

    protected static String httpsPostImplement(String serviceURL, String postData) throws IOException {
    	
    	String dataBuff = "";
    	
    	URL postURL = new URL(serviceURL);
        HttpsURLConnection connection = (HttpsURLConnection) postURL.openConnection();
        connection.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3"); 
        connection.connect();
        
        DataOutputStream writer = new DataOutputStream( connection.getOutputStream() ); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));        
        writer.writeBytes(postData); 
        
        String lines;
        while ((lines = reader.readLine()) != null) {
        	dataBuff = dataBuff+lines;
        }
        reader.close();
        
        connection.disconnect();
        return dataBuff;
    }
    
    final static int freezeLayoutBgColor = Color.argb(200, 20, 20, 20);
    protected static FrameLayout getFreezeLayout(Context masterActivityCTX)
    {			
		final FrameLayout freezeLayout = new FrameLayout((Activity) masterActivityCTX);
		freezeLayout.setBackgroundColor(freezeLayoutBgColor);
		freezeLayout.setClickable(true);
		
		final ProgressBar freezeProgressBar = 
				new ProgressBar((Activity) masterActivityCTX, null, android.R.attr.progressBarStyleLarge);
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		
		freezeProgressBar.setLayoutParams(lp);
		freezeLayout.addView(freezeProgressBar);
		
		return freezeLayout;
    }
}
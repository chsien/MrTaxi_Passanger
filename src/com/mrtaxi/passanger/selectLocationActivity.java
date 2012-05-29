package com.mrtaxi.passanger;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mrtaxi.core.HelloItemizedOverlay;
import com.mrtaxi.net.core.asycTask_todo;
import com.mrtaxi.net.implement.geoCoding;

public class selectLocationActivity extends MapActivity{

	/*Intent code*/
	private static int RECOGNIZE_SPEECH = 	0xA;
	
	/*Layout component*/
	private TextView  textView_location;
	private EditText  editText_location;
	private Button 	  button_request;
		
	/*assignTerminal Layout component*/
	private ImageButton imageButton_voiceDetect;
	private EditText 	editText_preLocation;
	
	/*google geocoding processing base on netMission*/
	private geoCoding geoCodingHandler;

	private String typeOfLocation;
	
	/*For Map*/
	private MapController 	mapController;
	private MapView 		mapView;
	private LocationManager locationManager;
	private Location 		currentLocation;
	private GeoPoint 		locationPoint;
	
	List<Overlay> 			mapOverlays;
	Drawable 				mapDrawable;
	OverlayItem 			overlayitem;
	HelloItemizedOverlay 	itemizedOverlay;
	
	Dialog dialog_assignLocation;
	android.content.DialogInterface.OnClickListener dialogOnClickListener_assignLocation = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int response) {
			switch(response)
			{
				case Dialog.BUTTON_POSITIVE:
				{
					geoCodingHandler.request(editText_preLocation.getText().toString(), selectLocationActivity.this, new asycTask_todo(){
	        			
	        			@Override
	        			public void onPostExecute()
	        			{
	        				locate();
		                    editText_preLocation.setText("");	                    
	        			}
	        			
	        		});
					break;
				}
				default:
				{
					editText_preLocation.setText("");
					break;
				}
			}
		}			
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	// full screen
    	getWindow().setFlags(
    	    WindowManager.LayoutParams.FLAG_FULLSCREEN,
    	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	// keep screen on
    	getWindow().setFlags(
    			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
    			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectlocation);
        
        //Initial geoCoding
        geoCodingHandler = new geoCoding();
        
        //link layout component
        textView_location = (TextView)findViewById(R.id.textView_location);
        editText_location = (EditText)findViewById(R.id.editText_location);
        button_request =	(Button)findViewById(R.id.button_request);
        
        //modify editText_location attribute
        editText_location.setInputType(InputType.TYPE_NULL);
        editText_location.setLongClickable(false);
        editText_location.setClickable(true);

        
        //construct dialog_assignLocation
        LayoutInflater inflater = getLayoutInflater();
        View view_assignLocation = inflater.inflate(R.layout.assignlocation,null);
        
        dialog_assignLocation = new AlertDialog.Builder(this)
        .setTitle("請輸入地點")
        .setView(view_assignLocation)
        .setPositiveButton("確定", dialogOnClickListener_assignLocation)
        .setNegativeButton("取消", dialogOnClickListener_assignLocation).create();
              
        editText_preLocation = (EditText)view_assignLocation.findViewById(R.id.editText_preLocation);
        imageButton_voiceDetect = (ImageButton)view_assignLocation.findViewById(R.id.imageButton_voiceDetect);
        imageButton_voiceDetect.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	dialog_assignLocation.dismiss();
	            	//geoImformation.request("台北車站");
	            	
	            	/*
	            	Log.v("check-json", geoImformation.getResultJSON());
	            	Log.v("check-formattedAddress", geoImformation.getFormattedAddress());
	            	Log.v("check-lat", Integer.toString(((geoImformation.getGeoPoint()).getLatitudeE6())));
	            	Log.v("check-lon", Integer.toString(((geoImformation.getGeoPoint()).getLongitudeE6())));
	            	*/
	            	
	                // 檢查是否支援語音辨識
	                PackageManager pm = getPackageManager();
	                List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
	                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	                if (activities.size() == 0) {
	                    Log.v("RECOGNIZE_SPEECH error", "Oops...ACTION_RECOGNIZE_SPEECH not present");
	                    return;
	                }
	                
	                // 啟動語音辨識
	                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.TAIWAN.toString());
	                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說出地點..");
	                selectLocationActivity.this.startActivityForResult(intent, RECOGNIZE_SPEECH);
	            }	            
	        }
	    );
        
        //link OnClickListener
        editText_location.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	dialog_assignLocation.show();
		        }
		    }
		);
        button_request.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	if(geoCodingHandler.getResultJSON() == null)
		        	{
		        		Dialog emptyTip = new AlertDialog.Builder(selectLocationActivity.this)
		                .setTitle("尚無輸入地點")
		                .setPositiveButton("確定", null).create();		        		
		        		emptyTip.show();
		        		
		        		return;
		        	}
		        	
		        	Bundle bData = new Bundle();
		        	bData.putString("typeOfLocation", typeOfLocation);
		        	bData.putString("location_address", geoCodingHandler.getFormattedAddress());
		        	bData.putString("location_json", geoCodingHandler.getResultJSON());
		        	setResult(RESULT_OK, selectLocationActivity.this.getIntent().putExtras(bData));
		        	finish();
		        }
		    }
		);
        
        //initial mapView
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true); 
        mapView.setSatellite(false); 
        //mapView.setStreetView(true);
        mapController = mapView.getController();         
        mapController.setZoom(18);
        

        
        //check type of request (typeOfLocation & create or modify)
        Bundle bData = this.getIntent().getExtras();
        
        typeOfLocation = bData.getString("typeOfLocation");
        
        if(typeOfLocation.equals("beginLocation"))
        {
        	textView_location.setText("出發地");
        	button_request.setText("確定出發地");
        	dialog_assignLocation.setTitle("請輸入出發地");
        	mapDrawable = this.getResources().getDrawable(R.drawable.default_marker);
        }
        else if(typeOfLocation.equals("terminalLocation"))
        {	
        	textView_location.setText("目的地");
        	button_request.setText("確定目的地");
        	dialog_assignLocation.setTitle("請輸入目的地");
        	mapDrawable = this.getResources().getDrawable(R.drawable.taxi_marker);
        }
        
        if(bData.getString("locationJSON") == null)
        {
	        if(typeOfLocation.equals("beginLocation"))
	        {
	        	boolean isGPSEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );

	        	if(!isGPSEnabled)
	        	{

                        Intent gpsIntent = new Intent();
                        gpsIntent.setClassName("com.android.settings",
                                        "com.android.settings.widget.SettingsAppWidgetProvider");
                        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
                        gpsIntent.setData(Uri.parse("custom:3"));
                        try {
                                PendingIntent.getBroadcast(selectLocationActivity.this, 0, gpsIntent, 0).send();
                        }
                        catch (CanceledException e) {
                                e.printStackTrace();
                        }
                        
                        //Another method need system uid
    	        		//Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, true);
    	        		//android:sharedUserId="android.uid.system"
	        	}

	        	
        		getLastLocation();
        		
        		if(currentLocation != null)
        		{
					geoCodingHandler.request(currentLocation.getLatitude(), currentLocation.getLongitude(), selectLocationActivity.this, new asycTask_todo(){
	        			
	        			@Override
	        			public void onPostExecute()
	        			{
	        				locate();         
	        			}
	        			
	        		});
        		}
        		else
        			dialog_assignLocation.show();
	        }
	        else if(typeOfLocation.equals("terminalLocation"))
	        {
	        	dialog_assignLocation.show();
	        }
        }
        else
        {
        	geoCodingHandler.set(bData.getString("locationJSON"));
        	
        	locate();
        }
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
        	if(requestCode == RECOGNIZE_SPEECH)
        	{
        		List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        		Log.v("sr-result", result.toString());
        		            	
        		geoCodingHandler.request(result.get(0), selectLocationActivity.this, new asycTask_todo(){
        			
        			@Override
        			public void onPostExecute()
        			{
        				locate();                 
        			}
        			
        		});
        	}
        }
    }
    
    private void locate()
    {
		locationPoint = geoCodingHandler.getGeoPoint();
		if(locationPoint == null)
		{
			Toast.makeText(this, "無法找到輸入位置", Toast.LENGTH_LONG).show(); 
			return;
		}
		
    	animateToLocation();
        editText_location.setText(geoCodingHandler.getFormattedAddress());
        editText_location.setSelection((editText_location.getText()).toString().length());
        
        if(overlayitem!=null)
        	itemizedOverlay.removeOverlay(overlayitem);
        else
        {
        	//initial map overlay
            mapOverlays = mapView.getOverlays();        
            itemizedOverlay = new HelloItemizedOverlay(mapDrawable);  
        	mapOverlays.add(itemizedOverlay);
        }
        
        overlayitem = new OverlayItem(locationPoint, "", "");
        itemizedOverlay.addOverlay(overlayitem);
        
    }
    
	public void getLastLocation(){ 
	    String provider = getBestProvider(); 
	    currentLocation = locationManager.getLastKnownLocation(provider); 
	    if(currentLocation != null){ 
	        setCurrentLocation(currentLocation); 
	    } 
	    else
	    { 
	        Toast.makeText(this, "目前所處環境尚無法定位", Toast.LENGTH_LONG).show(); 
	    } 
	}	  
	public void animateToLocation(){ 
	    if(locationPoint!=null){ 
	        mapController.animateTo(locationPoint); 
	    } 
	}
	public String getBestProvider(){ 
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria(); 
	    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); 
	    criteria.setAccuracy(Criteria.NO_REQUIREMENT); 
	    String bestProvider = locationManager.getBestProvider(criteria, true); 
	    return bestProvider; 
	}	  
	public void setCurrentLocation(Location location){ 
	    int currLatitude = (int) (location.getLatitude()*1E6); 
	    int currLongitude = (int) (location.getLongitude()*1E6); 
	    locationPoint = new GeoPoint(currLatitude,currLongitude); 
	  
	    currentLocation = new Location(""); 
	    currentLocation.setLatitude(locationPoint.getLatitudeE6() / 1e6); 
	    currentLocation.setLongitude(locationPoint.getLongitudeE6() / 1e6);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
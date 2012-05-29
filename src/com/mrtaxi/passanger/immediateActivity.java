package com.mrtaxi.passanger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.mrtaxi.net.core.asycTask_todo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;



public class immediateActivity extends Activity {

	/*Intent code*/
	private static int SELECT_LOCATION = 	0xA;
	private static int TAKE_PHOTO = 	0xB;
	
	/*Layout component*/
	private ImageView imageView_photo;
	private EditText  editText_beginLocation;
	private EditText  editText_terminalLocation;
	private Button    button_request;
	
	private void editText_beginLocation_clickTodo()
	{
    	Bundle bData = new Bundle() ;
    	bData.putString("typeOfLocation", "beginLocation");
    	bData.putString("locationJSON", string_beginLocation_json);
    	
    	Intent intent = new Intent();
        intent.setClass(immediateActivity.this, selectLocationActivity.class);
        intent.putExtras(bData);
        startActivityForResult(intent, SELECT_LOCATION);		
	}
	private void editText_terminalLocation_clickTodo()
	{
    	Bundle bData = new Bundle() ;
    	bData.putString("typeOfLocation", "terminalLocation");
    	bData.putString("locationJSON", string_terminalLocation_json);

    	Intent intent = new Intent();
        intent.setClass(immediateActivity.this, selectLocationActivity.class);
        intent.putExtras(bData);
        startActivityForResult(intent, SELECT_LOCATION);
	}
	
	/**/
	private String string_beginLocation_json;
	private String string_terminalLocation_json;
	private int automatic_check_location;

	private Bitmap bitmap_photo;
	private final int bitmap_photo_width = 320;
	private final int bitmap_photo_height = 540;
	private File tempFile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.immediate);
  
        /*set temp pic locate*/
        tempFile=new File("/sdcard/temp.jpg");
        
        automatic_check_location = 0;
        
        /*Layout linking*/
        imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
        editText_beginLocation 	= (EditText)  findViewById(R.id.editText_beginLocation);
        editText_terminalLocation 	= (EditText)  findViewById(R.id.editText_terminalLocation);
        button_request = (Button)findViewById(R.id.button_request);

        imageView_photo.setClickable(true);        
        imageView_photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
	    		Intent intent =  new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    		
	    		/*Use front camera*/
	    		intent.putExtra("camerasensortype", 2);
	    		
	    		intent.putExtra("crop", "true");
	    		intent.putExtra("aspectX", bitmap_photo_width);
	    		intent.putExtra("aspectY", bitmap_photo_height);
	    		intent.putExtra("outputX", bitmap_photo_width);
	    		intent.putExtra("outputY", bitmap_photo_height);
	    		intent.putExtra("scale", true);
	    		intent.putExtra("output", Uri.fromFile(tempFile));
	            intent.putExtra("outputFormat", "JPEG");
	            
	            startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        
        editText_beginLocation.setInputType(InputType.TYPE_NULL);
        editText_terminalLocation.setInputType(InputType.TYPE_NULL);
        
        editText_beginLocation.setLongClickable(false);
        editText_terminalLocation.setLongClickable(false);
        
        editText_beginLocation.setClickable(true);
        editText_terminalLocation.setClickable(true);
        
        editText_beginLocation.setOnClickListener(new OnClickListener() {        	
            public void onClick(View v) {
            	editText_beginLocation_clickTodo();
            }
        });
        editText_terminalLocation.setOnClickListener(new OnClickListener() {        	
            public void onClick(View v) {
            	editText_terminalLocation_clickTodo();
            }
        });
    }
	
	@Override
    public void onResume() {
		super.onResume();
		
		if(automatic_check_location < 2)
		{
			if(string_beginLocation_json==null)
			{				
				if(automatic_check_location > 0)
				{
					automatic_check_location++;
					return;
				}
				editText_beginLocation_clickTodo();
			}
			else if(string_terminalLocation_json==null)
				editText_terminalLocation_clickTodo();
			automatic_check_location++;
		}
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
        	if( requestCode == SELECT_LOCATION )
        	{
        		Bundle bData = data.getExtras();
        		
        		if(bData.getString("typeOfLocation").equals("beginLocation"))
        		{
        			editText_beginLocation.setText(bData.getString("location_address"));
        			editText_beginLocation.setSelection((editText_beginLocation.getText()).toString().length());
        			
        			string_beginLocation_json = bData.getString("location_json");
        		}
        		else if(bData.getString("typeOfLocation").equals("terminalLocation"))
        		{
        			editText_terminalLocation.setText(bData.getString("location_address"));
        			editText_terminalLocation.setSelection((editText_terminalLocation.getText()).toString().length());
        			
        			string_terminalLocation_json = bData.getString("location_json");       			
        		}
        	}
            if ( requestCode == TAKE_PHOTO ) {
            	
            	/*avoid OOM*/
            	if(bitmap_photo!=null)
                	bitmap_photo.recycle();
  	
            	Uri uri = Uri.fromFile(tempFile);            	
            	ContentResolver cr = this.getContentResolver();
            	
            	try {
					bitmap_photo = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					Log.v("error", e.toString());
				}

                /*Delete temp pic*/
                tempFile.delete();
                
                imageView_photo.setImageBitmap(bitmap_photo);
           }
        }
    }
    
}
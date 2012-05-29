package com.mrtaxi.passanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class reservationActivity extends Activity {
	/*Intent code*/
	private static int SELECT_LOCATION = 	0xA;
	
	/*Layout component*/
	private EditText  editText_beginLocation;
	private EditText  editText_terminalLocation;
	private Button    button_request;
	
	private void editText_beginLocation_clickTodo()
	{
    	Bundle bData = new Bundle() ;
    	bData.putString("typeOfLocation", "beginLocation");
    	bData.putString("locationJSON", string_beginLocation_json);
    	
    	Intent intent = new Intent();
        intent.setClass(reservationActivity.this, selectLocationActivity.class);
        intent.putExtras(bData);
        startActivityForResult(intent, SELECT_LOCATION);		
	}
	private void editText_terminalLocation_clickTodo()
	{
    	Bundle bData = new Bundle() ;
    	bData.putString("typeOfLocation", "terminalLocation");
    	bData.putString("locationJSON", string_terminalLocation_json);

    	Intent intent = new Intent();
        intent.setClass(reservationActivity.this, selectLocationActivity.class);
        intent.putExtras(bData);
        startActivityForResult(intent, SELECT_LOCATION);
	}
	
	/**/
	private String string_beginLocation_json;
	private String string_terminalLocation_json;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        
        /*Layout linking*/        
        editText_beginLocation 	= (EditText)  findViewById(R.id.editText_beginLocation);
        editText_terminalLocation 	= (EditText)  findViewById(R.id.editText_terminalLocation);
        button_request = (Button)findViewById(R.id.button_request);
        
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
		
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
        	if(requestCode == SELECT_LOCATION)
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
        }
    }
}
package com.mrtaxi.passanger;

import com.mrtaxi.core.localStorage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mainActivity extends Activity {
	
    /** Called when the activity is first created. */
	private localStorage localStorageHandler;
	
	/*Layout component*/
	private Button    button_immediate;
	private Button    button_reservation;
	private View	  view_standaloneTip;
	private TextView  textView_standaloneTip;
	
	Dialog dialog_clearRegData;
	android.content.DialogInterface.OnClickListener dialogOnClickListener_clearRegData = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int response) {
			switch(response)
			{
				case Dialog.BUTTON_POSITIVE:
				{
					localStorageHandler.clearRegData();

			    	Intent intent = new Intent();
			        intent.setClass(mainActivity.this, registerActivity.class);
			        startActivity(intent);				    
					break;
				}
			}
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*Layout linking*/
        button_immediate = (Button)findViewById(R.id.button_immediate);
        button_reservation = (Button)findViewById(R.id.button_reservation);
        view_standaloneTip = (View)findViewById(R.id.view_standaloneTip);
        textView_standaloneTip = (TextView)findViewById(R.id.textView_standaloneTip);

        button_immediate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	    	Intent intent = new Intent();
    	        intent.setClass(mainActivity.this, immediateActivity.class);
    	        startActivity(intent);
            }
        });
        button_reservation.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	    	Intent intent = new Intent();
    	        intent.setClass(mainActivity.this, reservationActivity.class);
    	        startActivity(intent);
            }
        });
        
        dialog_clearRegData = new AlertDialog.Builder(this)
        .setTitle("是否清除資料")
        .setPositiveButton("確定", dialogOnClickListener_clearRegData)
        .setNegativeButton("取消", dialogOnClickListener_clearRegData).create();
		
        localStorageHandler = new localStorage( this );
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo!=null&&netInfo.isConnected())
		{
			textView_standaloneTip.setVisibility(View.INVISIBLE);
			view_standaloneTip.setVisibility(View.INVISIBLE);

	        if(!localStorageHandler.isExistRegData())
	        {
	        	Toast.makeText(this, "使用前請先註冊", Toast.LENGTH_LONG).show(); 
		    	Intent intent = new Intent();
		        intent.setClass(this, registerActivity.class);
		        startActivity(intent);
	        }
		}
		else
		{
			textView_standaloneTip.setVisibility(View.VISIBLE);
			view_standaloneTip.setVisibility(View.VISIBLE);
			
		}
		
    }
    
    public void linkMenu(Menu menu)
    {
    	if(view_standaloneTip.getVisibility() == View.INVISIBLE)
    	{
    		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "修改資料").setIcon( android.R.drawable.ic_menu_edit);
    		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "清除資料").setIcon( android.R.drawable.ic_menu_delete );
    	}
    	menu.add(Menu.NONE, Menu.FIRST + 3, 3, "關於").setIcon( android.R.drawable.ic_menu_info_details );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	linkMenu(menu);    	
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case Menu.FIRST + 1:
			{
		    	Intent intent = new Intent();
		        intent.setClass(mainActivity.this, registerActivity.class);
		        startActivity(intent);
				break;
			}
    		case Menu.FIRST + 2:
    		{
    			dialog_clearRegData.show();
    			break;
    		}
    		case Menu.FIRST + 3:
    		{
    			break;
    		}
    		default:
    			break;
    	}
    	return true;
    }
}
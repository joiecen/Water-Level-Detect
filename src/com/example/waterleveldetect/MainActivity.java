package com.example.waterleveldetect;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
		
	private int START_DETECT_WATERLEVEL =1 ;
	private TextView wateroneTextView;
	private View wateroneView;
	private TextView watertwoTextView;
	private View watertwoView;
    private Button startdetectButton;
    private int waterlevel;
	
	private Messenger toserviceMessenger;
	private Messenger fromserviceMessenger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wateroneTextView = (TextView)findViewById(R.id.textView2);
		wateroneView = (View)findViewById(R.id.view1);
		watertwoTextView = (TextView)findViewById(R.id.textView3);
		watertwoView = (View)findViewById(R.id.view2);
		wateroneView.setBackgroundColor(Color.parseColor("#b487CEFA"));
		watertwoView.setBackgroundColor(Color.parseColor("#b487CEFA"));
		wateroneView.setLayoutParams(new LinearLayout.LayoutParams(150, 100));  
		watertwoView.setLayoutParams(new LinearLayout.LayoutParams(350, 100));
		LinearLayout.LayoutParams wonelp = new LinearLayout.LayoutParams(wateroneView.getLayoutParams());
		wonelp.setMargins(50, 0, 0, 0);
		wateroneView.setLayoutParams(wonelp);
		LinearLayout.LayoutParams wtwolp = new LinearLayout.LayoutParams(watertwoView.getLayoutParams());
		wtwolp.setMargins(50, 0, 0, 0);
		watertwoView.setLayoutParams(wtwolp);
		
		startdetectButton = (Button)findViewById(R.id.button1);
		startdetectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				START_DETECT_WATERLEVEL = 3;
				Intent intent = new Intent(MainActivity.this,BLEService.class);
				bindService(intent, conn, Context.BIND_AUTO_CREATE);		
				fromserviceMessenger = new Messenger(mHandler);	
			}
		});
		
			
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch (msg.what) {
			case BLEService.MSG_SET_VALUE:
				//更新UI操作
				//testTextView.setText("receive from service"+msg.arg1);
				if(msg.what == 6){
					waterlevel = 100;
				}
				//分析数据到具体的ml
				wateroneView.setBackgroundColor(Color.parseColor("#b487CEFA"));
				wateroneView.setLayoutParams(new LinearLayout.LayoutParams(10*msg.arg1, 100));         
				LinearLayout.LayoutParams wonelp = new LinearLayout.LayoutParams(wateroneView.getLayoutParams());
				wonelp.setMargins(50, 0, 0, 0);
				wateroneView.setLayoutParams(wonelp);
				//第二个进度条
				watertwoView.setBackgroundColor(Color.parseColor("#b487CEFA"));
				watertwoView.setLayoutParams(new LinearLayout.LayoutParams(10*msg.arg1, 100));
				LinearLayout.LayoutParams wtwolp = new LinearLayout.LayoutParams(watertwoView.getLayoutParams());
				wtwolp.setMargins(50, 0, 0, 0);
				watertwoView.setLayoutParams(wtwolp);
				break;

			default:
				break;
			}
		}		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private ServiceConnection conn = new ServiceConnection() {		
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			toserviceMessenger = new Messenger(service);	
			fromserviceMessenger = new Messenger(mHandler);
			Message message = Message.obtain(null, START_DETECT_WATERLEVEL);
	        message.replyTo = fromserviceMessenger;
	        try {
	            toserviceMessenger.send(message);
	            Log.i("toservicemessenger", " "+message);
	        } catch (RemoteException e) {
	            e.printStackTrace();
	        }
		}
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			toserviceMessenger = null;
		}
		
	};
	
	
}

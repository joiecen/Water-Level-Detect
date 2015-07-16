package com.example.waterleveldetect;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OneLIDActivity extends Activity {
	
	private int START_DETECT_WATERLEVEL =1 ;
    private Button startdetectButton;
    private int waterlevel;
    private String lidnum;
    private WaterView myWaterView;
    private TextView lidnameteTextView;
    private TextView nowwaterlevelnum;
    private TextView nowwaterrationum;
    private TextView nowwaterspeednum;
	
	private Messenger toserviceMessenger;
	private Messenger fromserviceMessenger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_lid);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("LID详情");
		actionBar.setDisplayHomeAsUpEnabled(true);	
		actionBar.setDisplayShowHomeEnabled(false);
		
		Intent getIntent = getIntent();
		lidnum = getIntent.getStringExtra("lidnum");
		lidnameteTextView = (TextView)findViewById(R.id.lidname);
		lidnameteTextView.setText("LID"+lidnum);
		nowwaterlevelnum = (TextView)findViewById(R.id.nowwaterlevelnum);
		nowwaterrationum = (TextView)findViewById(R.id.nowwaterrationum);
		nowwaterspeednum = (TextView)findViewById(R.id.nowwaterspeednum);
		myWaterView = (WaterView)findViewById(R.id.waterview1);
		myWaterView.setviewparam(90);

		startdetectButton = (Button)findViewById(R.id.button1);
		startdetectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				START_DETECT_WATERLEVEL = 3;
				Intent intent = new Intent(OneLIDActivity.this,BLEService.class);
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
				waterlevel = msg.arg1;
				System.out.println("message.arg2 = "+msg.arg2);
				myWaterView.setviewparam(3*waterlevel);
				myWaterView.invalidate();
				nowwaterlevelnum.setText(waterlevel+"m³");
				nowwaterrationum.setText(msg.arg1/2+"%");		
				nowwaterspeednum.setText("25m³/s");
				break;

			default:
				break;
			}
		}		
	};

	
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_lid, menu);
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
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}

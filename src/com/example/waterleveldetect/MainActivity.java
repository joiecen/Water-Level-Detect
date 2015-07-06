package com.example.waterleveldetect;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private ImageView lid1;
	private ImageView lid2;
	private ImageView lid3;
	private Button totalbutton;
	private final static String SH = "上海";
	private final static String BJ = "北京";
	private final static String XM = "厦门";
	private String lidnum;
	/////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("城市LID系统-移动端");
		lid1 = (ImageView)findViewById(R.id.lid1);
		lid2 = (ImageView)findViewById(R.id.lid2);
		lid3 = (ImageView)findViewById(R.id.lid3);	
		lid1.setOnClickListener(onClickListener);
		lid2.setOnClickListener(onClickListener);
		lid3.setOnClickListener(onClickListener);
		totalbutton = (Button)findViewById(R.id.button1);
		totalbutton.setOnClickListener(onClickListener);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		String title = item.getTitle().toString();
		if(title.equals(SH)){
			findViewById(R.id.mainlayout).setBackground(getResources().getDrawable(R.drawable.shanghaimap));
			lid1.setX(110);
			lid1.setY(380);
			lid2.setX(740);
			lid2.setY(300);
			lid3.setX(500);
			lid3.setY(850);
		}
		if(title.equals(BJ)){
			findViewById(R.id.mainlayout).setBackground(getResources().getDrawable(R.drawable.beijingmap));
			lid1.setX(110);
			lid1.setY(560);
			lid2.setX(650);
			lid2.setY(615);
			lid3.setX(260);
			lid3.setY(1090);
		}
		if(title.equals(XM)){
			findViewById(R.id.mainlayout).setBackground(getResources().getDrawable(R.drawable.xiamenmap));
			lid1.setX(350);
			lid1.setY(450);
			lid2.setX(750);
			lid2.setY(240);
			lid3.setX(450);
			lid3.setY(1200);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private View.OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.lid1:
				Intent intent1 = new Intent(MainActivity.this,OneLIDActivity.class);
				lidnum = "1";
				intent1.putExtra("lidnum", lidnum);
				startActivity(intent1);
				break;
			case R.id.lid2:
				Intent intent2 = new Intent(MainActivity.this,OneLIDActivity.class);
				lidnum = "2";
				intent2.putExtra("lidnum", lidnum);
				startActivity(intent2);
				break;
			case R.id.lid3:
				Intent intent3 = new Intent(MainActivity.this,OneLIDActivity.class);
				lidnum = "3";
				intent3.putExtra("lidnum", lidnum);
				startActivity(intent3);
				break;
			case R.id.button1:
				Intent totalintent = new Intent(MainActivity.this,TotalLIDActivity.class);
				startActivity(totalintent);
			default:
				break;
			}
			
		}
	};	
}

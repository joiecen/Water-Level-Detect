package com.example.waterleveldetect;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

@SuppressLint("NewApi")
public class BLEService extends Service{

	static final int MSG_SET_VALUE = 3;
    private final static String TAG = BLEService.class.getSimpleName();
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	
	private Messenger cMessenger;
	private Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SET_VALUE:
				Message message = Message.obtain(null,BLEService.MSG_SET_VALUE);
				message.arg1 = 0;                           
				cMessenger = msg.replyTo;
				
				//String mac = "666";                     //ble  macµÿ÷∑
				//BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
				//bluetoothDevice.connectGatt(getApplicationContext(), false, new WaterBleCallBack(mac));
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};	
    private Messenger servicemessenger = new Messenger(mhandler);
	
	@Override
    public void onCreate() {
		super.onCreate();
		mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int i = 5;
				int j = 100;
				try {
					while(i<100 || j>0){
						Message message = Message.obtain(null,BLEService.MSG_SET_VALUE);
						Thread.sleep(1000);
						if(i<100){
							message.arg1 = i++;
						}
						if(j>0){
							message.arg2 = j--;
						}
						if(!(cMessenger == null))
						cMessenger.send(message);
					}					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();		
		return servicemessenger.getBinder();
	}
	
	public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        } 
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        } 
        return true;
    }

}

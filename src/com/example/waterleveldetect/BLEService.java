package com.example.waterleveldetect;

import java.util.UUID;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BLEService extends Service{

	static final int MSG_SET_VALUE = 3;
    private final static String TAG = BLEService.class.getSimpleName();
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private static final UUID WL_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	private static final UUID WL_CHARACTERISTIC_UUID = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
	private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	private String mac = "78:A5:04:50:32:8C";
	private byte[] characteristicgetvalue;
	private int waterlevel = 100;

	
	private Messenger cMessenger;
	private Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SET_VALUE:
				Message message = Message.obtain(null,BLEService.MSG_SET_VALUE);
				message.arg1 = 0;                           
				cMessenger = msg.replyTo;
				
				if(BluetoothAdapter.checkBluetoothAddress(mac)){
					BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
					bluetoothDevice.connectGatt(getApplicationContext(), false, mGattCallback);
				}else {
					Toast.makeText(getApplicationContext(), "蓝牙不可用", Toast.LENGTH_SHORT).show();
				}				
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
						if(waterlevel == 100){
							System.out.println("waterlevel still 100");
						}else {
							message.arg1 = waterlevel;
						}
						
//						if(i<100){
//							message.arg1 = i++;
//						}
//						if(j>0){
//							message.arg2 = j--;
//						}
						if(!(cMessenger == null)){
							cMessenger.send(message);
						}
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
	
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
			if(status == BluetoothGatt.GATT_SUCCESS){
				BluetoothGattService service = gatt.getService(WL_SERVICE_UUID);
				BluetoothGattCharacteristic blegattchara = service.getCharacteristic(WL_CHARACTERISTIC_UUID);
				mBluetoothGatt.readCharacteristic(blegattchara);
				//要先完成writedescriptor 等待一段时间 再read？
				mBluetoothGatt.setCharacteristicNotification(blegattchara, true);
				BluetoothGattDescriptor descriptor = blegattchara.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(descriptor);
			}else{
				gatt.disconnect();
				gatt.close();
			}
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			super.onConnectionStateChange(gatt, status, newState);
			if(status == BluetoothGatt.GATT_SUCCESS){
				gatt.discoverServices();
			}else{
				gatt.disconnect();
				gatt.close();
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
			characteristicgetvalue = characteristic.getValue();
			waterlevel = (int)characteristicgetvalue[1];           //?
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
			gatt.readCharacteristic(characteristic);
		}	
		
	};

}

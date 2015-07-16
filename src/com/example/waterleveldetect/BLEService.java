package com.example.waterleveldetect;

import java.nio.CharBuffer;
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
	private String mac = "78:A5:04:7A:4E:4F";
	private byte[] characteristicgetvalue;
	private int waterlevel = 100;
	private double getfromble = 100.0;
	
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
					Toast.makeText(getApplicationContext(), "¿∂—¿≤ªø…”√", Toast.LENGTH_SHORT).show();
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
				int i = 30;
				try {
					while(i<=195){
						Message message = Message.obtain(null,BLEService.MSG_SET_VALUE);
						Thread.sleep(200);
						
							message.arg2 = calculatelevel(getfromble);
							System.out.println("message.arg2= "+message.arg2);
						
						i=i+5;
						message.arg1 = i;
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
				System.out.println("onservicediscovered status is "+status);    //status = 0 success
				BluetoothGattService service = gatt.getService(WL_SERVICE_UUID);
				BluetoothGattCharacteristic blegattchara = service.getCharacteristic(WL_CHARACTERISTIC_UUID);
				gatt.readCharacteristic(blegattchara);
				System.out.println("getcharacteristic is value is "+blegattchara.getValue());

				gatt.setCharacteristicNotification(blegattchara, true);
				BluetoothGattDescriptor descriptor = blegattchara.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				gatt.writeDescriptor(descriptor);
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

		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
			characteristicgetvalue = characteristic.getValue();
			StringBuffer buffer=new StringBuffer();
			switch (characteristicgetvalue.length) {
			case 13:
				for (int i = 8; i < characteristicgetvalue.length; i++) {
					byte b = characteristicgetvalue[i];
					buffer.append((char)b);
				}
				String getdata = buffer.toString();
				getfromble = Double.valueOf(getdata);
				System.out.println("getdata is : "+getfromble);
				break;
			case 2:
				break;
			case 1:
				break;
			default:
				break;
			}

		}	
		
	};
//   y=0.1797x^3-9.07x^2+53x+1441
	public int calculatelevel(double getfromble) {
		double calculateresult = 0.1797*Math.pow(getfromble, 3)-9.07*Math.pow(getfromble, 2)+53*getfromble+1441;
		int waterlevel = (int)Math.ceil(calculateresult);
		System.out.println("calculateresult is = "+calculateresult+"----"+" waterlevel is = "+waterlevel);
		return waterlevel;
	}
}

package com.example.waterleveldetect;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;

@SuppressLint("NewApi")
public class WaterBleCallBack extends BluetoothGattCallback{
	private String wbcb;
	public WaterBleCallBack(String wbcb){
		super();
		this.wbcb = wbcb;
	}
	
	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		// TODO Auto-generated method stub
		super.onServicesDiscovered(gatt, status);
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status,
			int newState) {
		// TODO Auto-generated method stub
		super.onConnectionStateChange(gatt, status, newState);
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
	}	
	
}

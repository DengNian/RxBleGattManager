package com.rainbow.kam.ble_gatt_manager.scanner;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import com.rainbow.kam.ble_gatt_manager.util.BluetoothHelper;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kang Young Won on 2016-05-24.
 */
public class RxBLE2 {

    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner scanner;

    private Subscriber<? super BleDevice> scanSubscriber;
    private final ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            scanSubscriber.onNext(BleDevice.create(result));
        }
    };


    public RxBLE2(final Application application) {
        BluetoothManager manager = (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }


    public Observable<BleDevice> observe() {
        if (!BluetoothHelper.IS_BLE_SUPPORTED || !bluetoothAdapter.isEnabled()) {
            return Observable.empty();
        }

        return Observable.create(new Observable.OnSubscribe<BleDevice>() {
            @Override
            public void call(Subscriber<? super BleDevice> subscriber) {
                scanSubscriber = subscriber;
                if (scanner == null) {
                    scanner = bluetoothAdapter.getBluetoothLeScanner();
                }
                scanner.startScan(callback);
            }
        })
                .onBackpressureBuffer()
//                .distinctUntilChanged()
                .doOnSubscribe(() -> {
                    if (scanner != null && bluetoothAdapter.isEnabled()) {
                        scanner.stopScan(callback);
                    }
                });
    }
}



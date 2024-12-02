//package com.hami.bluechat;
//
//import android.Manifest;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public class BluetoothManager {
//
//    private final BluetoothAdapter bluetoothAdapter;
//    private final Context context;
//
//    public BluetoothManager(Context context) {
//        this.context = context;
//        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (bluetoothAdapter == null) {
//            Log.e("BluetoothManager", "Device does not support Bluetooth");
//        }
//    }
//
//    // Enable Bluetooth if it's disabled
//    public void enableBluetooth() {
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            context.startActivity(enableBtIntent);
//        }
//    }
//
//    // Check if Bluetooth is enabled
//    public boolean isBluetoothEnabled() {
//        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
//    }
//
//    // Get paired devices
//    public List<BluetoothDevice> getPairedDevices() {
//        List<BluetoothDevice> pairedDevicesList = new ArrayList<>();
//        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                Log.e("BluetoothManager", "BLUETOOTH_CONNECT permission not granted");
//                return pairedDevicesList;
//            }
//            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//            pairedDevicesList.addAll(pairedDevices);
//        }
//        return pairedDevicesList;
//    }
//
//    // Start device discovery
//    public void startDiscovery() {
//        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                Log.e("BluetoothManager", "BLUETOOTH_SCAN permission not granted");
//                return;
//            }
//            bluetoothAdapter.startDiscovery();
//        } else {
//            Log.e("BluetoothManager", "Cannot start discovery. Bluetooth is disabled.");
//        }
//    }
//
//    // Cancel ongoing discovery
//    public void cancelDiscovery() {
//        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                Log.e("BluetoothManager", "BLUETOOTH_SCAN permission not granted");
//                return;
//            }
//            bluetoothAdapter.cancelDiscovery();
//        }
//    }
//
//    // Register a BroadcastReceiver for discovering devices
//    public void registerDiscoveryReceiver(BluetoothDeviceDiscoveryReceiver receiver) {
//        context.registerReceiver(receiver, BluetoothDeviceDiscoveryReceiver.getIntentFilter());
//    }
//
//    // Unregister the BroadcastReceiver
//    public void unregisterDiscoveryReceiver(BluetoothDeviceDiscoveryReceiver receiver) {
//        context.unregisterReceiver(receiver);
//    }
//
//    // Get the BluetoothAdapter instance
//    public BluetoothAdapter getBluetoothAdapter() {
//        return bluetoothAdapter;
//    }
//}

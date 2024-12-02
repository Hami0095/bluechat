package com.hami.bluechat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    private RecyclerView recyclerView;

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        recyclerView = findViewById(R.id.recyclerView);
        deviceList = new ArrayList<>();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check and request necessary permissions
        checkBluetoothPermissions();

        // Ensure Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Request the necessary permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
                return; // Exit to wait for the user to grant permission
            }
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Start scanning for Bluetooth devices
        startBluetoothScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkBluetoothPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        }
    }

    private void startBluetoothScan() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MainActivity", "Bluetooth scan permission not granted.");
            return;
        }

        bluetoothAdapter.startDiscovery();
        DeviceAdapter deviceAdapter = new DeviceAdapter(deviceList, this::onDeviceSelected);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);

        // Simulating adding devices (for simplicity, this should be handled by BroadcastReceiver)
        deviceList.clear();
        deviceList.addAll(bluetoothAdapter.getBondedDevices());
        deviceAdapter.notifyDataSetChanged();
    }

    private void onDeviceSelected(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MainActivity", "Bluetooth connect permission not granted.");
            return;
        }

        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("MY_UUID"));
            socket.connect();

            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("device_address", device.getAddress());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Bluetooth permissions granted");
            } else {
                Toast.makeText(this, "Bluetooth permissions are required for this feature.", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Bluetooth permissions denied");
            }
        }
    }

}

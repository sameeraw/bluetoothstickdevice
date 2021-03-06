package com.example.win8.bluetoothstick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by win 8 on 4/19/2017.
 */
public class ConnectingThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectingThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.d("mylog", "Socket's create() method failed");
        }
        mmSocket = tmp;
    }

    public void run() {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        // Cancel discovery because it otherwise slows down the connection.
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.d("mylog", "Could not close the client socket");
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        Log.d("mylog", "Successfull connection !!!!!!");

        //   manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.d("mylog", "Could not close the client socket");
        }
    }
}
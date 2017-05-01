package com.example.win8.bluetoothstick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by win 8 on 4/19/2017.
 */
public class ListeningThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    BluetoothAdapter mBluetoothAdapter;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ListeningThread() {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothStick", MY_UUID);
        } catch (IOException e) {
            Log.d("mylog", "Socket's listen() method failed");
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.d("mylog", "Socket's accept() method failed");
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                Log.d("mylog", "Successfull listening!!!!!!");

                // manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }



    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.d("mylog", "Could not close the connect socket");
        }
    }
}
package com.example.win8.bluetoothstick;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.*;

public class ListofDevices extends ListActivity  {

    private TextView textview1 ;
    BluetoothAdapter btAdapter;
    ListView listview ;
    private ListView mLvDevices;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private static final int REQUEST_ENABLE_BT = 1;
    ArrayAdapter<String> adapter ;
    Button scanbutton ;
    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_devices);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name for your device");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        ListeningThread t = new ListeningThread();
        t.start();
      //  Log.d("mylog" ,"listofdevices") ;

        listview=(ListView) findViewById(R.layout.activity_listof_devices);


        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked,
                mDeviceList);
        setListAdapter(adapter);
        scanbutton = (Button) findViewById(R.id.addBtn);


        scanbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(m_Text.equals("")){
                    Toast.makeText(ListofDevices.this,"Please Enter Name for Your Device !!!",Toast.LENGTH_SHORT).show();

                }else {
                    mDeviceList.clear();
                    scanDevices();
                }

            }
        }) ;



        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  itemValue = String.valueOf(((TextView) view).getText());
                char symbol= itemValue.charAt(itemValue.length() - 19) ;
             //    Log.d("mylog" , String.valueOf(symbol)) ;
        if(symbol=='#') {
             // Log.d("mylog" , "haririririri") ;

            String MAC = itemValue.substring(itemValue.length() - 17);
                    BluetoothDevice bluetoothDevice = btAdapter.getRemoteDevice(MAC);
                    // Initiate a connection request in a separate thread
                    ConnectingThread t = new ConnectingThread(bluetoothDevice);
                    t.start();
                    Toast.makeText(ListofDevices.this,"Initializing Connection Request to"+MAC,Toast.LENGTH_SHORT).show();

                }else{
           // Log.d("mylog" , "NAaaaaaaaaaa") ;

        }

            }
        });
    }

    private void scanDevices() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBluetoothState();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBtReceiver, filter);

        // Getting the Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter != null) {
            btAdapter.startDiscovery();
            Toast.makeText(this, "Starting discovery...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Bluetooth disabled or not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName()+" # "+device.getAddress()); // get mac address concat device.getAddress()
                adapter.notifyDataSetChanged();
               // Log.d("mylog" ,device.getAddress() + ", " + device.getName()) ;

            }
        }
    };
    private void CheckBluetoothState() {
        // Checks for the Bluetooth support and then makes sure it is turned on
        // If it isn't turned on, request to turn it on
        // List paired devices
        if(btAdapter==null) {
            //textview1.append("\nBluetooth NOT supported. Aborting.");
            mDeviceList.add("Bluetooth NOT supported. Aborting.");
            adapter.notifyDataSetChanged();
            return;
        } else {
            if (btAdapter.isEnabled()) {
               // textview1.append("\nBluetooth is enabled...");
                mDeviceList.add("Bluetooth is enabled...");
                adapter.notifyDataSetChanged();
                // Listing paired devices
                //textview1.append("\nPaired Devices are:");
                mDeviceList.add("Paired Devices are:");
                adapter.notifyDataSetChanged();

                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                   // textview1.append("\n  Device: " + device.getName() + ", " + device);
                     mDeviceList.add(device.getName()+" # "+device); // get mac address  concat "device"
                    adapter.notifyDataSetChanged();
                }
                mDeviceList.add("Available Devices are:");
                adapter.notifyDataSetChanged();
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


}

package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pro.matyschenko.armbuilder.armbuilder.DB.DatabaseHandler;
import pro.matyschenko.armbuilder.armbuilder.DB.Settings;
import pro.matyschenko.armbuilder.armbuilder.Meter;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.MeterListAdapter;
import pro.matyschenko.armbuilder.armbuilder.dto.MeterDTO;



public class MeasurementFragment extends AbstractTabFragment {
    Meter meter;
    long zero_value = 8416300; // Калибровочный коэффициент
    long input_value = 0;
    MeterListAdapter meterListAdapter;
    final int RECEIVE_MEASURE = 1;
    final int RECEIVE_BATTERY = 2;
    final static String TAG = "MEASURE";
    //private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private ConnectThread mConnectThread;
    //SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter btAdapter = null;

    public static MeasurementFragment newInstance(Context context) {
        MeasurementFragment fragment = new MeasurementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.measurement_fragment));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_measurement, container, false);
        meter = (Meter) v.findViewById(R.id.meter);
        meter.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         zero_value = input_value;
                                         meter.moveToValue(0);

                                     }
                                 }
        );
        RecyclerView rv = v.findViewById(R.id.recycle_measure);
        rv.setLayoutManager(new LinearLayoutManager(context));
        meterListAdapter = new MeterListAdapter(createMockMeterListData());
        rv.setAdapter(meterListAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        String address;
        address = getDeviceAddress();
        if (!address.equals("")) {
            mConnectThread = new ConnectThread(btAdapter.getRemoteDevice(address));
            mConnectThread.start();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");
        mConnectedThread.cancel();
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_MEASURE:
                    Log.d(TAG, "RECEIVE_MEASURE: ");
                    break;
                case RECEIVE_BATTERY:
                    Log.d(TAG, "RECEIVE_BATTERY");
                    break;

            }
        }
    };

    private String getDeviceAddress() {
        DatabaseHandler db = new DatabaseHandler(context);
        Settings setting = db.getSettings();
        String address;
        try{
        address = setting.get_address();
        } catch (Exception e) {return "";}
        Log.d(TAG, "...getDeviceAddress(): address is " + address);
        return address;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            Log.d(TAG, "...ConnectThread(): ");
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d(TAG, "...ConnectThread(): device.createRfcommSocketToServiceRecord");
            } catch (IOException e) { {Log.d(TAG, "...ConnectThread(): error" + e.getMessage() + "."); } }
            mmSocket = tmp;
            Log.d(TAG, "...ConnectThread(): mmSocket = tmp");
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();
            Log.d(TAG, "...ConnectThread(): Run");
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                Log.d(TAG, "...ConnectThread(): Try to connect");
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.d(TAG, "...ConnectThread(): Unable to connect");
                try {
                    mmSocket.close();
                } catch (IOException e) { Log.d(TAG, "...run(): error" + e.getMessage() + "."); }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            mConnectedThread = new ConnectedThread(mmSocket);
            Log.d(TAG, "...ConnectedThread.start()");
            mConnectedThread.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {Log.d(TAG, "...cancel(): error" + e.getMessage() + "."); }
        }
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "...ConnectedThread(): ");
            mmSocket = socket;
            InputStream tmpIn = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.d(TAG, "...ConnectedThread(): getInputStream error ");
            }

            mmInStream = tmpIn;
        }

        public void run() {
            Log.d(TAG, "...run(): ");
            byte[] buffer = new byte[256];  // buffer store for the stream
            StringBuilder sb = new StringBuilder();
            int bytes; // bytes returned from read()
            int type_of_message = 1; //message types (1) M123.01 - измерение, (2) B0.91 процент зарядки батареи
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    for (int i = 0; i < bytes; i++) {
                        switch (buffer[i]) {
                            case 77: //M
                                type_of_message = RECEIVE_MEASURE;
                                break;
                            case 66: //B
                                type_of_message = RECEIVE_BATTERY;
                                break;
                            case 13:
                                String value = sb.toString();
                                Log.d(TAG, "sent string : " + value);
                                mHandler.obtainMessage(type_of_message, sb.length(), -1, value).sendToTarget();
                                sb.delete(0, sb.length());
                                break;
                            default:
                                sb.append((char) buffer[i]);
                                break;
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "...run(): error" + e.getMessage() + ".");
                }
            }
        }


        /* Call this from the main activity to shutdown the connection */

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private List<MeterDTO> createMockMeterListData() {
        List<MeterDTO> data = new ArrayList<>();
        data.add(new MeterDTO("11.00"));
        data.add(new MeterDTO("12.00"));
        data.add(new MeterDTO("13.00"));
        data.add(new MeterDTO("14.00"));
        data.add(new MeterDTO("15.00"));

        return data;
    }
}

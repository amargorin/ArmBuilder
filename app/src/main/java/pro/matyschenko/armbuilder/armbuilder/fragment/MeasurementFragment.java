package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    MeterListAdapter meterListAdapter;


    public static float coefficient = 21500; // Калибровочный коэффициент
    long zero_value = 8416300; // Значение 0
    long global_value = 0;
    long input_value = 0;
    double add_value = 0; //тоннаж
    long add_counter = 0; // счетчик подходов
    long elapsedMillis = 0;
    double avg = 0; // среднее арифметическое
    double max = 0;         // Максимальное значение всех измерений
    double local_max = 0;         // Максимальное значение текущего измерения
    public static int threshold_value = 4;  // Пороговое значение (в кг) для детекции начала и конца повторения
    boolean state = false;  // Флаг проведения текущего измерения.
    private boolean add_to_save; // флаг для необходимости добавления макс. значения в список измерений.

    TextView total_value;
    TextView counter_value;
    TextView avg_value;
    TextView max_value;
    TextView percent;
    GridLayout gridLayout;
    ImageView battery;
    RecyclerView rv;

    private Button save_button;
    private Button reset_button;
    private Button start_button;

    final int RECEIVE_MEASURE = 1;
    final int RECEIVE_BATTERY = 2;
    final static String TAG = "MEASURE";
    private Chronometer mChronometer;
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
                                     public void onClick(View v) { // Calibrate
                                         zero_value = input_value;
                                         meter.moveToValue(0);
                                     }
                                 }
        );
        rv = v.findViewById(R.id.recycle_measure);
        rv.setLayoutManager(new LinearLayoutManager(context));
        meterListAdapter = new MeterListAdapter(createMockMeterListData());
        rv.setAdapter(meterListAdapter);
        mChronometer = v.findViewById(R.id.chronometer);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();
            }
        });
        reset_button = v.findViewById(R.id.button_reset);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!state) reset_Measurement();
            }
        });
        start_button = v.findViewById(R.id.button_start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (state){
                    stop_Measurement();
                    meterListAdapter.addElement(new MeterDTO(Double.toString(local_max))); //TODO: временно для проверки, удалить
                    //
                }
                else {start_Measurement();}
                reset_button.setEnabled(!state);
            }
        });
        save_button = v.findViewById(R.id.button_save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meterListAdapter.deleteAll();
            }
        });
        total_value = v.findViewById(R.id.total_value);
        counter_value = v.findViewById(R.id.count_value);
        avg_value =  v.findViewById(R.id.avg_value);
        max_value = v.findViewById(R.id.max_value);

        gridLayout = v.findViewById(R.id.grid_layout);
        battery = v.findViewById(R.id.battery);
        percent = v.findViewById(R.id.percent);

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
        if (mConnectedThread != null) mConnectedThread.cancel();
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int i = Integer.parseInt((String)msg.obj);
            switch (msg.what) {
                case RECEIVE_MEASURE:
                    Log.d(TAG, "RECEIVE_MEASURE: " + (String)msg.obj);
                    input_value = i; // input_value изпользуется для калибровки
                    try {
                        float f = (i - zero_value) / coefficient;
                        meter.moveToValue(f);
                        meter.setUpperText(String.format("%.2f", f));
                        if (f > max) max = f;
                        if (f > local_max) local_max = f;
                        if (f > threshold_value) {
                            if (!add_to_save) {
                                mChronometer.setBase(SystemClock.elapsedRealtime());
                                mChronometer.start();
                                add_to_save = true;
                                add_counter++;
                                add_value += local_max;
                                avg = add_value / add_counter;
                            }
                            set_Measurement();
                        } else {
                            if (add_to_save) {
                                meterListAdapter.addElement(new MeterDTO(String.format("%.2f",local_max)));
                                rv.scrollToPosition(meterListAdapter.getItemCount() -1);
                                mChronometer.stop();
                                stop_Measurement();
                                add_to_save = false;
                            }
                            local_max = 0.0;
                        }
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "NumberFormatException error : " + (String)msg.obj);
                    }
                    break;
                case RECEIVE_BATTERY:
                    Log.d(TAG, "RECEIVE_BATTERY");
                    percent.setText((String)msg.obj + "%");
                    int j = i / 10;
                    switch (j) {
                        case 10:
                            battery.setImageResource(R.drawable.battery_bluetooth);
                            break;
                        case 9:
                            battery.setImageResource(R.drawable.battery_90_bluetooth);
                            break;
                        case 8:
                            battery.setImageResource(R.drawable.battery_80_bluetooth);
                            break;
                        case 7:
                            battery.setImageResource(R.drawable.battery_70_bluetooth);
                            break;
                        case 6:
                            battery.setImageResource(R.drawable.battery_60_bluetooth);
                            break;
                        case 5:
                            battery.setImageResource(R.drawable.battery_50_bluetooth);
                            break;
                        case 4:
                            battery.setImageResource(R.drawable.battery_40_bluetooth);
                            break;
                        case 3:
                            battery.setImageResource(R.drawable.battery_30_bluetooth);
                            break;
                        case 2:
                            battery.setImageResource(R.drawable.battery_20_bluetooth);
                            break;
                        case 1:
                            battery.setImageResource(R.drawable.battery_10_bluetooth);
                            break;
                        default:
                            battery.setImageResource(R.drawable.battery_10_bluetooth);
                            break;
                    }
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

    private void reset_Measurement() {
        add_value = 0;
        add_counter = 0;
        avg = 0;
        max = 0;
//        total_value.setText(String.format("%1$.1f", add_value));
//        avg_value.setText(String.format("%1$.1f", avg));
//        counter_value.setText(String.format("%d", add_counter));
//        max_value.setText(String.format("%1$.1f", max));
//        meter.setUpperText(String.format ("%.2f", max));
//        mChronometer.setBase(SystemClock.elapsedRealtime());
        elapsedMillis = 0;
    }

    private void start_Measurement() {
        //state = true;
        //start_button.setText("Stop");
        //save_button.setEnabled(false);
        //max_value.setText(String.format("%1$.1f", 0.0));
        //meter.setUpperText(String.format ("%.2f", 0.0));
        //mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void stop_Measurement() {
        total_value.setText(String.format("%1$.1f", add_value));
        avg_value.setText(String.format("%1$.1f", avg));
        counter_value.setText(String.format("%d", add_counter));
        max_value.setText(String.format("%1$.1f", max));
        //mChronometer.setBase(SystemClock.elapsedRealtime());
        elapsedMillis = 0;
        add_value = 0;
        add_counter = 0;
        avg = 0;
        max = 0;
    }

    private void set_Measurement() {
        total_value.setText(String.format("%1$.1f", add_value));
        avg_value.setText(String.format("%1$.1f", avg));
        counter_value.setText(String.format("%d", add_counter));
        max_value.setText(String.format("%1$.1f", max));
        //meter.setUpperText(String.format ("%.2f", max));
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
            int type_of_message = 1; //message types (1) M80000000 - измерение, (2) B99 процент зарядки батареи
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
        return data;
    }
}

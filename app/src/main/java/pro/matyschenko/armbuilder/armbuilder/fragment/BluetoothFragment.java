package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pro.matyschenko.armbuilder.armbuilder.DB.DatabaseHandler;
import pro.matyschenko.armbuilder.armbuilder.DB.Settings;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.BluetoothListAdapter;
import pro.matyschenko.armbuilder.armbuilder.dto.BluetoothDTO;

import static android.app.Activity.RESULT_OK;


public class BluetoothFragment extends AbstractBluetoothFragment {

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContext(getActivity());
        view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        rv = view.findViewById(R.id.bluetooth_recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d("BTState", "onCreateView db created");
        db = new DatabaseHandler(context);
        if (CheckStateBT()) {
            initBTAdapter();
        }
        return  view;
    }

    private void initBTAdapter() {
        bluetoothListAdapter = new BluetoothListAdapter(getBluetoothListData(), context);
        rv.setAdapter(bluetoothListAdapter);
    }


    private List<BluetoothDTO> getBluetoothListData() {
        List<BluetoothDTO> data = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices; // список сопряженных устройств
        pairedDevices = bluetoothAdapter.getBondedDevices();
        Settings settings = db.getSettings();
        for (BluetoothDevice device : pairedDevices) {
            String devicename = device.getName();
            String macAddress = device.getAddress();
            Log.d("BTState", "settings = ");
            if (settings != null) {
                Log.d("BTState", "settings name " + settings.get_name());
                if (macAddress.equals(settings.get_address())){
                    data.add(new BluetoothDTO(devicename, macAddress, true));
                    Log.d("BTState", "fined settings name " + settings.get_name());
                }
                else {data.add(new BluetoothDTO(devicename, macAddress, false));
                }
            }
            else {data.add(new BluetoothDTO(devicename, macAddress, false));}
        }
        return data;
    }

    public  boolean CheckStateBT(){
        if (bluetoothAdapter == null) {
            Log.d("BTState", "Not compatible");
            return false;
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                Log.d("BTState", "before start activity");
                startActivityForResult(enableBT, REQUEST_ENABLE_BT);
                Log.d("BTState", "after start activity");
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                initBTAdapter();
                Log.d("BTState", "start activity OK");
            }else{
                //
                Log.d("BTState", "start activity NOK");
            }
        }
    }
}

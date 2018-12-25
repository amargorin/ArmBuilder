package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.BluetoothListAdapter;
import pro.matyschenko.armbuilder.armbuilder.dto.BluetoothDTO;

public class BluetoothFragment extends AbstractBluetoothFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        RecyclerView rv = view.findViewById(R.id.bluetooth_recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        bluetoothListAdapter = new BluetoothListAdapter(createMockBluetoothListData());
        rv.setAdapter(bluetoothListAdapter);
        return  view;
    }

    private List<BluetoothDTO> createMockBluetoothListData() {
        List<BluetoothDTO> data = new ArrayList<>();
        data.add(new BluetoothDTO("BT_Dev"));
        return data;
    }

}

package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.dto.BluetoothDTO;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.BluetoothViewHolder> {

    private List<BluetoothDTO> data;

    public BluetoothListAdapter(List<BluetoothDTO> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BluetoothListAdapter.BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bluetooth_item, viewGroup, false);
        Log.d("BTState", "onCreateViewHolder "+Integer.toString(i));
        return new BluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothListAdapter.BluetoothViewHolder bluetoothViewHolder, int i) {
        BluetoothDTO item = data.get(i);
        bluetoothViewHolder.position = i;
        Log.d("BTState", "onBind position "+Integer.toString(i));
        bluetoothViewHolder.title.setText(item.getTitle());
        bluetoothViewHolder.address.setText(item.getAddress());
        if(item.isConnected()){
            bluetoothViewHolder.imageView.setImageResource(R.drawable.bluetooth_connect);
        } else {
            bluetoothViewHolder.imageView.setImageResource(R.drawable.bluetooth_off);
        }
        Log.d("BTState", "onBindViewHolder " + Boolean.toString(item.isConnected()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BluetoothViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title, address;
        int position;
        ImageView imageView;

        public BluetoothViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bluetooth_state);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BluetoothDTO item = data.get(position);
                    if (setConnection(position)) {
                        notifyDataSetChanged();
                        Log.d("BTState", "Connected position " + Integer.toString(position));
                    }
                }
            });
            address = itemView.findViewById(R.id.device_address);
            title = itemView.findViewById(R.id.title);

        }

    }

    public void addElement(BluetoothDTO bluetoothDTO){
        data.add(bluetoothDTO);
    }

    public void deleteElement(BluetoothDTO bluetoothDTO){
        data.remove(bluetoothDTO);
    }

    public  boolean setConnection(int position) {
        boolean state = false;
        for (int i=0; i<data.size(); i++){
            BluetoothDTO item = data.get(i);
            if (i != position) {item.setConnected(false);}
            else {item.setConnected(true);
            //TODO: Сохранить в базе параметры последнего соединения
            state = true;}
        }
        return state;
    }
}

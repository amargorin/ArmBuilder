package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.DetailActivity;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.dto.BluetoothDTO;
import pro.matyschenko.armbuilder.armbuilder.dto.ExerciseDTO;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.BluetoothViewHolder> {

    private List<BluetoothDTO> data;

    public BluetoothListAdapter(List<BluetoothDTO> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BluetoothListAdapter.BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bluetooth_item, viewGroup, false);
        Log.d("index"," ExerciseListAdapter onCreateViewHolder() int i =" +Integer.toString(i));
        return new BluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothListAdapter.BluetoothViewHolder exerciseViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BluetoothViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;;
        int position;

        public BluetoothViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });

            title = itemView.findViewById(R.id.title);
        }

    }

    public void addElement(BluetoothDTO bluetoothDTO){
        data.add(bluetoothDTO);
    }

    public void deleteElement(BluetoothDTO bluetoothDTO){
        data.remove(bluetoothDTO);
    }
}

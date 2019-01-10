package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.dto.MeterDTO;

public class MeterListAdapter extends RecyclerView.Adapter<MeterListAdapter.MeterViewHolder> {

    private List<MeterDTO> data;

    public MeterListAdapter(List<MeterDTO> data) {
        Log.d("Meter"," MeterListAdapter constructor");
        this.data = data;
    }
    @NonNull
    @Override
    public MeterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meter_item, viewGroup, false);
        Log.d("Meter","onCreateViewHolder");
        return new MeterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterListAdapter.MeterViewHolder MeterViewHolder, int i) {
        MeterDTO item = data.get(i);
        Log.d("Meter",item.getTitle() + " - pos " + Integer.toString(i));
        MeterViewHolder.meterDTO = item;
        MeterViewHolder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MeterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        MeterDTO meterDTO;
        ImageButton deleteButton;

        public MeterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView_meter);
            Log.d("Meter","constructor MeterViewHolder");
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

            }
            });
            deleteButton = itemView.findViewById(R.id.delete_meter);
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    data.remove(meterDTO);
                    notifyDataSetChanged();
                }});

            title = itemView.findViewById(R.id.meter_title);
        }

    }

    public void addElement(MeterDTO meterDTO){
        data.add(meterDTO);
        notifyDataSetChanged();
    }

    public void deleteElement(MeterDTO meterDTO){
        data.remove(meterDTO);
    }

    public void deleteAll(){
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(){
        for (int i = 0; i < data.size(); i++){
            String title = data.get(i).getTitle();
            //TODO: Добавить все измерения в базу данных
        }
    }
}

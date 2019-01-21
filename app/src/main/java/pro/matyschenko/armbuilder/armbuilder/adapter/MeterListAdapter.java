package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import pro.matyschenko.armbuilder.armbuilder.DB.DatabaseHandler;
import pro.matyschenko.armbuilder.armbuilder.DB.Exercise;
import pro.matyschenko.armbuilder.armbuilder.DB.ExerciseGroup;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.dto.MeterDTO;

public class MeterListAdapter extends RecyclerView.Adapter<MeterListAdapter.MeterViewHolder> {

    private List<MeterDTO> data;
    Context mContext;
    DatabaseHandler databaseHandler;

    public MeterListAdapter(List<MeterDTO> data, Context context) {
        Log.d("Meter"," MeterListAdapter constructor");
        this.data = data;
        mContext = context;
        databaseHandler = new DatabaseHandler(context);
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

    public void addAll(Double add_value, Double avg, Double max, long add_counter, long elapsedMillis, String date) {
        ExerciseGroup exerciseGroup = databaseHandler.getCurrentExerciseGroup();
        String values;
        values = "[";
        for (int i = 0; i < data.size(); i++) {
            MeterDTO meterDTO = data.get(i);
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            try {
                Number number = format.parse(meterDTO.getTitle());
                values =  values + "\"" + number +"\"";
                if(i+1 != data.size()) values = values + ",";
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d("DatabaseHandler", " MeterListAdapter addAll() value " + meterDTO.getTitle() + " group " + String.valueOf(exerciseGroup.getID()));
        }
        values = values + "]";
        databaseHandler.addExercise(new Exercise(exerciseGroup.getID(),values, add_value, avg, max, add_counter, elapsedMillis, date));
    }
}

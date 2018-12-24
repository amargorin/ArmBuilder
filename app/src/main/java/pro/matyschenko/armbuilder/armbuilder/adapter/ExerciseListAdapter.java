package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.DetailActivity;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.dto.ExerciseDTO;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private List<ExerciseDTO> data;
    private Context context;

    public ExerciseListAdapter(List<ExerciseDTO> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_item, viewGroup, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        ExerciseDTO item = data.get(i);
        exerciseViewHolder.title.setText(item.getTitle());
       // exerciseViewHolder.title.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        ImageButton imageButton;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( view.getContext(), DetailActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
            imageButton = itemView.findViewById(R.id.edit_button);
            imageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, title.getText() + " Edit", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            title = itemView.findViewById(R.id.title);
        }

    }

    public void addElement(ExerciseDTO exerciseDTO){
        data.add(exerciseDTO);
    }
}

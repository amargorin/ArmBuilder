package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
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
import pro.matyschenko.armbuilder.armbuilder.dto.ExerciseDTO;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private List<ExerciseDTO> data;

    public ExerciseListAdapter(List<ExerciseDTO> data) {
        Log.d("index"," ExerciseListAdapter constructor");
        this.data = data;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_item, viewGroup, false);
        Log.d("index"," ExerciseListAdapter onCreateViewHolder() int i =" +Integer.toString(i));
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        ExerciseDTO item = data.get(i);
        Log.d("index"," ExerciseListAdapter onBindViewHolder() item.getTitle()=" + item.getTitle() + " " + Integer.toString(i));
        exerciseViewHolder.position = i;
        exerciseViewHolder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d("index"," ExerciseListAdapter getItemCount() data.size()=" + Integer.toString(data.size()));
        return data.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        ImageButton editButton, deleteButton;
        int position;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            Log.d("index","constructor ExerciseViewHolder");
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
            editButton = itemView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(view.getContext());
                    View dialogView = layoutInflaterAndroid.inflate(R.layout.exercise_input_dialog, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilderUserInput.setView(dialogView);
                    final EditText userInputDialogEditText = (EditText) dialogView.findViewById(R.id.userInputDialog);
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    ExerciseDTO item = data.get(position);
                                    Log.d("index","Dialog onClick OK");
                                    item.setTitle(userInputDialogEditText.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                }
            });
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(view.getContext());
                    View dialogView = layoutInflaterAndroid.inflate(R.layout.delete_item_dialog, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilderUserInput.setView(dialogView);
                    final EditText userInputDialogEditText = (EditText) dialogView.findViewById(R.id.userInputDialog);
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                   deleteElement(data.get(position));
                                    notifyDataSetChanged();
                                }
                            })

                            .setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                }
            });

            title = itemView.findViewById(R.id.title);
        }

    }

    public void addElement(ExerciseDTO exerciseDTO){
        data.add(exerciseDTO);
    }

    public void deleteElement(ExerciseDTO exerciseDTO){
        data.remove(exerciseDTO);
    }
}

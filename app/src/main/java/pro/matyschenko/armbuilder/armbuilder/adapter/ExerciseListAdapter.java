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

import pro.matyschenko.armbuilder.armbuilder.DB.DatabaseHandler;
import pro.matyschenko.armbuilder.armbuilder.DB.ExerciseGroup;
import pro.matyschenko.armbuilder.armbuilder.DetailActivity;
import pro.matyschenko.armbuilder.armbuilder.R;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private List<ExerciseGroup> data;
    Context mContext;
    DatabaseHandler databaseHandler;

    public ExerciseListAdapter(Context context) {
        Log.d("index"," ExerciseListAdapter constructor");
        mContext = context;
        databaseHandler = new DatabaseHandler(context);
        this.data =  databaseHandler.getAllExerciseGroups();
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
        ExerciseGroup item = data.get(i);
        Log.d("index"," ExerciseListAdapter onBindViewHolder() item.getTitle()=" + item.getTitle() + " " + Integer.toString(i));
        exerciseViewHolder.position = i;
        exerciseViewHolder.title.setText(item.getTitle());
        Log.d("DatabaseHandler"," ExerciseListAdapter onBindViewHolder() current = " + Integer.toString(item.is_current()));
        if(item.is_current() == 1){
            exerciseViewHolder.selectButton.setImageResource(R.drawable.radiobox_marked);
        } else {
            exerciseViewHolder.selectButton.setImageResource(R.drawable.radiobox_blank);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("index"," ExerciseListAdapter getItemCount() data.size()=" + Integer.toString(data.size()));
        return data.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        ImageButton editButton, deleteButton, selectButton;
        int position;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            Log.d("index","constructor ExerciseViewHolder");
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ExerciseGroup exerciseGroup = getGroupElement(position);
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    Log.d("DatabaseHandler","position = " + String.valueOf(position)+" ID = " + String.valueOf(exerciseGroup.getID()));
                    intent.putExtra("exercise_group_id", exerciseGroup.getID());
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
                                    ExerciseGroup item = data.get(position);
                                    Log.d("index","Dialog onClick OK");
                                    String title = userInputDialogEditText.getText().toString();
                                    item.setTitle(title);
                                    notifyDataSetChanged();
                                    int updated = databaseHandler.updateExerciseGroup(item);
                                    Log.d("DatabaseHandler", "Updated Exercise - " + Integer.toString(updated));
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
                                    deleteGroupElement(data.get(position));
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
            selectButton = itemView.findViewById(R.id.radiobox_button);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentGroupElement(data.get(position));
                    notifyDataSetChanged();
                }
            });
            title = itemView.findViewById(R.id.title);
        }

    }

    public void addGroupElement(ExerciseGroup exerciseGroup){
        DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
        exerciseGroup.setID(databaseHandler.addExerciseGroup(exerciseGroup));
        data.add(exerciseGroup);

    }

    public void deleteGroupElement(ExerciseGroup exerciseGroup){
        DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
        databaseHandler.deleteExerciseGroup(exerciseGroup);
        data.remove(exerciseGroup);
    }

    public ExerciseGroup getGroupElement(int index){
        return data.get(index);
    }

    public void  setCurrentGroupElement(ExerciseGroup exerciseGroup){
        for (int i=0; i<data.size();i++){
            ExerciseGroup eg = data.get(i);
            eg.set_current(0);
        }
        exerciseGroup.set_current(1);
        databaseHandler.setCurrentExerciseGroup(exerciseGroup);
    }
}

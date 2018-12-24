package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.ExerciseListAdapter;
import pro.matyschenko.armbuilder.armbuilder.dto.ExerciseDTO;

public class ExerciseFragment extends AbstractTabFragment {


    public static ExerciseFragment newInstance(Context context) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.exercises_fragment));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        RecyclerView rv = view.findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        exerciseListAdapter = new ExerciseListAdapter(createMockExerciseListData());
        rv.setAdapter(exerciseListAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise();
            }
        });
        return view;
    }

    private void addExercise() {
        exerciseListAdapter.addElement(new ExerciseDTO("New exercise"));
        exerciseListAdapter.notifyDataSetChanged();
    }

    private List<ExerciseDTO> createMockExerciseListData() {
        List<ExerciseDTO> data = new ArrayList<>();
        data.add(new ExerciseDTO("New exercise"));
        return data;
    }
}

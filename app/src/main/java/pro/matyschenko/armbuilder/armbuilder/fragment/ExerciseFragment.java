package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.matyschenko.armbuilder.armbuilder.DB.Exercise;
import pro.matyschenko.armbuilder.armbuilder.DB.ExerciseGroup;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.ExerciseListAdapter;

public class ExerciseFragment extends AbstractTabFragment {

    //public OnFragmentInteractionListener mListener;

    public static ExerciseFragment newInstance(Context context) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.exercises_fragment));
        Log.d("index"," ExerciseFragment constructor");
        return fragment;
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String name);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mListener = (OnFragmentInteractionListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        RecyclerView rv = view.findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        exerciseListAdapter = new ExerciseListAdapter(context);
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
        exerciseListAdapter.addGroupElement(new ExerciseGroup());
        exerciseListAdapter.notifyDataSetChanged();
    }

//    public void updateName(String name){
//        mListener.onFragmentInteraction(name);
//    }

}

package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.content.Context;
import android.os.Bundle;
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
        rv.setAdapter(new ExerciseListAdapter(createMockExerciseListData()));
        return view;
    }

    private List<ExerciseDTO> createMockExerciseListData() {
        List<ExerciseDTO> data = new ArrayList<>();
        data.add(new ExerciseDTO("Item1"));
        data.add(new ExerciseDTO("Item2"));
        data.add(new ExerciseDTO("Item3"));
        data.add(new ExerciseDTO("Item4"));
        data.add(new ExerciseDTO("Item5"));
        data.add(new ExerciseDTO("Item6"));
        data.add(new ExerciseDTO("Item7"));
        return data;
    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}

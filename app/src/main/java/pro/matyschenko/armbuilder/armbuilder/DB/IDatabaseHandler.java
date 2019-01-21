package pro.matyschenko.armbuilder.armbuilder.DB;

import java.util.List;

public interface IDatabaseHandler {
    public long addExercise(Exercise exercise);
    public long addExerciseGroup(ExerciseGroup exerciseGroup);
    public Exercise getExercise(int id);
    public ExerciseGroup getByID(long id);
    public void setCurrentExerciseGroup(ExerciseGroup exerciseGroup);
    public ExerciseGroup getCurrentExerciseGroup();
    public List<ExerciseGroup> getAllExerciseGroups();
    public List<Exercise> getAllExercisesByID(long id);
    public int getExerciseGroupCount();
    public int updateExercise(Exercise exercise);
    public int updateExerciseGroup(ExerciseGroup exerciseGroup);
    public void deleteExercise(Exercise exercise);
    public void deleteExerciseGroup(ExerciseGroup exerciseGroup);
    public void deleteAll();
    public void setSetting(Settings settings);
    public Settings getSettings();
}

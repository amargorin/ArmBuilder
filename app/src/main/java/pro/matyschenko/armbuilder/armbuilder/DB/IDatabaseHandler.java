package pro.matyschenko.armbuilder.armbuilder.DB;

import java.util.List;

public interface IDatabaseHandler {
    public void addExercise(Exercise exercise);
    public Exercise getExercise(int id);
    public Exercise getByName(String name);
    public List<Exercise> getAllExercises();
    public int getExercisesCount();
    public int updateExercise(Exercise exercise);
    public void deleteExercise(Exercise exercise);
    public void deleteAll();
    public void setSetting(Settings settings);
    public Settings getSettings();
}

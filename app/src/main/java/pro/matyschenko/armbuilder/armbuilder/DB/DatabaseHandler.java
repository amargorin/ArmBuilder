package pro.matyschenko.armbuilder.armbuilder.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "exerciseManager";
    private static final String TABLE_EXERCISE = "exercise";
    private static final String KEY_ID = "id";
    private static final String KEY_GROUP_ID = "exercise_id";
    private static final String KEY_VALUE_ARRAY = "value_array";
    private static final String KEY_VALUE_ADD = "add_value";
    private static final String KEY_VALUE_AVG = "avg_value";
    private static final String KEY_VALUE_MAX = "max_value";
    private static final String KEY_COUNTER_ADD = "add_counter";
    private static final String KEY_MILLS = "elapsedMillis";
    private static final String KEY_DATE = "date";

    private static final String TABLE_EXERCISE_GROUP = "exercise_group";
    private static final String KEY_EXERCISE_GROUP_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COUNT = "count";
    private static final String KEY_CURRENT = "current";

    private static final String TABLE_SETTINGS = "settings";
    private static final String KEY_DEVICE_NAME = "device_name";
    private static final String KEY_DEVICE_ADDRESS = "device_address";
    private static final String KEY_THRESHOLD = "threshold";

    final String TAG = "DatabaseHandler";
    long l;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("BTState", "onCreate(SQLiteDatabase db)");
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GROUP_ID + " INTEGER,"
                + KEY_VALUE_ARRAY + " TEXT," + KEY_VALUE_ADD + " DOUBLE,"
                + KEY_VALUE_AVG + " DOUBLE," + KEY_VALUE_MAX + " DOUBLE,"
                + KEY_COUNTER_ADD + " LONG," + KEY_MILLS + " LONG,"
                + KEY_DATE + " TEXT"+ ")";
        db.execSQL(CREATE_EXERCISE_TABLE);
        String CREATE_EXERCISE_GROUP_TABLE = "CREATE TABLE " + TABLE_EXERCISE_GROUP + "("
                + KEY_EXERCISE_GROUP_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_COUNT + " INTEGER," + KEY_CURRENT + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_EXERCISE_GROUP_TABLE);
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DEVICE_NAME + " TEXT,"
                + KEY_DEVICE_ADDRESS + " TEXT, " + KEY_THRESHOLD + " INTEGER)";
        db.execSQL(CREATE_SETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    @Override
    public long addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, exercise.get_group_id());
        values.put(KEY_VALUE_ARRAY, exercise.getValue());
        values.put(KEY_VALUE_ADD, exercise.getAddValue());
        values.put(KEY_VALUE_AVG, exercise.getAvgValue());
        values.put(KEY_VALUE_MAX, exercise.getMaxValue());
        values.put(KEY_COUNTER_ADD, exercise.getAddCounter());
        values.put(KEY_MILLS, exercise.get_elapsedMillis());
        values.put(KEY_DATE, exercise.get_date());
        Log.d(TAG, "Add Exercise- " + Long.toString(l));
        return db.insert(TABLE_EXERCISE, null, values);
    }

    @Override
    public long addExerciseGroup(ExerciseGroup exerciseGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT, exerciseGroup.is_current());
        values.put(KEY_NAME, exerciseGroup.getTitle());
        values.put(KEY_COUNT, exerciseGroup.get_count());
        Log.d(TAG, "Add ExerciseGroup - " + Long.toString(l));
        return db.insert(TABLE_EXERCISE_GROUP, null, values);
    }

    @Override
    public Exercise getExercise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_ID,
                        KEY_GROUP_ID, KEY_VALUE_ARRAY, KEY_VALUE_ADD, KEY_VALUE_AVG, KEY_VALUE_MAX,
                        KEY_COUNTER_ADD, KEY_MILLS, KEY_DATE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getDouble(2),
                cursor.getDouble(3), cursor.getDouble(4), cursor.getLong(5),
                cursor.getLong(6), cursor.getString(7));
        db.close();
        return exercise;
    }

    @Override
    public ExerciseGroup getByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE_GROUP, new String[] { KEY_EXERCISE_GROUP_ID,
                        KEY_NAME, KEY_COUNT, KEY_CURRENT}, KEY_EXERCISE_GROUP_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        ExerciseGroup exerciseGroup = new ExerciseGroup(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        db.close();
        return exerciseGroup;
    }

    @Override
    public void setCurrentExerciseGroup(ExerciseGroup exerciseGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "set current ExerciseGroup - " + exerciseGroup.getTitle() + " - " + String.valueOf(exerciseGroup.getID()));
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT, 0);
        db.update(TABLE_EXERCISE_GROUP, values, null, null);
        db.close();
        exerciseGroup.set_current(1);
        updateExerciseGroup(exerciseGroup);
    }

    @Override
    public ExerciseGroup getCurrentExerciseGroup() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE_GROUP, new String[] { KEY_EXERCISE_GROUP_ID,
                        KEY_NAME, KEY_COUNT, KEY_CURRENT}, KEY_CURRENT + "=?",
                new String[] { "1" }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        ExerciseGroup exerciseGroup = new ExerciseGroup(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        db.close();
        return exerciseGroup;
    }

    @Override
    public List<ExerciseGroup> getAllExerciseGroups() {
        List<ExerciseGroup> exerciseGroups = new ArrayList<ExerciseGroup>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_GROUP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "Get all ExerciseGroups");
        if (cursor.moveToFirst()) {
            do {
                ExerciseGroup exerciseGroup = new ExerciseGroup();
                exerciseGroup.setID(Integer.parseInt(cursor.getString(0)));
                exerciseGroup.setTitle(cursor.getString(1));
                exerciseGroup.set_count(Integer.parseInt(cursor.getString(2)));
                exerciseGroup.set_current(Integer.parseInt(cursor.getString(3)));
                exerciseGroups.add(exerciseGroup);
            } while (cursor.moveToNext());
        }
        db.close();
        return exerciseGroups;
    }

    @Override
    public List<Exercise> getAllExercisesByID(long id) {
        List<Exercise> exercises = new ArrayList<Exercise>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_ID, KEY_GROUP_ID,
                        KEY_VALUE_ARRAY, KEY_VALUE_ADD, KEY_VALUE_AVG, KEY_VALUE_MAX,
                        KEY_COUNTER_ADD, KEY_MILLS, KEY_DATE}, KEY_GROUP_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        Log.d(TAG, "Get all Exercise by ID");
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setID(Integer.parseInt(cursor.getString(0)));
                exercise.set_group_id(id);
                exercise.setValue(cursor.getString(2));
                exercise.setAddValue(Double.parseDouble(cursor.getString(3)));
                exercise.setAvgValue(Double.parseDouble(cursor.getString(4)));
                exercise.setMaxValue(Double.parseDouble(cursor.getString(5)));
                exercise.setAddCounter(Long.parseLong(cursor.getString(6)));
                exercise.set_elapsedMillis(Long.parseLong(cursor.getString(7)));
                exercise.set_date(cursor.getString(8));
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        db.close();
        return exercises;
    }

    @Override
    public int updateExercise(Exercise exercise) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Update Exercise - " + String.valueOf(exercise.getID()));
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, exercise.get_group_id());
        values.put(KEY_VALUE_ARRAY, exercise.getValue());
        values.put(KEY_VALUE_ADD, exercise.getAddValue());
        values.put(KEY_VALUE_AVG, exercise.getAvgValue());
        values.put(KEY_VALUE_MAX, exercise.getMaxValue());
        values.put(KEY_COUNTER_ADD, exercise.getAddCounter());
        values.put(KEY_MILLS, exercise.get_elapsedMillis());
        values.put(KEY_DATE, exercise.get_date());
        return db.update(TABLE_EXERCISE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(exercise.getID()) });

    }

    @Override
    public int updateExerciseGroup(ExerciseGroup exerciseGroup) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Update ExerciseGroup - " + exerciseGroup.getTitle() + " - " + String.valueOf(exerciseGroup.getID()));
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exerciseGroup.getTitle());
        values.put(KEY_COUNT, exerciseGroup.get_count());
        values.put(KEY_CURRENT, exerciseGroup.is_current());

        return db.update(TABLE_EXERCISE_GROUP, values, KEY_EXERCISE_GROUP_ID + " = ?",
                new String[] { String.valueOf(exerciseGroup.getID()) });

    }

    @Override
    public void deleteExercise(Exercise exercise) {
        Log.d(TAG, "Delete Exercise id- " + String.valueOf(exercise.getID()) + " group id - " + String.valueOf(exercise.get_group_id()));
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, KEY_ID + " = ?", new String[] { String.valueOf(exercise.getID()) });
        db.close();
    }

    @Override
    public void deleteExerciseGroup(ExerciseGroup exerciseGroup) {
        Log.d(TAG, "Delete ExerciseGroup - " + exerciseGroup.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        long id = exerciseGroup.getID();
        db.delete(TABLE_EXERCISE, KEY_GROUP_ID + " = ?", new String[] { String.valueOf(id) });
        db.delete(TABLE_EXERCISE_GROUP, KEY_EXERCISE_GROUP_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, null, null);
        db.delete(TABLE_EXERCISE_GROUP, null, null);
        db.close();
    }

    @Override
    public int getExerciseGroupCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXERCISE_GROUP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    @Override
    public void setSetting(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEVICE_NAME, settings.get_name());
        values.put(KEY_DEVICE_ADDRESS, settings.get_address());
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0){
        l = db.insert(TABLE_SETTINGS, null, values);}
        else {l = db.update(TABLE_SETTINGS,values, null, null);}
        Log.d("BTState", "Insert - " + Long.toString(l));
        db.close();
    }

    @Override
    public Settings getSettings() {
        Log.d("BTState", "Settings getSettings()");
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("BTState", "count= "+ Integer.toString(cursor.getCount()));
        if (cursor.moveToFirst()) {
            Settings settings = new Settings();
            settings.set_id(Integer.parseInt(cursor.getString(0)));
            settings.set_name(cursor.getString(1));
            settings.set_address(cursor.getString(2));
            cursor.close();
            return settings;
        }
        db.close();
        return null;

    }
}

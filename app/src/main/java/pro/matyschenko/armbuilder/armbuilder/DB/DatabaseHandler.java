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
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE_ARRAY = "value_array";
    private static final String KEY_VALUE_ADD = "add_value";
    private static final String KEY_VALUE_AVG = "avg_value";
    private static final String KEY_VALUE_MAX = "max_value";
    private static final String KEY_COUNTER_ADD = "add_counter";
    private static final String KEY_MILLS = "elapsedMillis";
    private static final String TABLE_SETTINGS = "settings";
    private static final String KEY_DEVICE_NAME = "device_name";
    private static final String KEY_DEVICE_ADDRESS = "device_address";

    final String TAG = "DatabaseHandler";
    long l;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("BTState", "onCreate(SQLiteDatabase db)");
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_VALUE_ARRAY + " TEXT," + KEY_VALUE_ADD + " DOUBLE,"
                + KEY_VALUE_AVG + " DOUBLE," + KEY_VALUE_MAX + " DOUBLE,"
                + KEY_COUNTER_ADD + " LONG," + KEY_MILLS + " LONG" + ")";
        db.execSQL(CREATE_EXERCISE_TABLE);
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DEVICE_NAME + " TEXT,"
                + KEY_DEVICE_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_SETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    @Override
    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_VALUE_ARRAY, exercise.getValue());
        values.put(KEY_VALUE_ADD, exercise.getAddValue());
        values.put(KEY_VALUE_AVG, exercise.getAvgValue());
        values.put(KEY_VALUE_MAX, exercise.getMaxValue());
        values.put(KEY_COUNTER_ADD, exercise.getAddCounter());
        values.put(KEY_MILLS, exercise.get_elapsedMillis());
        l = db.insert(TABLE_EXERCISE, null, values);
        Log.d(TAG, "Insert - " + Long.toString(l));
        db.close();
    }

    @Override
    public Exercise getExercise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_ID,
                        KEY_NAME, KEY_VALUE_ARRAY, KEY_VALUE_ADD, KEY_VALUE_AVG, KEY_VALUE_MAX,
                        KEY_COUNTER_ADD, KEY_MILLS}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getDouble(5), cursor.getLong(6),
                cursor.getLong(7));

        return exercise;
    }

    @Override
    public Exercise getByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_ID,
                        KEY_NAME, KEY_VALUE_ARRAY, KEY_VALUE_ADD, KEY_VALUE_AVG, KEY_VALUE_MAX,
                        KEY_COUNTER_ADD, KEY_MILLS}, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getDouble(5), cursor.getLong(6),
                cursor.getLong(7));

        return exercise;
    }

    @Override
    public List<Exercise> getAllExercises() {
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setID(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setValue(cursor.getString(2));
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        return exerciseList;
    }

    @Override
    public int updateExercise(Exercise exercise) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_VALUE_ARRAY, exercise.getValue());
        values.put(KEY_VALUE_ADD, exercise.getAddValue());
        values.put(KEY_VALUE_AVG, exercise.getAvgValue());
        values.put(KEY_VALUE_MAX, exercise.getMaxValue());
        values.put(KEY_COUNTER_ADD, exercise.getAddCounter());
        values.put(KEY_MILLS, exercise.get_elapsedMillis());

        return db.update(TABLE_EXERCISE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(exercise.getID()) });
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, KEY_ID + " = ?", new String[] { String.valueOf(exercise.getID()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, null, null);
        db.close();
    }

    @Override
    public int getExercisesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

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
        return null;

    }
}

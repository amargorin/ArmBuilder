package pro.matyschenko.armbuilder.armbuilder.DB;

public class ExerciseGroup {
    long _id;
    String _name;
    int _count;
    int _current;

    public ExerciseGroup(){
        this._name = "New exercise";
    }

    public ExerciseGroup(long id, String name, int count, int current){
        this._name = name;
        this._id = id;
        this._count = count;
        this._current = current;
    }

    public long getID(){
        return this._id;
    }

    public void setID(long id){
        this._id = id;
    }

    public String getTitle(){
        return this._name;
    }

    public void setTitle(String name){
        this._name = name;
    }

    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
    }

    public int is_current() {
        return _current;
    }

    public void set_current(int _current) {
        this._current = _current;
    }
}


package pro.matyschenko.armbuilder.armbuilder.DB;

public class Exercise {
    long _id;
    long _group_id;
    String _value_array;
    double _add_value;
    double _avg_value ;
    double _max;
    long _add_counter;
    long _elapsedMillis;
    String _date;

    public Exercise(){
    }

    public Exercise(long group_id, String value_array, double add_value, double avg_value, double max, long add_counter, long elapsedMillis, String date){
        this._value_array = value_array;
        this._group_id = group_id;
        this._max = max;
        this._add_value = add_value;
        this._avg_value = avg_value;
        this._add_counter = add_counter;
        this._elapsedMillis = elapsedMillis;
        this._date = date;
    }

    public long getID(){
        return this._id;
    }

    public void setID(long id){
        this._id = id;
    }

    public long get_group_id() {
        return _group_id;
    }

    public void set_group_id(long _group_id) {
        this._group_id = _group_id;
    }

    public String getValue(){
        return this._value_array;
    }

    public void setValue(String value_array){
        this._value_array = value_array;
    }

    public double getMaxValue(){
        return this._max;
    }

    public void setMaxValue(double max){
        this._max = max;
    }

    public double getAddValue(){
        return this._add_value;
    }

    public void setAddValue(double add_value){
        this._add_value= add_value;
    }

    public double getAvgValue(){
        return this._avg_value;
    }

    public void setAvgValue(double avg_value){
        this._avg_value = avg_value;
    }

    public long getAddCounter(){
        return this._add_counter;
    }

    public void setAddCounter(long add_counter){
        this._add_counter = add_counter;
    }

    public long get_elapsedMillis(){
        return this._elapsedMillis;
    }

    public void set_elapsedMillis(long elapsedMillis){
        this._elapsedMillis = elapsedMillis;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }
}

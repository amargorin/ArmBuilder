package pro.matyschenko.armbuilder.armbuilder.DB;

public class Exercise {
    int _id;
    String _name;
    String _value_array;
    double _add_value;
    double _avg_value ;
    double _max;
    long _add_counter;
    long _elapsedMillis;

    public Exercise(){
    }

    public Exercise(int id, String name, String value_array, double add_value, double avg_value, double max, long add_counter, long elapsedMillis){
        this._name = name;
        this._value_array = value_array;
        this._id = id;
        this._max = max;
        this._add_value = add_value;
        this._avg_value = avg_value;
        this._add_counter = add_counter;
        this._elapsedMillis = elapsedMillis;

    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
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
}

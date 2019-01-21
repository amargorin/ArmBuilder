package pro.matyschenko.armbuilder.armbuilder.DB;

public class Settings {
    int _id;
    int _threshold;
    String _name;
    String _address;

    public Settings(){
    }

    public Settings(String name, String address, int threshold)  {
        this._name = name;
        this._address = address;
        this._threshold = threshold;


    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public int get_threshold() {
        return _threshold;
    }

    public void set_threshold(int _threshold) {
        this._threshold = _threshold;
    }
}

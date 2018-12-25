package pro.matyschenko.armbuilder.armbuilder.dto;

public class BluetoothDTO {
    private String title;

    private String address;

    public BluetoothDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
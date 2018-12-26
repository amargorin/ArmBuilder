package pro.matyschenko.armbuilder.armbuilder.dto;


public class BluetoothDTO {
    private String title;
    private String address;
    private boolean connected;

    public BluetoothDTO(String title, String address) {
        this.title = title;
        this.address = address;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
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
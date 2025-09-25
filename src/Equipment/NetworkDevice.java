package Equipment;

/**
 * Класс для сетевых устройств.
 */
public class NetworkDevice extends Equipment {
    private String protocol;
    private int speedMbps;

    public NetworkDevice(int id, String name, String manufacturer, double price, int year, String description,
                         String protocol, int speedMbps) {
        super(id, name, manufacturer, price, year, description);
        this.protocol = protocol;
        this.speedMbps = speedMbps;
    }

    public String getProtocol() { return protocol; }
    public int getSpeedMbps() { return speedMbps; }

    public void setProtocol(String protocol) { this.protocol = protocol; }
    public void setSpeedMbps(int speedMbps) {this.speedMbps = speedMbps; }

}

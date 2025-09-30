package Equipment;

/**
 * Класс для сетевых устройств.
 */
public class NetworkDevice extends Equipment {
    private String protocol;
    private int speedMbps;

    public NetworkDevice(String name, String manufacturer, double price, int year, String description,
                         String protocol, int speedMbps) {
        super(name, manufacturer, price, year, description);
        this.protocol = protocol;
        this.speedMbps = speedMbps;
    }

    public String getProtocol() { return protocol; }
    public int getSpeedMbps() { return speedMbps; }

    public void setProtocol(String protocol) { this.protocol = protocol; }
    public void setSpeedMbps(int speedMbps) {this.speedMbps = speedMbps; }

    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.NETWORK_DEVICE;
    };

    @Override
    public String serialize() {
        return super.serialize() + DELIMITER +
                protocol + DELIMITER +
                speedMbps;
    }


}

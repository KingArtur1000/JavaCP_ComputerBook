package Equipment;

/**
 * Класс для периферийных устройств.
 */
public class Peripheral extends Equipment {
    private String type;

    public Peripheral(String name, String manufacturer, double price, int year, String description,
                      String type) {
        super(name, manufacturer, price, year, description);
        this.type = type;
    }

    public String getPeripheralType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.PERIPHERAL;
    };

    @Override
    public String serialize() {
        return super.serialize() + DELIMITER + type;
    }

}

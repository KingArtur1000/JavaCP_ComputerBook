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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

package Equipment;

/**
 * Класс для периферийных устройств.
 */
public class Peripheral extends Equipment {
    private String type;

    public Peripheral(int id, String name, String manufacturer, double price, int year, String description,
                      String type) {
        super(id, name, manufacturer, price, year, description);
        this.type = type;
    }
}

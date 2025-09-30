package Equipment;

/**
 * Базовый класс для любого оборудования.
 */
public abstract class Equipment {
    protected String name;
    protected String manufacturer;
    protected double price;
    protected int year;
    protected String description;

    public Equipment(String name, String manufacturer, double price, int year, String description) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.year = year;
        this.description = description;
    }

    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public double getPrice() { return price; }
    public int getYear() { return year; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + " (" + manufacturer + ")";
    }
}

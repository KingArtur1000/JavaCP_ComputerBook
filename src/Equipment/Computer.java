package Equipment;

/**
 * Класс для описания компьютеров.
 */
public class Computer extends Equipment {
    private String cpu;
    private int ram;
    private int storage;

    public Computer(int id, String name, String manufacturer, double price, int year, String description,
                    String cpu, int ram, int storage) {
        super(id, name, manufacturer, price, year, description);
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
    }
}

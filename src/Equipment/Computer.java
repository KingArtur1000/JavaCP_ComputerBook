package Equipment;

/**
 * Класс для описания компьютеров.
 */
public class Computer extends Equipment {
    private String cpu;
    private int ram;
    private int storage;

    public Computer(String name, String manufacturer, double price, int year, String description,
                    String cpu, int ram, int storage) {
        super(name, manufacturer, price, year, description);
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
    }

    public String getCpu() { return cpu; }
    public int getRam() { return ram; }
    public int getStorage() { return storage; }

    public void setCpu(String cpu) { this.cpu = cpu; }
    public void setRam(int ram) { this.ram = ram; }
    public void setStorage(int storage) { this.storage = storage; }

    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.COMPUTER;
    };

    @Override
    public String serialize() {
        return super.serialize() + DELIMITER +
                cpu + DELIMITER +
                ram + DELIMITER +
                storage;
    }

}

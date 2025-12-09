package Equipment;

/**
 * Класс {@code Computer} описывает объект типа "Компьютер"
 * и наследует общие характеристики от класса {@link Equipment}.
 * <p>
 * Дополнительно хранит сведения о процессоре, объёме оперативной памяти и хранилище.
 */
public class Computer extends Equipment {
    /** Модель или название процессора. */
    private String cpu;

    /** Объём оперативной памяти (в мегабайтах или гигабайтах, в зависимости от соглашения). */
    private int ram;

    /** Объём хранилища (в мегабайтах или гигабайтах, в зависимости от соглашения). */
    private int storage;

    /**
     * Конструктор для создания объекта {@code Computer}.
     *
     * @param name        название оборудования
     * @param manufacturer производитель
     * @param price       цена
     * @param year        год выпуска
     * @param description описание
     * @param cpu         модель процессора
     * @param ram         объём оперативной памяти
     * @param storage     объём хранилища
     */
    public Computer(String name, String manufacturer, double price, int year, String description,
                    String cpu, int ram, int storage) {
        super(name, manufacturer, price, year, description);
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
    }

    /**
     * Возвращает модель процессора.
     *
     * @return строка с названием процессора
     */
    public String getCpu() { return cpu; }

    /**
     * Возвращает объём оперативной памяти.
     *
     * @return значение RAM
     */
    public int getRam() { return ram; }

    /**
     * Возвращает объём хранилища.
     *
     * @return значение хранилища
     */
    public int getStorage() { return storage; }

    /**
     * Устанавливает модель процессора.
     *
     * @param cpu новая модель процессора
     */
    public void setCpu(String cpu) { this.cpu = cpu; }

    /**
     * Устанавливает объём оперативной памяти.
     *
     * @param ram новый объём RAM
     */
    public void setRam(int ram) { this.ram = ram; }

    /**
     * Устанавливает объём хранилища.
     *
     * @param storage новый объём хранилища
     */
    public void setStorage(int storage) { this.storage = storage; }

    /**
     * Определяет тип оборудования.
     *
     * @return {@link EQ_TYPES#COMPUTER} — тип "Компьютер"
     */
    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.COMPUTER;
    }

    /**
     * Сериализует объект {@code Computer} в строку, объединяя все поля через {@code DELIMITER}.
     * <p>
     * Формат: TYPE + DELIMITER + Equipment.serialize() + DELIMITER + cpu + DELIMITER + ram + DELIMITER + storage
     *
     * @return строка с сериализованными данными
     */
    @Override
    public String serialize() {
        return  getType() + DELIMITER +
                super.serialize() + DELIMITER +
                cpu + DELIMITER +
                ram + DELIMITER +
                storage;
    }
}

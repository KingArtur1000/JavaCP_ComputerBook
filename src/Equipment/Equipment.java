package Equipment;

/**
 * Абстрактный базовый класс {@code Equipment}, описывающий общее оборудование.
 * <p>
 * Содержит основные характеристики: название, производитель, цена, год выпуска и описание.
 * <br>
 * Конкретные типы оборудования должны наследовать этот класс и реализовывать метод {@link #getType()}.
 */
public abstract class Equipment {
    /** Название оборудования. */
    protected String name;

    /** Производитель оборудования. */
    protected String manufacturer;

    /** Цена оборудования. */
    protected double price;

    /** Год выпуска оборудования. */
    protected int year;

    /** Описание оборудования. */
    protected String description;

    /**
     * Перечисление типов оборудования.
     * <ul>
     *     <li>{@link EQ_TYPES#COMPUTER} — компьютер</li>
     *     <li>{@link EQ_TYPES#PERIPHERAL} — периферийное устройство</li>
     *     <li>{@link EQ_TYPES#NETWORK_DEVICE} — сетевое устройство</li>
     * </ul>
     */
    public enum EQ_TYPES {
        COMPUTER,
        PERIPHERAL,
        NETWORK_DEVICE
    }

    /** Разделитель для сериализации данных. */
    protected static final String DELIMITER = ";";

    /**
     * Конструктор для создания объекта {@code Equipment}.
     *
     * @param name        название оборудования
     * @param manufacturer производитель
     * @param price       цена
     * @param year        год выпуска
     * @param description описание
     */
    public Equipment(String name, String manufacturer, double price, int year, String description) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.year = year;
        this.description = description;
    }

    /**
     * Возвращает название оборудования.
     *
     * @return строка с названием
     */
    public String getName() { return name; }

    /**
     * Возвращает производителя оборудования.
     *
     * @return строка с названием производителя
     */
    public String getManufacturer() { return manufacturer; }

    /**
     * Возвращает цену оборудования.
     *
     * @return цена
     */
    public double getPrice() { return price; }

    /**
     * Возвращает год выпуска оборудования.
     *
     * @return год выпуска
     */
    public int getYear() { return year; }

    /**
     * Возвращает описание оборудования.
     *
     * @return строка с описанием
     */
    public String getDescription() { return description; }

    /**
     * Возвращает строковое представление объекта.
     * <p>
     * Формат: {@code name (manufacturer)}.
     *
     * @return строка с названием и производителем
     */
    @Override
    public String toString() {
        return name + " (" + manufacturer + ")";
    }

    /**
     * Абстрактный метод для определения типа оборудования.
     * <p>
     * Должен быть реализован в подклассах.
     *
     * @return тип оборудования из {@link EQ_TYPES}
     */
    protected abstract EQ_TYPES getType();

    /**
     * Сериализует объект {@code Equipment} в строку, объединяя все поля через {@link #DELIMITER}.
     * <p>
     * Формат: name + DELIMITER + manufacturer + DELIMITER + price + DELIMITER + year + DELIMITER + description
     *
     * @return строка с сериализованными данными
     */
    public String serialize() {
        return  name + DELIMITER +
                manufacturer + DELIMITER +
                price + DELIMITER +
                year + DELIMITER +
                description;
    }
}

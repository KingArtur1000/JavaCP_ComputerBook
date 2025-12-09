package Equipment;

/**
 * Класс {@code Peripheral} описывает периферийное устройство
 * и наследует общие характеристики от класса {@link Equipment}.
 * <p>
 * Дополнительно хранит сведения о типе периферии (например, клавиатура, мышь, принтер).
 */
public class Peripheral extends Equipment {
    /** Тип периферийного устройства (например, "Keyboard", "Mouse", "Printer"). */
    private String type;

    /**
     * Конструктор для создания объекта {@code Peripheral}.
     *
     * @param name         название оборудования
     * @param manufacturer производитель
     * @param price        цена
     * @param year         год выпуска
     * @param description  описание
     * @param type         тип периферийного устройства
     */
    public Peripheral(String name, String manufacturer, double price, int year, String description,
                      String type) {
        super(name, manufacturer, price, year, description);
        this.type = type;
    }

    /**
     * Возвращает тип периферийного устройства.
     *
     * @return строка с типом устройства
     */
    public String getPeripheralType() { return type; }

    /**
     * Устанавливает тип периферийного устройства.
     *
     * @param type новый тип устройства
     */
    public void setType(String type) { this.type = type; }

    /**
     * Определяет тип оборудования.
     *
     * @return {@link EQ_TYPES#PERIPHERAL} — тип "Периферийное устройство"
     */
    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.PERIPHERAL;
    }

    /**
     * Сериализует объект {@code Peripheral} в строку, объединяя все поля через {@code DELIMITER}.
     * <p>
     * Формат: TYPE + DELIMITER + Equipment.serialize() + DELIMITER + type
     *
     * @return строка с сериализованными данными
     */
    @Override
    public String serialize() {
        return  getType() + DELIMITER +
                super.serialize() + DELIMITER +
                type;
    }
}

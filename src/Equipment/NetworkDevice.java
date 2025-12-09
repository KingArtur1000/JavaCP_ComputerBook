package Equipment;

/**
 * Класс {@code NetworkDevice} описывает сетевое устройство
 * и наследует общие характеристики от класса {@link Equipment}.
 * <p>
 * Дополнительно хранит сведения о поддерживаемом сетевом протоколе и скорости передачи данных.
 */
public class NetworkDevice extends Equipment {
    /** Сетевой протокол, поддерживаемый устройством (например, Ethernet, Wi-Fi). */
    private String protocol;

    /** Скорость передачи данных в мегабитах в секунду (Mbps). */
    private int speedMbps;

    /**
     * Конструктор для создания объекта {@code NetworkDevice}.
     *
     * @param name         название оборудования
     * @param manufacturer производитель
     * @param price        цена
     * @param year         год выпуска
     * @param description  описание
     * @param protocol     сетевой протокол
     * @param speedMbps    скорость передачи данных (в Mbps)
     */
    public NetworkDevice(String name, String manufacturer, double price, int year, String description,
                         String protocol, int speedMbps) {
        super(name, manufacturer, price, year, description);
        this.protocol = protocol;
        this.speedMbps = speedMbps;
    }

    /**
     * Возвращает сетевой протокол.
     *
     * @return строка с названием протокола
     */
    public String getProtocol() { return protocol; }

    /**
     * Возвращает скорость передачи данных.
     *
     * @return скорость в мегабитах в секунду
     */
    public int getSpeedMbps() { return speedMbps; }

    /**
     * Устанавливает сетевой протокол.
     *
     * @param protocol новый сетевой протокол
     */
    public void setProtocol(String protocol) { this.protocol = protocol; }

    /**
     * Устанавливает скорость передачи данных.
     *
     * @param speedMbps новая скорость (в Mbps)
     */
    public void setSpeedMbps(int speedMbps) { this.speedMbps = speedMbps; }

    /**
     * Определяет тип оборудования.
     *
     * @return {@link EQ_TYPES#NETWORK_DEVICE} — тип "Сетевое устройство"
     */
    @Override
    protected EQ_TYPES getType() {
        return EQ_TYPES.NETWORK_DEVICE;
    }

    /**
     * Сериализует объект {@code NetworkDevice} в строку, объединяя все поля через {@code DELIMITER}.
     * <p>
     * Формат: TYPE + DELIMITER + Equipment.serialize() + DELIMITER + protocol + DELIMITER + speedMbps
     *
     * @return строка с сериализованными данными
     */
    @Override
    public String serialize() {
        return  getType() + DELIMITER +
                super.serialize() + DELIMITER +
                protocol + DELIMITER +
                speedMbps;
    }
}

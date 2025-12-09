import Equipment.*;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@code FileManager} отвечает за сохранение и загрузку каталога оборудования в CSV-файл.
 * <p>
 * Формат CSV:
 * <ul>
 *     <li>Первая колонка — тип оборудования ({@link Equipment.EQ_TYPES})</li>
 *     <li>Далее — базовые поля: name, manufacturer, price, year, description</li>
 *     <li>Затем — дополнительные поля в зависимости от типа:
 *         <ul>
 *             <li>{@link Computer}: cpu, ram, storage</li>
 *             <li>{@link Peripheral}: type</li>
 *             <li>{@link NetworkDevice}: protocol, speed</li>
 *         </ul>
 *     </li>
 * </ul>
 */
public class FileManager {
    /** Разделитель для CSV-файла. */
    private static final String DELIMITER = ";";

    /**
     * Сохраняет каталог оборудования в указанный CSV-файл.
     *
     * @param catalog каталог оборудования
     * @param file    файл для сохранения
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void saveCatalog(EquipmentCatalog catalog, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Equipment eq : catalog.getAll()) {
                writer.write(eq.serialize());
                writer.newLine();
            }
        }
    }

    /**
     * Загружает каталог оборудования из CSV-файла.
     *
     * @param file файл для загрузки
     * @return список объектов {@link Equipment}, считанных из файла
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static List<Equipment> loadCatalog(File file) throws IOException {
        List<Equipment> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Equipment eq = deserialize(line);
                if (eq != null) list.add(eq);
            }
        }
        return list;
    }

    /**
     * Десериализует строку CSV в объект {@link Equipment}.
     * <p>
     * В зависимости от типа оборудования создаётся соответствующий объект:
     * <ul>
     *     <li>{@link Computer}</li>
     *     <li>{@link Peripheral}</li>
     *     <li>{@link NetworkDevice}</li>
     * </ul>
     * Если тип неизвестен или произошла ошибка — возвращается {@code null}.
     *
     * @param line строка CSV
     * @return объект {@link Equipment}, либо {@code null}, если десериализация не удалась
     */
    private static Equipment deserialize(String line) {
        try {
            String[] parts = line.split(DELIMITER);
            Equipment.EQ_TYPES eq_type = Equipment.EQ_TYPES.valueOf(parts[0]);
            String name = parts[1];
            String manufacturer = parts[2];
            double price = Double.parseDouble(parts[3]);
            int year = Integer.parseInt(parts[4]);
            String description = parts[5];

            switch (eq_type) {
                case COMPUTER -> {
                    String cpu = parts[6];
                    int ram = Integer.parseInt(parts[7]);
                    int storage = Integer.parseInt(parts[8]);
                    return new Computer(name, manufacturer, price, year, description, cpu, ram, storage);
                }
                case PERIPHERAL -> {
                    String peripheralType = parts[6];
                    return new Peripheral(name, manufacturer, price, year, description, peripheralType);
                }
                case NETWORK_DEVICE -> {
                    String protocol = parts[6];
                    int speed = Integer.parseInt(parts[7]);
                    return new NetworkDevice(name, manufacturer, price, year, description, protocol, speed);
                }
                default -> {
                    System.out.println("Неизвестный тип устройства: " + eq_type);
                }
            }

            // fallback — создаём generic Peripheral
            return new Peripheral(name, manufacturer, price, year, description, "Generic");
        } catch (Exception e) {
            return null;
        }
    }
}

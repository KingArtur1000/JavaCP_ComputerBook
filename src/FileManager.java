import Equipment.*;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для сохранения и загрузки каталога оборудования в CSV-файл.
 */
public class FileManager {
    private static final String DELIMITER = ";";

    public static void saveCatalog(EquipmentCatalog catalog, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Equipment eq : catalog.getAll()) {
                writer.write(eq.serialize());
                writer.newLine();
            }
        }
    }

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
                    int storage =  Integer.parseInt(parts[8]);
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
                default -> { System.out.println("Неизвестный тип устройства!" + eq_type); }
            }

            return new Peripheral(name, manufacturer, price, year, description, "Generic");
        } catch (Exception e) {
            return null;
        }
    }
}

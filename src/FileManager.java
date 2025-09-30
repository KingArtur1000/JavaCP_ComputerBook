import Equipment.*;

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
                writer.write(serialize(eq));
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

    private static String serialize(Equipment eq) {
        return  eq.getName() + DELIMITER +
                eq.getManufacturer() + DELIMITER +
                eq.getPrice() + DELIMITER +
                eq.getYear() + DELIMITER +
                eq.getDescription();
    }

    private static Equipment deserialize(String line) {
        try {
            String[] parts = line.split(DELIMITER);
            String name = parts[0];
            String manufacturer = parts[1];
            double price = Double.parseDouble(parts[2]);
            int year = Integer.parseInt(parts[3]);
            String description = parts[4];
            return new Peripheral(name, manufacturer, price, year, description, "Generic");
        } catch (Exception e) {
            return null;
        }
    }
}

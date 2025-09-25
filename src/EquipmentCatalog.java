import Equipment.Equipment;

import java.util.ArrayList;
import java.util.List;

/**
 * Каталог оборудования. Хранит список и предоставляет методы управления.
 */
public class EquipmentCatalog {
    private List<Equipment> items = new ArrayList<>();

    public void addEquipment(Equipment eq) {
        items.add(eq);
    }

    public void removeEquipment(int id) {
        items.removeIf(e -> e.getId() == id);
    }

    public List<Equipment> findByName(String name) {
        return items.stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<Equipment> getAll() {
        return new ArrayList<>(items);
    }
}

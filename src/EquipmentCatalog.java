import Equipment.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Каталог оборудования. Хранит список и предоставляет методы управления.
 */
public class EquipmentCatalog {
    private final List<Equipment> items = new ArrayList<>();

    public void addEquipment(Equipment eq) {
        items.add(eq);
    }

    public void removeEquipmentByName(String name) {
        items.removeIf(e -> Objects.equals(e.getName(), name));
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

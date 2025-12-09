import Equipment.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс {@code EquipmentCatalog} представляет каталог оборудования.
 * <p>
 * Хранит список объектов {@link Equipment} и предоставляет методы для управления ими:
 * <ul>
 *     <li>Добавление нового оборудования</li>
 *     <li>Удаление оборудования по имени</li>
 *     <li>Поиск оборудования по имени (частичное совпадение)</li>
 *     <li>Получение полного списка оборудования</li>
 * </ul>
 */
public class EquipmentCatalog {
    /** Список оборудования, хранящийся в каталоге. */
    private final List<Equipment> items = new ArrayList<>();

    /**
     * Добавляет объект оборудования в каталог.
     *
     * @param eq объект {@link Equipment}, который нужно добавить
     */
    public void addEquipment(Equipment eq) {
        items.add(eq);
    }

    /**
     * Удаляет оборудование из каталога по имени.
     * <p>
     * Удаляются все объекты, у которых имя совпадает с указанным.
     *
     * @param name имя оборудования для удаления
     */
    public void removeEquipmentByName(String name) {
        items.removeIf(e -> Objects.equals(e.getName(), name));
    }

    /**
     * Находит оборудование по имени.
     * <p>
     * Поиск выполняется по подстроке без учёта регистра.
     *
     * @param name часть имени оборудования
     * @return список объектов {@link Equipment}, удовлетворяющих условию поиска
     */
    public List<Equipment> findByName(String name) {
        return items.stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    /**
     * Возвращает полный список оборудования.
     * <p>
     * Возвращается копия списка, чтобы избежать прямого изменения внутреннего состояния.
     *
     * @return список всех объектов {@link Equipment}
     */
    public List<Equipment> getAll() {
        return new ArrayList<>(items);
    }
}

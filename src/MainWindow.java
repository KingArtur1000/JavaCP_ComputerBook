import Equipment.*;
import UI.EquipmentFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Главное окно приложения "Каталог компьютерной техники".
 *
 * @Author: N.S. Pavlovskiy
 *
 */
public class MainWindow extends JFrame {
    private EquipmentCatalog catalog;
    private JTable table;
    private DefaultTableModel tableModel;

    public MainWindow(EquipmentCatalog catalog) {
        this.catalog = catalog;
        setTitle("Каталог компьютерной техники");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // Создание объектов меню
        JMenuBar menuBar = getBar();

        // Назначаем панель меню
        setJMenuBar(menuBar);

        String[] columns = {
                "Название", "Производитель", "Цена", "Год", "Описание",
                "Тип", "CPU", "RAM", "Storage", "DeviceType", "Protocol", "Speed"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Добавляем возможность сортировки по столбцам
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Добавить");
        JButton removeButton = new JButton("Удалить");

        addButton.addActionListener(this::onAdd);
        removeButton.addActionListener(this::onRemove);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JMenuBar getBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu whatMenu = new JMenu("?");

        // Меню вкладки -Файл-
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem loadItem = new JMenuItem("Загрузить");

        // Меню вкладки -?-
        JMenuItem aboutAuthorItem = new JMenuItem("Об авторе");

        // Привязываемся к событиям
        saveItem.addActionListener(e -> onSave());
        loadItem.addActionListener(e -> onLoad());
        aboutAuthorItem.addActionListener(e -> onClickAboutAuthorItem());

        // Привязываем элементы к вкладке -Файл-
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        // Привязываем элементы к вкладке -?-
        whatMenu.add(aboutAuthorItem);

        // Привязываем вкладки к панели меню
        menuBar.add(fileMenu);
        menuBar.add(whatMenu);
        return menuBar;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Equipment eq : catalog.getAll()) {
            Object[] row = new Object[13];
            row[0] = eq.getName();
            row[1] = eq.getManufacturer();
            row[2] = eq.getPrice();
            row[3] = eq.getYear();
            row[4] = eq.getDescription();
            row[5] = eq.getClass().getSimpleName(); // Computer / Peripheral / NetworkDevice

            switch (eq) {
                case Computer c -> {
                    row[6] = c.getCpu();
                    row[7] = c.getRam();
                    row[8] = c.getStorage();
                }
                case Peripheral p -> row[9] = p.getPeripheralType();
                case NetworkDevice n -> {
                    row[10] = n.getProtocol();
                    row[11] = n.getSpeedMbps();
                }
                default -> {
                }
            }

            tableModel.addRow(row);
        }
    }


    private void onAdd(ActionEvent e) {
        EquipmentFormDialog dialog = new EquipmentFormDialog(this);
        dialog.setVisible(true);

        Equipment newEq = dialog.getResult();
        if (newEq != null) {
            catalog.addEquipment(newEq);
            refreshTable();
        }
    }

    private void onRemove(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            catalog.removeEquipmentByName(name);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку для удаления");
        }
    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser();

        // Задаём имя файла по умолчанию
        chooser.setSelectedFile(new File("catalog.csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FileManager.saveCatalog(catalog, chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Каталог сохранён");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    private void onLoad() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                catalog = new EquipmentCatalog();
                for (Equipment eq : FileManager.loadCatalog(chooser.getSelectedFile())) {
                    catalog.addEquipment(eq);
                }
                refreshTable();
                JOptionPane.showMessageDialog(this, "Каталог загружен");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }


    public void onClickAboutAuthorItem() {
        JOptionPane.showMessageDialog(this, "Автор: Павловский Никита Сергеевич - студент БНТУ :)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EquipmentCatalog catalog = new EquipmentCatalog();
            MainWindow window = new MainWindow(catalog);
            window.setVisible(true);
        });
    }
}

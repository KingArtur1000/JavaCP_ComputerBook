import Equipment.Equipment;
import UI.EquipmentFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        // Назначаем панель меню
        setJMenuBar(menuBar);

        String[] columns = {"ID", "Название", "Производитель", "Цена", "Год", "Описание"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Equipment eq : catalog.getAll()) {
            tableModel.addRow(new Object[]{
                    eq.getId(),
                    eq.getName(),
                    eq.getManufacturer(),
                    eq.getPrice(),
                    eq.getYear(),
                    eq.getDescription()
            });
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
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            catalog.removeEquipment(id);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку для удаления");
        }
    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser();
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

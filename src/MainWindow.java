import Equipment.*;
import UI.EquipmentFormDialog;
import UI.FontSizeChangerFormDialog;
import Utils.FontShortcutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Главное окно приложения {@code "Каталог компьютерной техники"}.
 * <p>
 * Отвечает за отображение таблицы оборудования, меню управления и кнопок действий.
 * Поддерживает:
 * <ul>
 *     <li>Добавление нового оборудования</li>
 *     <li>Удаление выбранного оборудования</li>
 *     <li>Сохранение и загрузку каталога из CSV-файла</li>
 *     <li>Изменение размера шрифта таблицы</li>
 *     <li>Отображение информации об авторе</li>
 * </ul>
 *
 * @author N.S. Pavlovskiy
 */
public class MainWindow extends JFrame {
    /** Каталог оборудования. */
    private EquipmentCatalog catalog;
    /** Таблица для отображения оборудования. */
    private JTable table;
    /** Модель таблицы. */
    private DefaultTableModel tableModel;

    /**
     * Конструктор главного окна.
     *
     * @param catalog каталог оборудования
     */
    public MainWindow(EquipmentCatalog catalog) {
        this.catalog = catalog;
        setTitle("Каталог компьютерной техники");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    /**
     * Инициализация пользовательского интерфейса:
     * <ul>
     *     <li>Создание меню</li>
     *     <li>Настройка таблицы</li>
     *     <li>Добавление кнопок управления</li>
     * </ul>
     */
    private void initUI() {
        // Создание объектов меню
        JMenuBar menuBar = getBar();

        // Назначаем панель меню
        setJMenuBar(menuBar);

        String[] columns = {
                "Тип устройства", "Название", "Производитель", "Цена", "Год", "Описание",
                "Процессор", "RAM", "Хранилище", "Тип периферии", "Протокол", "Скорость (Mb/s)"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Шрифт по умолчанию для заголовков в таблице
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFont(new Font("Arial", Font.PLAIN, 16));

        // Добавляем возможность сортировки по столбцам
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        // Привязываемся к обработчику комбинаций горячих клавиш
        new FontShortcutManager(table, scrollPane);
        table.requestFocusInWindow();

        JButton addButton = new JButton("Добавить");
        addButton.setToolTipText("Выводит окно для добавления компьютерного оборудования");
        addButton.setBackground(new Color(95, 212, 124));
        addButton.setForeground(new Color(255, 255, 255));
        JButton removeButton = new JButton("Удалить");
        removeButton.setToolTipText("Удаляет выбранную запись");
        removeButton.setBackground(new Color(216, 53, 53));
        removeButton.setForeground(new Color(255, 255, 255));
        JButton exitButton = new JButton("Выход");
        exitButton.setToolTipText("Выйти из программы");


        addButton.addActionListener(this::onAdd);
        removeButton.addActionListener(this::onRemove);
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,                                   // родительское окно
                    "Вы действительно хотите выйти?",       // сообщение
                    "Подтверждение выхода",                 // заголовок
                    JOptionPane.YES_NO_OPTION,              // кнопки
                    JOptionPane.QUESTION_MESSAGE            // иконка
            );

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });


        JPanel buttonPanel = new JPanel(new BorderLayout());

        JPanel centerBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerBottomPanel.add(addButton);
        centerBottomPanel.add(removeButton);

        buttonPanel.add(centerBottomPanel, BorderLayout.CENTER);
        buttonPanel.add(exitButton, BorderLayout.EAST);


        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    /**
     * Создаёт меню приложения.
     *
     * @return панель меню {@link JMenuBar}
     */
    private JMenuBar getBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu settingsMenu = new JMenu("Настройки");
        JMenu whatMenu = new JMenu("?");

        // Меню вкладки -Файл-
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem loadItem = new JMenuItem("Загрузить");

        // Меню вкладки -Настройки-
        JMenuItem fontSizeItem = new JMenuItem("Размер шрифта");

        // Меню вкладки -?-
        JMenuItem aboutAuthorItem = new JMenuItem("Об авторе");

        // Привязываемся к событиям
        saveItem.addActionListener(e -> onSave());
        loadItem.addActionListener(e -> onLoad());
        aboutAuthorItem.addActionListener(e -> onClickAboutAuthorItem());
        fontSizeItem.addActionListener(e -> {
            FontSizeChangerFormDialog dialog = new FontSizeChangerFormDialog(this, table);
            dialog.setVisible(true);
        });

        // Привязываем элементы к вкладке -Файл-
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        // Привязываем элементы к вкладке -Настройки-
        settingsMenu.add(fontSizeItem);

        // Привязываем элементы к вкладке -?-
        whatMenu.add(aboutAuthorItem);

        // Привязываем вкладки к панели меню
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(whatMenu);
        return menuBar;
    }

    /**
     * Обновляет содержимое таблицы на основе текущего каталога.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Equipment eq : catalog.getAll()) {
            Object[] row = new Object[13];
            row[0] = eq.getClass().getSimpleName(); // Computer / Peripheral / NetworkDevice
            row[1] = eq.getName();
            row[2] = eq.getManufacturer();
            row[3] = eq.getPrice();
            row[4] = eq.getYear();
            row[5] = eq.getDescription();

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


    /**
     * Обработчик кнопки "Добавить".
     *
     * @param e событие
     */
    private void onAdd(ActionEvent e) {
        EquipmentFormDialog dialog = new EquipmentFormDialog(this);
        dialog.setVisible(true);

        Equipment newEq = dialog.getResult();
        if (newEq != null) {
            catalog.addEquipment(newEq);
            refreshTable();
        }
    }

    /**
     * Обработчик кнопки "Удалить".
     *
     * @param e событие
     */
    private void onRemove(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Вы действительно хотите удалить \"" + name + "\"?",
                    "Подтверждение удаления",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                catalog.removeEquipmentByName(name);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку для удаления");
        }
    }


    /**
     * Сохраняет каталог в CSV-файл.
     */
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

    /**
     * Загружает каталог из CSV-файла.
     */
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

    /**
     * Отображает информацию об авторе.
     */
    public void onClickAboutAuthorItem() {
        JOptionPane.showMessageDialog(this, "Автор: Павловский Никита Сергеевич - студент БНТУ :)");
    }

    /**
     * Точка входа в приложение.
     * <p>
     * Отображает заставку {@link SplashScreenWindow}, затем через 5 секунд
     * закрывает её и открывает главное окно {@link MainWindow}.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SplashScreenWindow splash = new SplashScreenWindow();
        splash.setVisible(true);

        // отдельный поток
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
            SwingUtilities.invokeLater(() -> {
                splash.dispose();
                EquipmentCatalog catalog = new EquipmentCatalog();
                MainWindow window = new MainWindow(catalog);
                window.setVisible(true);
            });
        }).start();
    }
}

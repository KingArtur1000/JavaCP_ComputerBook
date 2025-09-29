package UI;

import Equipment.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог для добавления оборудования.
 */
public class EquipmentFormDialog extends JDialog {
    private JTextField nameField, manufacturerField, priceField, yearField, descriptionField;
    private JComboBox<String> typeCombo;
    private JPanel dynamicPanel;
    private List<JTextField> extraFields = new ArrayList<>();
    private Equipment result;

    public EquipmentFormDialog(JFrame parent) {
        super(parent, "Добавить оборудование", true);

        // Системный стиль (Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setSize(500, 450);
        setLocationRelativeTo(parent);
        initForm();
    }

    private void initForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Заголовок
        JLabel title = new JLabel("Добавить оборудование", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        formPanel.add(title);

        // Тип устройства
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        typePanel.add(new JLabel("Тип устройства:"));
        typeCombo = new JComboBox<>(new String[]{"Computer", "Peripheral", "NetworkDevice"});
        typeCombo.addActionListener(e -> updateDynamicFields());
        typePanel.add(typeCombo);
        formPanel.add(typePanel);

        // Базовые поля
        formPanel.add(labeledField("Название:", nameField = new JTextField(), "Введите название устройства"));
        formPanel.add(labeledField("Производитель:", manufacturerField = new JTextField(), "Введите производителя"));
        formPanel.add(labeledField("Цена:", priceField = new JTextField(), "Например: 499.99"));
        formPanel.add(labeledField("Год:", yearField = new JTextField(), "Например: 2024"));
        formPanel.add(labeledField("Описание:", descriptionField = new JTextField(), "Краткое описание"));

        // Динамическая панель
        dynamicPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(dynamicPanel);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        // Стили кнопок
        styleButton(okButton, new Color(60, 179, 113));   // зелёный
        styleButton(cancelButton, new Color(220, 20, 60)); // красный

        okButton.addActionListener(this::onOk);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        add(new JScrollPane(formPanel));
        updateDynamicFields();
    }

    /** Удобный метод для подписанных полей с подсказкой */
    private JPanel labeledField(String label, JTextField field, String tooltip) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120, 25)); // фикс ширины для выравнивания
        panel.add(lbl, BorderLayout.WEST);

        field.setToolTipText(tooltip);
        styleTextField(field); // общий стиль
        panel.add(field, BorderLayout.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0)); // добавляем вертикальный зазор
        return panel;
    }




    /** Красивые кнопки */
    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 30));
    }

    /** Обновление динамических полей */
    private void updateDynamicFields() {
        dynamicPanel.removeAll();
        extraFields.clear();

        String selectedType = (String) typeCombo.getSelectedItem();
        if ("Computer".equals(selectedType)) {
            addDynamicField("CPU:");
            addDynamicField("RAM (GB):");
            addDynamicField("Storage (GB):");
        } else if ("Peripheral".equals(selectedType)) {
            addDynamicField("Тип устройства:");
        } else if ("NetworkDevice".equals(selectedType)) {
            addDynamicField("Протокол:");
            addDynamicField("Скорость (Mbps):");
        }

        dynamicPanel.revalidate();
        dynamicPanel.repaint();
        pack();
    }

    private void addDynamicField(String label) {
        dynamicPanel.add(new JLabel(label));
        JTextField field = new JTextField();
        styleTextField(field); // 🔑 одинаковый стиль
        dynamicPanel.add(field);
        extraFields.add(field);
    }


    private void onOk(ActionEvent e) {
        try {
            int id = (int) (Math.random() * 10000);
            String name = nameField.getText().trim();
            String manufacturer = manufacturerField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());
            String description = descriptionField.getText().trim();

            String selectedType = (String) typeCombo.getSelectedItem();

            switch (selectedType) {
                case "Computer" -> {
                    String cpu = extraFields.get(0).getText().trim();
                    int ram = Integer.parseInt(extraFields.get(1).getText().trim());
                    int storage = Integer.parseInt(extraFields.get(2).getText().trim());
                    result = new Computer(id, name, manufacturer, price, year, description, cpu, ram, storage);
                }
                case "Peripheral" -> {
                    String type = extraFields.get(0).getText().trim();
                    result = new Peripheral(id, name, manufacturer, price, year, description, type);
                }
                case "NetworkDevice" -> {
                    String protocol = extraFields.get(0).getText().trim();
                    int speed = Integer.parseInt(extraFields.get(1).getText().trim());
                    result = new NetworkDevice(id, name, manufacturer, price, year, description, protocol, speed);
                }
            }

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода: " + ex.getMessage());
        }
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(250, 25)); // 🔑 одинаковый размер
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // чтобы не растягивалось по высоте
    }



    public Equipment getResult() {
        return result;
    }
}

package UI;

import Equipment.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EquipmentFormDialog extends JDialog {
    private JTextField nameField, manufacturerField, priceField, yearField, descriptionField;
    private JComboBox<String> typeCombo;
    private JPanel dynamicPanel;
    private final List<JTextField> extraFields = new ArrayList<>();
    private Equipment result;

    private final String typeComputerString = "Компьютер";
    private final String typePeripheralString = "Периферия";
    private final String typeNetworkDeviceString = "Сетевое устройство";

    public EquipmentFormDialog(JFrame parent) {
        super(parent, "Добавить оборудование", true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Параметры окна с добавлением оборудования
        setSize(400, 500);
        setLocationRelativeTo(parent);
        initForm();
    }

    private void initForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // отступы
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Заголовок
        JLabel title = new JLabel("Добавить оборудование", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // в Swing GridBagConstraints по умолчанию ВСЕГДА сетка 2*2 (даже если нигде ранее не указывали gridx=1)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(title, gbc);
        row++;

        // Тип устройства
        gbc.gridwidth = 1;  // Аналог ColumnSpan в WPF
        gbc.gridx = 0; gbc.gridy = row; // Положение ячейки в сетке (Аналог Grid.Column, Grid.Row)
        gbc.weightx = 0.15; // Аналог * в ColumnStraits в WPF (сколько места должен занимать столбец)
        JLabel typeLabel = new JLabel("Тип устройства:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(typeLabel, gbc);

        typeCombo = new JComboBox<>(new String[]{typeComputerString, typePeripheralString, typeNetworkDeviceString});
        typeCombo.addActionListener(e -> updateDynamicFields());
        typeCombo.setFont(new Font("Arial", Font.BOLD, 14));
        typeCombo.setBackground(Color.WHITE);
        typeCombo.setForeground(Color.DARK_GRAY);
        typeCombo.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
        typeCombo.setPreferredSize(new Dimension(200, 28));
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);
        row++;

        // Базовые поля
        nameField = addLabeledField(formPanel, gbc, row++, "Название:");
        manufacturerField = addLabeledField(formPanel, gbc, row++, "Производитель:");
        priceField = addLabeledField(formPanel, gbc, row++, "Цена:");
        yearField = addLabeledField(formPanel, gbc, row++, "Год:");
        descriptionField = addLabeledField(formPanel, gbc, row++, "Описание:");

        // Динамическая панель (Отступы будут разные, т.к. дин. панель не связана напрямую с gbc базовых полей);
        // поэтому можно не пытаться подогнать Insets под базовые поля, и отступы считаются от самого объёмного label по тексту
        dynamicPanel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(0, 0, 0, 0); // отступы
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(dynamicPanel, gbc);
        row++;

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");
        styleButton(okButton, new Color(60, 179, 113));
        styleButton(cancelButton, new Color(220, 20, 60));
        okButton.addActionListener(this::onOk);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(new JScrollPane(formPanel));
        updateDynamicFields();
    }

    private JTextField addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String label) {
        JLabel lbl = new JLabel(label);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.15;
        panel.add(lbl, gbc);

        JTextField field = new JTextField();
        styleTextField(field);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);

        return field;
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    private void styleButton(JButton button, Color bg) {
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 30));
    }

    private void updateDynamicFields() {
        dynamicPanel.removeAll();
        extraFields.clear();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        String selectedType = (String) typeCombo.getSelectedItem();

        switch (selectedType) {
            case typeComputerString:
                addDynamicField("Процессор (CPU):", row++, gbc);
                addDynamicField("Оперативная память (RAM) (GB):", row++, gbc);
                addDynamicField("Хранилище Storage (GB):", row++, gbc);
                break;
            case typePeripheralString:
                addDynamicField("Тип:", row++, gbc);
                break;
            case typeNetworkDeviceString:
                addDynamicField("Протокол:", row++, gbc);
                addDynamicField("Скорость (Mbps):", row++, gbc);
                break;
            case null, default:
                break;
        }
        

        // Перерисовываем форму
        dynamicPanel.revalidate();
        dynamicPanel.repaint();

        // Полностью игнорирует setSize() и подгоняет окно под минимальные размеры всех вложенных компонентов
        //pack();
    }

    private void addDynamicField(String label, int row, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(label);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.15;
        gbc.insets = new Insets(5, 5, 5, 0);
        dynamicPanel.add(lbl, gbc);

        JTextField field = new JTextField();
        styleTextField(field);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        dynamicPanel.add(field, gbc);

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
                case typeComputerString -> {
                    String cpu = extraFields.get(0).getText().trim();
                    int ram = Integer.parseInt(extraFields.get(1).getText().trim());
                    int storage = Integer.parseInt(extraFields.get(2).getText().trim());
                    result = new Computer(id, name, manufacturer, price, year, description, cpu, ram, storage);
                }
                case typePeripheralString -> {
                    String type = extraFields.getFirst().getText().trim();
                    result = new Peripheral(id, name, manufacturer, price, year, description, type);
                }
                case typeNetworkDeviceString -> {
                    String protocol = extraFields.get(0).getText().trim();
                    int speed = Integer.parseInt(extraFields.get(1).getText().trim());
                    result = new NetworkDevice(id, name, manufacturer, price, year, description, protocol, speed);
                }
                case null -> {}
                default -> throw new IllegalStateException("Unexpected value: " + selectedType);
            }

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода: " + ex.getMessage());
        }
    }

    public Equipment getResult() {
        return result;
    }
}

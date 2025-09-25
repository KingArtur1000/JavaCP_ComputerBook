package UI;

import Equipment.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог для добавления оборудования.
 * Возвращает объект Equipment (Computer / Peripheral / NetworkDevice).
 */
public class EquipmentFormDialog extends JDialog {
    private JTextField nameField, manufacturerField, priceField, yearField, descriptionField;
    private JComboBox<String> typeCombo;
    private JPanel dynamicPanel; // панель для дополнительных полей
    private List<JTextField> extraFields = new ArrayList<>();
    private Equipment result;

    public EquipmentFormDialog(JFrame parent) {
        super(parent, "Добавить оборудование", true);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        initForm();
    }

    private void initForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // --- Тип устройства ---
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        typePanel.add(new JLabel("Тип устройства:"));
        typeCombo = new JComboBox<>(new String[]{"Computer", "Peripheral", "NetworkDevice"});
        typeCombo.addActionListener(e -> updateDynamicFields());
        typePanel.add(typeCombo);
        formPanel.add(typePanel);

        // --- Базовые поля ---
        formPanel.add(labeledField("Название:", nameField = new JTextField()));
        formPanel.add(labeledField("Производитель:", manufacturerField = new JTextField()));
        formPanel.add(labeledField("Цена:", priceField = new JTextField()));
        formPanel.add(labeledField("Год:", yearField = new JTextField()));
        formPanel.add(labeledField("Описание:", descriptionField = new JTextField()));

        // --- Динамическая панель ---
        dynamicPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(dynamicPanel);

        // --- Кнопки ---
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");
        okButton.addActionListener(this::onOk);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        add(new JScrollPane(formPanel));

        updateDynamicFields();
    }

    /** Удобный метод для подписанных полей */
    private JPanel labeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }

    /**
     * Обновление динамических полей в зависимости от выбранного типа.
     */
    private void updateDynamicFields() {
        dynamicPanel.removeAll();
        extraFields.clear();

        String selectedType = (String) typeCombo.getSelectedItem();
        if ("Computer".equals(selectedType)) {
            dynamicPanel.add(new JLabel("CPU:"));
            JTextField cpuField = new JTextField();
            dynamicPanel.add(cpuField);
            extraFields.add(cpuField);

            dynamicPanel.add(new JLabel("RAM (GB):"));
            JTextField ramField = new JTextField();
            dynamicPanel.add(ramField);
            extraFields.add(ramField);

            dynamicPanel.add(new JLabel("Storage (GB):"));
            JTextField storageField = new JTextField();
            dynamicPanel.add(storageField);
            extraFields.add(storageField);

        } else if ("Peripheral".equals(selectedType)) {
            dynamicPanel.add(new JLabel("Тип устройства:"));
            JTextField typeField = new JTextField();
            dynamicPanel.add(typeField);
            extraFields.add(typeField);

        } else if ("NetworkDevice".equals(selectedType)) {
            dynamicPanel.add(new JLabel("Протокол:"));
            JTextField protocolField = new JTextField();
            dynamicPanel.add(protocolField);
            extraFields.add(protocolField);

            dynamicPanel.add(new JLabel("Скорость (Mbps):"));
            JTextField speedField = new JTextField();
            dynamicPanel.add(speedField);
            extraFields.add(speedField);
        }

        // 🔑 Обновляем UI
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
        pack(); // пересчитать размеры диалога
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

    public Equipment getResult() {
        return result;
    }
}

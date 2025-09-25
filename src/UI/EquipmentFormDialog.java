package UI;

import Equipment.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Диалог для добавления оборудования.
 * Возвращает объект Equipment (по умолчанию Peripheral).
 */
public class EquipmentFormDialog extends JDialog {
    private JTextField nameField, manufacturerField, priceField, yearField, descriptionField;
    private Equipment result;

    public EquipmentFormDialog(JFrame parent) {
        super(parent, "Добавить оборудование", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        initForm();
    }

    /**
     * Инициализация формы ввода.
     */
    private void initForm() {
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Название:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Производитель:"));
        manufacturerField = new JTextField();
        add(manufacturerField);

        add(new JLabel("Цена:"));
        priceField = new JTextField();
        add(priceField);

        add(new JLabel("Год:"));
        yearField = new JTextField();
        add(yearField);

        add(new JLabel("Описание:"));
        descriptionField = new JTextField();
        add(descriptionField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        okButton.addActionListener(this::onOk);
        cancelButton.addActionListener(e -> dispose());

        add(okButton);
        add(cancelButton);
    }

    /**
     * Обработчик кнопки OK.
     */
    private void onOk(ActionEvent e) {
        try {
            int id = (int) (Math.random() * 10000); // временный ID
            String name = nameField.getText().trim();
            String manufacturer = manufacturerField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());
            String description = descriptionField.getText().trim();

            // Пока создаём Peripheral по умолчанию
            result = new Peripheral(id, name, manufacturer, price, year, description, "Generic");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода: " + ex.getMessage());
        }
    }

    /**
     * Получить результат (новый объект Equipment).
     */
    public Equipment getResult() {
        return result;
    }
}

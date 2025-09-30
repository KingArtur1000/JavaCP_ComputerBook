package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

public class FontSizeChangerFormDialog extends JDialog {
    private JSlider slider;

    public FontSizeChangerFormDialog(JFrame parent, JTable table) {
        super(parent, "Изменение размера шрифта", true);

        setLayout(new BorderLayout(10, 10));
        setSize(400, 150);
        setLocationRelativeTo(parent);

        // Ползунок от 10 до 30, по умолчанию 16
        slider = new JSlider(JSlider.HORIZONTAL, 10, 30, 16);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Слушатель изменения значения
        ChangeListener listener = e -> {
            int size = slider.getValue();
            applyFontSize(table, size);
        };
        slider.addChangeListener(listener);

        add(new JLabel("Выберите размер шрифта:", SwingConstants.CENTER), BorderLayout.NORTH);
        add(slider, BorderLayout.CENTER);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /** Метод для применения размера шрифта к таблице */
    public static void applyFontSize(JTable table, int size) {
        table.setFont(new Font("Arial", Font.PLAIN, size));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, size));
        table.setRowHeight(size + 8); // чтобы строки не обрезались
        table.revalidate();
        table.repaint();
    }
}

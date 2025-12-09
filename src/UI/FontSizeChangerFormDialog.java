package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

/**
 * Диалоговое окно {@code FontSizeChangerFormDialog} для изменения размера шрифта в таблице {@link JTable}.
 * <p>
 * Позволяет пользователю выбрать размер шрифта с помощью ползунка (от 10 до 30, по умолчанию 16).
 * Изменения применяются сразу к таблице и её заголовку.
 */
public class FontSizeChangerFormDialog extends JDialog {
    /** Ползунок для выбора размера шрифта. */
    private JSlider slider;

    /**
     * Конструктор диалогового окна изменения размера шрифта.
     *
     * @param parent родительское окно
     * @param table  таблица, к которой будет применяться выбранный размер шрифта
     */
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

        // Заголовок
        add(new JLabel("Выберите размер шрифта:", SwingConstants.CENTER), BorderLayout.NORTH);
        // Ползунок
        add(slider, BorderLayout.CENTER);

        // Кнопка закрытия
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Применяет выбранный размер шрифта к таблице и её заголовку.
     * <p>
     * - Шрифт таблицы устанавливается как {@code Arial, PLAIN, size}.<br>
     * - Шрифт заголовка устанавливается как {@code Arial, BOLD, size}.<br>
     * - Высота строки увеличивается на 8 пикселей, чтобы текст не обрезался.
     *
     * @param table таблица, к которой применяется размер шрифта
     * @param size  размер шрифта
     */
    public static void applyFontSize(JTable table, int size) {
        table.setFont(new Font("Arial", Font.PLAIN, size));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, size));
        table.setRowHeight(size + 8); // чтобы строки не обрезались
        table.revalidate();
        table.repaint();
    }
}

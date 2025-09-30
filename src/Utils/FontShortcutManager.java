package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontShortcutManager {
    private final JTable table;
    private final int menuMask; // CTRL на Windows/Linux, CMD на macOS

    public FontShortcutManager(JTable table) {
        this.table = table;
        this.menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        installKeyBindings();
        installMouseWheelControl();
    }

    private void installKeyBindings() {
        InputMap im = table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = table.getActionMap();

        // Увеличение шрифта
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuMask | InputEvent.SHIFT_DOWN_MASK), "fontIncrease"); // Shift+'=' -> '+'
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, menuMask), "fontIncrease");     // если VK_PLUS доступен
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, menuMask), "fontIncrease");      // numpad '+'
        im.put(KeyStroke.getKeyStroke('+', menuMask), "fontIncrease");                  // символ '+'

        // Уменьшение шрифта
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuMask), "fontDecrease");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, menuMask), "fontDecrease"); // numpad '-'
        im.put(KeyStroke.getKeyStroke('-', menuMask), "fontDecrease");                  // символ '-'

        am.put("fontIncrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(+1); }
        });
        am.put("fontDecrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(-1); }
        });
    }

    private void installMouseWheelControl() {
        MouseWheelListener wheel = e -> {
            // CTRL/CMD зажат?
            if ((e.getModifiersEx() & menuMask) != 0 || e.isControlDown()) {
                changeFontSize(e.getWheelRotation() < 0 ? +1 : -1);
                e.consume(); // чтобы не скроллилась таблица
            }
        };
        // Ставим слушатель и на таблицу, и на её контейнеры
        table.addMouseWheelListener(wheel);
        Container p = table.getParent();
        if (p instanceof JViewport vp) {
            vp.addMouseWheelListener(wheel);
            Container sp = vp.getParent();
            if (sp instanceof JScrollPane scroll) {
                scroll.addMouseWheelListener(wheel);
            }
        }
    }

    private void changeFontSize(int delta) {
        Font current = table.getFont();
        int newSize = Math.max(10, Math.min(36, current.getSize() + delta)); // пределы 10..36
        table.setFont(new Font("Arial", Font.PLAIN, newSize));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, newSize));
        table.setRowHeight(newSize + 8);
        table.revalidate();
        table.repaint();
    }
}

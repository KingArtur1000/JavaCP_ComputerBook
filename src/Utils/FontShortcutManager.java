package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontShortcutManager {
    private final JTable table;
    private final JScrollPane scrollPane;
    private final int menuMask; // CTRL на Win/Linux, CMD на macOS
    private boolean zoomMode = false;

    public FontShortcutManager(JTable table, JScrollPane scrollPane) {
        this.table = table;
        this.scrollPane = scrollPane;
        this.menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        installKeyBindings();
        installCtrlTracking();
        installMouseWheelControl();
    }

    private void installKeyBindings() {
        InputMap im = table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = table.getActionMap();

        // Увеличение
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuMask | InputEvent.SHIFT_DOWN_MASK), "fontIncrease"); // Shift+='+'
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, menuMask), "fontIncrease");      // numpad '+'
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, menuMask), "fontIncrease");     // если доступно
        im.put(KeyStroke.getKeyStroke('+', menuMask), "fontIncrease");                  // символ

        // Уменьшение
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuMask), "fontDecrease");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, menuMask), "fontDecrease"); // numpad '-'
        im.put(KeyStroke.getKeyStroke('-', menuMask), "fontDecrease");

        am.put("fontIncrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(+1); }
        });
        am.put("fontDecrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(-1); }
        });
    }

    // Трекер зажатого CTRL/CMD через диспетчер клавиш (работает по всему окну)
    private void installCtrlTracking() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ev -> {
            boolean ctrlOrCmd = (ev.getModifiersEx() & menuMask) != 0;
            if (ev.getID() == KeyEvent.KEY_PRESSED || ev.getID() == KeyEvent.KEY_RELEASED) {
                zoomMode = ctrlOrCmd;
                // отключаем колесо у скролла, пока зажат CTRL/CMD
                if (scrollPane != null) {
                    scrollPane.setWheelScrollingEnabled(!zoomMode);
                }
            }
            return false; // не потребляем событие
        });
    }

    private void installMouseWheelControl() {
        MouseWheelListener wheel = e -> {
            // Поддержка тачпада: используем precise rotation
            boolean ctrlDown = ((e.getModifiersEx() & menuMask) != 0) || e.isControlDown();
            if (ctrlDown) {
                double rot = (e instanceof MouseWheelEvent m) ? m.getPreciseWheelRotation() : e.getWheelRotation();
                changeFontSize(rot < 0 ? +1 : -1);
                e.consume(); // предотвращаем дальнейший скролл
            }
        };

        // Ставим слушатель на все слои, чтобы перехватывать в любом случае
        table.addMouseWheelListener(wheel);
        if (table.getParent() instanceof JViewport vp) {
            vp.addMouseWheelListener(wheel);
            if (vp.getParent() instanceof JScrollPane sp) {
                sp.addMouseWheelListener(wheel);
            }
        }
    }

    private void changeFontSize(int delta) {
        Font current = table.getFont();
        int newSize = Math.max(10, Math.min(36, current.getSize() + delta));
        table.setFont(new Font("Arial", Font.PLAIN, newSize));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, newSize));
        table.setRowHeight(newSize + 8);
        table.revalidate();
        table.repaint();
    }
}

package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Класс {@code FontShortcutManager} управляет изменением размера шрифта в таблице {@link JTable}
 * с помощью горячих клавиш и колесика мыши.
 * <p>
 * Поддерживает:
 * <ul>
 *     <li>Увеличение/уменьшение шрифта через сочетания клавиш (CTRL/CMD + '+', '-').</li>
 *     <li>Изменение размера шрифта при прокрутке колесика мыши или тачпада с зажатым CTRL/CMD.</li>
 *     <li>Автоматическое отключение стандартного скролла при активации режима увеличения.</li>
 * </ul>
 */
public class FontShortcutManager {
    /** Таблица, для которой управляется размер шрифта. */
    private final JTable table;

    /** Обертка таблицы со скроллом. */
    private final JScrollPane scrollPane;

    /** Маска для клавиши меню (CTRL на Windows/Linux, CMD на macOS). */
    private final int menuMask;

    /** Флаг режима увеличения (true, если зажат CTRL/CMD). */
    private boolean zoomMode = false;

    /**
     * Конструктор {@code FontShortcutManager}.
     *
     * @param table      таблица, для которой будет управляться размер шрифта
     * @param scrollPane контейнер со скроллом, в котором находится таблица
     */
    public FontShortcutManager(JTable table, JScrollPane scrollPane) {
        this.table = table;
        this.scrollPane = scrollPane;
        this.menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        installKeyBindings();
        installCtrlTracking();
        installMouseWheelControl();
    }

    /**
     * Устанавливает горячие клавиши для увеличения и уменьшения шрифта.
     * <p>
     * Поддерживаются:
     * <ul>
     *     <li>CTRL/CMD + '+' (включая numpad и символ '+')</li>
     *     <li>CTRL/CMD + '-' (включая numpad и символ '-')</li>
     * </ul>
     */
    private void installKeyBindings() {
        InputMap im = table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = table.getActionMap();

        // Увеличение
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, menuMask | InputEvent.SHIFT_DOWN_MASK), "fontIncrease");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, menuMask), "fontIncrease");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, menuMask), "fontIncrease");
        im.put(KeyStroke.getKeyStroke('+', menuMask), "fontIncrease");

        // Уменьшение
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, menuMask), "fontDecrease");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, menuMask), "fontDecrease");
        im.put(KeyStroke.getKeyStroke('-', menuMask), "fontDecrease");

        am.put("fontIncrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(+1); }
        });
        am.put("fontDecrease", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { changeFontSize(-1); }
        });
    }

    /**
     * Устанавливает трекер для отслеживания зажатой клавиши CTRL/CMD.
     * <p>
     * При активации режима увеличения отключается стандартная прокрутка {@link JScrollPane}.
     */
    private void installCtrlTracking() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ev -> {
            boolean ctrlOrCmd = (ev.getModifiersEx() & menuMask) != 0;
            if (ev.getID() == KeyEvent.KEY_PRESSED || ev.getID() == KeyEvent.KEY_RELEASED) {
                zoomMode = ctrlOrCmd;
                if (scrollPane != null) {
                    scrollPane.setWheelScrollingEnabled(!zoomMode);
                }
            }
            return false;
        });
    }

    /**
     * Устанавливает обработчик колесика мыши/тачпада для изменения размера шрифта.
     * <p>
     * При зажатом CTRL/CMD вращение колесика увеличивает или уменьшает размер шрифта.
     */
    private void installMouseWheelControl() {
        MouseWheelListener wheel = e -> {
            boolean ctrlDown = ((e.getModifiersEx() & menuMask) != 0) || e.isControlDown();
            if (ctrlDown) {
                double rot = (e instanceof MouseWheelEvent m) ? m.getPreciseWheelRotation() : e.getWheelRotation();
                changeFontSize(rot < 0 ? +1 : -1);
                e.consume();
            }
        };

        table.addMouseWheelListener(wheel);
        if (table.getParent() instanceof JViewport vp) {
            vp.addMouseWheelListener(wheel);
            if (vp.getParent() instanceof JScrollPane sp) {
                sp.addMouseWheelListener(wheel);
            }
        }
    }

    /**
     * Изменяет размер шрифта таблицы и её заголовка.
     * <p>
     * Размер ограничен диапазоном от 10 до 36.
     *
     * @param delta изменение размера (+1 — увеличить, -1 — уменьшить)
     */
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

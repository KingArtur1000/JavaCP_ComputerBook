import javax.swing.*;
import java.awt.*;

public class SplashScreenWindow extends JWindow {

    public SplashScreenWindow() {
        // Основная панель с BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Левая часть: картинка
        ImageIcon icon = new ImageIcon(getClass().getResource("images/logo.jpg")); // путь к картинке
        Image scaledImage = icon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(imageLabel, BorderLayout.WEST);

        // Правая часть: текст
        JLabel textLabel = new JLabel(
                "<html>" +
                        "<div style='text-align:center;'>" +
                        "<h2 style='margin:5px;'>МИНИСТЕРСТВО ОБРАЗОВАНИЯ РЕСПУБЛИКИ БЕЛАРУСЬ</h2>" +
                        "<h3 style='margin:5px;'>БЕЛОРУССКИЙ НАЦИОНАЛЬНЫЙ ТЕХНИЧЕСКИЙ УНИВЕРСИТЕТ</h3>" +
                        "<h3 style='margin:5px;'>Факультет информационных технологий и робототехники</h3>" +
                        "<h2 style='margin:15px;'>Курсовая работа</h2>" +
                        "<h3 style='margin:5px;'>по дисциплине «Программирование на Java»</h3>" +
                        "<h1 style='margin:15px;'>Книга компьютерной техники</h1>" +
                        "<p style='margin:10px; font-size:18px;'>Выполнил: студент группы 10702423</p>" +
                        "<p style='margin:5px; font-size:18px;'>Павловский Никита Сергеевич</p>" +
                        "<p style='margin:10px; font-size:18px;'>Преподаватель: к.ф.-м.н., доц.</p>" +
                        "<p style='margin:5px; font-size:18px;'>Сидорик Валерий Владимирович</p>" +
                        "<h3 style='margin-top:20px;'>Минск 2025</h3>" +
                        "</div>" +
                        "</html>",
                SwingConstants.CENTER
        );

        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(textLabel, BorderLayout.CENTER);

        // Нижняя часть: кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nextButton = new JButton("Далее");
        JButton exitButton = new JButton("Выход");

        // Обработчики кнопок
        nextButton.addActionListener(e -> {
            dispose();
            EquipmentCatalog catalog = new EquipmentCatalog();
            MainWindow window = new MainWindow(catalog);
            window.setVisible(true);
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(nextButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Добавляем всё в окно
        getContentPane().add(mainPanel);

        setSize(1000, 650);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreenWindow splash = new SplashScreenWindow();
            splash.setVisible(true);
        });
    }
}

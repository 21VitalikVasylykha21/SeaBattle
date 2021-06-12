package Game.Multiplayer.Panels;

import GUI.JButtonCircle;
import GUI.JFieldCircle;
import Game.Multiplayer.MainLogicGame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Клас, що реалізує нижню панель кнопок
 * @author Vitalik
 */
public class PanelButtons extends JPanel {
    private View view;
    private JFieldCircle infoField;
    private JButtonCircle startGameButton;
    private JButtonCircle exitButton;
    private JButtonCircle restartGameButton;
    private JButtonCircle goMenuButton;

    /**
     * Конструктор класу PanelButtons
     * @param view форма на яку додаємо кнопки
     */
    public PanelButtons(View view) {
        this.view = view;
        setLayout(null);
        setPreferredSize(new Dimension(400, 50));
        infoField = new JFieldCircle();
        setTextInfo("РОЗСТАНОВЛЕННЯ КОРАБЛІВ");
        infoField.setEnabled(false);
        infoField.setBounds(30, 15, 160, 25);
        startGameButton = new JButtonCircle("Начати ігру");
        startGameButton.setBounds(210, 7, 150, 40);
        startGameButton.addActionListener(new ActionButtonStartClass());
        exitButton = new JButtonCircle("Прервати и вийти");
        exitButton.setBounds(370, 7, 150, 40);
        exitButton.addActionListener(new ActionButtonDisconnect());
        exitButton.setEnabled(false);
        restartGameButton = new JButtonCircle("Іграти ще раз");
        restartGameButton.setBounds(530, 7, 150, 40);
        restartGameButton.setEnabled(false);
        restartGameButton.addActionListener(new ActionButtonRestartGame());
        goMenuButton= new JButtonCircle("Повернутися в меню");
        goMenuButton.setBounds(690, 7, 190, 40);
        goMenuButton.setEnabled(false);
        goMenuButton.addActionListener(new ActionButtonMenu());
        add(infoField);
        add(startGameButton);
        add(exitButton);
        add(restartGameButton);
        add(goMenuButton);
    }

    /**
     * Метод, що повертає кнопку Іграти ще раз
     * @return кнопку "Іграти ще раз"
     */
    public JButton getRestartGameButton() {
        return restartGameButton;
    }
    /**
     * Метод, що повертає кнопку Повернутися в меню
     * @return кнопку "Повернутися в меню"
     */
    public JButton getGoMenuButton() {
        return goMenuButton;
    }
    /**
     * Метод, що повертає кнопку Начати ігру
     * @return кнопку "Начати ігру"
     */
    public JButton getStartGameButton() {
        return startGameButton;
    }

    /**
     * Метод, що повертає кнопку Начати ігру
     * @return кнопку "Начати ігру"
     */
    public JButton getExitButton() {
        return exitButton;
    }
    /**
     * Метод, що задає текс полю введення infoField
     * @param text текст, якмй потрібно задати
     */
    public void setTextInfo(String text) {
        infoField.setText(text.toUpperCase());
    }

    //класс слухач для кнопки "Начать игру"
    private class ActionButtonStartClass implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            view.startGame();
        }
    }

    //класс слухач для кнопки "Перервати і вийти"
    private class ActionButtonDisconnect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.disconnectGameRoom();
            exitButton.setEnabled(false);
            restartGameButton.setEnabled(true);
            goMenuButton.setEnabled(true);
        }
    }

    //класс слухач для кнопики "Іграти ще раз"
    private class ActionButtonRestartGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.init();
        }
    }
    //класс слухач для кнопики "Повернутися в меню"
    private class ActionButtonMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.goMainMenu();
        }
    }
}

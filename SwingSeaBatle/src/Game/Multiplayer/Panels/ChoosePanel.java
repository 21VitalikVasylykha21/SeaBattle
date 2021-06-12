package Game.Multiplayer.Panels;

import GUI.JButtonCircle;
import Game.Multiplayer.MainLogicGame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;

/**
 * Клас, що реалізує панель додавання кораблів
 * @author Vitalik
 */
public class ChoosePanel extends JPanel {
    
    private View view;
    private JPanel panelRadio;
    private JPanel panelPlacement;
    private JRadioButton oneDeck;
    private JRadioButton twoDeck;
    private JRadioButton threeDeck;
    private JRadioButton fourDeck;
    private JRadioButton vertical;
    private JRadioButton horizontal;
    private JButton clearField;
    private JButton autoAddField;
    private ButtonGroup groupDeck;
    private ButtonGroup groupPlacement;
    private JTextArea removeShip;

    /**
     * Конструктор класу ChoosePanel
     * @param view форма, на яку додавати панелі
     */
    public ChoosePanel(View view) {
        this.view = view;
        setLayout(null);
        this.setPreferredSize(new Dimension(255, 400));
        removeShip = new JTextArea("Щоб видалити         корабель натиснітьправою кнопкою   мишки на нього");
        removeShip.setBounds(13, 10, 230, 140);
        removeShip.setLineWrap(true);
        removeShip.setFont(new Font("Century Gothic", Font.ITALIC, 22));
        removeShip.setEditable(false);
        removeShip.setBackground(new Color(41, 59, 95));
        removeShip.setForeground(Color.WHITE);
        removeShip.setCursor(null);
        removeShip.setFocusable(false);
        panelRadio = new JPanel();
        panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));
        panelRadio.setBounds(13, 140, 230, 130);
        panelRadio.setFont(new Font("Century Gothic", Font.ITALIC, 18));
        panelRadio.setForeground(Color.WHITE);
        panelPlacement = new JPanel();
        panelPlacement.setLayout(new BoxLayout(panelPlacement, BoxLayout.Y_AXIS));
        panelPlacement.setBounds(13, 270, 230, 90);
        clearField = new JButtonCircle("Видалити всі кораблі");
        clearField.setBounds(13, 370, 230, 30);
        clearField.addActionListener(new ActionClearField());
        autoAddField = new JButtonCircle("Автоматичне розташування");
        autoAddField.setBounds(13, 410, 230, 30);
        autoAddField.addActionListener(new ActionAutoAddField());
        panelRadio.setBorder(BorderFactory.createTitledBorder(null, "Ярусність", TitledBorder.CENTER, TitledBorder.TOP, new Font("Century Gothic", Font.ITALIC, 18), Color.WHITE));
        panelPlacement.setBorder(BorderFactory.createTitledBorder(null, "Орієнтація", TitledBorder.CENTER, TitledBorder.TOP, new Font("Century Gothic", Font.ITALIC, 18), Color.WHITE));
        oneDeck = new JRadioButton();
        oneDeck.setFont(new Font("Century Gothic", Font.ITALIC, 11));
        oneDeck.setBackground(new Color(41, 59, 95));
        oneDeck.setForeground(Color.WHITE);
        oneDeck.setFocusable(false);
        setNameOneDeck(4);
        twoDeck = new JRadioButton();
        twoDeck.setFont(new Font("Century Gothic", Font.ITALIC, 11));
        twoDeck.setBackground(new Color(41, 59, 95));
        twoDeck.setForeground(Color.WHITE);
        twoDeck.setFocusable(false);
        setNameTwoDeck(3);
        threeDeck = new JRadioButton();
        threeDeck.setFont(new Font("Century Gothic", Font.ITALIC, 11));
        threeDeck.setBackground(new Color(41, 59, 95));
        threeDeck.setForeground(Color.WHITE);
        threeDeck.setFocusable(false);
        setNameThreeDeck(2);
        fourDeck = new JRadioButton();
        fourDeck.setFont(new Font("Century Gothic", Font.ITALIC, 11));
        fourDeck.setBackground(new Color(41, 59, 95));
        fourDeck.setForeground(Color.WHITE);
        fourDeck.setFocusable(false);
        setNameFourDeck(1);
        vertical = new JRadioButton("Вертикальна");
        horizontal = new JRadioButton("Горизонтальна");
        vertical.setFont(new Font("Century Gothic", Font.ITALIC, 14));
        vertical.setBackground(new Color(41, 59, 95));
        vertical.setFocusable(false);
        vertical.setForeground(Color.WHITE);
        horizontal.setFont(new Font("Century Gothic", Font.ITALIC, 14));
        horizontal.setBackground(new Color(41, 59, 95));
        horizontal.setFocusable(false);
        horizontal.setForeground(Color.WHITE);
        groupDeck = new ButtonGroup();
        groupPlacement = new ButtonGroup();
        panelRadio.add(oneDeck);
        panelRadio.add(twoDeck);
        panelRadio.add(threeDeck);
        panelRadio.add(fourDeck);
        panelPlacement.add(vertical);
        panelPlacement.add(horizontal);
        add(panelRadio);
        add(panelPlacement);
        add(clearField);
        add(autoAddField);
        add(removeShip);
        groupDeck.add(oneDeck);
        groupDeck.add(twoDeck);
        groupDeck.add(threeDeck);
        groupDeck.add(fourDeck);
        groupPlacement.add(vertical);
        groupPlacement.add(horizontal);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Picture.getImage(Picture.INFO.name()), 2, 0, this);
    }

     /**
     * Метод, що встановлює значення для радіо кнопок однопалубного
     * @param count  кількість, скільки залишилося однопалубних кораблів
     */
    public void setNameOneDeck(int count) {
        String text = "Однопалубний, залишолося - " + count;
        oneDeck.setText(text);
    }

    /**
     * Метод, що встановлює значення для радіо кнопок двохпалубного
     * @param count  кількість, скільки залишилося двохпалубного кораблів
     */
    public void setNameTwoDeck(int count) {
        String text = "Двухпалубний, залишолося - " + count;
        twoDeck.setText(text);
    }

    /**
     * Метод, що встановлює значення для радіо кнопок трьохпалубного
     * @param count  кількість, скільки залишилося трьохпалубних кораблів
     */
    public void setNameThreeDeck(int count) {
        String text = "Трьохпалубний, залишолося - " + count;
        threeDeck.setText(text);
    }

    /**
     * Метод, що встановлює значення для радіо кнопок чотирьох палубних
     * @param count  кількість, скільки залишилося чотирьох палубних кораблів
     */
    public void setNameFourDeck(int count) {
        String text = "Чотири палубний, залишолося - " + count;
        fourDeck.setText(text);
    }

    //возвращает кол-во палуб в зависимости - какой радиоБаттон выбран
    /**
     * Метод, що повертає кількіть палуб в залежності від того, яка радіо 
     * кнопка  вибрана
     * @return  кількість палуб
     */
    public int getCountDeck() {
        if (oneDeck.isSelected()) return 1;
        else if (twoDeck.isSelected()) return 2;
        else if (threeDeck.isSelected()) return 3;
        else if (fourDeck.isSelected()) return 4;
        else return 0;
    }

    /**
     * Метод, що повертає орієнтацію корабля
     * @return 1 якщо вертикальний, 2 якщо горизонтальний
     */
    public int getPlacement() {
        if (vertical.isSelected()) return 1;
        else if (horizontal.isSelected()) return 2;
        else return 0;
    }

    private class ActionClearField implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.loadEmptyMyField();
        }
    }
    
    private class ActionAutoAddField implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.autoAddShipMyField();
        }
    }
}

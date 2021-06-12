package Game.Multiplayer.Panels;

import Game.Multiplayer.MainLogicGame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Клас, що представляє собою панель гравчя
 * @author Vitalik
 */
public class MyField extends JPanel {
    private View view;
    private ChoosePanel choosePanel;

    /**
     * Метод, що задає панель вибору (додавання кораблів)
     * @param choosePanel екземпляр класу ChoosePanel
     */
    public void setChoosePanel(ChoosePanel choosePanel) {
        this.choosePanel = choosePanel;
    }

    /**
     * Конструктор класу MyField
     * @param view форма, на яку потрібно добавити
     */
    public MyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
        this.addMouseListener(new ActionMouse());
    }

    /**
     * Змінений метод відрисовки
     * @param g графічний параметр
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintMyField(g);
    }

    //клас - слухач на натиск кнопки на панелі
    private class ActionMouse extends MouseAdapter {
        /**
         * Зміненний метод, натиску на панель
         * @param e екземпляр класу MouseEvent, натис кнопки 
         */
        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int countDeck = choosePanel.getCountDeck();
            int placement = choosePanel.getPlacement();
            Ship ship;
            Ship removedShip;
            if (e.getButton() == MouseEvent.BUTTON1 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE)) {
                if (countDeck > 0 && countDeck <= 4) {
                    switch (placement) {
                        case 1: {
                            ship = new Ship(countDeck, false);
                            ship.createVerticalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        case 2: {
                            ship = new Ship(countDeck, true);
                            ship.createHorizontalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        default:
                            View.callInformationWindow("Не вибрано орієнтацію розміщення.");
                    }
                } else {
                    View.callInformationWindow("Не вибрано кількість палуб.");
                    return;
                }
            } else if (e.getButton() == MouseEvent.BUTTON3 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) &&
                    (removedShip = view.removeShip(x, y)) != null) {
                view.changeCountShipOnChoosePanel(removedShip.getCountDeck());
            }
            repaint(); 
            view.changeCountShipOnChoosePanel(countDeck);
        }
    }
}

package Game.Multiplayer.Panels;

import Game.Multiplayer.MainLogicGame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Клас, що представляє собою ігрове поле суперника
 * @author Vitalik
 */
public class EnemyField extends JPanel {
    private View view;

    /**
     * Конструктор класу EnemyField
     * @param view на яку форму завантажувати
     */
    public EnemyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
    }

    /**
     * Змінений метод відрисовки
     * @param g графічний параметр
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintEnemyField(g);
    }

    /**
     * Метод, що додає слухача др панелі поля суперника
     */
    public void addListener() {
        addMouseListener(new ActionMouse());
    }

    /**
     * Метод, що видаляє слухача у панелі поля суперника
     */
    public void removeListener() {
        MouseListener[] listeners = getMouseListeners();
        for (MouseListener lis : listeners) {
            removeMouseListener(lis);
        }
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
            if (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) {
                view.sendShot(x, y);
            }
        }
    }
}

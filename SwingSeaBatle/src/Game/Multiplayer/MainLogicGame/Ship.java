package Game.Multiplayer.MainLogicGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас, що представляє собою корабель
 * @author Vitalik
 */
public class Ship implements Serializable {
    private final int countDeck;  //количество палуб
    private List<Box> boxesOfShip; //список всех босов данногго корабля
    private final boolean isHorizontalPlacement; //ориентация корабля

    /**
     * Метод, що повертає список всі палуби корабля
     * @return список палуб
     */
    public List<Box> getBoxesOfShip() {
        return boxesOfShip;
    }

    /**
     * Метод, що чи корабель горизонтальний
     * @return істину, якщо корабель має горизонтальну орієнтацію
     */
    public boolean isHorizontalPlacement() {
        return isHorizontalPlacement;
    }

    /**
     * Метод, що повертає кількість палуб
     * @return кількість палуб
     */
    public int getCountDeck() {
        return countDeck;
    }

    /**
     * Конструктор класу Ship
     * @param countDeck кількість палуб
     * @param isHorizontalPlacement горизонтальність
     */
    public Ship(int countDeck, boolean isHorizontalPlacement) {
        this.countDeck = countDeck;
        this.isHorizontalPlacement = isHorizontalPlacement;
        boxesOfShip = new ArrayList<>(countDeck);
    }

    /**
     * Метод, що створює корабель горизонтальної орієнтації по координатам
     * @param x координата по осі Х
     * @param y координата по осі У
     */
    public void createHorizontalShip(int x, int y) {
        int pointLimitValueForPaint = (Picture.COLUMNS - countDeck) * Picture.IMAGE_SIZE;
        for (int i = 0; i < countDeck; i++) {
            Box newBox;
            if (x > pointLimitValueForPaint) {
                newBox = new Box(Picture.SHIP, pointLimitValueForPaint + i * Picture.IMAGE_SIZE, y);
                boxesOfShip.add(newBox);
            } else {
                newBox = new Box(Picture.SHIP, (x + i * Picture.IMAGE_SIZE), y);
                boxesOfShip.add(newBox);
            }
        }
    }

   /**
     * Метод, що створює корабель вертикальної орієнтації по координатам
     * @param x координата по осі Х
     * @param y координата по осі У
     */
    public void createVerticalShip(int x, int y) {
        int pointStartPaint = (Picture.ROWS - countDeck) * Picture.IMAGE_SIZE;
        for (int i = 0; i < countDeck; i++) {
            Box newBox;
            if (pointStartPaint < y) {
                newBox = new Box(Picture.SHIP, x, pointStartPaint + i * Picture.IMAGE_SIZE);
                boxesOfShip.add(newBox);
            } else {
                newBox = new Box(Picture.SHIP, x, (y + i * Picture.IMAGE_SIZE));
                boxesOfShip.add(newBox);
            }
        }
    }

    /**
     * Змінений метод, що перевіряє чи кораблі рівні
     * @param o обєкт, з яким зрівнювати
     * @return істину, якщо вони рівні
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Ship ship = (Ship) o;
        return countDeck == ship.getCountDeck() && (boxesOfShip != null && ship.getBoxesOfShip() != null &&
                boxesOfShip.hashCode() == ship.getBoxesOfShip().hashCode());
    }
}

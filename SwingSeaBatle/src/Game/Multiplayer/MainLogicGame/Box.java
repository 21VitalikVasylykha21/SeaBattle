package Game.Multiplayer.MainLogicGame;

import java.io.Serializable;

/**
 * Клас, що представляє собою палуби кораблів
 * @author Vitalik
 */
public class Box implements Serializable {
    private int x;
    private int y;
    private Picture picture;
    private boolean isOpen = false;

    /**
     * Метод, що повертає чи відкритий дана палуба
     * @return булеву зміну
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Метод, що задає чи відкрита дана палуба
     * @param open булева зміна чи відкрита дана палуба
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * Метод, що повертає картинку даного блока
     * @return екземляз класу Picture
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * Метод, що задається картинку даного блока
     * @param picture екземляз класу Picture до даної палуби
     */
    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    /**
     * Метод, що задає даній палубі координати і картинку
     * @param picture картинка даної палуби
     * @param x координа по осі Х
     * @param y координа по осі У
     */
    public Box(Picture picture, int x, int y) {
        this.picture = picture;
        this.x = x;
        this.y = y;
    }

    /**
     * Метод, що повертає координату Х до палуби
     * @return координату Х
     */
    public int getX() {
        return x;
    }

    /**
     * Метод, що повертає координату У до палуби
     * @return координату У
     */
    public int getY() {
        return y;
    }
}


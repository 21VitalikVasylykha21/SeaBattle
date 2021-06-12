package Game.Multiplayer.Connect;

import Game.Multiplayer.MainLogicGame.*;
import java.io.Serializable;
import java.util.List;

/**
 * Клас, що представляє собою повідомлення що передаємо
 * @author Vitalik
 */
public class Message implements Serializable {
    private int x;
    private int y;
    private MessageType messageType;
    private Box[][]  gameField;
    private List<Ship> listOfAllShips;

    /**
     * Конструкто класу
     * @param messageType тип повідомлення, що відправляємо
     * @param gameField масив з палубами кораблів
     * @param allShipsOfEnemy список із всіма кораблями
     */
    public Message(MessageType messageType, Box[][] gameField, List<Ship> allShipsOfEnemy) {
        this.messageType = messageType;
        this.gameField = gameField;
        this.listOfAllShips = allShipsOfEnemy;
    }

    /**
     * Конструктор класу, перевизначенний
     * @param messageType тип повідомлення
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Конструктор класу, перевизначенний
     * @param messageType тип повідомлення
     * @param x координата по осі Х
     * @param y координата по осі У
     */
    public Message(MessageType messageType,int x, int y) {
        this.x = x;
        this.y = y;
        this.messageType = messageType;
    }
    /**
     * Метод, що повертає координату Х
     * @return координату Х
     */
    public int getX() {
        return x;
    }

    /**
     * Метод, що повертає координату У
     * @return координату У
     */
    public int getY() {
        return y;
    }

    /**
     * Метод, що повертає тип повідомлення
     * @return екземпляр класу MessageType
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Метод, що повертає масив із палубами на полі
     * @return масив екземпляру класу Box, із палубами 
     */
    public Box[][] getGameField() {
        return gameField;
    }

    /**
     * Метод, що повертає список із всіма короблями 
     * @return  список із екземплярами класу Ship
     */
    public List<Ship> getListOfAllShips() {
        return listOfAllShips;
    }
}

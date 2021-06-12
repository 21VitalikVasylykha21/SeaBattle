package Game.Multiplayer.MainLogicGame;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private Box[][] myField = new Box[Picture.COLUMNS][Picture.ROWS]; 
    private Box[][] enemyField = new Box[Picture.COLUMNS][Picture.ROWS]; 
    private List<Ship> shipsOneDeck = new ArrayList<>();
    private List<Ship> shipsTwoDeck = new ArrayList<>(); 
    private List<Ship> shipsThreeDeck = new ArrayList<>(); 
    private List<Ship> shipsFourDeck = new ArrayList<>(); 
    private List<Ship> allShipsOfEnemy = new ArrayList<>(); 

    /**
     * Метод, що приймає список із всіма короблями
     *
     * @param allShipsOfEnemy
     */
    public void setAllShipsOfEnemy(List<Ship> allShipsOfEnemy) {
        this.allShipsOfEnemy = allShipsOfEnemy;
    }

    /**
     * Метод, що повертає спикок всіх своїх кораблів
     * @return List із всіма своїми кораблями
     */
    public List<Ship> getAllShips() {
        List<Ship> allBoxesOfShips = new ArrayList<>();
        allBoxesOfShips.addAll(shipsFourDeck);
        allBoxesOfShips.addAll(shipsThreeDeck);
        allBoxesOfShips.addAll(shipsTwoDeck);
        allBoxesOfShips.addAll(shipsOneDeck);
        return allBoxesOfShips;
    }

    /**
     * Метод, що повертає спикок всіх боксів із всіх своїх кораблів
     * @return List із всіма боксами із своїх кораблів
     */
    public List<Box> getAllBoxesOfShips() {
        List<Box> allBoxes = new ArrayList<>();
        List<Ship> allShips = getAllShips();
        for (Ship ship : allShips) {
            allBoxes.addAll(ship.getBoxesOfShip());
        }
        return allBoxes;
    }

    /**
     * Метод, що повертає список однопалубних кораблів
     * @return список із однопалубними короблями
     */
    public List<Ship> getShipsOneDeck() {
        return shipsOneDeck;
    }

    /**
     * Метод, що повертає список двохпалубних кораблів
     * @return список із двохпалубними короблями
     */
    public List<Ship> getShipsTwoDeck() {
        return shipsTwoDeck;
    }

    /**
     * Метод, що повертає список трьохпалубних кораблів
     * @return список із трьохпалубними короблями
     */
    public List<Ship> getShipsThreeDeck() {
        return shipsThreeDeck;
    }

    /**
     * Метод, що повертає список чотирьохпалубних кораблів
     * @return список із чотирьохлубними короблями
     */
    public List<Ship> getShipsFourDeck() {
        return shipsFourDeck;
    }
    
    /**
     * Метод, що повертає масив із боксами із свого поля 
     * @return масив боксів із свого поля
     */
    public Box[][] getMyField() {
        return myField;
    }

    /**
     * Метод, що приймає масив із боксами свого поля
     * @param myField  масив боксів, із нагошо поля
     */
    public void setMyField(Box[][] myField) {
        this.myField = myField;
    }

    /**
     * Метод, що повертає бокси поля суперника
     * @return масив із боксами поля суперника
     */
    public Box[][] getEnemyField() {
        return enemyField;
    }

    /**
     * Метод, що задає масив із палубами суперника
     * @param enemyField масив боксів суперника
     */
    public void setEnemyField(Box[][] enemyField) {
        this.enemyField = enemyField;
    }

    
    /**
     * Метод, що встановлює значення вказаної палуби в вказаний масив
     * @param fieldBox масив 
     * @param box  палуба, яку необхідно встановити в масив
     */
    public void addBoxInField(Box[][] fieldBox, Box box) {
        int i = box.getX() / Picture.IMAGE_SIZE;
        int j = box.getY() / Picture.IMAGE_SIZE;
        fieldBox[i][j] = box;
    }

     /**
     * Метод, що повертає палубу із вказаного масива по координатам панелі
     * @param field масив в якому необхідно знайти палубу
     * @param x координата по осі Х
     * @param y координата по осі У
     * @return палубу
     */
    public Box getBox(Box[][] field, int x, int y) {
        int i = x / Picture.IMAGE_SIZE;
        int j = y / Picture.IMAGE_SIZE;
        int lenght = field.length - 1;
        if (!(i > lenght || j > lenght)) {
            return field[i][j];
        }
        return null;
    }

    //метод, который устанавливает значение isOpen в true (открывает боксы после попадания в корабль) боксов, находящихся рядом с боксом корабля, определенного входными координатами
    /**
     * Метод, що відкриває палуби після попадання, які находять близько біля 
     * палуби корабля, певного вхідного корабля
     * @param x координа корабля по осі Х
     * @param y координа корабля по осі Х
     * @param isHorizontalPlacement булеве значеня, чи корабель горизонтально 
     *                              розташований чи ні 
     */
    public void openBoxAroundBoxOfShipEnemy(int x, int y, boolean isHorizontalPlacement) {
        if (isHorizontalPlacement) {
            Box boxUp = getBox(enemyField, x, y - Picture.IMAGE_SIZE);
            if (boxUp != null) {
                boxUp.setOpen(true);
            }
            Box boxDown = getBox(enemyField, x, y + Picture.IMAGE_SIZE);
            if (boxDown != null) {
                boxDown.setOpen(true);
            }
        } 
        else {
            Box boxLeft = getBox(enemyField, x - Picture.IMAGE_SIZE, y);
            if (boxLeft != null) {
                boxLeft.setOpen(true);
            }
            Box boxRight = getBox(enemyField, x + Picture.IMAGE_SIZE, y);
            if (boxRight != null) {
                boxRight.setOpen(true);
            }
        }
    }

    /**
     * Відкриття всіх пустих палуб по перемитру корабля, у випадках коли всі 
     * палуби корабля підбиті
     * @param ship заданий корабель
     */
    public void openAllBoxesAroundShip(Ship ship) {
        Box startBox = ship.getBoxesOfShip().get(0);
        Box endBox = ship.getBoxesOfShip().get(ship.getCountDeck() - 1);
        for (int i = startBox.getX() - Picture.IMAGE_SIZE; i <= startBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = startBox.getY() - Picture.IMAGE_SIZE; j <= startBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) {
                    box.setOpen(true);
                }
            }
        }
        for (int i = endBox.getX() - Picture.IMAGE_SIZE; i <= endBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = endBox.getY() - Picture.IMAGE_SIZE; j <= endBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) {
                    box.setOpen(true);
                }
            }
        }
    }

     /**
     * Додавання заданого корабля в необхідний список кораблів, в залежності 
     * від кількості палуб
     * @param ship 
     */
    public void addShip(Ship ship) {
        int countDeck = ship.getCountDeck();
        switch (countDeck) {
            case 1: {
                if (shipsOneDeck.size() < 4) {
                    shipsOneDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else {
                    View.callInformationWindow("Перебір однопалубних. Максимально можливо - 4.");
                }
                break;
            }
            case 2: {
                if (shipsTwoDeck.size() < 3) {
                    shipsTwoDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else {
                    View.callInformationWindow("Перебір двухпалубних. Максимально можливо - 3.");
                }
                break;
            }
            case 3: {
                if (shipsThreeDeck.size() < 2) {
                    shipsThreeDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else {
                    View.callInformationWindow("Перебір трьохпалубних. Максимально можливо - 2.");
                }
                break;
            }
            case 4: {
                if (shipsFourDeck.size() < 1) {
                    shipsFourDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else {
                    View.callInformationWindow("Чотирьохпалубний вже добавлений. Максимально можливо - 1.");
                }
                break;
            }
        }
    }

    /**
     * Метод, що повертає палубу, в який був зроблений вистір, корабля суперника
     * @param boxShot
     * @return якщо координати палуби дорівнює координаті бокса одного 
     * із кораблів ворога, інакше нічого
     */
    public Ship getShipOfEnemy(Box boxShot) {
        for (Ship ship : allShipsOfEnemy) {
            for (Box box : ship.getBoxesOfShip()) {
                if (boxShot.getX() == box.getX() && boxShot.getY() == box.getY()) {
                    return ship;
                }
            }
        }
        return null;
    }

    /**
     * Видалення корабля в потрібному списку - використовуючи в процесі розтавлення
     * і видалення кораблів на своєму ігровому полі
     * @param ship корабель, який необхідно видалити
     */
    public void removeShip(Ship ship) {
        if (shipsOneDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsOneDeck.remove(ship);
            }
        } else if (shipsTwoDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsTwoDeck.remove(ship);
            }
        } else if (shipsThreeDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsThreeDeck.remove(ship);
            }
        } else if (shipsFourDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsFourDeck.remove(ship);
            }
        }
    }
}

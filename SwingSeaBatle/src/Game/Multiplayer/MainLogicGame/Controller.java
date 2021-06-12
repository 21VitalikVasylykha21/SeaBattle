package Game.Multiplayer.MainLogicGame;

import Game.Multiplayer.Connect.*;
import Game.Multiplayer.Panels.MyField;
import StartPoinMenu.MainMenu;
import Users.Player;
import Users.SQL;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Random;

/**
 * Клас, що представляє собою контролел всіх оосновних дій гри
 * @author Vitalik
 */
public class Controller {
    private View view;
    private Model model;
    private MainMenu menu;
    private Connection connection;

    /**
     * Конструктор класу Controller
     * @param view форма
     * @param model модель
     * @param menu головне меню програми
     */
    public Controller(View view, Model model, MainMenu menu) {
        this.view = view;
        this.model = model;
        this.menu = menu;
    }
    
    /**
     * Метод, що повертає екземпляр класу MainMenu
     * @return 
     */
    public MainMenu getMenu(){
        return menu;
    }

    /**
     * Метод, що очищую ігрове поле і виводить його
     */
    public void loadEmptyMyField() {
        model.getShipsOneDeck().clear();
        model.getShipsTwoDeck().clear();
        model.getShipsThreeDeck().clear();
        model.getShipsFourDeck().clear();
        model.setMyField(new Box[Picture.COLUMNS][Picture.ROWS]);
    
        for (int i = 0; i < Picture.ROWS; i++) {
            for (int j = 0; j < Picture.COLUMNS; j++) {
                if (i == 0 && j == 0) continue;
                else if (i == 0 && j != 0) { 
                    model.addBoxInField(model.getMyField(), new Box(Picture.valueOf("SYM" + j), Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                } else if (i != 0 && j == 0) {
                    model.addBoxInField(model.getMyField(), new Box(Picture.valueOf("NUM" + i), Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                } else { 
                    model.addBoxInField(model.getMyField(), new Box(Picture.EMPTY, Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                }
            }
        }
    }

    /**
     * Метод, що додає корабель перевіряючи чи не перететинається із іншим кораблем
     * @param ship корабель, який додаємо
     * @return повертає true, якщо додано  і false, якщо ні  
     */
    public boolean addShip(Ship ship) {
        List<Box> boxesOfShip = ship.getBoxesOfShip();
        for (Box boxShip : boxesOfShip) {
            if (checkAround(boxShip, boxesOfShip)) {
                boxesOfShip.clear();
                return false;
            }
        }
        if (boxesOfShip.size() != 0) model.addShip(ship);
        return true;
    }

    /**
     * Метод, що Видаляє корабель по координатам
     * @param x координата по осі Х
     * @param y координата по осі У
     * @return повертає видалений порабель
     */
    public Ship removeShip(int x, int y) {
        List<Ship> allShips = model.getAllShips(); 
        for (Ship ship : allShips) {
            for (Box box : ship.getBoxesOfShip()) {
                if (x == box.getX() && y == box.getY()) { 
                    model.removeShip(ship);
                    return ship;
                }
            }
        }
        return null;
    }

    //Метод, що перевіряє на перетин кораблів
    private boolean checkAround(Box box, List<Box> boxesOfShip) {
        int myX = box.getX();
        int myY = box.getY();
        for (int i = myX - Picture.IMAGE_SIZE; i <= myX + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = myY - Picture.IMAGE_SIZE; j <= myY + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box boxFromMatrix = model.getBox(model.getMyField(), i, j);
                if (boxFromMatrix != null && boxFromMatrix.getPicture() == Picture.SHIP && !boxesOfShip.contains(boxFromMatrix)) {
                     boxesOfShip.clear();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Метод, що  відкриває пусті клітинки навколо підбитого корабля на полі суперника
     * @param boxShot підбита палуба
     */
    public void openBoxesAround(Box boxShot) {
        Ship ship = model.getShipOfEnemy(boxShot); 
        if (ship != null) {
            if (ship.getCountDeck() == getCountOpenBoxOfShip(ship)) model.openAllBoxesAroundShip(ship);
            else if (getCountOpenBoxOfShip(ship) == 1) return;
            else {
                for (Box box : ship.getBoxesOfShip()) {
                    if (box.isOpen())
                        model.openBoxAroundBoxOfShipEnemy(box.getX(), box.getY(), ship.isHorizontalPlacement());
                }
            }
        }
    }

     /**
     * Метод, що повертає кількість відкритих палуб коробля
     * @param ship корабель, якй потрібно перевірити 
     * @return  кількість відкритих палуб у поданому кораблі
     */
    public int getCountOpenBoxOfShip(Ship ship) {
        int count = 0;
        for (Box box : ship.getBoxesOfShip()) {
            if (box.isOpen()) count++;
        }
        return count;
    }

    //Перевірка на завершення гри
    private boolean checkEndGame() {
        List<Box> allBoxesOfShip = model.getAllBoxesOfShips(); 
        for (Box box : allBoxesOfShip) {
           
            if (box.getPicture() == Picture.SHIP) return false;
        }
        return true;
    }

    /**
     * Метод, що перевіряє на чи всі добавлення кораблів перед початком іги
     * @return повертає true, якщо додано  і false, якщо ні  
     */
    public boolean checkFullSetShips() {
       
        if (model.getShipsOneDeck().size() == 4 &&
                model.getShipsTwoDeck().size() == 3 &&
                model.getShipsThreeDeck().size() == 2 &&
                model.getShipsFourDeck().size() == 1) return true;
        else return false;
    }

    
    /**
     * Метод, що створює ігрову кімнату
     * @param port порт сервера
     * @throws IOException 
     */
    public void createGameRoom(int port) throws IOException {
        Server server = new Server(port);
        server.start();
    }


    /**
     * Метод, що підключає клієнта до сервера
     * @param ip
     * @param port порт сервера
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void connectToRoom(String ip, int port) throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket(ip, port));
        Message message = connection.receive();
        if (message.getMessageType() == MessageType.ACCEPTED) {
            connection.send(new Message(MessageType.FIELD, model.getMyField(), model.getAllShips()));
            Message messageField = connection.receive(); 
            if (messageField.getMessageType() == MessageType.FIELD) {
                model.setEnemyField(messageField.getGameField()); 
                model.setAllShipsOfEnemy(messageField.getListOfAllShips());
            }
        }
    }

     /**
     * Метод, що відключає від сервера
     * @throws IOException 
     */
    public void disconnectGameRoom() throws IOException {
        connection.send(new Message(MessageType.DISCONNECT));
        setPlayerInfo(menu.player,false);
        Music.playMusic("lose");
     
    }

    /**
     * Метод, що відправлляє повідомлення на сервер з координатами пострілу
     * @param x координата по осі Х
     * @param y координата по осі У
     * @return повертає true, якщо відправлено повідомлення і false, якщо ні  
     * @throws IOException 
     */
    public boolean sendMessage(int x, int y) throws IOException {
        Box box = model.getBox(model.getEnemyField(), x, y);
        if (!box.isOpen()) {
            box.setOpen(true);
            openBoxesAround(box); 
            connection.send(new Message(MessageType.SHOT, x, y)); 
            return true;
        } else return false;
    }

    /**
     * Метод, що автоматично розташовує кораблі на своєму ігровому полі
     * @param myField наше ігрове поле
     */
    public void autoAddShipMyField(MyField myField){
        loadEmptyMyField();
        myField.repaint();            
        Random random = new Random();
        int i = 10;
        int countDeck = 4;
        while (i != 0) {
            int x = ((random.nextInt(360) + 40)/ Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = ((random.nextInt(360) + 40)/ Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            boolean Orientation = (random.nextInt(10) >= 5);
            Ship ship = new Ship(countDeck, Orientation);
            if (Orientation){
                ship.createVerticalShip(x, y);
            }else{
                ship.createHorizontalShip(x, y);
            }
            if (addShip(ship)) {
                i--;
                if (i == 4 | i == 7 | i == 9)
                    countDeck --;
            }
        }
    }
    
    /**
     * Метод, що приймає повідомлення від сервера
     * @return повертає true, якщо відправлено повідомлення і false, якщо ні  
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public boolean receiveMessage() throws IOException, ClassNotFoundException {
        Message message = connection.receive(); 
        if (message.getMessageType() == MessageType.SHOT) {
            int x = message.getX();
            int y = message.getY();
            Box box = model.getBox(model.getMyField(), x, y);
            if (box.getPicture() == Picture.EMPTY) {
                box.setPicture(Picture.POINT);
                Music.playMusic("shot");
            } else if (box.getPicture() == Picture.SHIP) {
                box.setPicture(Picture.DESTROY_SHIP);
                Music.playMusic("hit");
            }
            model.addBoxInField(model.getMyField(), box);
            if (checkEndGame()) {
                connection.send(new Message(MessageType.DEFEAT));
                View.callInformationWindow("Ви проіграли! Всі Ваші кораблі винищені.");
                Music.playMusic("lose");
                setPlayerInfo(menu.player,false);
                return false;
            }
            return true;
        } else if (message.getMessageType() == MessageType.DISCONNECT) {
            connection.send(new Message(MessageType.MY_DISCONNECT));
            View.callInformationWindow("Ваш суперник покинув ігру. Ви отримали технічну перемогу!");
            Music.playMusic("winner");  
            setPlayerInfo(menu.player,true);
            return false;
        } else if (message.getMessageType() == MessageType.DEFEAT) {
            connection.send(new Message(MessageType.MY_DISCONNECT));
            View.callInformationWindow("Всі кораблі суперника знищені. Ви отримали перемогу!");
            Music.playMusic("winner");
            setPlayerInfo(menu.player,true);
            return false;
        } else return false;
    }
    
    private void setPlayerInfo(Player p, boolean wins){
        p.setCounterBatlle(p.getCounterBatlle() + 1);
        if (wins){
            p.setCounterWins(p.getCounterWins() + 1);
            p.setPoints(p.getPoints() + 20);
        }else{
            p.setCounterLose(p.getCounterLose() + 1);
            p.setPoints(p.getPoints() - 20);
        }
        p.setRank(checkRangPlayers(p).getRank());
        SQL dataBase = new SQL();
        if(dataBase.Conect().equals("Ok")){
            dataBase.editPlayer(p);
        }
    }
    
    private Player checkRangPlayers(Player p){
        if(p.getPoints()  >= 10 && p.getPoints() <= 50){
            p.setRank("Матрос");
        }else if(p.getPoints()  >= 50 && p.getPoints() < 80){
            p.setRank("Старшина");
        }else if(p.getPoints()  >= 80 && p.getPoints() < 130){
            p.setRank("Головний корабельний старшина");
        }else if(p.getPoints()  >= 130 && p.getPoints() < 160){
            p.setRank("Лейтенант");
        }else if(p.getPoints()  >= 160 && p.getPoints() < 200){
            p.setRank("Капітан");
        }else if(p.getPoints()  >= 200 && p.getPoints() < 300){
            p.setRank("Командор");
        }else if(p.getPoints()  >= 300){
            p.setRank("Адмірал");
        }
        return p;
    }
}

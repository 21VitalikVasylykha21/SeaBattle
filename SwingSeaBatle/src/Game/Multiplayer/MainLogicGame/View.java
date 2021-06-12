package Game.Multiplayer.MainLogicGame;

import GUI.JFieldCircle;
import Game.Multiplayer.Panels.*;
import StartPoinMenu.MainMenu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;

public class View extends JFrame {

    private Controller controller;
    private Model model;
    private MyField myField; //панель свого ігрового поля
    private EnemyField enemyField; //панель ігрового поля суперника
    private ChoosePanel choosePanel; //панель налаштувань додавання корабля
    private PanelButtons panelButtons; //панель кнопок


    /**
     * Конструктор класу View Створюється форму гравця і задається їй початкові 
     * значення
     */
    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getContentPane().setBackground(Color.BLACK);
        setTitle("Battle Sea");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(Picture.getImage("icon"));
        Music.playMusic("fon");
    }
    
    public void goMainMenu (){
        Music.stopPlayMusic();
        Music.playMusic("fon_menu");
        this.dispose();
        MainMenu s = controller.getMenu();
        s.FrmMenu.setVisible(true);
    }

    /**
     * Метод, що приймає controller
     *
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Метод, що приймає model
     *
     * @param model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Метод, що ініцілізує весь графічний інтерфейс
     */
    public void init() {
        if (enemyField != null) {
            remove(enemyField);
            remove(myField);
            remove(panelButtons);
        }
        controller.loadEmptyMyField();
        add(choosePanel = new ChoosePanel(this), BorderLayout.EAST);
        add(myField = new MyField(this), BorderLayout.WEST);
        add(panelButtons = new PanelButtons(this), BorderLayout.SOUTH);
        myField.setChoosePanel(choosePanel);
        pack();
        revalidate();
        setVisible(true);
        
        UIManager.put("OptionPane.background", new Color(27, 79, 114));
        UIManager.put("Panel.background", new Color(27, 79, 114));
        UIManager.put("Button.background", new Color(52, 152, 219));
        UIManager.put("Button.setBorderPainted", false);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    /**
     * Метод, який викликає інформацію діалового вікна з текстом
     *
     * @param message текст який потрібно вивести
     */
    public static void callInformationWindow(String message) {
        JOptionPane.showMessageDialog(
                null, message,
                "Увага!", JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Метод, що загружує своє пусте ігрове поле
     */
    public void loadEmptyMyField() {
        controller.loadEmptyMyField();
        myField.repaint();            //переотрисовка нашего игрового поля
        //встановлення імені RadioButton на панелі налаштуваннь додававння кораблів
        choosePanel.setNameOneDeck(4);
        choosePanel.setNameTwoDeck(3);
        choosePanel.setNameThreeDeck(2);
        choosePanel.setNameFourDeck(1);
    }

    /**
     * Метод, що добавляє корабель
     *
     * @param ship корабель, що потрібно добавити
     */
    public void addShip(Ship ship) {
        if(!controller.addShip(ship)) callInformationWindow("Неможливо добавити корабель на дану область, тому що пересікається з іншим");
    }

    /**
     * Метод що, автоматично розташовує кораблі на своєму ігровому полі
     */
    public void autoAddShipMyField() {
        controller.autoAddShipMyField(myField);
        choosePanel.setNameOneDeck(0);
        choosePanel.setNameTwoDeck(0);
        choosePanel.setNameThreeDeck(0);
        choosePanel.setNameFourDeck(0);
    }

    /**
     * Видалення корабля з ігрового поля
     *
     * @param x координата Х
     * @param y координата У
     * @return повертає повертає видалений корабель
     */
    public Ship removeShip(int x, int y) {
        return controller.removeShip(x, y);
    }

    /**
     * Метод, що змінює імя в RadioButton при при видалення або добавленні
     * кораблів
     *
     * @param countDeck скільки палубний корабль
     */
    public void changeCountShipOnChoosePanel(int countDeck) {
        switch (countDeck) {
            case 1: {
                choosePanel.setNameOneDeck(4 - model.getShipsOneDeck().size());
                break;
            }
            case 2: {
                choosePanel.setNameTwoDeck(3 - model.getShipsTwoDeck().size());
                break;
            }
            case 3: {
                choosePanel.setNameThreeDeck(2 - model.getShipsThreeDeck().size());
                break;
            }
            case 4: {
                choosePanel.setNameFourDeck(1 - model.getShipsFourDeck().size());
                break;
            }
        }
        choosePanel.revalidate();
    }

    /**
     * Метод, що перемальовує своє ігрове поле
     *
     * @param g
     */
    public void repaintMyField(Graphics g) {
        Box[][] matrix = model.getMyField(); 
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j];
                if (box == null) {
                    continue;
                }
                g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), myField);
            }
        }
    }

    /**
     * Метод, що перемальовує ігрове поле суперника
     * @param g
     */
    public void repaintEnemyField(Graphics g) {
        Box[][] matrix = model.getEnemyField(); 
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j];
                if (box == null) {
                    continue;
                }
                if ((box.getPicture() == Picture.EMPTY || box.getPicture() == Picture.SHIP)) {
                    if (box.isOpen() && box.getPicture() == Picture.EMPTY) {
                        g.drawImage(Picture.getImage(Picture.POINT.name()), box.getX(), box.getY(), enemyField);
                    } 
                    else if ((box.isOpen() && box.getPicture() == Picture.SHIP)) {
                        g.drawImage(Picture.getImage(Picture.DESTROY_SHIP.name()), box.getX(), box.getY(), enemyField);
                    } 
                    else {
                        g.drawImage(Picture.getImage(Picture.CLOSED.name()), box.getX(), box.getY(), enemyField);
                    }
                } 
                else {
                    g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), enemyField);
                }
            }
        }
    }
    

    /**
     * Метод, для кнопки Старт Ігри
     */
    public void startGame() {
        if (controller.checkFullSetShips()) { 
              String[] options = {"Створити кімнату", "Підключитися до кімнати"};
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Створити кімнату, введіть 4-ьох значний номер кімнати,");
            JLabel label2 = new JLabel("або підключіться до вже створенної:");
            label1.setForeground(Color.WHITE);
            label1.setForeground(Color.WHITE);
            JFieldCircle field = new JFieldCircle();
            JFieldCircle ip_fielt = new JFieldCircle();
            field.placeholder("Ведіть порт");
            ip_fielt.placeholder("Ведіть IP");
            panel.setBackground(new Color(27, 79, 114));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label1);
            panel.add(label2);
            panel.add(ip_fielt);
            panel.add(field);
            
            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Створення кімнати:",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try {
                if (selectedOption == 0) { 
                    int port = Integer.parseInt(field.getText().trim());
                    String ip = ip_fielt.getText();
                    controller.createGameRoom(port); 
                    panelButtons.setTextInfo("ОЧІКУЄМО СУПЕРНИКА");
                    panelButtons.revalidate();
                    panelButtons.getExitButton().setEnabled(true);
                    View.callInformationWindow("Очікуємо суперника: після підключення суперника до кімнати, зявиться повідомлення. Потім начнеться ігра. Ваш хід перший.");
                    controller.connectToRoom(ip, port); 
                    View.callInformationWindow("Другий гравець підключився! Можемо начинати битву.");
                    refreshGuiAfterConnect(); 
                    panelButtons.setTextInfo("ЗАРАЗ ВАШ ХІД");
                    panelButtons.getExitButton().setEnabled(true); 
                    enemyField.addListener();
                } else if (selectedOption == 1) { 
                    int port = Integer.parseInt(field.getText().trim());
                    String ip = ip_fielt.getText();
                    controller.connectToRoom(ip, port); 
                    View.callInformationWindow("Ви вдало підключилися до кімнати. Ваш суперник стріляє першим.");
                    refreshGuiAfterConnect();
                    panelButtons.setTextInfo("ЗАРАЗ ХІД СУПЕРНИКА");
                    new ReceiveThread().start();
                }
            } catch (Exception e) {
                View.callInformationWindow("Виникла помилка при створенні кімнати, або некоректний номер кімнати, спробуйте іще раз.");
                e.printStackTrace();
            }
        } else {
            View.callInformationWindow("Ви добавили не всі кораблі на своє ігрове поле!");
        }
    }

    /**
     * Метод, який відключає клієнта від сервера
     */
    public void disconnectGameRoom() {
        try {
            controller.disconnectGameRoom();
            View.callInformationWindow("Ви відключилися від комнати. Ігра закінчена. Ви зазнали технічної поразки.");
            enemyField.removeListener(); 
        } catch (Exception e) {
            View.callInformationWindow("Виникла помилка при відключення від кімнати.");
        }
    }

    /**
     * Метод, що обновлює форму після підключення суперника
     */
    public void refreshGuiAfterConnect() {
        MouseListener[] listeners = myField.getMouseListeners();
        for (MouseListener lis : listeners) {
            myField.removeMouseListener(lis);
        }
        choosePanel.setVisible(false);
        remove(choosePanel);          
        add(enemyField = new EnemyField(this), BorderLayout.EAST); 
        enemyField.repaint(); 
        pack();  
        panelButtons.getStartGameButton().setEnabled(false); 
        revalidate();
    }

    /**
     * Метощ, що відправляє на сервер повідомлення з координатима відмальованої
     * клітинки
     *
     * @param x кордината по осі Х клітинки
     * @param y кордината по осі У клітинки
     */
    public void sendShot(int x, int y) {
        try {
            boolean isSendShot = controller.sendMessage(x, y); 
            if (isSendShot) { 
                enemyField.repaint();
                enemyField.removeListener(); 
                panelButtons.setTextInfo("Зараз хід суперника");
                panelButtons.getExitButton().setEnabled(false); 
                new ReceiveThread().start();
            }
        } catch (Exception e) {
            View.callInformationWindow("Виникла помилка при відправленні пострілу.");
            e.printStackTrace();
        }
    }

    //клас-потік, який оцікує повідомлення від сервера
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                boolean continueGame = controller.receiveMessage(); 
                myField.repaint();
                if (continueGame) { //если вернулось true, то...
                    panelButtons.setTextInfo("Зараз Ваш хід");
                    panelButtons.getExitButton().setEnabled(true); 
                    enemyField.addListener();  
                } else { 
                    panelButtons.setTextInfo("ІГРА ЗАКІНЧЕННА");
                    panelButtons.getExitButton().setEnabled(false);
                    enemyField.removeListener();
                    panelButtons.getRestartGameButton().setEnabled(true);
                    panelButtons.getGoMenuButton().setEnabled(true);
                }

            } catch (IOException | ClassNotFoundException e) {
                View.callInformationWindow("Виникла помилка під час прийому повідомлення від сервера.");
                e.printStackTrace();
            }
        }
    }
}

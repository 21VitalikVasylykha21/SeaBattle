package Game.Multiplayer.Connect;

import Game.Multiplayer.MainLogicGame.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Vitalik
 */
public class Server extends Thread {
    private ServerSocket serverSocket;
    private Box[][] fieldPlayer1; 
    private Box[][] fieldPlayer2; 
    private List<Ship> allShipsPlayer1; 
    private List<Ship> allShipsPlayer2; 
    private volatile boolean allPlayersConnected = false; 
    private List<Connection> listConnection = new ArrayList<>(); 

    /**
     * Конструктор класу Server
     * @param port Порт для підключення сервера
     * @throws IOException 
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        startServer();
    }

    //запуск сервера
    private void startServer() {
        try {
            while (!allPlayersConnected) { 
                Socket socket = serverSocket.accept();
                if (listConnection.size() == 0) { 
                    Connection connection = new Connection(socket);
                    listConnection.add(connection); 
                    connection.send(new Message(MessageType.ACCEPTED)); 
                    Message message = connection.receive();  
                    if (message.getMessageType() == MessageType.FIELD) {
                        fieldPlayer1 = message.getGameField();
                        allShipsPlayer1 = message.getListOfAllShips();
                    }
                    new ThreadConnection(connection).start();
                }
                else if (listConnection.size() == 1) {
                    Connection connection = new Connection(socket);
                    listConnection.add(connection);
                    connection.send(new Message(MessageType.ACCEPTED));
                    Message message = connection.receive();
                    if (message.getMessageType() == MessageType.FIELD) {
                        fieldPlayer2 = message.getGameField();
                        allShipsPlayer2 = message.getListOfAllShips();
                        connection.send(new Message(MessageType.FIELD, fieldPlayer1, allShipsPlayer1));
                        listConnection.get(0).send(new Message(MessageType.FIELD, fieldPlayer2, allShipsPlayer2));
                    }
                    new ThreadConnection(connection).start();
                    allPlayersConnected = true;
                }
            }
            serverSocket.close();
        } catch (Exception e) {
            View.callInformationWindow("Виникла помилка при запуску сервера кімнати.");
        }
    }

    //клас-потік виконує для основного цикла спілкування клієнта і сервера
    private class ThreadConnection extends Thread {
        private Connection connection;
        private volatile boolean stopCicle = false; 

        /**
         * Конструктор класу ThreadConnection
         * @param connection підключення
         */
        public ThreadConnection(Connection connection) {
            this.connection = connection;
        }

        private void mainCicle(Connection connection) {
            try {
                while (!stopCicle) {
                    Message message = connection.receive(); 
                    
                    if (message.getMessageType() == MessageType.DISCONNECT || message.getMessageType() == MessageType.DEFEAT) {
                        sendMessageEnemy(message);
                        stopCicle = true;
                    } else if (message.getMessageType() == MessageType.MY_DISCONNECT) {
                        stopCicle = true;
                    } else sendMessageEnemy(message);
                }
                connection.close();
            } catch (Exception e) {
                View.callInformationWindow("Помилка при обміні вистрілами. Звязок втрачений");
            }
        }

        //відправлення повідомлення на сервер
        private void sendMessageEnemy(Message message) throws IOException {
            for (Connection con : listConnection) {
                if (!connection.equals(con)) {
                    con.send(message);
                }
            }
        }

        @Override
        public void run() {
            mainCicle(connection);
        }
    }
}

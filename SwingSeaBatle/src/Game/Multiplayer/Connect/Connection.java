package Game.Multiplayer.Connect;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    /**
     * Конструктор класу підєднання
     * @param socket сокет під яким підєднуємося
     * @throws IOException 
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    /**
     * Відправлення повідомлення в сервері
     * @param message що саме відправяємо
     * @throws IOException 
     */
    public void send(Message message) throws IOException {
        synchronized (this.out){
            out.writeObject(message);
        }
    }
    /**
     * Метод, що отримує повідомлення
     * @return повертаэ отримане повыдомлення
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (this.in){
            Message message = (Message) in.readObject();
            return message;
        }
    }
    /**
     * Метод, що закриваэ потік
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}

package Users;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Клас, що представляє собою корисутвача
 * @author Vitalik
 */
public class Player {

    String Name;
    String Password;
    int Points;
    String Rank;
    int CounterBatlle;
    int CounterWins;
    int CounterLose;
    String NameImage;

    /**
     * Конструктор класу Player
     * @param name імя користувача
     * @param password пароль користувача
     * @param points кількість очків користувача
     * @param rank звання користувача
     * @param counterBatlle кількість битв користувача
     * @param counterWins кількість виграних битв користувача
     * @param counterLose кількість проіграних битв користувача
     * @param nameImage  назва фотографії користувача
     */
    public void Player(String name, String password, int points, String rank, int counterBatlle, int counterWins, int counterLose, String nameImage) {
        Name = name;
        Password = password;
        Points = points;
        Rank = rank;
        CounterBatlle = counterBatlle;
        CounterWins = counterWins;
        CounterLose = counterLose;
        NameImage = nameImage;
    }

    /**
     * Метод, що задає імя користувача
     * @param name імя користувача 
     */
    public void setName(String name) {
        Name = name;
    }

     /**
     * Метод, що задає пароль користувача
     * @param password пароль користувача 
     */
    public void setPassword(String password) {
        Password = password;
    }

     /**
     * Метод, що задає кількість очків користувача
     * @param points кількість очків користувача 
     */
    public void setPoints(int points) {
        Points = points;
    }

     /**
     * Метод, що задає звання користувача
     * @param rank звання користувача 
     */
    public void setRank(String rank) {
        Rank = rank;
    }
     /**
     * Метод, що задає кількість битв користувача
     * @param counterBatlle кількість битв користувача 
     */
    public void setCounterBatlle(int counterBatlle) {
        CounterBatlle = counterBatlle;
    }
    
     /**
     * Метод, що задає кількість виграних битв  користувача
     * @param counterWins кількість виграних битв  користувача 
     */
    public void setCounterWins(int counterWins) {
        CounterWins = counterWins;
    }
    
     /**
     * Метод, що задає кількість проіграних битв користувача
     * @param counterLose кількість проіграних битв користувача 
     */
    public void setCounterLose(int counterLose) {
        CounterLose = counterLose;
    }
    
     /**
     * Метод, що задає назва фотографії користувача
     * @param nameImage назва фотографії користувача 
     */
    public void setImage(String nameImage) {
        NameImage = nameImage;
    }
    
    /**
     * Метод, що повертає імя користувача
     * @return імя користувача
     */
    public String getName(){
        return Name;
    }
    
    /**
     * Метод, що повертає пароль користувача
     * @return пароль користувача
     */
    public String getPassword(){
        return Password;
    }
    
    /**
     * Метод, що повертає кількість очків користувача
     * @return кількість очків користувача
     */
    public int getPoints(){
        return Points;
    }
    
    /**
     * Метод, що повертає звання користувача
     * @return звання користувача
     */
    public String getRank(){
        return Rank;
    }
    /**
     * Метод, що повертає кількість битв користувача
     * @return кількість битв користувача
     */
    public int getCounterBatlle(){
        return CounterBatlle;
    }
    /**
     * Метод, що повертає кількість виграних битв користувача
     * @return кількість виграних битв користувача
     */
    public int getCounterWins(){
        return CounterWins;
    }
    /**
     * Метод, що повертає кількість проіграних битв користувача
     * @return кількість проіграних битв користувача
     */
    public int getCounterLose(){
        return CounterLose;
    }
    /**
     * Метод, що повертає назву фотографії користувача
     * @return назву фотографії користувача
     */
    public String getImage(){
        return NameImage;
    }
    
    /**
     * Метод що хешує строку в SHA-256
     * @param txtPassword строку яку потрібно захешувати
     * @return повертає захешовану строку
     */
    public String hexPassword(String txtPassword){
        try {
            return toHexString(getSHA(txtPassword));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //метод що хешує строку
    private byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    //метод,що хешує строку
    private String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}

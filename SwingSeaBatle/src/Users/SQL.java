package Users;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Клас, що реалізує роботу із базою даних
 * @author Vitalik
 */
public class SQL {

    private final String user = "root";//КОРИСТУВАЧ
    private final String password = "";//ПАРОЛЬ
    private final String Host = "127.0.0.1";
    private final String Port = "3306";
    private final String DB = "sea_battle_database";
    private final String CP = "utf8";

    private Connection conn = null; //обєкт що представляє зєднання з БД
    public String tbl = "user";//ТАБЛИЦЯ З ЯКОЮ БУДЕМО ПРАЦЮВАТИ
    private Statement s = null;

    /**
     *Метод, що підключається до бази даних
     * @return
     */
    public String Conect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(this.Host);
        dataSource.setDatabaseName(this.DB);
        dataSource.setPort(Integer.parseInt(this.Port));

        try {
            dataSource.setServerTimezone("UTC");
        } catch (SQLException ex) {
            System.out.println("Err on setting Timezone :\n" + ex.toString());
        }
        try {
            dataSource.setCharacterEncoding(CP);
        } catch (SQLException ex) {
            System.out.println("Err on setting CP :\n" + ex.toString());
        }
        try {
            conn = dataSource.getConnection();
            return "Ok";
        } catch (SQLException ex) {
            System.out.println("Err on getting connection :\n" + ex.toString());
        }
        return "not Ok";
    }
    
    /**
     * Метод, що змінює значення користувача
     * @param player Корисувач, якого дані будемо змінювати в базі даних
     */
    public void editPlayer(Player player){
        String mySQLquery = "UPDATE `user` SET  `rank` = ?, `counter_poins` = ?, `counter_battle` = ?, `counter_wins` = ?, `counter_lose` = ? WHERE `user`.`name` = ?;";
        PreparedStatement X;
        try {
            X = (PreparedStatement) conn.prepareStatement(mySQLquery);
            X.setString(1, player.getRank());
            X.setInt(2,  player.getPoints());
            X.setInt(3,  player.getCounterBatlle());
            X.setInt(4, player.getCounterWins());
            X.setInt(5, player.getCounterLose());
            X.setString(6, player.getName());
            X.execute();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Сталася помилка", "Помилка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Метод, що додає корисутвача
     * @param player Корисувач, якого будемо додавати в базу даних
     */
    public void addPlayer(Player player) {
        String mySQLquery = "INSERT INTO `user` (`name`, `password`, `rank`, `counter_poins`, `counter_battle`, `counter_wins`, `counter_lose`, `image`) VALUES (?,?,?,?,?,?,?,?);";
        PreparedStatement X;
        try {
            X = (PreparedStatement) conn.prepareStatement(mySQLquery);
            X.setString(1, player.getName());
            X.setString(2, player.getPassword());
            X.setString(3, "Салага");
            X.setString(4, "0");
            X.setString(5, "0");
            X.setString(6, "0");
            X.setString(7, "0");
            X.setString(8, player.getImage());
            X.execute();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Сталася помилка при реєстрації", "Помилка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Метод, що повертає інформацію, з таблиці instructions_for_game
     * @return інформацію, про правила гри з таблиці instructions_for_game
     */
    public String getInformation() {
        String rez = "Помилка!!!";
        try {
            if (s == null) {
                s = conn.createStatement();
            }
            ResultSet r = s.executeQuery("SELECT * FROM `instructions_for_game`");

            if (r.next()) {
                return r.getString(1);
            }
        } catch (SQLException e) {
            return rez;
        }
        return rez;
    } 

    /**
     * Метод, що повертає інформацію про користувача
     * @param name імя користувача, інформацію якого потрібно повернути
     * @return  екземпляр класу Player, користувача
     */
    public Player getInfoPlayer(String name) {
        Player p = new Player();
        try {
            if (s == null) {
                s = conn.createStatement();
            }
            ResultSet r = s.executeQuery("SELECT * FROM `user` WHERE `name` LIKE '" + name +"'");

            if(r.next()){
               p.setName(r.getString(1));
                p.setPassword(r.getString(2));
                p.setRank(r.getString(3));
                p.setPoints(Integer.parseInt(r.getString(4)));
                p.setCounterBatlle(Integer.parseInt(r.getString(5)));
                p.setCounterWins(Integer.parseInt(r.getString(6)));
                p.setCounterLose(Integer.parseInt(r.getString(7)));
                p.setImage(r.getString(8)); 
            }
           
            return p;
        } catch (SQLException e) {
            System.out.println(e + "EROOOR" );
            return p;
        }
    }

    /**
     * Метод, що повертає  назву фотографіЇ рангн користувача
     * @param rank Ранг користувча
     * @return повертає назву фотографії користувача
     */
    public String getImageInRang(String rank) {
        String rez = "Помилка!!!";
        try {
            if (s == null) {
                s = conn.createStatement();
            }
            ResultSet r = s.executeQuery("SELECT `image_rank` FROM `rank_player` WHERE `rank` LIKE '"+ rank +"'");
            rez = "";
            if(r.next()){
                return r.getString(1);
            }
        } catch (SQLException e) {
            return rez;
        }
        return rez;
    }

    /**
     * Метод що повертає посортовану таблицю рейтингу
     * @return ResultSet - рейтингу
     */
    public ResultSet getRaiting() {
        ResultSet Xrez = null;
        try {
            if (s == null) {
                s = (Statement) conn.createStatement();
            }
            Xrez = s.executeQuery("SELECT `name`, `counter_poins`, `rank` FROM `user` ORDER BY `counter_poins` DESC");
            return Xrez;
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            return Xrez;
        }
    }
    
    /**
     * Метод що повертає посортовану таблицю звань
     * @return ResultSet - звань
     */
     public ResultSet getRank() {
        ResultSet Xrez = null;
        try {
            if (s == null) {
                s = (Statement) conn.createStatement();
            }
            Xrez = s.executeQuery("SELECT * FROM `rank_player` ORDER BY `counter_rank` DESC");
            return Xrez;
        } catch (SQLException ex) {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
            return Xrez;
        }
    }

    /**
     * Метод, що повертає екземпляр класу Connection 
     * @return Connection даного підключення
     */
    public Connection getCon() {
        return conn;
    }
}

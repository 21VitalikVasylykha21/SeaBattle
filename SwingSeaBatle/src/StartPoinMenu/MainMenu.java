package StartPoinMenu;

import GUI.ImageInTable;
import GUI.JFieldCircle;
import Game.Multiplayer.MainLogicGame.Controller;
import Game.Multiplayer.MainLogicGame.Model;
import Game.Multiplayer.MainLogicGame.Music;
import Game.Multiplayer.MainLogicGame.Picture;
import Game.Multiplayer.MainLogicGame.View;
import Users.Player;
import Users.SQL;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.sql.PreparedStatement;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.sql.Connection;
import javax.swing.UIManager;

/**
 * Клас, що реалізує собою головне меню гри
 * @author Vitalik
 */
public class MainMenu {

    public Player player;
    SQL sql;
    Connection con = null;
    MainMenu menu;
    boolean activeUser = false;
    String imageName = "";

    public JFrame FrmMenu;

    JPanel PnlFonBtnExit;
    JPanel PnlFonBtnStartGame;
    JPanel PnlFonImagePlayer;
    JPanel PnlFonInfoPlayer;
    JPanel PnlFonBtnRating;
    JButton BtnStartGame;
    JButton BtnExit;
    JButton BtnBack;
    JButton AddImage;
    JButton BtnSound;
    JButton BtnRating;
    JFieldCircle admin;
    JFieldCircle password_two;
    JFieldCircle password;
    JTable rating;
    JTable rank;
    JLabel header;
    JLabel ImagePlayer;
    JLabel LblFonMenu;
    DefaultTableModel model;
    DefaultTableModel modelRank;
    JScrollPane scroll;
    JTextArea Info;
    JTextArea InfoPlayers;
    Box content = new Box(BoxLayout.Y_AXIS);
    Box contentrank = new Box(BoxLayout.Y_AXIS);

    /**
     * Конструктор класу MainMenu
     */
    public MainMenu() {
        menu = this;
        UIManager.put("OptionPane.background", new Color(27, 79, 114));
        UIManager.put("Panel.background", new Color(27, 79, 114));
        UIManager.put("Button.background", new Color(52, 152, 219));
        UIManager.put("Button.setBorderPainted", false);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        Music.playMusic("fon_menu");
        //Головна форма
        FrmMenu = new JFrame();
        FrmMenu.setBounds(40, 40, 800, 440);
        FrmMenu.setResizable(false);
        FrmMenu.setLayout(null);
        FrmMenu.setIconImage(Picture.getImage("Icon"));
        FrmMenu.setTitle("Мережева гра: Морський бій");
        FrmMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrmMenu.setLocationRelativeTo(null);

        //Фонова мітка з гіфкою
        LblFonMenu = new JLabel();
        LblFonMenu.setBounds(0, 0, 800, 440);
        LblFonMenu.setVisible(true);
        Image image = Toolkit.getDefaultToolkit().createImage("res\\img\\game\\MenuFon.gif");
        ImageIcon imageIcon = new ImageIcon(image);
        imageIcon.setImageObserver(LblFonMenu);
        LblFonMenu.setIcon(imageIcon);

        //Оголошення елементів
        //Кнопочка наазд
        ImageIcon IconBtnBack = new ImageIcon(Picture.getImage("Back"));
        BtnBack = new JButton(IconBtnBack);

        ImageIcon IconBtnSound = new ImageIcon(Picture.getImage("sound_on"));
        BtnSound = new JButton(IconBtnSound);

        //Почати гру
        PnlFonBtnStartGame = new JPanel();
        BtnStartGame = new JButton("Почати гру");

        //Вийти
        PnlFonBtnExit = new JPanel();
        BtnExit = new JButton("Вийти з гри");

        //Інструкція по використаню
        Info = new JTextArea();

        //Рейтинг гравців
        PnlFonBtnRating = new JPanel();
        BtnRating = new JButton("Рейтинг гравців");

        password = new JFieldCircle();
        password_two = new JFieldCircle();
        admin = new JFieldCircle();

        header = new JLabel();
        header.setForeground(Color.white);
        header.setFont(new Font("Century Gothic", Font.ITALIC, 35));

        PnlFonImagePlayer = new JPanel();
        ImagePlayer = new JLabel("   Пред перегляд фото");

        PnlFonInfoPlayer = new JPanel();
        InfoPlayers = new JTextArea();

        rating = new JTable() {
            private static final long serialVersionUID = 1L;

            /**
             * дана дія домож уникнути редагування клітинок при двойному кліку
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        ;
        };
        
        rank = new JTable() {
            private static final long serialVersionUID = 1L;

            /**
             * дана дія домож уникнути редагування клітинок при двойному кліку
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        ;
        };
        
        //Властивості елементів
        content.setBackground(new Color(22, 33, 62));
        content.setBounds(100, 20, 630, 370);
        content.setVisible(false);
        ViewTable(rating, content);

        contentrank.setBackground(new Color(22, 33, 62));
        contentrank.setBounds(10, 10, 760, 310);
        contentrank.setVisible(false);
        ViewTable(rank, contentrank);

        PnlFonInfoPlayer.setBounds(50, 50, 300, 255);
        PnlFonInfoPlayer.setBackground(new Color(52, 152, 219, 150));
        PnlFonInfoPlayer.setVisible(false);

        //Почати гру
        PnlFonBtnStartGame.setBounds(290, 330, 220, 40);
        PnlFonBtnStartGame.setVisible(true);

        BtnStartGame.setBounds(290, 330, 220, 40);
        BtnStartGame.setVisible(true);
        draw(BtnStartGame, PnlFonBtnStartGame);
        sql = new SQL();
        String rez = sql.Conect();

        BtnStartGame.addActionListener((ActionEvent e) -> {
            if (BtnRating.getText().equals("Рейтинг гравців")) {
                BtnRating.setText("Каюта");
                BtnStartGame.setText("Гра по мережі");
                BtnExit.setText("Правила гри");
                BtnSound.setVisible(false);
                BtnBack.setVisible(true);
                PnlFonBtnRating.setVisible(true);
            } else if (BtnStartGame.getText().equals("Зареєструватися")) {
                setVisibleSingUp(true);
            } else if (BtnStartGame.getText().equals("Гра по мережі")) {
                if (activeUser) {
                    FrmMenu.dispose();
                    UIManager.put("Panel.background", new Color(41, 59, 95));
                    Music.stopPlayMusic();
                    View view = new View();
                    Model model1 = new Model();
                    Controller controller = new Controller(view, model1, menu);
                    view.setController(controller);
                    view.setModel(model1);
                    view.init();
                } else {
                    JOptionPane.showMessageDialog(null, "Щоб начати ігру, необхідно авторизуватися в особистому кабінеті!", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (BtnStartGame.getText().equals("Додати фото")) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
                fileChooser.addChoosableFileFilter(fnef);
                fileChooser.setDialogTitle("Вибір фотографії");
                fileChooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
                fileChooser.setBounds(0, 0, 100, 100);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        imageName = "res\\img\\players\\" + selectedFile.getName();
                        File f = new File(imageName);
                        copyFileUsingJavaFiles(selectedFile, f);
                        PnlFonImagePlayer.setVisible(false);
                        setImage(imageName, ImagePlayer, 300);
                        imageName = selectedFile.getName();
                    } catch (IOException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else if (BtnStartGame.getText().equals("Звання")) {
                PnlFonBtnExit.setVisible(false);
                PnlFonBtnRating.setVisible(false);
                BtnExit.setVisible(false);
                BtnRating.setVisible(false);
                rank();
                contentrank.setVisible(true);
                BtnStartGame.setText("Назад");
            } else if (BtnStartGame.getText().equals("Назад")) {
                contentrank.setVisible(false);
                PnlFonBtnExit.setVisible(true);
                PnlFonBtnRating.setVisible(true);
                BtnExit.setVisible(true);
                BtnRating.setVisible(true);
                BtnStartGame.setText("Звання");
                setUserRoom(true, player);
            }
        });

        //Вийти
        PnlFonBtnExit.setBounds(540, 330, 180, 40);
        PnlFonBtnExit.setVisible(true);

        BtnExit.setBounds(540, 330, 180, 40);
        BtnExit.setVisible(true);
        draw(BtnExit, PnlFonBtnExit);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setVisible(false);

        BtnExit.addActionListener((ActionEvent e) -> {
            if (BtnExit.getText().equals("Вийти з гри")) {
                System.exit(0);
            } else if (BtnExit.getText().equals("Правила гри")) {
                if (rez.equals("Ok")) {
                    PnlFonBtnStartGame.setVisible(false);
                    PnlFonBtnExit.setVisible(false);
                    PnlFonBtnRating.setVisible(false);
                    BtnExit.setVisible(false);
                    BtnRating.setVisible(false);
                    BtnStartGame.setVisible(false);
                    Info.setLineWrap(true);
                    Font text = new Font("Century Gothic", Font.ITALIC, 21);
                    Info.setFont(text);
                    Info.setEditable(false);
                    Info.setForeground(Color.WHITE);
                    Info.setText("                                           Правила гри\n "
                            + sql.getInformation());
                    Info.setBackground(new Color(27, 79, 114, 150));
                    Info.setCursor(null);
                    Info.setFocusable(false);
                    contents.setBounds(70, 10, 700, 390);
                    contents.add(Info);
                    JScrollPane scroll1 = new JScrollPane(Info);
                    contents.add(scroll1);
                    contents.setVisible(true);
                    contents.setBackground(new Color(27, 79, 114, 150));
                    scroll1.setBackground(new Color(27, 79, 114, 150));
                } else {
                    JOptionPane.showMessageDialog(null, "Виникла помилка під час підключення до бази даних!", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (BtnExit.getText().equals("Увійти")) {
                try {
                    con = sql.getCon();
                    player = new Player();
                    String USR = admin.getText();
                    String PWD = player.hexPassword(admin.getText());
                    String zaput = "SELECT * FROM `user` WHERE `name`=? AND `password`=? LIMIT 0,1";
                    PreparedStatement stm = con.prepareStatement(zaput);
                    stm.setString(1, USR);
                    stm.setString(2, PWD);
                    ResultSet rez1 = stm.executeQuery();
                    if (rez1.next()) {
                        setVisibleSingUp(false);
                        setVisibleAuthorization(false);
                        player = sql.getInfoPlayer(USR);
                        setUserRoom(true, player);
                        activeUser = true;
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(null, "Некоректна пара ім’я/пароль, повторіть ввід", "Помилка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }catch (SQLException ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (BtnExit.getText().equals("Зареєструватися")) {
                if (admin.equals("Введіть логін") || password.equals("Введіть пароль") || password_two.equals("Повторіть пароль")) {
                    JOptionPane.showMessageDialog(null, "Заповніть пусті поля!", "Помилка", JOptionPane.ERROR_MESSAGE);
                } else if (password_two.getText().equals(password.getText())) {
                    player = new Player();
                    player.setName(admin.getText());
                    player.setPassword(player.hexPassword(password.getText()));
                    player.setImage(imageName);
                    sql.addPlayer(player);
                    setVisibleSingUp(false);
                    setVisibleAuthorization(false);
                    setUserRoom(true, sql.getInfoPlayer(player.getName()));
                    activeUser = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Паролі не збігаються, повторіть ввід", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (BtnExit.getText().equals("Вийти із каюти")) {
                activeUser = false;
                BtnRating.setText("Рейтинг гравців");
                BtnStartGame.setText("Початок гри");
                BtnExit.setText("Вийти з гри");
                BtnSound.setVisible(true);
                setUserRoom(false, player);
            }
        });

        //Рейтинг гравців
        PnlFonBtnRating.setBounds(40, 330, 220, 40);
        PnlFonBtnRating.setVisible(true);

        BtnRating.setBounds(40, 330, 220, 40);
        BtnRating.setVisible(true);
        draw(BtnRating, PnlFonBtnRating);

        BtnRating.addActionListener((ActionEvent e) -> {
            if (BtnRating.getText().equals("Рейтинг гравців")) {
                String rez1 = sql.Conect();
                if (rez1.equals("Ok")) {
                    PnlFonBtnStartGame.setVisible(false);
                    PnlFonBtnExit.setVisible(false);
                    PnlFonBtnRating.setVisible(false);
                    BtnExit.setVisible(false);
                    BtnRating.setVisible(false);
                    BtnStartGame.setVisible(false);
                    BtnBack.setVisible(true);
                    raiting();
                    content.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Виникла помилка під час підключення до бази даних!", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (BtnRating.getText().equals("Каюта")) {
                String rez2 = sql.Conect();
                if (rez2.equals("Ok")) {
                    if (activeUser) {
                        setUserRoom(true, player);
                    } else {
                        setVisibleAuthorization(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Виникла помилка під час підключення до бази даних!", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (BtnRating.getText().equals("Відмінити")) {
                setVisibleSingUp(false);
                setVisibleAuthorization(false);
            } else if (BtnRating.getText().equals("Меню")) {
                BtnRating.setText("Рейтинг гравців");
                BtnStartGame.setText("Початок гри");
                BtnExit.setText("Вийти з гри");
                BtnSound.setVisible(true);
                setUserRoom(false, player);
            }
        });

        //Кнопочка назад      
        BtnBack.setBounds(10, 10, 50, 50);
        BtnBack.setBorderPainted(false);
        BtnBack.setFocusPainted(false);
        BtnBack.setContentAreaFilled(false);
        BtnBack.setVisible(false);

        BtnBack.addActionListener((ActionEvent e) -> {
            if (!contents.isVisible()) {
                BtnRating.setText("Рейтинг гравців");
                BtnStartGame.setText("Початок гри");
                BtnExit.setText("Вийти з гри");
                BtnSound.setVisible(true);
                BtnBack.setVisible(false);
                if (content.isVisible()) {
                    content.setVisible(false);
                    PnlFonBtnStartGame.setVisible(true);
                    PnlFonBtnExit.setVisible(true);
                    PnlFonBtnRating.setVisible(true);
                    BtnExit.setVisible(true);
                    BtnRating.setVisible(true);
                    BtnStartGame.setVisible(true);
                }
            } else if (contents.isVisible()) {
                contents.setVisible(false);
                BtnSound.setVisible(false);
                PnlFonBtnStartGame.setVisible(true);
                PnlFonBtnExit.setVisible(true);
                PnlFonBtnRating.setVisible(true);
                BtnExit.setVisible(true);
                BtnRating.setVisible(true);
                BtnStartGame.setVisible(true);
                BtnRating.setText("Каюта");
                BtnStartGame.setText("Гра по мережі");
                BtnExit.setText("Правила гри");
            }
        });

        BtnSound.setBounds(10, 10, 50, 50);
        BtnSound.setBorderPainted(false);
        BtnSound.setFocusPainted(false);
        BtnSound.setContentAreaFilled(false);
        BtnSound.setVisible(true);

        BtnSound.addActionListener((ActionEvent e) -> {
            if (Music.playSound) {
                Music.stopPlayMusic();
                Music.playSound = false;
                BtnSound.setIcon(new ImageIcon(Picture.getImage("sound_off")));
                
            } else {
                Music.playSound = true;
                BtnSound.setIcon(new ImageIcon(Picture.getImage("sound_on")));
                Music.playMusic("fon_menu");
            }
        });

        //Додавання на форму елементів
        FrmMenu.add(PnlFonBtnStartGame);
        FrmMenu.add(BtnStartGame);
        FrmMenu.add(PnlFonBtnRating);
        FrmMenu.add(BtnRating);
        FrmMenu.add(PnlFonBtnExit);
        FrmMenu.add(BtnExit);
        FrmMenu.add(BtnBack);
        FrmMenu.add(BtnSound);
        FrmMenu.add(admin);
        FrmMenu.add(password_two);
        FrmMenu.add(password);
        FrmMenu.add(contents);
        FrmMenu.add(content);
        FrmMenu.add(contentrank);
        FrmMenu.add(header);
        FrmMenu.add(ImagePlayer);
        FrmMenu.add(PnlFonImagePlayer);
        FrmMenu.add(InfoPlayers);
        FrmMenu.add(PnlFonInfoPlayer);

        //Fon
        FrmMenu.add(LblFonMenu);
        //Frm Settings
        FrmMenu.setVisible(true);
    }

    //копіювання файла
    private void copyFileUsingJavaFiles(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    //авторизація корисутувача
    private void setVisibleAuthorization(boolean visible) {
        Font f = new Font("Century Gothic", Font.ITALIC, 18);

        header.setVisible(visible);
        header.setBounds(270, 60, 300, 50);
        header.setText("Авторизація");

        admin.setFont(f);
        admin.setVisible(visible);
        admin.setBounds(200, 150, 400, 40);

        password.setFont(f);
        password.setVisible(visible);
        password.setBounds(200, 220, 400, 40);

        admin.placeholder("Введіть логін");
        password.placeholder("Введіть пароль");

        BtnBack.setVisible(!visible);
        if (visible) {
            BtnRating.setText("Відмінити");
            BtnStartGame.setText("Зареєструватися");
            BtnExit.setText("Увійти");
            PnlFonBtnStartGame.setBounds(280, 330, 240, 40);
            BtnStartGame.setBounds(280, 330, 240, 40);
        } else {
            BtnRating.setText("Каюта");
            BtnStartGame.setText("Гра по мережі");
            BtnExit.setText("Правила гри");
            PnlFonBtnStartGame.setBounds(290, 330, 220, 40);
            BtnStartGame.setBounds(290, 330, 220, 40);
        }
    }

    //реєстрація користувача
    private void setVisibleSingUp(boolean visible) {
        Font f = new Font("Century Gothic", Font.ITALIC, 18);

        admin.setBounds(325, 100, 400, 40);
        password.setBounds(325, 160, 400, 40);
        password_two.setFont(f);
        password_two.setVisible(visible);
        password_two.setBounds(325, 220, 400, 40);
        password_two.placeholder("Повторіть пароль");

        PnlFonImagePlayer.setBackground(new Color(27, 79, 114, 150));
        PnlFonImagePlayer.setBounds(10, 10, 300, 300);
        PnlFonImagePlayer.setVisible(visible);

        ImagePlayer.setForeground(Color.white);
        ImagePlayer.setFont(new Font("Century Gothic", Font.ITALIC, 24));
        ImagePlayer.setBounds(10, 10, 300, 300);
        ImagePlayer.setVisible(visible);

        header.setText("Реєстрація");
        header.setBounds(430, 30, 200, 50);
        if (visible) {
            BtnRating.setText("Відмінити");
            BtnStartGame.setText("Додати фото");
            BtnExit.setText("Зареєструватися");
            PnlFonBtnStartGame.setBounds(280, 330, 210, 40);
            BtnStartGame.setBounds(280, 330, 210, 40);
            PnlFonBtnExit.setBounds(510, 330, 240, 40);
            BtnExit.setBounds(510, 330, 240, 40);
        } else {
            BtnRating.setText("Відминити");
            BtnStartGame.setText("Зареєструватися");
            BtnExit.setText("Увійти");
            PnlFonBtnExit.setBounds(540, 330, 180, 40);
            BtnExit.setBounds(540, 330, 180, 40);
        }
    }

    //створення круглої форми фотографії
    private void setImage(String name, JLabel s, int size) {
        try {
            BufferedImage master = ImageIO.read(new File(name));
            int diameter = Math.min(master.getWidth(), master.getHeight());
            BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = mask.createGraphics();
            applyQualityRenderingHints(g2d);
            g2d.fillOval(0, 0, diameter - 1, diameter - 1);
            g2d.dispose();
            BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            g2d = masked.createGraphics();
            applyQualityRenderingHints(g2d);
            int x = (diameter - master.getWidth()) / 2;
            int y = (diameter - master.getHeight()) / 2;
            g2d.drawImage(master, x, y, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
            g2d.drawImage(mask, 0, 0, null);
            g2d.dispose();

            s.setIcon(new ImageIcon(new ImageIcon(masked).getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT)));
        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    
    private void draw(JButton btn, JPanel pln) {
        Font text = new Font("Century Gothic", Font.ITALIC, 22);
        pln.setBackground(new Color(27, 79, 114, 150));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(text);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pln.setBackground(new Color(52, 152, 219, 150));

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pln.setBackground(new Color(27, 79, 114, 150));
            }
        });
    }

    private void raiting() {
        model = new DefaultTableModel();
        String[] columnsHeader = new String[]{"Імя користувача", "Кількість очків", "Звання"};

        ResultSet X = sql.getRaiting();
        int i = 3, ii = 1;
        model.setRowCount(0);//
        try {
            model.setColumnIdentifiers(columnsHeader);
            while (X.next()) {
                model.setRowCount(model.getRowCount() + 1);
                for (int j = 1; j <= i; j++) {
                    String g = X.getString(j);
                    if (j == 3) {
                        SQL image = new SQL();
                        image.tbl = "user";
                        image.Conect();
                        model.setValueAt("res\\img\\rank\\" + image.getImageInRang(g) + ".png", ii - 1, j - 1);
                        continue;
                    }
                    model.setValueAt(g, ii - 1, j - 1);//виводимо дані у модел
                }
                ii++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        rating.setModel(model);
        rating.getColumn("Звання").setCellRenderer(new ImageInTable());
    }

    private void rank() {
        modelRank = new DefaultTableModel();
        String[] columnsHeader = new String[]{"Звання", "Кількість очків", "Інформація", "Позначення"};

        String rez = sql.Conect();
        if (rez.equals("Ok")) {

            ResultSet X = sql.getRank();
            int i = 4, ii = 1;
            modelRank.setRowCount(0);//
            try {
                modelRank.setColumnIdentifiers(columnsHeader);
                while (X.next()) {
                    modelRank.setRowCount(modelRank.getRowCount() + 1);
                    for (int j = 1; j <= i; j++) {
                        String g = X.getString(j);
                        if (j == 4) {
                            modelRank.setValueAt("res\\img\\rank\\" + g + ".png", ii - 1, j - 1);
                            continue;
                        }
                        modelRank.setValueAt(g, ii - 1, j - 1);//виводимо дані у модел
                    }
                    ii++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            rank.setModel(modelRank);
            rank.getColumn("Позначення").setCellRenderer(new ImageInTable());

        } else {
            JOptionPane.showMessageDialog(null, "Виникла помилка під час підключення до бази даних!", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    //інтерфейс таблиць
    private void ViewTable(JTable table, Box contents) {
        JTableHeader heder = table.getTableHeader();
        heder.setBackground(new Color(27, 79, 114));
        heder.setForeground(Color.yellow);
        heder.setFont(new Font("Tahome", Font.BOLD, 14));
        ((DefaultTableCellRenderer) heder.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.setBackground(new Color(22, 33, 62));
        table.setForeground(Color.yellow);
        table.setRowHeight(60);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setGridColor(new Color(52, 152, 219));
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(22, 33, 62));
        scroll.setBorder(new LineBorder(new Color(27, 79, 114), 5));
        contents.add(scroll);
        contents.setBorder(new LineBorder(new Color(27, 79, 114, 150), 5));
    }

    //"Каюта" користувача
    private void setUserRoom(boolean visible, Player p) {
        PnlFonInfoPlayer.setVisible(visible);

        if (visible) {
            BtnRating.setText("Меню");
            BtnStartGame.setText("Звання");
            BtnExit.setText("Вийти із каюти");
            PnlFonBtnStartGame.setBounds(280, 330, 190, 40);
            BtnStartGame.setBounds(280, 330, 190, 40);
            PnlFonBtnExit.setBounds(490, 330, 250, 40);
            BtnExit.setBounds(490, 330, 250, 40);

            ImagePlayer.setBounds(420, 40, 270, 270);
            setImage("res\\img\\players\\" + p.getImage(), ImagePlayer, 270);
            BtnBack.setVisible(!visible);
        } else {
            PnlFonBtnStartGame.setBounds(290, 330, 220, 40);
            BtnStartGame.setBounds(290, 330, 220, 40);
            PnlFonBtnExit.setBounds(540, 330, 180, 40);
            BtnExit.setBounds(540, 330, 180, 40);
        }

        ImagePlayer.setVisible(visible);
        InfoPlayers.setLineWrap(true);
        Font text = new Font("Century Gothic", Font.ITALIC, 24);
        InfoPlayers.setFont(text);
        InfoPlayers.setEditable(false);
        InfoPlayers.setForeground(Color.WHITE);
        InfoPlayers.setText("\n   Ім'я: " + p.getName()
                + "\n" + "  Звання: " + p.getRank()
                + "\n" + "  Кількість очків: " + p.getPoints()
                + "\n" + "  Зіграно ігр: " + p.getCounterBatlle()
                + "\n" + "  Перемог: " + p.getCounterWins()
                + "\n" + "  Проіграшів: " + p.getCounterLose()
        );
        InfoPlayers.setBackground(new Color(27, 79, 114, 150));
        InfoPlayers.setCursor(null);
        InfoPlayers.setBounds(50, 50, 300, 255);
        InfoPlayers.setFocusable(false);
        InfoPlayers.setVisible(visible);

    }
}

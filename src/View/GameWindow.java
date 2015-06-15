package View; /**
 * Created by developermsv on 03.06.2015.
 */
import Controler.EventHandler;

import javax.sound.sampled.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static java.awt.Toolkit.getDefaultToolkit;

public class GameWindow  {
    public MyJLabel [][] arrayPlayerField;
    public MyJLabel [][] arrayComputerField;
    public JFrame mainWindow;
    JLabel lbNamePlayer;
    ImagePanel panelCenter;
    EventHandler eventHandler;
    JButton btnNewGame;
    // графическое состояние ячейки
    static final char SYMBOL_CLEAR_CELL = '.';
    static final char SYMBOL_SHIP = 'X';
    static final char SYMBOL_DESTROYED_SHIP = '+';
    static final char SYMBOL_AFTER_SHOOT = '*';

    public ImageIcon imDeck;
    public ImageIcon imDeckDestroy;
    public ImageIcon imAfterShoot;
    public ImageIcon imAim;
    static final int FIELD_SIZE = 10;    // размер поля для игры
    final Dimension WINDOW_SIZE = new Dimension(820,690);
    final Dimension cellLabelSize = new Dimension(35,37);
    final int playerFieldCornerX = 33;
    final int playerFieldCornerY = 33;
    final int computerFieldCornerX = 436;
    final int computerFieldCornerY = 33;
    public JDialog dialogNewGame;
    public JTextField tePlayerName;
    public JCheckBox chAutoShooting;
    public JCheckBox chShowShip;
    DefaultListModel listModel;
    private Dimension sizeScreen;
    private JScrollPane mainScrollPaneInfo;
    public JDialog dialogWinner;
    public URL ptsAfterShootWater;
    public URL ptsKorablprotivnikapoptoplen1;
    public URL ptsMipeteryalikorabl1;
    public URL ptsEstpopadanie1;
    public URL ptsVnaspopadanie1;
    public URL ptsProigaral;
    public URL ptsPobeda;

    public GameWindow (EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        // создаем центральный обработчик событий
        mainWindow = new JFrame("Sea Battle v 1.12.06");
        mainWindow.setMinimumSize(WINDOW_SIZE);
        sizeScreen = getDefaultToolkit().getScreenSize();
        mainWindow.setLocation((sizeScreen.width - WINDOW_SIZE.width) / 2, (sizeScreen.height - WINDOW_SIZE.height) / 2);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());
        // подгружаем картинки
        initImages();
        //инициализируем пути к звукам
        initAudio();
        // создаем панели
        createPanel();
        arrayComputerField = new MyJLabel[FIELD_SIZE][FIELD_SIZE];
        arrayPlayerField = new MyJLabel[FIELD_SIZE][FIELD_SIZE];
        //mainWindow.pack();
        mainWindow.setVisible(true);

    }


    void fillArrayFields(int startX,int startY,MyJLabel [][] array,MouseListener mouseListener){
        int y = startY;
        for (int i = 0; i < array.length; i++) {
            int x = startX;
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == null) {
                    array[i][j] = new MyJLabel();
                    if (mouseListener != null) {
                        array[i][j].addMouseListener(mouseListener);
                    }
                    array[i][j].setBounds(x, y, cellLabelSize.width, cellLabelSize.height);
                    panelCenter.add(array[i][j]);
                }
                array[i][j].stateIcon = null;
                array[i][j].setIcon(array[i][j].stateIcon);
                array[i][j].setName(String.valueOf(i)+ " " + String.valueOf(j));
                        x += cellLabelSize.width - 1;
            }
            y += cellLabelSize.height - 1;
        }
    }
    private void createPanel() {
        // создаем верхнюю панель
        JPanel panelTop = new JPanel();
        panelTop.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1),
        BorderFactory.createLineBorder(Color.GRAY, 1)));
        mainWindow.add(panelTop, BorderLayout.PAGE_START);
        lbNamePlayer = new JLabel("Игрок: безымянный");
        lbNamePlayer.setPreferredSize(new Dimension(WINDOW_SIZE.width / 2, 20));
        JLabel lbComputer = new JLabel("Компьютер");
        panelTop.add(lbNamePlayer);
        panelTop.add(lbComputer);
        // создаем центральную панель поля
        panelCenter = new ImagePanel("/View/images/field.jpg");
        mainWindow.add(panelCenter, BorderLayout.CENTER);
        // нижняя панель с кнопками и панелью инфы
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new BorderLayout());
        mainWindow.add(panelBottom, BorderLayout.PAGE_END);
        panelBottom.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // панель информации
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelInfo.setLayout(new BorderLayout());
        listModel = new DefaultListModel();
        listModel.addElement("Для начала или перезапуска игры нажмите кнопку \"Новая игра / Перезапуск\"");
        JList list = new JList(listModel);
        mainScrollPaneInfo = new JScrollPane(list);
        panelInfo.add(mainScrollPaneInfo, BorderLayout.CENTER);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Лог игры"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelBottom.add(panelInfo, BorderLayout.PAGE_START);
        JPanel panelButton = new JPanel();
        panelBottom.add(panelButton, BorderLayout.PAGE_END);
        btnNewGame = new JButton("Новая игра / Перезапуск");
        btnNewGame.setActionCommand("btnNewGame");
        btnNewGame.addActionListener(eventHandler);
        panelButton.add(btnNewGame, BorderLayout.EAST);
    }

    private void initImages(){
        imDeck = ImagePanel.createIcon("/View/images/deck.jpg");
        imDeckDestroy = ImagePanel.createIcon("/View/images/deckdestroy.jpg");
        imAfterShoot = ImagePanel.createIcon("/View/images/aftershoot.jpg");
        imAim = ImagePanel.createIcon("/View/images/aim.jpg");
    }
    private void initAudio(){
        ptsAfterShootWater =  GameWindow.class.getResource("/View/audio/AfterShootWater1.wav");
        ptsVnaspopadanie1 =  GameWindow.class.getResource("/View/audio/vnaspopadanie1.wav");
        ptsEstpopadanie1 =  GameWindow.class.getResource("/View/audio/estpopadanie1.wav");
        ptsKorablprotivnikapoptoplen1 = GameWindow.class.getResource("/View/audio/korablprotivnikapoptoplen1.wav");
        ptsMipeteryalikorabl1 = GameWindow.class.getResource("/View/audio/mipeteryalikorabl1.wav");
        ptsProigaral = GameWindow.class.getResource("/View/audio/proigaral.wav");
        ptsPobeda = GameWindow.class.getResource("/View/audio/pobeda.wav");
    }
    public void InitPanelCenter() {
        panelCenter.setVisible(true);
        // заполняеми поле пока пустыми View.MyJLabel
        fillArrayFields(playerFieldCornerX, playerFieldCornerY, arrayPlayerField,null);
        fillArrayFields(computerFieldCornerX, computerFieldCornerY, arrayComputerField,eventHandler);
        lbNamePlayer.setText("Игрок: " + tePlayerName.getText());
        listModel.clear();

    }
    public void newGameWindow () {
        dialogNewGame = new JDialog();
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10,10,5,5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialogNewGame.add(panel);
        JPanel pn1 = new JPanel();
        pn1.setLayout(new BorderLayout());
        JLabel label = new JLabel(" Ваше имя: ");
        tePlayerName = new JTextField(30);
        pn1.add(label, BorderLayout.WEST);
        pn1.add(tePlayerName, BorderLayout.CENTER);

        JPanel pn2 = new JPanel();
        pn2.setBorder(new EmptyBorder(5,0,5,0));
        pn2.setLayout(new BorderLayout());
        chAutoShooting = new JCheckBox();
        chAutoShooting.setText("автоматический выстрел (отсутствует звук)");
        chShowShip = new JCheckBox();
        chShowShip.setText("показать корабли компьютера");
        chShowShip.setSelected(true);
        pn2.add(chAutoShooting, BorderLayout.WEST);
        pn2.add(chShowShip, BorderLayout.EAST);

        JPanel pn3 = new JPanel();
        JButton btnStart = new JButton("Начинаем!");
        btnStart.setActionCommand("btnStart");
        btnStart.addActionListener(eventHandler);
        JButton btnCancel = new JButton("Отмена");
        btnCancel.setActionCommand("btnCancel");
        btnCancel.addActionListener(eventHandler);
        pn3.add(btnStart);
        pn3.add(btnCancel);

        dialogNewGame.setLocationRelativeTo(null);
        dialogNewGame.setTitle("Введите параметры новой игры");
        panel.add(pn1);
        panel.add(pn2);
        panel.add(pn3);
        dialogNewGame.pack();
        dialogNewGame.setLocation((sizeScreen.width - dialogNewGame.getWidth()) / 2, (sizeScreen.height - dialogNewGame.getHeight()) / 2);
        dialogNewGame.setModal(true);
        dialogNewGame.setVisible(true);
    }
    public void showMessage(String message) {
        listModel.addElement(message);
        mainScrollPaneInfo.getVerticalScrollBar().setValue(mainScrollPaneInfo.getVerticalScrollBar().getMaximum());
    };
    public void repaintLabel(MyJLabel lb,ImageIcon im){
        if (lb.getIcon() != im){
            lb.setStateIcon(im);
            lb.setIcon(im);
        }
    }

    public void showWinnerDialog(String dialogMessage) {
        dialogWinner = new JDialog();
        dialogWinner.setTitle("Поздравление победителю");

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10,10,5,5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialogWinner.add(panel);
        JPanel pn1 = new JPanel();
        pn1.setLayout(new BorderLayout());
        JLabel label = new JLabel(dialogMessage);
        pn1.add(label, BorderLayout.CENTER);

        JPanel pn3 = new JPanel();
        JButton btnStart = new JButton("Новая игра!");
        btnStart.setActionCommand("btnStart2");
        btnStart.addActionListener(eventHandler);
        JButton btnCancel = new JButton("Выход из игры");
        btnCancel.setActionCommand("btnExit");
        btnCancel.addActionListener(eventHandler);
        pn3.add(btnStart);
        pn3.add(btnCancel);
        panel.add(pn1);
        panel.add(pn3);
        dialogWinner.pack();
        dialogWinner.setLocation((sizeScreen.width - dialogWinner.getWidth()) / 2, (sizeScreen.height - dialogWinner.getHeight()) / 2);
        dialogWinner.setModal(true);
        dialogWinner.setVisible(true);
    }

    public void playSound(URL toPlay) {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(toPlay);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

        clip.start();
    }
}

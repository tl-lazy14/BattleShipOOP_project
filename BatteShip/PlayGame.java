package BatteShip;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PlayGame extends JLabel implements ActionListener {
    private JFrame frame;
    private Map playerMap; // bản đồ của người chơi -> máy bắn trên này
    private Map computerMap; // panel1 chứa 2 bản đồ// panel2 chứa thông tin chơi (turn,...)
    private JLabel historyText;
    private Clip clip;

    private static boolean isHard;
    private static boolean isPlaySound;
//    private static int xLeftPl; // tọa độ cua map PL
//    private static int yUpPL; // tọa độ của map PL
//    private static int xLeftCM;
//    private static int yUpCM;
    private static Queue<String> Q = new LinkedList<>();


    private static boolean[][] markP, markC; // markP : đánh dấu điểm đã bắn trên computerMap, ngược lại với markC
    private static boolean isPlayer; // turn của player hay của computer
    private static int playerHit = 0, computerHit = 0; // số điểm bắn trúng hiện tại
    private static int sumHit=15;//Tổng số mảnh tàu cần bắn trúng
    private static int sumTurn=0;
    private static int Score=0;

    private static ArrayList<String> A; // ứng với bản đồ của máy chơi
    private static ArrayList<String> B; // ứng với bản đồ của người chơi
    private static boolean[]isNgangPlayer;
    private static boolean[]isNgangComputer;
    private static Ship[] ship;

    public PlayGame(int w, int h, Map player, Map computer, boolean gamemode, ArrayList<String> pl,
                    ArrayList<String> cm, boolean[] isNgangPl,boolean[] isNgangcm, boolean playsound) {
        super();
        if (gamemode)
            isHard = true;
        else
            isHard = false;
        isPlaySound=playsound;

        A = pl;
        B = cm;
        isNgangPlayer =isNgangPl;
        isNgangComputer=isNgangcm;

        playerMap = player;
        playerMap.setOpaque(false);
        playerMap.setLocation(30,140);
        ship=new Ship[6];
        for (int tmp=1;tmp<=5;tmp++) {
            if (A.get(tmp) == "")
                continue;
            int x = Integer.parseInt(A.get(tmp));
            int leng = x / 10000;
            int j = (x - leng * 10000) / 100;
            int i = x % 100;
            boolean k=isNgangPlayer[tmp];
            ship[tmp]=new Ship(leng,50*leng,50,k);
            ship[tmp].setLocation(30+56*(j-1)+3,140+56*(i-1)+5);
            this.add(ship[tmp]);
        }

        computerMap = computer;
        computerMap.setOpaque(false);
        computerMap.setLocation(playerMap.getX()+600,playerMap.getY());

        historyText=new JLabel();
        historyText.setText("<html><div style='font-size:25; color:red;text-align:center'> Player </div>");
        historyText.setFont(new Font("Arial", Font.BOLD, 17));
        historyText.setVerticalAlignment(SwingConstants.CENTER);
        historyText.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane scroller = new JScrollPane(historyText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setOpaque(true);
        scroller.setLocation(0,0);
        scroller.setBackground(Color.WHITE);
        scroller.setSize(300,100);

        init();
        addAction();
        this.add(playerMap);
        this.add(computerMap);
        this.add(scroller);


        this.setSize(w,h);
        this.setIcon(new ImageIcon(loadImage("/img/bg3.jpg",w,h)));

        frame = new JFrame("Battle Ship");
        frame.setIconImage(loadImage("/img/logo.png", 90, 90));
        frame.add(this);
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        playSound("/sound/sound.wav");
        if (!isPlaySound) clip.stop();

    }

    private void init() {
        markP = new boolean[11][11];
        markC = new boolean[11][11];
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                markP[i][j] = false;
                markC[i][j] = false;
            }
        }
        isPlayer = true;
        System.out.println(B);
    }

    private void updateHistoryText(int i,int j,boolean isHit){
        String k;
        if(isHit) k="HIT";
        else k="MISS";
        sumTurn++;
        historyText.setText(historyText.getText()+"<br>"+"<html>Step "+sumTurn+" : "+k+" x = "+i+"; y = "+j);
    }

    private boolean shot(int i, int j) {
        markC[i][j] = true;
        if (playerMap.isShip[i][j]) {
            playerMap.mapPiece[i][j].setIcon(new ImageIcon(loadImage("/img/RED.png", 56, 56)));

            computerHit++;
            return true;
        }
        playerMap.mapPiece[i][j].setIcon(new ImageIcon(loadImage("/img/WHITE.png", 56, 56)));
        return false;
    }

    private void hitRandom() {
        Random rd = new Random();
        int i = rd.nextInt(10) + 1;
        int j = rd.nextInt(10) + 1;
        if (!markC[i][j]) {
            shot(i, j);
            isPlayer = true;
        } else
            hitRandom();
    }

    //hit random cho chế độ khó
    private void hitRandomHard() {
        if (!Q.isEmpty()) {
            int x = Integer.parseInt(Q.peek());
            int i = x / 100;
            int j = x % 100;
            Q.remove();
            if(!markC[i][j]){
                boolean c = shot(i, j);
                if (c) {

                    if (j < 10 && !markC[i][j + 1])
                        Q.add("" + (i * 100 + j + 1));
                    if (j > 1 && !markC[i][j - 1])
                        Q.add("" + (i * 100 + j - 1));
                    if (i < 10 && !markC[i+1][j])
                        Q.add("" + ((i+1) * 100 + j));
                    if (i > 1 && !markC[i-1][j])
                        Q.add("" + ((i-1) * 100 + j));

                }
                isPlayer = true;
                return;
            }else hitRandomHard();

        }
        Random rd = new Random();
        int i = rd.nextInt(10) + 1;
        int j = rd.nextInt(10) + 1;
        if (!markC[i][j]) {
            boolean c = shot(i, j);
            if (c) {
                if (j < 10 && !markC[i][j + 1])
                    Q.add("" + (i * 100 + j + 1));
                if (j > 1 && !markC[i][j - 1])
                    Q.add("" + (i * 100 + j - 1));
                if (i < 10 && !markC[i+1][j])
                    Q.add("" + ((i+1) * 100 + j));
                if (i > 1 && !markC[i-1][j])
                    Q.add("" + ((i-1) * 100 + j));
            }
            isPlayer = true;
            return;
        } else
            hitRandomHard();
    }

    private void addAction() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                computerMap.mapPiece[i][j].setActionCommand("" + (i * 100 + j));
                computerMap.mapPiece[i][j].addActionListener(this);
            }
        }
    }

    private void checkDeadOnComputerMap() {
        ship=new Ship[6];
        for (int tmp=1;tmp<=5;tmp++) {
            int x = Integer.parseInt(B.get(tmp));
            int leng = x / 10000;
            int j = (x - leng * 10000) / 100;
            int i = x % 100;
            boolean k=isNgangComputer[tmp];
            boolean c = true;

            for (int t = 0; t < leng; t++) {
                if(k){
                    if (!markP[i][j + t])
                        c = false;
                }else{
                    if (!markP[i+t][j])
                        c = false;
                }

            }
            if (!c)
                continue;
            for(int t=0;t<leng;t++){
                if(k){
                    computerMap.mapPiece[i][j+t].setIcon(null);
                }else{
                    computerMap.mapPiece[i+t][j].setIcon(null);
                }

            }
            ship[tmp]=new Ship(leng,50*leng,50,k);
            ship[tmp].setLocation(630+56*(j-1)+3,140+56*(i-1)+5);
            this.add(ship[tmp]);
        }
    }

    private boolean isPlayerWin() {
        if (playerHit == sumHit)
            return true;
        return false;
    }

    private boolean isComputerWin() {
        if (computerHit == sumHit)
            return true;
        return false;
    }

    private Image loadImage(String s, int w, int h) {
        BufferedImage i = null; // doc anh duoi dang Buffered Image
        try {
            i = ImageIO.read(MainMenu.class.getResource(s));
        } catch (Exception e) {
            System.out.println("Duong dan anh k hop le!");
        }

        Image dimg = i.getScaledInstance(w, h, Image.SCALE_SMOOTH); // thay doi kich thuoc anh
        return dimg;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPlayer)
            return;
        int x = Integer.parseInt(e.getActionCommand());
        int i = x / 100;
        int j = x % 100;
        if (markP[i][j]) {
            return;
        }
        markP[i][j] = true;
        if (computerMap.isShip[i][j]) {
            computerMap.mapPiece[i][j].setIcon(new ImageIcon(loadImage("/img/hit.png", 56, 56)));
            playerHit++;
            updateHistoryText(i,j,true);
            checkDeadOnComputerMap();
        }else{
            computerMap.mapPiece[i][j].setIcon(new ImageIcon(loadImage("/img/WHITE.png", 56, 56)));
            updateHistoryText(i,j,false);
        }
        isPlayer = false;
        if (isPlayerWin()) {
            gameOver(true);
            return;
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        if (isHard)
            hitRandomHard();
        else
            hitRandom();
        if (isComputerWin()) {
            gameOver(false);
        }
    }

    private void gameOver(boolean isPlayerWin){
        if (isPlayerWin) {
            Score=100+sumHit-computerHit-sumTurn;
            setHighScore();
            clip.stop();
            new EndScreen(1120,690,true,Score*100, isPlaySound);
        } else {
            clip.stop();
            new EndScreen(1120,690,false,0, isPlaySound);
        }
        frame.dispose();
        computerHit=0;
        playerHit=0;
        sumTurn=0;
    }

    private void setHighScore() {
//		JOptionPane.showMessageDialog(frame, "set ");
//		URL url = MainMenu.class.getResource("/HighScore/highscore.txt");
//		File file = new File(url.getPath());
        File file = new File("D:/BattleShip-OOP/high.txt");
        int[] A = new int[6];
        try {
            file.createNewFile();
            Scanner scan = new Scanner(file);
            for (int i = 0; i < 5; i++) {
                if (scan.hasNextInt()) {
                    A[i] = scan.nextInt();
                }
                else {
                    A[i] = 0;
                }
            }
            scan.close();
            A[5] = Score*100;
            Arrays.parallelSort(A);
        } catch (Exception e) {
            System.out.println("File not found");
        }
        String s = "" + A[5] + " " + A[4] + " " + A[3] + " " + A[2] + " " + A[1];
        try {
            FileWriter fw = new FileWriter("D:/BattleShip-OOP/high.txt");
            fw.write(s);
            fw.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void playSound(String link) {
        try {
            URL url = this.getClass().getResource(link);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
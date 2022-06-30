package BatteShip;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class HighScore extends JLabel  {
    private JFrame frame;
    private JLabel title;
    private JButton reset;
    public JButton back;

    private JLabel showScore; // show điểm cao
    private JLabel[] score;

    private Clip clip;
    private static boolean isPlaySound;

    public HighScore(int w,int h, boolean playSound){
        isPlaySound = playSound;
        this.setSize(w, h);

        this.setIcon(new ImageIcon(loadImage("/img/BGHighScore.jpg", w, h)));

        title = new JLabel();
        title.setIcon(new ImageIcon(loadImage("/img/title.png", 350, 130)));
        title.setBounds(385, 100, 350, 130);

        showScore = new JLabel();
        score = new JLabel[6];
        showScore.setLayout(new GridLayout(5, 1));
        for (int i = 1; i < 6; i++) {
            score[i] = new JLabel("");
            showScore.add(score[i]);
        }

        reset=new JButton("RESET");
        reset.setBounds(750, 440, 120, 65);
        reset.setFont(new Font("Snap ITC", Font.BOLD, 25));
        reset.setBackground(Color.decode("#A0522D"));
        reset.setForeground(Color.decode("#FFD700"));
        reset.setBorder(new LineBorder(Color.decode("#A0522D")));
        reset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reset.setFocusable(false);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetHighScore();
                showScore();
            }});

        back=new JButton("BACK");
        back.setBounds(750, 350, 120, 65);
        back.setFont(new Font("Snap ITC", Font.BOLD, 25));
        back.setBackground(Color.decode("#A0522D"));
        back.setForeground(Color.decode("#FFD700"));
        back.setBorder(new LineBorder(Color.decode("#A0522D")));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip.stop();
                new MainMenu(1120, 690, isPlaySound);
                frame.dispose();
            }});
        
        showScore.setBounds(470, 270, 200, 200);
        showScore();

        this.add(title);
        this.add(back);
        this.add(reset);
        this.add(showScore);

        frame = new JFrame("Battle Ship");
        frame.setIconImage(loadImage("/img/logo.png",90,90));
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(this);

        playSound("/sound/HGsound.wav");
        if (!isPlaySound) clip.stop();
    }

    private void resetHighScore() {
        String s = "0 0 0 0 0";
        try {
//			URL url = MainMenu.class.getResource("/HighScore/highscore.txt");
//			FileWriter fw = new FileWriter(url.getPath());
            FileWriter fw = new FileWriter("D:/BattleShip-OOP/highscore.txt");
            fw.write(s);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void showScore(){
        File file = new File("D:/BattleShip-OOP/highscore.txt");

        int[] A = new int[6];
        try {
            file.createNewFile();
            Scanner scan = new Scanner(file);
            for (int i = 1; i < 6; i++) {
                A[i] = scan.nextInt();
            }
            scan.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }
        for (int i = 1; i < 6; i++) {
            score[i].setText("" + i + ". " + A[i]);
            score[i].setForeground(Color.decode("#EB5A37"));
            score[i].setFont(new Font("Snap ITC", Font.BOLD, 30));
        }
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

    private void playSound(String link) {
		try {
			URL url = this.getClass().getResource(link);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
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
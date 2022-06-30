package BatteShip;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

public class MainMenu implements ActionListener  {
    private JFrame frame;
    private JLabel menu1;
    private JLabel showRules;
    private ImageIcon menuBackground;
    private JTextArea content;

    private JButton play;
    private JButton gameMode;
    private JButton gameRule;
    private JButton highScore;
    private JButton sound;
    private JButton exit;

    private Clip clip;
    private static boolean isPlaySound ;

    public MainMenu(int w, int h, boolean playSound){
        frame = new JFrame("Battle Ship");
        isPlaySound = playSound;

        frame.setIconImage(loadImage("/img/logo.png", 90, 90));
        frame.setSize(w, h);

        menu1 = new JLabel();
        menu1.setSize(w, h);
        menuBackground=new ImageIcon(loadImage("/img/menu1_background.jpg", w, h));
        menu1.setIcon(menuBackground);

        play = new JButton("PLAY");
        play.setBounds(w / 2 - 130,  h / 5 + 30, 260, 50);
        play.setFont(new Font("Snap ITC", Font.BOLD, 30));
        play.setBackground(Color.GREEN);
        play.setBorder(new LineBorder(Color.GREEN));
        play.setFocusable(false);
        play.setCursor(new Cursor(Cursor.HAND_CURSOR));
        play.setActionCommand("Play");
        play.addActionListener(this);

        gameMode = new JButton("EASY");
        gameMode.setBounds(w / 2 - 130,  h / 5 + 90, 260, 50);
        gameMode.setFont(new Font("Snap ITC", Font.BOLD, 30));
        gameMode.setBackground(new Color(255, 102, 102));
        gameMode.setBorder(new LineBorder(new Color(255, 102, 102)));
        gameMode.setFocusable(false);
        gameMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameMode.setActionCommand("easy");
        gameMode.addActionListener(this);

        gameRule = new JButton();
        gameRule.setSize(60, 60);
        gameRule.setIcon(new ImageIcon(loadImage("/img/ruleGame.png", 80, 80)));
		gameRule.setBounds(5, 15, 60, 60);
		gameRule.setBackground(Color.BLACK);
		gameRule.setBorder(new LineBorder(Color.BLACK));
        gameRule.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gameRule.setActionCommand("rules on");
		gameRule.addActionListener(this);

        showRules = new JLabel();
		showRules.setIcon(new ImageIcon(loadImage("/img/frameRules1.png", 600, 800)));
		showRules.setBounds(270, -130, 600, 800);
		showRules.setVisible(false);

        String fileContent = "";

        try {
			File file = new File("D:/nhap.txt");
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				fileContent = fileContent.concat(scan.nextLine() + "\n");
			}
			scan.close();
		} catch (Exception e) {
			System.out.println("File not found");
		}

        content = new JTextArea(fileContent);
		content.setFont(new Font("Arial", Font.BOLD, 16));
		content.setForeground(Color.YELLOW);
		content.setBackground(Color.BLACK);
		content.setBounds(58, 190, 490, 395);

        showRules.add(content);

        highScore = new JButton("HIGHSCORE");
        highScore.setBounds(w / 2 - 130,  h / 5 + 150, 260, 50);
        highScore.setFont(new Font("Snap ITC", Font.BOLD, 30));
        highScore.setBackground(Color.GRAY);
        highScore.setBorder(new LineBorder(Color.GRAY));
        highScore.setFocusable(false);
        highScore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        highScore.setActionCommand("Highscore");
        highScore.addActionListener(this);

        sound = new JButton();
        sound.setBounds(w / 2 - 130,  h / 5 + 210, 260, 50);
        sound.setFont(new Font("Snap ITC", Font.BOLD, 30));
        sound.setBackground(Color.ORANGE);
        sound.setBorder(new LineBorder(Color.ORANGE));
        sound.setFocusable(false);
        sound.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPlaySound) {
            sound.setText("SOUND OFF");
            sound.setActionCommand("sound on");
        } else {
            sound.setText("SOUND ON");
            sound.setActionCommand("sound off");
        }
        sound.addActionListener(this);

        exit = new JButton("EXIT");
        exit.setBounds(w / 2 - 130,  h / 5 + 270, 260, 50);
        exit.setFont(new Font("Snap ITC", Font.BOLD, 30));
        exit.setBackground(Color.RED);
        exit.setBorder(new LineBorder(Color.RED));
        exit.setFocusable(false);
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit (0);

            }});

        menu1.add(showRules);
        menu1.add(play);
        menu1.add(gameMode);
        menu1.add(highScore);
        menu1.add(sound);
        menu1.add(exit);
        menu1.add(gameRule);

        frame.add(menu1);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        playSound("/sound/sound.wav");
        if (!isPlaySound) clip.stop();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("sound on".equals(e.getActionCommand())) {
            sound.setText("SOUND ON");
            sound.setActionCommand("sound off");
            isPlaySound = false;
            clip.stop();
        }

        if ("sound off".equals(e.getActionCommand())) {
            sound.setText("SOUND OFF");
            sound.setActionCommand("sound on");
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaySound = true;
        }

        if ("easy".equals(e.getActionCommand())) {
            gameMode.setText("HARD");
            gameMode.setActionCommand("hard");
        }

        if ("hard".equals(e.getActionCommand())) {
            gameMode.setText("EASY");
            gameMode.setActionCommand("easy");
        }

        if ("Play".equals(e.getActionCommand())) {
            clip.stop();
            new Creator(1120, 820, gameMode.getActionCommand(), isPlaySound);
            frame.dispose();
        }


        if("Highscore".equals(e.getActionCommand())){
            clip.stop();
            new HighScore(1120,690, isPlaySound);
            menu1.setVisible(false);
			frame.remove(menu1);
			frame.setVisible(false);
        }

        if ("rules on".equals(e.getActionCommand())) {
			showRules.setVisible(true);
			gameRule.setActionCommand("rules off");
		}

        if ("rules off".equals(e.getActionCommand())) {
			showRules.setVisible(false);
			gameRule.setActionCommand("rules on");
		}

    }

}
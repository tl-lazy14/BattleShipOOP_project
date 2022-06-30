package BatteShip;

import javax.imageio.ImageIO;
import java.net.URL;
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
import java.io.IOException;

public class EndScreen extends JLabel {
    private JFrame frame;
    private JButton back;
    private JButton highscore;
    private JLabel Score;
    private JLabel Text;

    private Clip clip;
    private static boolean isPlaySound;

    public EndScreen(int w,int h,boolean isPlayWin,int score, boolean playSound){
        super();
        isPlaySound = playSound;
        this.setSize(w, h);
        if(isPlayWin){
            this.setIcon(new ImageIcon(loadImage("/img/win_bg.png", w, h)));
            Text=new JLabel();
            Text.setText("SCORE");
            Text.setForeground(Color.RED);
            Text.setFont(new Font("Snap ITC", Font.BOLD, 50));
            Text.setBounds(w/2-100,h/4-50,400,100);

            this.add(Text);


            Score=new JLabel();
            Score.setText(String.valueOf(score));
            Score.setFont(new Font("Snap ITC", Font.BOLD, 40));
            Score.setBounds(w/2-70,h/3,200,100);

            this.add(Score);

            playSound("/sound/winSound.wav");
            if (!isPlaySound) clip.stop();
        }
        else{
            this.setIcon(new ImageIcon(loadImage("/img/youLose.jpg", w, h)));

            playSound("/sound/loseSound.wav");
            if (!isPlaySound) clip.stop();
        }

        highscore=new JButton("Highscore");
        highscore.setBounds(w*3 / 5+50,  4*h / 5, 260, 50);
        highscore.setFont(new Font("Snap ITC", Font.BOLD, 30));
        highscore.setBackground(Color.GREEN);
        highscore.setBorder(new LineBorder(Color.GREEN));
        highscore.setFocusable(false);
        highscore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        highscore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip.stop();
                new HighScore(1120,690, isPlaySound);
                frame.dispose();
            }});
        this.add(highscore);

        back=new JButton("Play Again");
        back.setBounds(w / 5 - 80,  4*h / 5, 260, 50);
        back.setFont(new Font("Snap ITC", Font.BOLD, 30));
        back.setBackground(Color.LIGHT_GRAY);
        back.setBorder(new LineBorder(Color.LIGHT_GRAY));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip.stop();
                new MainMenu(1120, 690, isPlaySound);
                frame.dispose();
            }});
        this.add(back);

        frame = new JFrame("Battle Ship");
        frame.setIconImage(loadImage("/img/logo.png",90,90));
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(this);

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
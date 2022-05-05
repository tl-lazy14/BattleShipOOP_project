package BatteShip;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class EndScreen extends JLabel {
    private JFrame frame;
    private JButton back;
    private JButton highscore;
    private JLabel Score;
    private JLabel Text;

    public EndScreen(int w,int h,boolean isPlayWin,int score){
        super();
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
        }
        else{
            this.setIcon(new ImageIcon(loadImage("/img/lost-game.jpg", w, h)));
        }

        highscore=new JButton("SCORE");
        highscore.setBounds(w*3 / 5,  4*h / 5, 260, 50);
        highscore.setFont(new Font("Snap ITC", Font.BOLD, 30));
        highscore.setBackground(Color.GREEN);
        highscore.setBorder(new LineBorder(Color.GREEN));
        highscore.setFocusable(false);
        highscore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HighScore(1120,690);
                frame.dispose();
            }});
        this.add(highscore);

        back=new JButton("Play Again");
        back.setBounds(w / 5-50,  4*h / 5, 260, 50);
        back.setFont(new Font("Snap ITC", Font.BOLD, 30));
        back.setBackground(Color.GRAY);
        back.setBorder(new LineBorder(Color.GRAY));
        back.setFocusable(false);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu(1120, 690, false);
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
}

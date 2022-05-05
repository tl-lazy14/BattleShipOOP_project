package BatteShip;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class HighScore extends JLabel  {
    public JFrame frame;
    private JButton reset;
    public JButton back;

    private JLabel showScore; // show điểm cao
    private JLabel[] score;

    public HighScore(int w,int h){
        super();
        this.setSize(w, h);

        this.setIcon(new ImageIcon(loadImage("/img/highscore_bg.png", w, h)));

        showScore = new JLabel();
        score = new JLabel[6];
        showScore.setLayout(new GridLayout(5, 1));
        for (int i = 1; i < 6; i++) {
            score[i] = new JLabel("");
            showScore.add(score[i]);
        }
        showScore();
        showScore.setBounds(w/2-100, 180, 200, 250);
        this.add(showScore);


        reset=new JButton("RESET");
        reset.setBounds(w / 5-50,  4*h / 5, 260, 50);
        reset.setFont(new Font("Snap ITC", Font.BOLD, 30));
        reset.setBackground(Color.GRAY);
        reset.setBorder(new LineBorder(Color.GRAY));
        reset.setFocusable(false);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetHighScore();
                showScore();
            }});
        this.add(reset);


        back=new JButton("BACK");
        back.setBounds(3*w / 5,  4*h / 5, 260, 50);
        back.setFont(new Font("Snap ITC", Font.BOLD, 30));
        back.setBackground(Color.RED);
        back.setBorder(new LineBorder(Color.RED));
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

    private void resetHighScore() {
        String s = "0 0 0 0 0";
        try {
//			URL url = MainMenu.class.getResource("/HighScore/highscore.txt");
//			FileWriter fw = new FileWriter(url.getPath());
            FileWriter fw = new FileWriter("D:/high.txt");
            fw.write(s);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void showScore(){
        File file = new File("D:/high.txt");

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
            score[i].setFont(new Font("Snap ITC", Font.BOLD, 25));
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

}

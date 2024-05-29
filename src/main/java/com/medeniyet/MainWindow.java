package com.medeniyet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class MainWindow extends JFrame implements ActionListener, Serializable {

    private JButton logButton;
    private JTextField nameBox;

    MainWindow(){

        super();
        this.setTitle("Medeniyet Sudoku");
        ImageIcon sudokuIcon = new ImageIcon("images/favicon.png");
        this.setIconImage(sudokuIcon.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800,500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));

        JLabel title;

        try {

            BufferedImage originalImage = ImageIO.read(new File("images/logo.png"));
            Image scaledImage = originalImage.getScaledInstance(250, 180, Image.SCALE_SMOOTH);
            ImageIcon logo = new ImageIcon(scaledImage);
            title = new JLabel(logo);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

        }catch (IOException e){

            title = new JLabel("Sudoku");
            title.setFont(new Font("Arial", Font.BOLD, 42));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            e.printStackTrace();

        }

        //Kullanıcı adı
        JLabel user = new JLabel("Kullanıcı Adı");
        user.setFont(new Font("Arial", Font.PLAIN, 25));
        user.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Text field for the name of the user
        JTextField nameBox = new JTextField();
        nameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameBox.setMaximumSize(new Dimension(300,60)); 
        Border padding = new EmptyBorder(10, 10, 10, 10);
        Border border = BorderFactory.createCompoundBorder(nameBox.getBorder(),padding);
        nameBox.setBorder(border);
        nameBox.setFont(new Font("Arial",Font.PLAIN,17));
        nameBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.nameBox = nameBox;

        //Giriş butonu
        JButton logButton = new JButton("Giriş");
        logButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Border padding2 = new EmptyBorder(10, 10, 10, 10);
        Border border2 = BorderFactory.createCompoundBorder(logButton.getBorder(),padding2);
        logButton.setBorder(border2);
        logButton.setFont(new Font("Arial",Font.PLAIN,18));
        logButton.addActionListener(this);
        logButton.setFocusable(false);
        logButton.setBackground(new Color(17,33,59));
        logButton.setForeground(Color.WHITE);
        this.logButton = logButton;


        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(5));
        this.add(user);
        this.add(Box.createVerticalStrut(18));
        this.add(nameBox);
        this.add(Box.createVerticalStrut(20)); 
        this.add(logButton);
        this.add(Box.createVerticalGlue());

        //Enter tuşuyla giris yapmayı sağlar
        this.getRootPane().setDefaultButton(logButton);


    }
    
    public void display(){

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == this.logButton){

            String name = this.nameBox.getText();

            if(!name.isEmpty()){

                MenuWindow pencere2 = new MenuWindow(name);
                pencere2.display();
                this.dispose();

            }else{

                CustomDialog dialog = new CustomDialog(this,"Geçerli bir isim giriniz","Tamam",new MyCallback(){

                    @Override
                    public void run(){

                    }


                });

                dialog.setVisible(true);
            }

        }


    }

}

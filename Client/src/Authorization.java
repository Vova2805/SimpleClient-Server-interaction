import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Authorization {

    private static JFrame frame;
    private JTextField textField;
    private JPasswordField passwordField;

    public static void main(String []args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Authorization window = new Authorization();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Authorization()
    {
        initialize();
    }
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(0, 153, 255));
        frame.setBounds(100, 100, 283, 202);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);

        JLabel lblNewLabel = new JLabel("Welcome");
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 95, SpringLayout.WEST, frame.getContentPane());
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 17));
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        springLayout.putConstraint(SpringLayout.NORTH, textField, 6, SpringLayout.SOUTH, lblNewLabel);
        springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, lblNewLabel);
        springLayout.putConstraint(SpringLayout.EAST, textField, -38, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        springLayout.putConstraint(SpringLayout.NORTH, passwordField, 18, SpringLayout.SOUTH, textField);
        springLayout.putConstraint(SpringLayout.SOUTH, passwordField, 38, SpringLayout.SOUTH, textField);
        springLayout.putConstraint(SpringLayout.EAST, passwordField, -38, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(passwordField);

        JLabel lblNewLabel_1 = new JLabel("Login");
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, -2, SpringLayout.NORTH, textField);
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 10, SpringLayout.WEST, frame.getContentPane());
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Password");
        springLayout.putConstraint(SpringLayout.WEST, passwordField, 18, SpringLayout.EAST, lblNewLabel_2);
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_2, 18, SpringLayout.SOUTH, lblNewLabel_1);
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_2, 0, SpringLayout.WEST, lblNewLabel_1);
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        frame.getContentPane().add(lblNewLabel_2);

        JButton btnSubmit = new JButton("Submit");
        springLayout.putConstraint(SpringLayout.NORTH, btnSubmit, -50, SpringLayout.SOUTH, frame.getContentPane());
        btnSubmit.addActionListener(new ActionListener() {

            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent arg0) {
                if(!textField.getText().equals("sa")||!passwordField.getText().toString().equals("samsungace3"))
                {
                    JOptionPane.showMessageDialog(frame, "Invalid data! Check and try again!");
                }
                else
                {
                    frame.dispose();
                    Editor Window = new Editor();
                }
            }
        });
        springLayout.putConstraint(SpringLayout.SOUTH, btnSubmit, -23, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnSubmit, 0, SpringLayout.WEST, lblNewLabel);
        springLayout.putConstraint(SpringLayout.EAST, btnSubmit, -89, SpringLayout.EAST, frame.getContentPane());
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frame.getContentPane().add(btnSubmit);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author chicco
 */
public class StartInterface extends JFrame {

    public static void main(String[] args) throws CloneNotSupportedException {
        new StartInterface();
    }

    private final int delay = 1000;
    private Timer timer;

    public StartInterface() throws CloneNotSupportedException {
        this.setSize(540, 240);
        this.setResizable(false);
        this.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.add(new JLabel(new ImageIcon(getClass().getResource("/start.png"))));
        timer = new Timer(delay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                try {
                    new UserInterface();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(StartInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                timer.stop();
            }
        });
        timer.start();
        this.setVisible(true);
    }

}

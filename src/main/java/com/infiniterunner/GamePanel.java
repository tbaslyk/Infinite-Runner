package com.infiniterunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * @author Tomasz Baslyk
 */
public class GamePanel extends javax.swing.JPanel {

    private Player player;
    private Obstacle obstacle;
    private boolean animateSwitch;
    private boolean running;

    public GamePanel() {

        animateSwitch = false;
        running = true;

        obstacle = new Obstacle();
        player = new Player();
        initpanel();

        animatePlayer();
        moveObstacle();
        checkCollision();

    }

    public void initpanel() {

        setSize(800, 600);

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawBackground(g);
        drawPlayer(g);
        drawObstacle(g);

        Toolkit.getDefaultToolkit().sync();

    }

    public void drawPlayer(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        if (player.getVisible()) {
            if (!animateSwitch) {
                g2.drawImage(player.getImage1(), player.getX(), player.getY(), this);
            } else {
                g2.drawImage(player.getImage2(), player.getX(), player.getY(), this);
            }
        }
    }

    public void drawBackground(Graphics g) {

        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/background.png"));
        Image background = i.getImage();

        g.drawImage(background, 0, 0, this);

    }

    public void drawObstacle(Graphics g) {

        g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getW(), obstacle.getH());

    }

    public void moveObstacle() {

        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (running) {
                    obstacle.moveHorizontal(-20);
                    repaint();

                    if (obstacle.getX() <= -20) {
                        obstacle.moveHorizontal(800);
                    }
                }
                else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    public void drawJump() {
        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                boolean jumpComplete = player.jump();
                repaint();

                if (jumpComplete) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    public void animatePlayer() {

        int timerDelay = 100;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!animateSwitch) {
                    animateSwitch = true;
                    repaint();
                } else {
                    animateSwitch = false;
                    repaint();
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    public void checkCollision() {
        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Rectangle playerBounds = player.getBounds();
                Rectangle obstacleBounds = obstacle.getBounds();

                if (obstacleBounds.intersects(playerBounds)) {
                    player.setVisible(false);
                    running = false;
                }

            }
        });
        t.setRepeats(true);
        t.start();

    }

}

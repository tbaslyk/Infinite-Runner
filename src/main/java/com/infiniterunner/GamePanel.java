package com.infiniterunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * @author Tomasz Baslyk
 */
public class GamePanel extends javax.swing.JPanel {

    private Player player;
    private Obstacle obstacle;
    private boolean animationSwap;
    private boolean jumpInProgress;
    private boolean hitboxEnabled;
    private boolean running;
    private boolean pauseEnabled;
    private int count;
    private JLabel lblCounter;

    public GamePanel() {

        jumpInProgress = false;
        animationSwap = false;
        running = true;
        hitboxEnabled = false;
        pauseEnabled = false;
        count = 0;

        lblCounter = new JLabel("0");
        lblCounter.setFont(new java.awt.Font("Dialog", 0, 30));
        lblCounter.setLocation(100, 100);

        obstacle = new Obstacle();
        player = new Player();

        initpanel();
        add(lblCounter);

        animatePlayer();
        moveObstacle();
        checkCollision();
        counter();

    }

    public void initpanel() {

        setSize(800, 600);

    }

    public void counter() {
        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (running && !pauseEnabled) {
                    count++;
                    String stringCount = String.valueOf(count);
                    lblCounter.setText(stringCount);
                }
            }
        });
        t.setRepeats(true);
        t.start();

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawBackground(g);
        drawPlayer(g);
        drawObstacle(g);
        drawCollision(g);

        Toolkit.getDefaultToolkit().sync();

    }

    public void drawPlayer(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        if (player.getVisible()) {
            if (!animationSwap) {
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

                if (running && !pauseEnabled) {
                    obstacle.moveHorizontal(-20);
                    repaint();

                    if (obstacle.getX() <= -20) {
                        obstacle.moveHorizontal(800);
                    }
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    public void drawJump() {
        int timerDelay = 50;

        if (!jumpInProgress) {

            Timer t = new Timer(timerDelay, new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    jumpInProgress = true;
                    boolean jumpComplete = player.jump();
                    repaint();

                    if (jumpComplete) {
                        ((Timer) e.getSource()).stop();
                        jumpInProgress = false;
                    }

                }
            });
            t.setRepeats(true);
            t.start();
        }
    }

    public void drawCollision(Graphics g) {

        if (hitboxEnabled) {
            Rectangle playerBounds = player.getBounds();
            Rectangle obstacleBounds = obstacle.getBounds();

            int x = (int) playerBounds.getX();
            int y = (int) playerBounds.getY();
            int w = (int) playerBounds.getWidth();
            int h = (int) playerBounds.getHeight();

            g.drawRect(x, y, w, h);
        }
    }

    public void animatePlayer() {

        int timerDelay = 100;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!pauseEnabled) {
                    if (!animationSwap) {
                        animationSwap = true;
                        repaint();
                    } else {
                        animationSwap = false;
                        repaint();
                    }
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
                    lost();
                }

            }
        });
        t.setRepeats(true);
        t.start();

    }

    public void lost() {

        player.setVisible(false);
        running = false;

    }

    public void restart() {

        player.reset();
        obstacle.reset();
        count = 0;

        running = true;

    }

    public void hitboxToggle(boolean hitboxEnabled) {
        this.hitboxEnabled = hitboxEnabled;
    }

    public void pauseToggle(boolean pauseEnabled) {
        this.pauseEnabled = pauseEnabled;
    }
    
    public boolean pauseStatus() {
        return pauseEnabled;
    }
    
    public boolean hitboxStatus() {
        return hitboxEnabled;
    }
    
    public boolean runningStatus() {
        return running;
    }

}

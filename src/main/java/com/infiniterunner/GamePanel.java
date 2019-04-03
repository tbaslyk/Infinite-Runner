package com.infiniterunner;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * @author Tomasz Baslyk
 */
public class GamePanel extends javax.swing.JPanel {

    private Player player;
    private Obstacle knight;
    private Obstacle cloud;
    private boolean animationSwap;
    private boolean jumpInProgress;
    private boolean hitboxEnabled;
    private boolean running;
    private boolean pauseEnabled;
    private boolean muteEnabled;
    private int count;
    private JLabel lblCounter;
    private JLabel lblLost;

    private File pixelFontFile;
    private Font pixelFont;

    public GamePanel() {

        jumpInProgress = false;
        animationSwap = false;
        running = true;
        hitboxEnabled = false;
        pauseEnabled = false;
        muteEnabled = false;
        count = 0;

        FxPlayer startSound = new FxPlayer("/com/infiniterunner/beginsound.wav");
        startSound.play();

        pixelFontFile = new File("./src/main/resources/com/infiniterunner/PressStart2P.ttf");

        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, pixelFontFile);
        } catch (FontFormatException | IOException ex) {
            System.out.println("Internal error: " + ex.getMessage());
        }

        lblCounter = new JLabel("0");
        lblCounter.setFont(new java.awt.Font("Press Start 2P", 0, 28));
        lblCounter.setForeground(new java.awt.Color(255, 255, 255));
        lblCounter.setBounds(360, 50, 200, 80);

        lblLost = new JLabel("You lost");
        lblLost.setFont(new java.awt.Font("Press Start 2P", 0, 28));
        lblLost.setForeground(new java.awt.Color(255, 255, 255));
        lblLost.setBounds(270, 175, 300, 80);

        lblLost.setVisible(false);

        knight = new Obstacle(2000, 305, "knight1", "knight2");
        createCloud();
        player = new Player();

        initpanel();
        add(lblCounter);
        add(lblLost);

        animatePlayer();
        moveKnight();
        moveCloud();
        checkCollision();
        counter();

    }

    public void initpanel() {

        setSize(800, 600);
        setLayout(null);

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
        drawCloud(g);
        drawPlayer(g);
        drawKnight(g);
        drawCollision(g);

        Toolkit.getDefaultToolkit().sync();

    }
    
    public void createCloud() {
        
        cloud = new Obstacle(800, 50, "cloud" + Randomizer.randomizeNumber(3) + "");
        
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

        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/background3.png"));
        Image background = i.getImage();

        g.drawImage(background, 0, 0, this);

    }

    public void drawCloud(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(cloud.getImage1(), cloud.getX(), cloud.getY(), this);
    }

    public void moveCloud() {

        int timerDelay = 1000;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (!pauseEnabled) {
                    cloud.moveHorizontal(-20);

                    if (cloud.getX() <= -800) {
                        createCloud();
                        cloud.setX(800);
                    }
                }

            }
        });
        t.setRepeats(true);
        t.start();

    }

    public void drawKnight(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        if (!animationSwap) {
            g2.drawImage(knight.getImage1(), knight.getX(), knight.getY(), this);
        } else {
            g2.drawImage(knight.getImage2(), knight.getX(), knight.getY(), this);
        }
    }

    public void moveKnight() {

        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (running && !pauseEnabled) {
                    knight.moveHorizontal(-20);
                    repaint();

                    if (knight.getX() <= -110) {
                        knight.moveHorizontal(800);
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
                    boolean jumpComplete = player.jump(pauseEnabled);
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
            Rectangle knightBounds = knight.getBounds();

            int xP = (int) playerBounds.getX();
            int yP = (int) playerBounds.getY();
            int wP = (int) playerBounds.getWidth();
            int hP = (int) playerBounds.getHeight();

            int xO = (int) knightBounds.getX();
            int yO = (int) knightBounds.getY();
            int wO = (int) knightBounds.getWidth();
            int hO = (int) knightBounds.getHeight();

            g.drawRect(xP, yP, wP, hP);
            g.drawRect(xO, yO, wO, hO);
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
                Rectangle obstacleBounds = knight.getBounds();

                if (obstacleBounds.intersects(playerBounds)) {
                    if (running) {
                        lost();
                    }
                }

            }
        });
        t.setRepeats(true);
        t.start();

    }

    public void lost() {

        player.setVisible(false);
        running = false;

        lblLost.setVisible(true);

        if (!muteEnabled) {
            FxPlayer deathSound = new FxPlayer("/com/infiniterunner/deathsound.wav");
            deathSound.play();
        }
    }

    public void restart() {

        player.reset();
        knight.setX(2000);
        cloud.setX(500);
        count = 0;

        running = true;
        createCloud();

        lblLost.setVisible(false);

        if (!muteEnabled) {
            FxPlayer restartSound = new FxPlayer("/com/infiniterunner/beginsound.wav");
            restartSound.play();
        }

    }

    public void hitboxToggle(boolean hitboxEnabled) {
        this.hitboxEnabled = hitboxEnabled;
    }

    public void pauseToggle(boolean pauseEnabled) {
        this.pauseEnabled = pauseEnabled;
    }

    public void muteToggle(boolean muteEnabled) {
        this.muteEnabled = muteEnabled;
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

    public boolean muteStatus() {
        return muteEnabled;
    }

}

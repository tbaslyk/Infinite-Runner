package com.infiniterunner;

import java.awt.BorderLayout;
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
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * @author Tomasz Baslyk
 */
public class GamePanel extends javax.swing.JPanel {

    private Player player;
    private Obstacle enemy;
    private Obstacle cloud;
    private boolean animationSwap;
    private boolean jumpInitiated;
    private boolean hitboxEnabled;
    private boolean running;
    private boolean pauseEnabled;
    private boolean muteEnabled;
    private int count;
    private int tutorialTime;
    private final JLabel lblCounter;

    private File pixelFontFile;
    private Font pixelFont;

    public GamePanel() {

        animationSwap = false;
        running = true;
        hitboxEnabled = false;
        pauseEnabled = false;
        muteEnabled = false;
        jumpInitiated = false;
        count = 0;
        tutorialTime = 0;

        FxPlayer startSound = new FxPlayer("/com/infiniterunner/beginsound.wav");
        startSound.play();

        lblCounter = new JLabel("0");
        lblCounter.setFont(new java.awt.Font("Press Start 2P", 0, 28));
        lblCounter.setForeground(new java.awt.Color(255, 255, 255));
        lblCounter.setBounds(300, 50, 200, 80);

        enemy = new Obstacle(2000, 300, "dinos1", "dinos2");
        player = new Player();

        initPanel();
        add(lblCounter, BorderLayout.PAGE_END);

        lblCounter.setHorizontalAlignment(SwingConstants.CENTER);

        initLogic();
        initFonts();

    }

    public final void initPanel() {

        setSize(800, 600);
        setLayout(null);
    }

    public final void initLogic() {
        animatePlayer();
        moveEnemy();
        moveCloud();
        checkCollision();
        counter();
        createCloud();
    }

    public final void initFonts() {
        pixelFontFile = new File("./src/main/resources/com/infiniterunner/PressStart2P.ttf");

        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, pixelFontFile);
        } catch (FontFormatException | IOException ex) {
            System.out.println("Internal error: " + ex.getMessage());
        }
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

        if (running) {
            drawBackground(g);
            drawInstructions(g);
            drawCloud(g);
            drawPlayer(g);
            drawEnemy(g);
            drawCollision(g);
        } else {
            drawBackground(g);
            drawEnemy(g);
            drawCloud(g);
            drawCollision(g);
            drawDeathScreen(g);
        }

        Toolkit.getDefaultToolkit().sync();

    }

    public void createCloud() {

        cloud = new Obstacle(800, 50, "cloud" + Randomizer.randomizeNumber(3) + "");

    }

    public void drawInstructions(Graphics g) {

        if (tutorialTime < 150) {

            ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/instructions.png"));
            Image instructions = i.getImage();

            g.drawImage(instructions, 0, 0, this);

            tutorialTime++;

        }

    }

    public void drawDeathScreen(Graphics g) {

        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/deathScreen.png"));
        Image deathScreen = i.getImage();

        g.drawImage(deathScreen, 0, 0, this);

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

    public void drawCloud(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(cloud.getImage1(), cloud.getX(), cloud.getY(), this);
    }

    public void moveCloud() {

        int timerDelay = 1000;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (!pauseEnabled && running) {
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

    public void drawEnemy(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        if (!animationSwap) {
            g2.drawImage(enemy.getImage1(), enemy.getX(), enemy.getY(), this);
        } else {
            g2.drawImage(enemy.getImage2(), enemy.getX(), enemy.getY(), this);
        }
    }

    public void moveEnemy() {

        int timerDelay = 50;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (running && !pauseEnabled) {
                    enemy.moveHorizontal(-20);
                    repaint();

                    if (enemy.getX() <= -110) {
                        enemy.moveHorizontal(860);
                    }
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    public void drawJump() {
        
        int timerDelay = 50;

        if (!jumpInitiated) {
            Timer t = new Timer(timerDelay, new ActionListener() {

                boolean jumpComplete = false;

                public void actionPerformed(ActionEvent e) {

                    if (!jumpComplete && running) {

                        jumpComplete = player.jump(pauseEnabled);
                        jumpInitiated = true;
                        repaint();

                        if (jumpComplete) {
                            ((Timer) e.getSource()).stop();
                            jumpComplete = false;
                            jumpInitiated = false;
                        }
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
            Rectangle enemyBounds = enemy.getBounds();

            int xP = (int) playerBounds.getX();
            int yP = (int) playerBounds.getY();
            int wP = (int) playerBounds.getWidth();
            int hP = (int) playerBounds.getHeight();

            int xO = (int) enemyBounds.getX();
            int yO = (int) enemyBounds.getY();
            int wO = (int) enemyBounds.getWidth();
            int hO = (int) enemyBounds.getHeight();

            g.drawRect(xP, yP, wP, hP);
            g.drawRect(xO, yO, wO, hO);
        }
    }

    public void animatePlayer() {

        int timerDelay = 100;

        Timer t = new Timer(timerDelay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!pauseEnabled && running) {
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
                Rectangle obstacleBounds = enemy.getBounds();

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

        tutorialTime = 150;
        player.setVisible(false);
        running = false;

        if (!muteEnabled) {
            FxPlayer deathSound = new FxPlayer("/com/infiniterunner/deathsound.wav");
            deathSound.play();
        }
    }

    public void restart() {

        pauseEnabled = false;

        player.setVisible(true);
        enemy.setX(2000);
        cloud.setX(500);
        count = 0;

        running = true;
        createCloud();

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

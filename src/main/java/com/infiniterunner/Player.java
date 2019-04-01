package com.infiniterunner;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * @author Tomasz Baslyk
 */
public class Player {

    int x;
    int y;
    int w;
    int h;

    Image running1;
    Image running2;

    private boolean jumpMaxReached;

    public Player() {

        jumpMaxReached = false;

        x = 200;
        y = 300;

        loadImages();

    }

    private void loadImages() {

        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/dino1.png"));
        ImageIcon ii = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/dino2.png"));

        running1 = i.getImage();
        running2 = ii.getImage();

        h = running1.getHeight(null);
        w = running1.getWidth(null);

    }

    //Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Image getImage1() {
        return running1;
    }

    public Image getImage2() {
        return running2;
    }

    //Movements
    public void moveHorizontal(int dx) {

        x += dx;
    }

    public void moveVertical(int dy) {

        y += dy;
    }

    public boolean jump() {

        boolean jumpComplete = false;

        if (y > 175 && !jumpMaxReached) {
            moveVertical(-20);
        } else {
            jumpMaxReached = true;
        }

        if (jumpMaxReached) {

            moveVertical(20);

            if (y >= 300) {
                jumpComplete = true;
                jumpMaxReached = false;
            }
        }
        return jumpComplete;
    }

}

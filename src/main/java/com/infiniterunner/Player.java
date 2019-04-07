package com.infiniterunner;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * @author Tomasz Baslyk
 */
public class Player {

    private int x;
    private int y;
    private int w;
    private int h;
    
    Image running1;
    Image running2;

    private boolean jumpMaxReached;
    private boolean visible;

    public Player() {

        visible = true;
        jumpMaxReached = false;

        x = 150;
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public boolean getVisible() {
        return visible;
    }

    // Setters
    public void moveHorizontal(int dx) {

        x += dx;
    }

    public void moveVertical(int dy) {

        y += dy;
    }

    public boolean jump(int jumpHeight) {
        
        boolean jumpComplete = false;

            if (y > jumpHeight && !jumpMaxReached) {
                moveVertical(-15);
            } else {
                jumpMaxReached = true;
            }

            if (jumpMaxReached) {
                moveVertical(15);
                if (y >= 300) {
                    jumpComplete = true;
                    jumpMaxReached = false;
                }
            }

        return jumpComplete;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void setY(int y) {
        this.y = y;
    }

}

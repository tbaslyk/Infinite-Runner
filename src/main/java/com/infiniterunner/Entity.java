package com.infiniterunner;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * @author Tomasz Baslyk
 */
public class Entity {

    private int x;
    private int y;
    private int w;
    private int h;
    
    private Image image1;
    private Image image2;

    // First constructor for two images (animation)
    public Entity(int x, int y, String imageName1, String imageName2) {

        this.x = x;
        this.y = y;

        loadImages(imageName1, imageName2);
    }
    
    // Second constructor for one image (static)
    public Entity(int x, int y, String imageName) {
        
        this.x = x;
        this.y = y;
        
        loadImage(imageName);
    }

    // To load two images
    private void loadImages(String imageName1, String imageName2) {

        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/" + imageName1 + ".png"));
        ImageIcon ii = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/" + imageName2 + ".png"));

        image1 = i.getImage();
        image2 = ii.getImage();

        h = image1.getHeight(null);
        w = image1.getWidth(null);

    }
    
    // To load one image
    private void loadImage(String imageName) {
        
        ImageIcon i = new javax.swing.ImageIcon(getClass().getResource("/com/infiniterunner/" + imageName + ".png"));
        
        image1 = i.getImage();
        
        h = image1.getHeight(null);
        w = image1.getWidth(null);
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
        return image1;
    }

    public Image getImage2() {
        return image2;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    // Movements
    public void moveHorizontal(int dx) {

        x += dx;
    }

    public void setX(int x) {
        this.x = x;
    }
}

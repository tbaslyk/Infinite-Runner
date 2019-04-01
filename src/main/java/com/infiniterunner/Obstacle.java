package com.infiniterunner;

/**
 * @author Tomasz Baslyk
 */
public class Obstacle {

    private int x;
    private int y;
    private int w;
    private int h;

    public Obstacle() {

        x = 800;
        y = 300;
        w = 20;
        h = 85;
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

    // Movements
    public void moveHorizontal(int dx) {
        
        x += dx;
    }
}

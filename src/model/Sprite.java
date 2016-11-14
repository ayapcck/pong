package model;

import java.awt.*;

/**
 * Represents a sprite
 */
public abstract class Sprite {

    protected Color color;
    protected double x, y;
    protected int width, height;

    // Constructs a sprite
    // Effects: sprite is at designated x, y coords, with given height and width
    public Sprite(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Draws the sprite
    // Modifies: g
    // Effects: draws sprite onto g
    public abstract void draw(Graphics g);

}

package model;

import java.awt.*;
import java.util.Random;

/**
 * Created by user on 05/10/2015.
 */
public class Ball extends Sprite {

    public static final int SIDE_LENGTH = 10;
    public static final int STARTING_SPEED = 4;
    public static final int NEGDX = -STARTING_SPEED;
    public static final int POSDX = STARTING_SPEED;

    public double dx, dy;
    private double savedDx, savedDy;
    private String key;

    // Constructs a ball
    // Effects: ball at location (x, y)
    public Ball(int x, int y, String key) {
        super(x, y, SIDE_LENGTH, SIDE_LENGTH);
        color = new Color(41, 193, 0);
        this.key = key;
    }

    // Gets random dx and dy
    public void getStartingMovement() {
        getRandomDx();
        getRandomDy();
    }

    // Gets a random starting direction for horiz and vert
    // Modifies: this
    // Effects: dx and dy set to random
    private void getRandomDx() {
        Random generator = new Random();
        int rand = generator.nextInt(100);
        if (rand >= 49) dx = POSDX;
        else dx = NEGDX;
    }
    private void getRandomDy() {
        Random generator = new Random();
        int rand = generator.nextInt(100);
        if (rand > 49) dy = POSDX;
        else dy = NEGDX;
    }

    // Dras the ball onto Grapphics object
    // Modifies: g
    // Effects: Ball is drawn to screen according to its coords and dimensions
    @Override
    public void draw(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(color);
        g.fillOval((int)getX() - SIDE_LENGTH / 2, (int)getY() - SIDE_LENGTH / 2, SIDE_LENGTH, SIDE_LENGTH);
        g.setColor(savedCol);
    }

    // Moves the sprite
    // Modifies: this
    // Effects: sprite is moved
    public void move() {
        x += dx;
        y += dy;
        handleTopBottom();
    }

    // Simulates a bounce off the top or bottom
    // Modifies: this
    // Effects: If ball is past bounds of game box, put back in game box and change direction simulating a bounce
    private void handleTopBottom() {
        if (this.getY() - this.getHeight()/2 <= 0 || this.getY() + this.getHeight()/2 >= PGame.HEIGHT) {
            dy *= -1;
            this.y += dy;
        }
    }

    // Resets balls horizontal movement
    // Modifies: this
    // Effects: dx is set to either posDX or negDX depending on sign of dx
    public void resetDx(boolean greaterThan0) {
        if (greaterThan0) dx = POSDX;
        else dx = NEGDX;
    }

    // Starts the movement of the ball
    // Effects: sets dx and dy to saved values
    public void startMoving() {
        dx = savedDx;
        dy = savedDy;
    }
    // Stops the movement of the ball
    // Effects: saves dx and dy, sets dx and dy to 0
    public void stopMoving() {
        savedDx = dx;
        savedDy = dy;
        dx = 0;
        dy = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ball)) return false;

        Ball ball = (Ball) o;

        return !(key != null ? !key.equals(ball.key) : ball.key != null);

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
package model;

import java.awt.*;

/**
 * Created by user on 05/10/2015.
 */
public class Paddle extends Sprite {

    public static final int X_SIZE = 10;
    public static final int REGULAR_Y_SIZE = 100;

    public int dy = 4;
    private int savedDy;
    private double savedYPos;
    public double savedXPos;
    private char direction = 'U';
    private boolean cheating = false;

    // Constructs a paddle
    // Effects: paddle at (x, y)
    public Paddle(int x, int y) {
        super(x, y, X_SIZE, REGULAR_Y_SIZE);
        color = new Color(41, 193, 0);
    }

    private void setY(int y) {
        this.y = y;
    }
    public void setX(double x) { this.x = x; }

    public boolean isCheating() {
        return cheating;
    }
    public char getDirection() { return direction; }

    @Override
    public void draw(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(color);
        g.fillRect((int) getX() - getWidth() / 2, (int) getY() - getHeight() / 2, getWidth(), getHeight());
        g.setColor(savedCol);
    }

    // Moves paddle according to direction
    // Modifies: this
    // Effects: Moves paddle up by dy for 'U'
    //          Moves paddle down by dy for 'D'
    //          does not move paddle past game boundary
    public void move() {
        double top = y - (getHeight() / 2);
        double bot = y + (getHeight() / 2);
        if (direction == 'U') {
            if (top - dy > 0)
                y -= dy;
            else
                y = (getHeight() / 2);
        }
        else if (direction == 'D') {
            if (bot + dy < PGame.HEIGHT)
                y += dy;
            else
                y = PGame.HEIGHT - (getHeight() / 2);
        }
    }

    // Sets direction of paddle
    // Requires: dir is either 'U' or 'D'
    // Modifies: this
    // Effects: Sets direction to dir
    public void changeDirection(char dir) {
        direction = dir;
    }

    // Starts paddle movement
    // Effects: sets dy to saved value
    public void startMovement() {
        dy = savedDy;
    }
    // Stops paddle movement
    // Effects: saves value of dy, sets dy to 0
    public void stopMovement() {
        savedDy = dy;
        dy = 0;
    }

    // Resizes the paddle height
    // Effects: changes paddle height to given int, stops movement
    public void startCheating() {
        this.height = PGame.HEIGHT;
        savedYPos = getY();
        setY(getHeight() / 2);
        cheating = true;
    }
    // Effects: reverts paddle height to original height, starts movement again
    public void stopCheating() {
        setY((int)savedYPos);
        this.height = REGULAR_Y_SIZE;
        cheating = false;
    }
}

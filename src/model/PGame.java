package model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a Pong game
 */
public class PGame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private static final int LEFT_PAD_START = 10;
    private static final int RIGHT_PAD_START = WIDTH - 10;

    private Ball mainBall;
    private ArrayList<Ball> cheatBall;
    private ArrayList<Ball> spentBalls;
    private int halfBall, halfPaddleHeight, halfPaddleWidth;
    private Paddle leftPaddle, rightPaddle;
    public boolean isRightWon, isLeftWon, isInstructScreen, isPauseScreen, isCheatScreen, isOptionScreen;
    private boolean isStartScreen = true;
    private boolean isOverScreen = false;
    private boolean cheatsEnabled = false;
//    private boolean paddlesCloser = false;
    private boolean computerPlaying = true;
    private int scoreRight, scoreLeft;

    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }
    public int getScoreRight() { return scoreRight; }
    public int getScoreLeft() { return scoreLeft; }
    public boolean isComputerPlaying() { return computerPlaying; }
    public void setComputerPlaying(boolean val) { computerPlaying = val; }

    // Constructor
    // Effects: set up the game, halts mainBall movement
    public PGame() {
        setup();
        mainBall.dy = 0;
        mainBall.dx = 0;
    }

    // Updates game on clock tick
    // Modifies: this
    // Effects: updates game
    public void update() {
        moveMainBall();
        moveCheatBalls();
        movePaddles();
//        handleCheatingPaddle();
        // handle bounces for mainBall
        handlePaddleBounce(mainBall);
        // handle bounces for cheat Balls
        for (Ball b : cheatBall) handlePaddleBounce(b);
        // handle points for mainBall
        handlePoints(mainBall);
        // handle points for cheat Balls
        spentBalls = new ArrayList<Ball>();
        for (Ball b : cheatBall) handlePoints(b);
        for (Ball b : spentBalls) cheatBall.remove(b);
        // handles computer ai for rightPaddle paddle
        if (computerPlaying) computerPaddleMove();
        checkGameOver();
    }

//    private void handleCheatingPaddle() {
//        if (paddlesCloser) {
//            leftPaddle.savedXPos = leftPaddle.getX();
//            rightPaddle.savedXPos = rightPaddle.getX();
//            leftPaddle.setX(leftPaddle.getX() + (WIDTH/2 - leftPaddle.getX()));
//            rightPaddle.setX(rightPaddle.getX()  - (rightPaddle.getX() - WIDTH/2));
//        }
//        else if (!paddlesCloser) {
//            leftPaddle.setX(leftPaddle.savedXPos);
//            rightPaddle.setX(rightPaddle.savedXPos);
//        }
//    }

    // Changes direction of rightPaddle paddle depending on location of mainBall
    // Modifies: rightPaddle
    // Effects: changes direction of rightPaddle to stay located with mainBall
    private void computerPaddleMove() {
        double ballY = mainBall.getY();
        double paddleBot = rightPaddle.getY() + rightPaddle.getHeight()/2.5;
        double paddleTop = rightPaddle.getY() - rightPaddle.getHeight()/2.5;
        if (ballY > paddleBot) rightPaddle.changeDirection('D');
        if (ballY < paddleTop) rightPaddle.changeDirection('U');
    }

    // Checks to see if game has been won
    // Effects: sets game over to true if either score has reached 5
    private void checkGameOver() {
        if (scoreLeft >= 5) {
            isLeftWon = true;
            stopMovement();
        }
        if (scoreRight >= 5) {
            isRightWon = true;
            stopMovement();
        }
        isOverScreen = isLeftWon || isRightWon;
    }

    // Modifies: scoreRight, scoreLeft, given Ball
    // Effects: awards the correct side with a point, if a point should be awarded,
    //          if given Ball is mainBall, resets mainBall for next turn
    //          otherwise remove given Ball from cheatBall
    private void handlePoints(Ball ball) {
        if (ball.equals(mainBall)) {
            if (isPointRight(ball)) {
                scoreRight += 1;
                resetMainBall();
            }
            if (isPointLeft(ball)) {
                scoreLeft += 1;
                resetMainBall();
            }
        }
        else {
            if (isPointRight(ball)) {
                scoreRight += 1;
                ball.x = PGame.WIDTH/2;
                ball.stopMoving();
                spentBalls.add(ball);
            }
            if (isPointLeft(ball)) {
                scoreLeft += 1;
                ball.x = PGame.WIDTH/2;
                ball.stopMoving();
                spentBalls.add(ball);
            }
        }
    }

    // Reset mainBall
    // Modifies: mainBall
    // Effects: puts mainBall back in random center starting location,
    //          direction is towards player who just scored
    private void resetMainBall() {
        mainBall.x = WIDTH / 2;
        mainBall.y = getRandomY();
        mainBall.dx *= -1;
        mainBall.resetDx(mainBall.dx > 0);
    }

    // Get a random Y value
    // Effects: returns random Y value between 0 and HEIGHT
    private int getRandomY() {
        Random randomGenerator = new Random();
        int randY = randomGenerator.nextInt(HEIGHT);
        if (randY <= mainBall.SIDE_LENGTH / 2 || randY >= HEIGHT - (mainBall.SIDE_LENGTH / 2))
            randY = randomGenerator.nextInt(HEIGHT);
        return randY;
    }

    // Checks to see if given Ball hits the leftPaddle side
    // Effects: return true if given Ball hit leftPaddle side, false otherwise
    private boolean isPointRight(Ball ball) {
        double ballX = ball.getX() - halfBall;
        double topBall = ball.getY() - halfBall;
        double botBall = ball.getY() + halfBall;
        double topPaddle = leftPaddle.getY() - (leftPaddle.getHeight() / 2);
        double botPaddle = leftPaddle.getY() + (leftPaddle.getHeight() / 2);
        int offset = ball.getHeight();
        boolean withinRange = (botBall >= botPaddle + offset) &&
                (topBall <= (topPaddle - offset));
        boolean isTouching;
        if (withinRange)
            isTouching = ballX < (leftPaddle.getX() + (leftPaddle.getWidth() / 2));
        else
            isTouching = ballX <= 0;
        return isTouching;
    }

    // Effects: return true if given Ball's leading edge is past the paddle, false otherwise
    private boolean isPointLeft(Ball ball) {
        double ballX = ball.getX() + halfBall;
        double topBall = ball.getY() - halfBall;
        double botBall = ball.getY() + halfBall;
        double topPaddle = rightPaddle.getY() - (rightPaddle.getHeight() / 2);
        double botPaddle = rightPaddle.getY() + (rightPaddle.getHeight() / 2);
        int offset = ball.getHeight();
        boolean withinRange = (botBall >= botPaddle + offset) &&
                (topBall <= (topPaddle - offset));
        boolean isTouching;
        if (withinRange)
            isTouching = ballX > (rightPaddle.getX() - (rightPaddle.getWidth() / 2));
        else
            isTouching = ballX >= PGame.WIDTH;
        return isTouching;
    }

    // Simulates a bounce off the paddles
    // Modifies: this
    // Effects: If given Ball touches a paddle, bounce off the paddle
    //          The farther from center the given Ball hits, the faster it will move after bounce
    private void handlePaddleBounce(Ball ball) {
        double midPaddle, multiplier;
        if (bounceRight(ball)) {
            midPaddle = rightPaddle.getY();
            multiplier = getMultiplier(midPaddle, ball);
            if (ball.dy < 0 && rightPaddle.getDirection() == 'D') ball.dy *= -1;
            else if (ball.dy > 0 && rightPaddle.getDirection() == 'U') ball.dy *= -1;
            ball.dx *= -1;
            ball.resetDx(ball.dx > 0);
            ball.dx *= multiplier;
            ball.x += ball.dx;
        }
        else if (bounceLeft(ball)){
            midPaddle = leftPaddle.getY();
            multiplier = getMultiplier(midPaddle, ball);
            if (ball.dy < 0 && leftPaddle.getDirection() == 'D') ball.dy *= -1;
            else if (ball.dy > 0 && leftPaddle.getDirection() == 'U') ball.dy *= -1;
            ball.dx *= -1;
            ball.resetDx(ball.dx > 0);
            ball.dx *= multiplier;
            ball.x += ball.dx;
        }
        else if (topBotLeft(ball)) ball.dy *= -1;
        else if (topBotRight(ball)) ball.dy *= -1;
    }

    // Handles bouncing off of top or bottom of rightPaddle paddle
    // Effects: simulated bouncing off top or bottom of paddle
    private boolean topBotRight(Ball ball) {
        double ballTop = ball.getY() - halfBall;
        double ballBot = ball.getY() + halfBall;
        double ballRight = ball.getHeight() + halfBall;
        double paddleTop = rightPaddle.getY() - halfPaddleHeight;
        double paddleBot = rightPaddle.getY() + halfPaddleHeight;
        double paddleFront = rightPaddle.getX() - halfPaddleWidth;
        boolean topBounce = ballBot >= paddleTop && ballRight > paddleFront;
        boolean botBounce = ballTop <= paddleBot && ballRight > paddleFront;
        return topBounce || botBounce;
    }

    // Handles bouncing off of top or bottom of leftPaddle paddle
    // Effects: simulated bouncing off top or bottom of paddle
    private boolean topBotLeft(Ball ball) {
        double ballTop = ball.getY() - halfBall;
        double ballBot = ball.getY() + halfBall;
        double ballLeft = ball.getX() - halfBall;
        double paddleTop = leftPaddle.getY() - halfPaddleHeight;
        double paddleBot = leftPaddle.getY() + halfPaddleHeight;
        double paddleFront = leftPaddle.getX() + halfPaddleWidth;
        boolean topBounce = ballBot >= paddleTop && ballLeft < paddleFront;
        boolean botBounce = ballTop <= paddleBot && ballLeft < paddleFront;
        return topBounce || botBounce;
    }

    // Takes the middle of a paddle and the given Ball's y location, computes the offset between mid and given Ball
    // Modifies: nothing
    // Effects: result is distance between given Ball.y and midPoint
    //          then turns result into a double between 0 and 2
    private double getMultiplier(double midPoint, Ball ball) {
        double result;
        double hitPos = ball.getY();
        if (ball.getY() > midPoint) {
            result = hitPos - midPoint;
        }
        else {
            result = Math.abs(hitPos - midPoint);
        }
        result /= rightPaddle.getHeight()/4;
        result += 1;
        return result;
    }

    // Check to see if given Ball should bounce off rightPaddle paddle
    // Effects: return true if given Ball should bounce, false otherwise
    private boolean bounceRight(Ball ball) {
        int halfPaddleHeight = rightPaddle.getHeight()/2;
        double xBallEdge = ball.getX() + halfBall;
        double padEdge = rightPaddle.getX() - (rightPaddle.getWidth()/2);
        boolean touchingPad = xBallEdge > padEdge;
        boolean inPadRange = ball.getY() + halfBall < rightPaddle.getY() + halfPaddleHeight &&
                ball.getY() - halfBall > rightPaddle.getY() - halfPaddleHeight;
        return touchingPad && inPadRange;
    }

    // Check to see if given Ball should bounce off leftPaddle paddle
    // Effects : return true if given Ball should bounce, false otherwise
    private boolean bounceLeft(Ball ball) {
        int halfPaddleHeight = leftPaddle.getHeight()/2;
        double xBallEdge = ball.getX() - halfBall;
        double padEdge = leftPaddle.getX() + (leftPaddle.getWidth()/2);
        boolean touchingPad = xBallEdge < padEdge;
        boolean inPadRange = ball.getY() + halfBall < leftPaddle.getY() + halfPaddleHeight &&
                ball.getY() - halfBall > leftPaddle.getY() - halfPaddleHeight;
        return touchingPad && inPadRange;
    }

    // Moves mainBall
    // Modifies: this
    // Effects: moves mainBall to location at next time
    private void moveMainBall() {
        mainBall.move();
    }

    // Moves cheat balls, if any
    // Effects: moves each Ball in cheatBall to location at next time
    private void moveCheatBalls() {
        for (Ball b : cheatBall) {
            b.move();
        }
    }

    // Moves paddles
    // Modifies: this
    // Effects: moves paddles according to location at next time
    private void movePaddles() {
        rightPaddle.move();
        leftPaddle.move();
    }

    // Set/resets the game
    // Modifies: this
    // Effects: puts paddles and mainBall in starting position
    //          score set to 0
    //          cheating is disabled
    private void setup() {
        mainBall = new Ball(WIDTH / 2, getRandomY(), "main");
        mainBall.getStartingMovement();
        halfBall = mainBall.getHeight() / 2;
        cheatBall = new ArrayList<Ball>();
        spentBalls = new ArrayList<Ball>();
        leftPaddle = new Paddle(LEFT_PAD_START, HEIGHT / 2);
        rightPaddle = new Paddle(RIGHT_PAD_START, HEIGHT / 2);
        halfPaddleHeight = leftPaddle.getHeight()/2;
        halfPaddleWidth = leftPaddle.getWidth()/2;
        scoreRight = 0;
        scoreLeft = 0;
        isRightWon = false;
        isLeftWon = false;
        isInstructScreen = false;
        isPauseScreen = false;
        isOptionScreen = false;
    }

    // Pauses the game if unpaused, unpause if paused
    // Modifies: this
    // Effects: stop mainBall, each Ball in cheatBall and paddle movement, display pause screen and instructions
    public void togglePause() {
        if (isPauseScreen()) {
            startMovement();
            isPauseScreen = false;
        }
        else {
            stopMovement();
            isPauseScreen = true;
        }
    }

    // Starts the mainBall, each Ball in cheatBall, and paddle movement
    private void startMovement() {
        mainBall.startMoving();
        for (Ball b : cheatBall) b.startMoving();
        leftPaddle.startMovement();
        rightPaddle.startMovement();
    }
    // Stops the mainBall, each Ball in cheatBall, and paddle movement
    private void stopMovement() {
        mainBall.stopMoving();
        for (Ball b : cheatBall) b.stopMoving();
        leftPaddle.stopMovement();
        rightPaddle.stopMovement();
    }

    // Should start screen be displayed?
    // Effects: return true if start game, false otherwise
    public boolean isStartScreen() {
        return isStartScreen;
    }
    public void setStartScreen(boolean val) { isStartScreen = val; }
    public void startGame() {
        allScreensOff();
        setup();
    }
    // Should instruction screen be displayed?
    // Effects: return true if instruction screen flag is set, false otherwise
    public boolean isInstructScreen() {
        return isInstructScreen;
    }
    public void setIsInstruct(boolean val) {
        isInstructScreen = val;
    }
    // Should cheat screen be displayed?
    // Effects: returns true if cheat flag is set, false otherwise
    public boolean isCheatScreen() { return isCheatScreen; }
    public void setIsCheatScreen(boolean val) { isCheatScreen = val; }
    public boolean isOptionScreen() { return isOptionScreen; }
    public void setIsOptionScreen(boolean val) { isOptionScreen = val; }
    // Are cheats enabled?
    // Effect: return true if cheats are enabled, false otherwise
    public boolean isCheatsEnabled() {
        return cheatsEnabled;
    }
    // Should pause screen be displayed
    // Effects: return true if game is paused, false otherwise
    public boolean isPauseScreen() { return isPauseScreen; }
    // Should game over screen be displayed?
    // Effects: return true if game over, false otherwise
    public boolean isOverScreen() {
        return isOverScreen;
    }
    // Effects: toggles cheatsEnabled
    public void toggleCheats() {
        resetCheats();
        cheatsEnabled = !cheatsEnabled;
    }
    // Effects: Turns all screen flags off
    public void allScreensOff() {
        isCheatScreen = false;
        isOverScreen = false;
        isStartScreen = false;
        isPauseScreen = false;
        isInstructScreen = false;
        isOptionScreen = false;
    }

    private void resetCheats() {
        if (leftPaddle.isCheating()) leftPaddle.stopCheating();
        if (rightPaddle.isCheating()) rightPaddle.stopCheating();
    }

    // Effects: adds new Ball to cheatBall
    private void addBall() {
        Ball newBall  = new Ball(WIDTH / 2, getRandomY(), "");
        newBall.getStartingMovement();
        cheatBall.add(newBall);
    }

    // Draws mainBall, each Ball in cheatBall, and the leftPaddle and rightPaddle Paddles
    public void draw(Graphics g) {
        mainBall.draw(g);
        for (Ball b : cheatBall) b.draw(g);
        leftPaddle.draw(g);
        rightPaddle.draw(g);
    }

    // Responds to key press codes
    // Modifies: this
    // Effects: moves proper paddle depending on key presses
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                if (!computerPlaying) rightPaddle.changeDirection('U');
                break;
            case KeyEvent.VK_W:
                leftPaddle.changeDirection('U');
                break;
            case KeyEvent.VK_DOWN:
                if (!computerPlaying) rightPaddle.changeDirection('D');
                break;
            case KeyEvent.VK_S:
                leftPaddle.changeDirection('D');
                break;
            case KeyEvent.VK_R:
                if(!isStartScreen() && !isInstructScreen() && !isCheatScreen()) {
                    setup();
                    cheatsEnabled = false;
                }
                break;
            case KeyEvent.VK_M:
                isStartScreen = true;
                setup();
                break;
            case KeyEvent.VK_X:
                System.exit(0);
                break;
            case KeyEvent.VK_P:
                if(!isStartScreen() && !isInstructScreen() && !isOverScreen() && !isCheatScreen()) {
                    togglePause();
                }
                break;
            case KeyEvent.VK_Q:
                if (cheatsEnabled) {
                    if (!leftPaddle.isCheating()) leftPaddle.startCheating();
                    else leftPaddle.stopCheating();
                }
                break;
            case KeyEvent.VK_O:
                if (cheatsEnabled) {
                    if (!rightPaddle.isCheating()) rightPaddle.startCheating();
                    else rightPaddle.stopCheating();
                }
                break;
            case KeyEvent.VK_C:
                toggleCheats();
                break;
            case KeyEvent.VK_SPACE:
                if (cheatsEnabled && cheatBall.size() < 5) {
                    addBall();
                }
        }
    }
}

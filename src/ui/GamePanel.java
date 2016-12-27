package ui;

import model.PGame;
import model.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The panel in which the game is rendered
 */
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener{

    private static final Font FONT = new Font("Arial", 20, 20);
    private static final Color STAND_BUTTON_COLOR = Color.BLACK;
    private static final Color STAND_BUTTON_OUTLINE = Color.GREEN;
    private static final Color STAND_TEXT = Color.GREEN;
    private static final int STAND_BUTTON_WIDTH = 150;
    private static final int STAND_BUTTON_HEIGHT = 75;
    private static final String RIGHT_WON = "The right side won!";
    private static final String LEFT_WON = "The left side won!";
    private static final String REPLAY = "R to replay!";
    private static final String AUTHOR = "Created by Aya Peacock";
    private static final String PAUSE = "The game is paused!";
    private static final String OPTIONS = "Choose a play mode!";
    private static final String[] INSTRUCTIONS =
            {"At any time, press 'X' to exit the game",
                    "At any time, press 'M' to return to the main menu.",
                    "During the game, or once the game has ended, press 'R' to restart the game",
                    "The right side uses 'W' and 'S' for movement",
                    "The left side uses the up and down arrows for movement"};
    private static final String[] CHEATS =
            {"Full Paddle: LS = 'Q', RS = 'O'",
                    "Add a ball: Spacebar",
                    //"Play closer:"
            };
    private enum SCREENS {START, INSTRUCT, CHEAT, SPECIAL}
    private PGame game;
    private Button instructions, instructReturn, toCheatScreen, cheatReturn, enableCheats, cheatCloser,
            computer, versus;
    private boolean closerEnabled = false;
    private int closerYPos = 50;


    // Constructs a game panel
    // Effects: sets size and color of background panel,
    //          updates this with the game to be displayed
    public GamePanel(PGame g) {
        addMouseListener(this);
        setPreferredSize(new Dimension(PGame.WIDTH, PGame.HEIGHT));
        setBackground(Color.BLACK);
        this.game = g;
        int halfButton = STAND_BUTTON_WIDTH / 2;
        int leftButton = g.getWidth()/4 - halfButton;
        int rightButton = g.getWidth() - g.getWidth()/4 - halfButton;
        int centerButton = g.getWidth()/2 - halfButton;
        instructions = new Button("Instructions", leftButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        instructReturn = new Button("Back", centerButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        toCheatScreen = new Button("", 0, PGame.HEIGHT - 10, 10, 10, Color.GREEN, Color.GREEN, STAND_TEXT);
        cheatReturn = new Button("Return", leftButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        enableCheats = new Button("Enable", rightButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        computer = new Button("Computer", centerButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        versus = new Button("One vs. One", rightButton, 400,
                STAND_BUTTON_WIDTH, STAND_BUTTON_HEIGHT, STAND_BUTTON_COLOR, STAND_BUTTON_OUTLINE, STAND_TEXT);
        cheatCloser = new Button("", g.getWidth()/2 - 15, 300, 40, 40, Color.BLACK, Color.GREEN, Color.GREEN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
        Color saved = g.getColor();
        g.setColor(Color.GREEN);
        g.fillRect((game.getWidth() / 2) - 2, 0, 4, game.getHeight());
        g.setColor(saved);

        if (game.isOverScreen()) gameOver(g);
        if (game.isStartScreen()) startScreen(g);
        if (game.isInstructScreen()) instructionScreen(g);
        if (game.isPauseScreen()) pauseScreen(g);
        if (game.isCheatScreen()) cheatScreen(g);
    }

    // Draws the start screen messages
    // Modifies: g
    // Effects: draws welcome message, author, and start instructions
    private void startScreen(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(STAND_TEXT);
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        centreString(AUTHOR, g, fm, PGame.HEIGHT / 2 - 50);
        instructions.draw(g);
        computer.draw(g);
        versus.draw(g);
        toCheatScreen.draw(g);
        g.setColor(savedCol);
    }

    // Draws the instruction screen
    // Modifies: g
    // Effects: draws the instructions on the screen
    private void instructionScreen(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(STAND_TEXT);
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        int yPos = 50;
        for (String s : INSTRUCTIONS) {
            centreString(s, g, fm, yPos);
            yPos += 50;
        }
        instructReturn.draw(g);
        toCheatScreen.draw(g);
        g.setColor(savedCol);
    }

    // Draws the cheat screen
    // Modifies: g
    // Effects: draws cheat instructions on the screen
    private void cheatScreen(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(STAND_TEXT);
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        closerYPos = 50;
        for (String s : CHEATS) {
            centreString(s, g, fm, closerYPos);
            closerYPos += 50;
        }
        cheatCloser.setY((double) closerYPos - 25);
        //cheatCloser.draw(g);
        if (closerEnabled) checkButton(cheatCloser, g);
        cheatReturn.draw(g);
        enableCheats.draw(g);
        g.setColor(savedCol);
    }

    // Simulates checking of check box
    private void checkButton(Button button, Graphics g) {
        int x = (int) button.getX() + 10;
        int  y = (int) button.getY() + 10;
        int side = button.getWidth() - 20;
        Color savedCol = g.getColor();
        g.setColor(button.getOheckColor());
        g.fillRect(x, y, side, side);
        g.setColor(savedCol);
    }

    // Draws the pause screen
    // Modifies: g
    // Effects: draws pause info onto g
    private void pauseScreen(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(STAND_TEXT);
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        int yPos = PGame.HEIGHT / 2;
        centreString(PAUSE, g, fm, yPos);
        toCheatScreen.draw(g);
        g.setColor(savedCol);
    }

    // Draws the game over message and replay instructions screen
    // Modifies: g
    // Effects: draws "Game over" and replay instructions onto g
    private void gameOver(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(STAND_TEXT);
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        if (game.isRightWon)
            centreString(RIGHT_WON, g, fm, PGame.HEIGHT / 2 - 50);
        else if (game.isLeftWon)
            centreString(LEFT_WON, g, fm, PGame.HEIGHT / 2 - 50);
        centreString(REPLAY, g, fm, PGame.HEIGHT / 2);
        g.setColor(savedCol);
    }

    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, (PGame.WIDTH - width) / 2, yPos);
    }

    // Draws the game
    // Modifies: g
    // Effects: the game is drawn onto the Graphics object g
    private void drawGame(Graphics g) {
        game.draw(g);
    }

    // Updates the GamePanel
    public void update() {
        if (game.isCheatsEnabled()) enableCheats.setText("Disable");
        else enableCheats.setText("Enable");
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (inButton(x, y, instructions) && onProperScreen(SCREENS.START)) {
            game.allScreensOff();
            game.setIsInstruct(true);
        }
//        if (inButton(x, y, start) && onProperScreen(SCREENS.START)) {
//            game.allScreensOff();
//            game.setIsOptionScreen(true);
//        }
        if (inButton(x, y, versus) && onProperScreen(SCREENS.START)) {
            game.setComputerPlaying(false);
            game.startGame();
        }
        if (inButton(x, y, computer) && onProperScreen(SCREENS.START)) {
            game.setComputerPlaying(true);
            game.startGame();
        }
        if (inButton(x, y, instructReturn) && onProperScreen(SCREENS.INSTRUCT)) {
            game.allScreensOff();
            game.setStartScreen(true);
        }
        if (inButton(x, y, toCheatScreen) && onProperScreen(SCREENS.SPECIAL)) {
            game.setIsCheatScreen(true);
        }
        if (inButton(x, y, cheatReturn) && onProperScreen(SCREENS.CHEAT)) {
            game.setIsCheatScreen(false);
        }
        if (inButton(x, y, enableCheats) && onProperScreen(SCREENS.CHEAT)) {
            game.toggleCheats();
        }
        if (inButton(x, y, cheatCloser) && onProperScreen(SCREENS.CHEAT)) {
            toggleCheatCloser();
        }
    }

    public void toggleCheatCloser() { closerEnabled = !closerEnabled; }

    private boolean onProperScreen(SCREENS screen) {
        switch (screen) {
            case START:
                return onStartScreen();
            case INSTRUCT:
                return onInstructScreen();
            case CHEAT:
                return onCheatScreen();
            case SPECIAL:
                return cheatButtonScreen();
        }
        return false;
    }

    // Effects: return true if current screen is start screen
    private boolean onStartScreen() {
        return game.isStartScreen() && !game.isInstructScreen() && !game.isPauseScreen()
                && !game.isCheatScreen() && !game.isOverScreen();
    }
    // Effects: return true if current screen is instruction screen
    private boolean onInstructScreen() {
        return !game.isStartScreen() && game.isInstructScreen() && !game.isPauseScreen()
                && !game.isCheatScreen() && !game.isOverScreen();
    }
    // Effects: return true of current screen is cheat screen
    private boolean onCheatScreen() {
        return game.isCheatScreen();
    }
    // Effects: return true if current screen is any screen but playing and over screen
    private boolean cheatButtonScreen() {
        return (game.isStartScreen() || game.isInstructScreen() || game.isPauseScreen())
                && !game.isCheatScreen() && !game.isOverScreen();
    }

    // Checks to see if the x and y are in given button
    // Effects: returns true if the x and y are inside button
    private boolean inButton(int x, int y, Button button) {
        int buttonX = (int) button.getX();
        int buttonY = (int) button.getY();
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();
        Rectangle bound = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        return bound.contains(x, y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

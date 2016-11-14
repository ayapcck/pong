package ui;

import model.PGame;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the panel in which the scoreboard is displayed
 */
public class ScorePanel extends JPanel {

    private static final String SCORE = "Score: ";
    private static final Color background = Color.BLACK;
    private static final Color text = Color.GREEN;
    private static final int LBL_HEIGHT = 40;
    private PGame game;
    private String left, right;

    // Constructs a score panel
    // Effects: sets the background color and sets the initial labels;
    //          updates this with the game whose score is to be displayed
    public ScorePanel(PGame g) {
        game = g;
        setBackground(background);
        left = SCORE + game.getScoreLeft();
        right = SCORE + game.getScoreRight();
        setPreferredSize(new Dimension(PGame.WIDTH, LBL_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawScreen(g);
        if(game.isOverScreen() || game.isPauseScreen()) {
             turnToBlack(g);
        }
        if (game.isStartScreen()) {
            cover(g);
            drawTitle("PONG", g);
        }
        if (game.isInstructScreen()) {
            cover(g);
            drawTitle("INSTRUCTIONS", g);
        }
        if (game.isCheatScreen()) {
            cover(g);
            drawTitle("CHEATS", g);
        }
    }

    // Draws given title to center of score panel
    private void drawTitle(String title, Graphics g) {
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        int yPos = LBL_HEIGHT - LBL_HEIGHT/4;
        centreString(title, g, fm, yPos);
    }

    private void drawScreen(Graphics g) {
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        int yPos = LBL_HEIGHT - LBL_HEIGHT/4;
        g.setColor(text);
        g.fillRect(0, getHeight()-4, PGame.WIDTH, 4);
        leftString(left, g, fm, yPos);
        rightString(right, g, fm, yPos);
    }

    // Overwrites score panel with green line
    private void cover(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, LBL_HEIGHT);
        g.setColor(text);
        g.fillRect(0, getHeight() - 4, PGame.WIDTH, 4);
        g.setColor(savedCol);
    }

    // Overwrites score panel
    private void turnToBlack(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PGame.WIDTH, PGame.HEIGHT);
        g.setColor(savedCol);
    }

    // Draws given string onto g at given y on left of screen
    // Modifies: g
    // Effects: draws str onto g at left of screen at given yPos
    private void leftString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, width, yPos);
    }
    // Draws given string onto g at given y on right of screen
    // Modifies: g
    // Effects: draws str onto g at right of screen at given yPos
    private void rightString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, PGame.WIDTH - 2*width, yPos);
    }
    // Draws given string onto g at given y on center of screen
    // Modifies: g
    // Effects: draws str onto g at center of screen at given yPos
    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, (PGame.WIDTH-width)/2, yPos);
    }

    // Updates the score panel
    // Modifies: this
    // Effects: updates score for each side, reflecting current state of game
    public void update() {
        right = SCORE + game.getScoreRight();
        left = SCORE + game.getScoreLeft();
        repaint();
    }
}

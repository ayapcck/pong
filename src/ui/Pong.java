package ui;

import model.PGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * Represents the main window in which Pong is played in
 */
public class Pong extends JFrame {

    private static final int INTERVAL = 20;
    private PGame game;
    private GamePanel gp;
    private ScorePanel sp;
    private Timer t;

    // Constructs main window
    // Effects: sets up window in which Pong is to be played
    public Pong() {
        super("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        game = new PGame();
        gp = new GamePanel(game);
        sp = new ScorePanel(game);
        add(gp);
        add(sp, BorderLayout.NORTH);
        addKeyListener(new KeyHandler());
        pack();
        centerOnScreen();
        setVisible(true);
        addTimer();
        t.start();
    }

    // Centers frame on desktop
    // Modifies: this
    // Effects: location of frame is set to be centered on desktop
    private void centerOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    private void addTimer() {
        t = new Timer(INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.update();
                gp.update();
                sp.update();
            }
        });
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { game.keyPressed(e); }
    }

    public static void main(String[] args) { new Pong(); }

}

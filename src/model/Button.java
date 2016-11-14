package model;

import java.awt.*;

/**
 * Created by user on 15/02/2016.
 */
public class Button extends Sprite {

    protected static final Font font = new Font ("Arial", 20, 20);
    private Color outline, color, textCol;
    private String text;
    private int border = 5;
    Rectangle rect;

    /**
     * @param text the button's text.
     * @param x the <i>x</i> coordinate.
     * @param y the <i>y</i> coordinate.
     * @param height the height of the button.
     * @param width the width of the button
     * @param color color of the button
     * @param outline color of outline of button **/
    public Button(String text, int x, int y, int width, int height, Color color, Color outline, Color textCol) {
        super ((double) x, (double) y, width, height);
        rect = new Rectangle((int)this.x, (int)this.y, this.width, this.height);
        this.color = color;
        this.outline = outline;
        this.text = text;
        this.textCol = textCol;
    }

    public Color getOheckColor() { return outline; }
    public void setY(double y) {this.y = y;}

    public void setText(String text) {
        this.text = text;
    }

    public void draw(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(outline);
        g.fillRect((int) x, (int) y, width, height);
        g.setColor(color);
        g.fillRect((int) x + border, (int) y + border, width - 2 * border, height - 2 * border);
        g.setColor(textCol);
        drawCenteredString(g, text, font);
        g.setColor(savedCol);
    }

    public void drawCenteredString(Graphics g, String text, Font font) {
        FontMetrics fm = g.getFontMetrics(font);
        int x = (int) this.x + (rect.width - fm.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text
        int y = (int) this.y + (rect.height + fm.getHeight()) / 2 - border;
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
}

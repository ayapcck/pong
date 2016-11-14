package test;

import model.Ball;
import model.PGame;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by user on 07/10/2015.
 */
public class TestBall extends TestSprite{

    private Ball ball;



    @Test
    public void testHandleBoundary() {
        sprite = new Ball(PGame.WIDTH / 2, PGame.HEIGHT - 1, "");
        ball = (Ball) sprite;
        ball.dy = 1;
        ball.move();
        assertEquals(PGame.HEIGHT - 1, ball.getY());
        assertEquals(-1, ball.dy);

        sprite = new Ball(PGame.WIDTH / 2, 1, "");
        ball = (Ball) sprite;
        ball.dy = -1;
        ball.move();
        assertEquals(1, ball.getY());
        assertEquals(1, ball.dy);
    }

}

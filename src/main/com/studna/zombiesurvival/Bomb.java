package main.com.studna.zombiesurvival;

import org.apache.commons.lang3.time.StopWatch;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.Random;

/**
 * <h1>Bomb</h1>
 * Class Bomb implements one of the guns, which has a player available.
 * Bombs change their color during their execution time, which is limited.
 * Bomb will disappear in three scenarios.
 *      1. if the lifetime is over.
 *      2. if the bomb gets out of the window
 *      3. if the bomb hits enemy
 *  If the bomb hits enemy, it is going resize and then it will dissappear.
 **/
public class Bomb
{
    public StopWatch watch;
    public boolean hit;
    private Random rnd;
    public int ticks;
    private int radius;

    public CircleShape shape;
    public Vector2f currVelocity;
    public float maxVelocity;

    public Bomb()
    {
        watch = new StopWatch();
        hit = false;
        ticks = 0;
        radius = 7;
        currVelocity = new Vector2f(0, 0);
        shape = new CircleShape(radius);
        maxVelocity = 10;
        shape.setFillColor(Color.GREEN);
        rnd = new Random();
    }

    public void Draw(RenderWindow window)
    {
        window.draw(shape);
    }

    private void switchColor(int i)
    {

        switch (i)
        {
            case 0:
                shape.setFillColor(Color.GREEN);
                break;
            case 1:
                shape.setFillColor(Color.YELLOW);
                break;
            case 2:
                shape.setFillColor(Color.RED);
                break;
        }
    }

    public void resize()
    {

        int i = rnd.nextInt(3);
        ticks++;
        switchColor(i);

        if (shape.getRadius() < 30)
        {
            radius++;
            shape.setRadius(radius);
        }

    }

    public void Move(Vector2f direction)
    {
        int i = rnd.nextInt(3);
        switchColor(i);

        shape.setPosition(new Vector2f(shape.getPosition().x + direction.x, shape.getPosition().y + direction.y));
    }
}

package com.studna.zombiesurvival;

import org.apache.commons.lang3.time.StopWatch;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.Random;


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
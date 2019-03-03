package main.com.studna.zombiesurvival;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;

public class Laser
{
    public RectangleShape shape;
    public float fading;
    public int alpha;

    public Laser()
    {
        fading = 0.05f;
        alpha = 255;
        shape = new RectangleShape();
        shape.setFillColor(new Color(77, 204, 74));
    }

    public void fade()
    {
        alpha -= 10;
        shape.setFillColor(new Color(77, 204, 74, alpha));
        ;
    }

    public void draw(RenderWindow window)
    {
        window.draw(shape);
    }
}

package main.com.studna.zombiesurvival;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Object bullet represents one of the three types of missile, with which
 * a player shoots enemies
 */
public class Bullet
{
    public CircleShape shape;
    public Vector2f currVelocity;
    public float maxVelocity;

    public Bullet()
    {
        currVelocity = new Vector2f(0, 0);
        shape = new CircleShape(5);
        maxVelocity = 15;
        shape.setFillColor(Color.GREEN);
    }

    public void Draw(RenderWindow window)
    {
        window.draw(shape);
    }

    public void Move(Vector2f direction)
    {
        shape.setPosition(new Vector2f(shape.getPosition().x + direction.x, shape.getPosition().y + direction.y));
    }
}

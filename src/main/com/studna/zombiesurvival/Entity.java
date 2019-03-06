package main.com.studna.zombiesurvival;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

/** Class Entity holds basic properties of players and enemies */
public class Entity
{
    public Sprite sprite;
    public Vector2f position;
    public float rotation;
    public int health;
    public boolean dead;
    public float movementSpeed;
}
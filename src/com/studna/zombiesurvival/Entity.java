package com.studna.zombiesurvival;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

public class Entity
{
    public Sprite sprite;
    public Vector2f position;
    public float rotation;
    public int health;
    public boolean dead;
    public float movementSpeed;
}
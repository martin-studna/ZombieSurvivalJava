package com.studna.zombiesurvival;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/* This class stores all the keyboard and mouse events */
public class InputState
{
    public Vector2f mousePosition;
    public Vector2f mousePositionFromCenter;
    public boolean[] isKeyPressed;
    public boolean isLMBPressed;

    public InputState()
    {
        isKeyPressed = new boolean[Keyboard.Key.values().length];
    }
}

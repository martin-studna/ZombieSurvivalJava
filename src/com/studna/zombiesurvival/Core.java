package com.studna.zombiesurvival;

import org.apache.commons.lang3.time.StopWatch;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.event.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.studna.zombiesurvival.GameMode.*;


/*
 * This class initialize and runs the game. Handles events from keyboard and mouse.
 * */
public class Core
{
    private GameMode mode = Game;
    private RenderWindow window;

    public void Run() throws IOException, ContextActivationException
    {
        float lastTime = 0f;
        float currentTime = 0f;

        Init();

        while (window.isOpen())
        {
            currentTime = GameWorld.stopwatch.getTime();
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            for (Event event : window.pollEvents())
            {

                if (event == null) continue;

                if (event.type == Event.Type.MOUSE_WHEEL_MOVED) OnMouseWheelEvent(event.asMouseWheelEvent());

                if (event.type == Event.Type.KEY_PRESSED) OnKeyPressed(event.asKeyEvent());

                if (event.type == Event.Type.KEY_RELEASED) OnKeyReleased(event.asKeyEvent());

                if (event.asMouseEvent() != null) OnMouseMoved(event.asMouseEvent());

                if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) OnMouseButtonPressed(event.asMouseButtonEvent());

                if (event.type == Event.Type.MOUSE_BUTTON_RELEASED) OnMouseButtonReleased(event.asMouseButtonEvent());
            }
            switch (mode)
            {
                case Closing:
                    window.close();
                    break;

                case Menu:
                    // no menu implemented
                    mode = Game;
                    break;

                case Game:
                    GameWorld.update(deltaTime, window);
                    GameWorld.draw(window);
                    if (GameWorld.player.health <= 0) return;
                    break;
            }

            if (!window.isOpen()) break;

            window.display();
        }
    }

    /*Initialize GameWorld and other key things like window and others */
    private void Init() throws IOException, ContextActivationException
    {
        // Create window
        window = new RenderWindow(new VideoMode(800, 800), "Zombie Survival", WindowStyle.CLOSE, new ContextSettings(24, 8, 2));
        window.setFramerateLimit(100);
        window.setVerticalSyncEnabled(true);
        window.setActive();

        GameWorld.player = new Player();
        GameWorld.font = new Font();
        GameWorld.font.loadFromFile(Paths.get("./data/fonts/freesans.ttf"));
        GameWorld.score = new Text("Score: " + GameWorld.player.score, GameWorld.font, 24);
        GameWorld.score.setPosition(window.getSize().x - 150, 0);
        GameWorld.score.setColor(Color.BLACK);
        GameWorld.stopwatch = new StopWatch();
        GameWorld.enemyWatch = new StopWatch();
        GameWorld.enemyWatch.start();
        GameWorld.stopwatch.start();
        GameWorld.inputState = new InputState();
        GameWorld.MapSize = window.getSize().x;
        GameWorld.spawnCounter = 40;
        GameWorld.background = new RectangleShape(new Vector2f(window.getSize().x, window.getSize().y));
        GameWorld.background.setPosition(new Vector2f(0, 0));
        GameWorld.bullets = new ArrayList<Bullet>();
        GameWorld.lasers = new ArrayList<Laser>();
        GameWorld.bombs = new ArrayList<Bomb>();
        GameWorld.enemies = new ArrayList<Enemy>();
    }

    /* Event for gun switching */
    private void OnMouseWheelEvent(MouseWheelEvent e)
    {
        GameWorld.player.gunType = (GameWorld.player.gunType + e.delta) % 3;

        if (GameWorld.player.gunType < 0) GameWorld.player.gunType = 2;
    }

    /* Close window */
    private void OnKeyPressed(KeyEvent e)
    {
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) window.close();

        int keyCode = e.key.ordinal();

        if (keyCode < Keyboard.Key.values().length) GameWorld.inputState.isKeyPressed[keyCode] = true;
    }

    /* Events from keyboard */
    private void OnKeyReleased(KeyEvent e)
    {
        int keyCode = e.key.ordinal();

        if (keyCode < Keyboard.Key.values().length) GameWorld.inputState.isKeyPressed[keyCode] = false;
    }

    /* Mouse position */
    private void OnMouseMoved(MouseEvent e)
    {
        GameWorld.inputState.mousePosition = new Vector2f(e.position.x, e.position.y);

        GameWorld.inputState.mousePositionFromCenter = new Vector2f(GameWorld.inputState.mousePosition.x - window.getSize().x / 2, GameWorld.inputState.mousePosition.y - window.getSize().y / 2);
    }

    /* Left mouse button pressed or released */
    private void OnMouseButtonPressed(MouseButtonEvent e)
    {
        if (e.button == Mouse.Button.LEFT) GameWorld.inputState.isLMBPressed = true;
    }

    private void OnMouseButtonReleased(MouseButtonEvent e)
    {
        if (e.button == Mouse.Button.LEFT) GameWorld.inputState.isLMBPressed = false;
    }
}

package main.com.studna.zombiesurvival;

import org.apache.commons.lang3.time.StopWatch;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Holds all the important states in the game: Player, enemies, bullets and etc */
public class GameWorld
{

    public static float MapSize;

    public static RectangleShape background;

    public static SoundBuffer soundBuffer;
    public static Sound sound;
    public static StopWatch stopwatch;
    public static StopWatch enemyWatch;

    public static Player player;
    public static InputState inputState;
    public static List<Bullet> bullets;
    public static List<Enemy> enemies;
    public static List<Laser> lasers;
    public static List<Bomb> bombs;

    public static int bulletCounter;
    public static int bombCounter;
    public static boolean laserFired;

    public static int bulletReload;
    public static int bombReload;
    public static int laserReload;

    public static int spawnCounter;
    public static int gunType;
    public static int killed;

    public static Font font;
    public static Text chosenGun;
    public static Text score;

    public static Texture enemyTexture;
    public static Texture playerTexture;
    public static Texture backgroundTexture;

    /** This method updates all states of the game
     * @param deltaTime
     * @param window
     */
    public static void update(float deltaTime, RenderWindow window) throws IOException
    {
        player.healthText.setString("Health: " + player.health);
        player.setGunText();
        score.setString("Score: " + player.score);

        player.setReloadText();

        player.update(deltaTime, inputState);
        player.mousePosWindow = new Vector2f(Mouse.getPosition(window).x, Mouse.getPosition(window).y);
        player.Center = new Vector2f(player.sprite.getPosition().x + player.sprite.getScale().x / 2, player.sprite.getPosition().y + player.sprite.getScale().y / 2);

        player.rotation();

        if (Mouse.isButtonPressed(Mouse.Button.LEFT) && player.gunType == 0 && bulletCounter <= 200)
        {
            player.shoot();
            bulletCounter++;
        } else if (Mouse.isButtonPressed(Mouse.Button.LEFT) && player.gunType == 1 && !laserFired)
        {
            player.laser();
            laserFired = true;
        } else if (Mouse.isButtonPressed(Mouse.Button.LEFT) && player.gunType == 2 && bombCounter <= 100)
        {
            player.bomb();
            bombCounter++;
        }

        if (bulletCounter >= 200)
        {
            bulletReload++;
            if (bulletReload >= 200)
            {
                bulletCounter = 0;
                bulletReload = 0;
            }
        }

        if (bombCounter >= 100)
        {
            bombReload++;
            if (bombReload >= 250)
            {
                bombCounter = 0;
                bombReload = 0;
            }
        }

        if (laserFired)
        {
            laserReload++;
            if (laserReload >= 100)
            {
                laserReload = 0;
                laserFired = false;
            }
        }

        if (enemyWatch.getTime() > 1400)
        {
            enemies.add(new Enemy());
            enemyWatch.reset();
            enemyWatch.start();
        }

        for (int i = 0; i < lasers.size(); i++)
        {
            lasers.get(i).fade();

            if (lasers.get(i).alpha <= 0) lasers.remove(i);
        }

        for (Bullet bullet : bullets)
        {
            bullet.Move(bullet.currVelocity);
        }

        for (int i = 0; i < bombs.size(); i++)
        {
            bombs.get(i).ticks++;
            if (bombs.get(i).ticks <= 40 && bombs.get(i).hit == false) bombs.get(i).Move(bombs.get(i).currVelocity);

            if (bombs.get(i).shape.getPosition().x > window.getSize().x || bombs.get(i).shape.getPosition().x < 0 || bombs.get(i).shape.getPosition().y < 0 || bombs.get(i).shape.getPosition().y > window.getSize().y)
            {
                bombs.remove(i);
                break;
            }

            if (bombs.get(i).ticks > 40)
            {
                bombs.get(i).shape.setRadius(30);
            }
            if (bombs.get(i).ticks > 45) bombs.remove(i);
        }


        for (int i = 0; i < bullets.size(); i++)
        {
            if (bullets.get(i).shape.getPosition().x > window.getSize().x || bullets.get(i).shape.getPosition().x < 0 || bullets.get(i).shape.getPosition().y < 0 || bullets.get(i).shape.getPosition().y > window.getSize().y)
            {
                bullets.remove(i);
            }
        }

        for (int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).Move(player, i);

            if (enemies.get(i).dead)
            {
                enemies.get(i).playSound();
                enemies.remove(i);
                break;
            }

            if (enemies.get(i).health < 0)
            {
                enemies.get(i).playSound();
                enemies.set(i, enemies.get(enemies.size() - 1));
                enemies.get(i).giveScore();
                enemies.remove(enemies.size() - 1);
                break;
            }

            for (int j = 0; j < bullets.size(); j++)
            {
                if (enemies.size() > 0 && enemies.get(i).sprite.getGlobalBounds().intersection(bullets.get(j).shape.getGlobalBounds()) != null)
                {
                    enemies.get(i).playSound();
                    bullets.set(j, bullets.get(bullets.size() - 1));
                    bullets.remove(bullets.size() - 1);
                    enemies.get(i).health -= 10;
                    break;
                }
            }

            for (int j = 0; j < lasers.size(); j++)
            {
                if (enemies.size() > 0 && enemies.get(i).sprite.getGlobalBounds().intersection(lasers.get(j).shape.getGlobalBounds()) != null)
                {
                    enemies.get(i).playSound();
                    enemies.get(i).health -= 40;
                    break;
                }
            }

            for (int j = 0; j < bombs.size(); j++)
            {
                if (enemies.size() > 0 && enemies.get(i).sprite.getGlobalBounds().intersection(bombs.get(j).shape.getGlobalBounds()) != null)
                {
                    enemies.get(i).playSound();
                    bombs.get(j).shape.setRadius(30);
                    enemies.get(i).health -= 20;

                    if (!bombs.get(j).hit)
                    {
                        bombs.get(j).hit = true;
                        bombs.get(j).watch.start();
                    }

                }
                if (bombs.get(j).hit && bombs.get(j).watch.getTime(TimeUnit.MILLISECONDS) == 20)
                {
                    bombs.remove(j);

                }
            }
        }
    }

    /** This method draws all the objects to the specified window
     * @param window
     */
    public static void draw(RenderWindow window)
    {

        window.clear(Color.WHITE);
        window.draw(player.gunReloadText);
        player.draw(window);
        window.draw(player.healthText);
        window.draw(score);
        window.draw(player.gunText);
        drawBullets(window);
        drawEnemies(window);
        drawLasers(window);
        drawBombs(window);
    }

    private static void drawEnemies(RenderWindow window)
    {
        for (Enemy enemy : enemies)
        {
            enemy.draw(window);
        }
    }

    private static void drawBullets(RenderWindow window)
    {
        for (Bullet bullet : bullets)
        {
            bullet.Draw(window);
        }
    }

    private static void drawLasers(RenderWindow window)
    {
        for (Laser laser : lasers)
        {
            laser.draw(window);
        }
    }

    private static void drawBombs(RenderWindow window)
    {
        for (Bomb bomb : bombs)
        {
            bomb.Draw(window);
        }
    }
}

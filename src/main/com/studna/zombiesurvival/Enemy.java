package main.com.studna.zombiesurvival;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class Enemy extends Entity
{

    public Sound hurtSound;
    public boolean Collision;
    public Vector2f DirNorm;
    public boolean dead;
    public int damage;

    private final float AttackDelay = 550f;

    private float attackTimer;

    public Enemy() throws IOException
    {
        SoundBuffer buffer = new SoundBuffer();
        buffer.loadFromFile(Paths.get("./data/sounds/zombie-hurt.wav"));
        hurtSound = new Sound(buffer);
        sprite = new Sprite();
        Texture texture = new Texture();

        Random rnd = new Random();

        int type = rnd.nextInt(3);
        texture.loadFromFile(Paths.get("./data/sprites/zombie.png"));

        switch (type)
        {
            case 0:
                damage = 10;
                movementSpeed = 8;
                sprite.setColor(Color.GREEN);
                health = 50;
                break;
            case 1:
                damage = 20;
                movementSpeed = 5;
                health = 100;
                break;
            case 2:
                damage = 30;
                sprite.setColor(Color.RED);
                movementSpeed = 3;
                health = 200;
                break;
        }
        sprite.setTexture(texture);
        Collision = false;

        /*Set initial position of enemy*/
        int position = rnd.nextInt(4);
        switch (position)
        {
            case 0:
                sprite.setPosition(new Vector2f(0, (float) rnd.nextDouble() * 750));
                break;
            case 1:
                sprite.setPosition(new Vector2f((float) rnd.nextDouble() * 1000, 0));
                break;
            case 2:
                sprite.setPosition(new Vector2f((float) rnd.nextDouble() * 1000, 750));
                break;
            case 3:
                sprite.setPosition(new Vector2f(1000, (float) rnd.nextDouble() * 1000));
                break;
        }
    }

    /*Give score to a player. */
    public void giveScore()
    {
        switch (damage)
        {
            case 10:
                GameWorld.player.score += 10;
                break;
            case 20:
                GameWorld.player.score += 20;
                break;
            case 30:
                GameWorld.player.score += 30;
                break;
        }
    }

    public void playSound()
    {
        hurtSound.play();
    }

    /* Enemy tries to reach player position */
    public void Move(Player player, int i)
    {
        Vector2f direction = new Vector2f(player.sprite.getPosition().x - sprite.getPosition().x, player.sprite.getPosition().y - sprite.getPosition().y);
        DirNorm = new Vector2f((float) ((direction.x / (float) Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2)))), (float) (direction.y / (float) Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2))));

        if (!collides(player, i))
            sprite.setPosition(new Vector2f(sprite.getPosition().x + DirNorm.x * movementSpeed, sprite.getPosition().y + DirNorm.y * movementSpeed));
        else
        {
            dead = true;
            player.playHurt();
            player.health -= damage;
        }


    }

    public void draw(RenderWindow window)
    {
        window.draw(sprite);
    }

    public boolean collides(Player player, int j)
    {
        if (sprite.getGlobalBounds().intersection(player.sprite.getGlobalBounds()) != null) return true;

        return false;
    }
}

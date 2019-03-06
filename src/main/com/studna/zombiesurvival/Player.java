package main.com.studna.zombiesurvival;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.io.IOException;
import java.nio.file.Paths;

/** In class Player we handle all important events like shooting, moving, giving score and decreasing health to the player */
public class Player extends Entity
{
    private Sound hurtSound;
    private Sound shootSound;
    private Sound bombSound;
    private Sound laserSound;

    public Text healthText;
    public Text gunText;
    public Text gunReloadText;
    public int score;
    public int gunType;
    public Vector2f Center;
    public Vector2f aimDir;
    public Vector2f aimDirNorm;
    public Vector2f mousePosWindow;
    public float rotationAngle;

    public Player() throws IOException
    {
        Font font = new Font();
        font.loadFromFile(Paths.get("./data/fonts/freesans.ttf"));

        healthText = new Text("Health: " + health, font, 24);
        healthText.setPosition(0, 0);
        healthText.setColor(Color.BLACK);

        gunText = new Text("Gun: " + gunType, font, 24);
        gunText.setColor(Color.BLACK);
        gunText.setPosition(650, 30);

        gunReloadText = new Text("Reload: " + GameWorld.bulletReload / 200 + "%", font, 24);
        gunReloadText.setColor(Color.BLACK);
        gunReloadText.setPosition(650, 60);


        SoundBuffer shootSoundBuffer = new SoundBuffer();
        shootSoundBuffer.loadFromFile(Paths.get("./data/sounds/gunshot.wav"));
        SoundBuffer hurtSoundBuffer = new SoundBuffer();
        hurtSoundBuffer.loadFromFile(Paths.get("./data/sounds/hurt.wav"));
        hurtSound = new Sound(hurtSoundBuffer);
        shootSound = new Sound(shootSoundBuffer);

        SoundBuffer laserSoundBuffer = new SoundBuffer();
        laserSoundBuffer.loadFromFile(Paths.get("./data/sounds/laser.wav"));
        laserSound = new Sound(laserSoundBuffer);

        SoundBuffer bombSoundBuffer = new SoundBuffer();
        bombSoundBuffer.loadFromFile(Paths.get("./data/sounds/bomb.wav"));
        bombSound = new Sound(bombSoundBuffer);

        position = new Vector2f(200, 200);
        sprite = new Sprite();
        sprite.setOrigin(new Vector2f(50, 50));
        sprite.setPosition(new Vector2f(200, 200));
        Texture texture = new Texture();
        texture.loadFromFile(Paths.get("./data/sprites/player.png"));
        sprite.setTexture(texture);
        movementSpeed = 0.5f;
        health = 100;
    }

    public void playHurt()
    {
        hurtSound.play();
    }

    /** Updates text with reload time for specific gun */
    public void setReloadText()
    {
        switch (gunType)
        {
            case 0:
                double reload = (((double) GameWorld.bulletReload) / 200) * 100;
                gunReloadText.setString("Reload: " + reload + "%");
                break;
            case 1:
                double reload2 = (((double) GameWorld.laserReload) / 100) * 100;
                gunReloadText.setString("Reload: " + reload2 + "%");
                break;
            case 2:
                double reload3 = (((double) GameWorld.bombReload) / 250) * 100;
                gunReloadText.setString("Reload: " + reload3 + "%");
                break;
        }
    }

    /** Updates text with current chosen weapon */
    public void setGunText()
    {
        switch (gunType)
        {
            case 0:
                gunText.setString("Gun: Pistol");
                break;
            case 1:
                gunText.setString("Gun: Laser");
                break;
            case 2:
                gunText.setString("Gun: Bomb");
                break;
        }
    }

    public void rotation()
    {
        float dx = Center.x - mousePosWindow.x;
        float dy = Center.y - mousePosWindow.y;
        rotationAngle = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
        sprite.setOrigin(new Vector2f(sprite.getOrigin().x / 2, sprite.getOrigin().y / 2));
        sprite.setRotation(rotationAngle + 180);

        aimDir = new Vector2f(mousePosWindow.x - Center.x, mousePosWindow.y - Center.y);
        aimDirNorm = new Vector2f(aimDir.x / (float) Math.sqrt(Math.pow(aimDir.x, 2) + Math.pow(aimDir.y, 2)), aimDir.y / (float) Math.sqrt(Math.pow(aimDir.x, 2) + Math.pow(aimDir.y, 2)));
    }

    public void update(float deltaTime, InputState inputState)
    {
        if (health <= 0) {
            return;
        }

        if (inputState.isKeyPressed[Keyboard.Key.A.ordinal()])
            position = new Vector2f(position.x - deltaTime * movementSpeed, position.y);

        if (inputState.isKeyPressed[Keyboard.Key.D.ordinal()])
            position = new Vector2f(position.x + deltaTime * movementSpeed, position.y);

        if (inputState.isKeyPressed[Keyboard.Key.W.ordinal()])
            position = new Vector2f(position.x, position.y - deltaTime * movementSpeed);

        if (inputState.isKeyPressed[Keyboard.Key.S.ordinal()])
            position = new Vector2f(position.x, position.y + deltaTime * movementSpeed);

        if (position.x > 0 && position.x < GameWorld.MapSize && position.y > 0 && position.y < GameWorld.MapSize)
            sprite.setPosition(position);
    }

    public void draw(RenderWindow window)
    {
        window.draw(sprite);
    }

    public void laser()
    {
        laserSound.play();
        Laser laser = new Laser();
        laser.shape.setPosition(sprite.getPosition());
        laser.shape.setSize(new Vector2f(1500, 10));
        laser.shape.setRotation(sprite.getRotation());
        GameWorld.lasers.add(laser);
    }

    public void bomb()
    {
        bombSound.play();
        Bomb bomb = new Bomb();
        bomb.shape.setPosition(sprite.getPosition().x, sprite.getPosition().y);
        bomb.currVelocity = new Vector2f(aimDirNorm.x * bomb.maxVelocity, aimDirNorm.y * bomb.maxVelocity);
        GameWorld.bombs.add(bomb);

    }

    public void shoot()
    {
        shootSound.play();
        Bullet bullet = new Bullet();
        GameWorld.bulletCounter++;
        bullet.shape.setPosition(sprite.getPosition().x, sprite.getPosition().y);
        bullet.currVelocity = new Vector2f(aimDirNorm.x * bullet.maxVelocity, aimDirNorm.y * bullet.maxVelocity);
        GameWorld.bullets.add(bullet);
    }
}

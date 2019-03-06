package main.com.studna.zombiesurvival;

import org.jsfml.window.ContextActivationException;

import java.io.IOException;


/**
 * <h1> Zombie Survival </h1>
 *
 * Zombie Survival is simple shooter game written in Java.
 * Your goal is to kill as many zombies as possible and survive
 * for the longest time. As a player you can shoot zombies with
 * three types of guns: pistol, bomb and laser. Every gun takes
 * different amount of damage to zombies, has different reload
 * time and has different number of missiles. There are also three
 * types of zombies in the game. There is a basic minion, which does
 * not have any special abilities. Then there is a sprinter (green one),
 * which moves quite fast, but takes less damage to the player. Finally
 * there is a boss (red), which takes the biggest amount of damage to the player,
 * but he is the slowest one.
 *
 * @author: Martin Studna
 * @version: 1.0
 * @since 2018-02-17
 */

public class Main
{
    public static void main(String[] args) throws IOException, ContextActivationException
    {
        Core core = new Core();
        core.Run();
    }
}

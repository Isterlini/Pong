// load windows drivers to execute code from Game classes
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Launcher
{
    public static void main(String[] args)
    {
        // create an instance of our game
        //    and run the game in Windows
        // also pass in title of window, width, and height
        new LwjglApplication( new PongGame(), "Pong 2.0", 800, 600 );
        
    }
}
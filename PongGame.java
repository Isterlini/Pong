public class PongGame extends BaseGame
{
    // can declare variables here to make data accessible by all screens
    
    public void create()
    {
        super.create();
        
        // set first screen to be loaded
        BaseGame.setActiveScreen( new MainMenu() );
    }
    
}
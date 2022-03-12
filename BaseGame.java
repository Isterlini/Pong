// build on the Game class functionality
import com.badlogic.gdx.Game;
// manage a list of actors
import com.badlogic.gdx.scenes.scene2d.Stage;
// import Gdx class to access window/screen functions
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

// imports for working with BitMap fonts
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public abstract class BaseGame extends Game
{
    /**
     *  Stores reference to game; used when calling setActiveScreen method.
     */
    public static BaseGame game;
    
    // for use with labels
    public static LabelStyle labelStyle; // BitmapFont + Color
     
    /**
     *  Called when game is initialized; stores global reference to game object.
     */
    public BaseGame() 
    {        
        game = this;
    }
    
    // called when game object is initialized
    public void create()
    {
        // parameters for generating a custom bitmap font
        FreeTypeFontGenerator fontGenerator = 
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 48;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        
        // controls appearance when scaling
        fontParameters.minFilter = TextureFilter.Linear;
        fontParameters.magFilter = TextureFilter.Linear;
        
        // create a Bitmap font for use in program from given font name (FontGenerator)
        //   and using data from FontParameters.
        BitmapFont customFont = fontGenerator.generateFont(fontParameters);
        // initialize the style object
        labelStyle = new LabelStyle();
        labelStyle.font = customFont;
        
    }
    
    /**
     *  Used to switch screens while game is running.
     *  Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen s)
    {
        game.setScreen(s);
    }
    
    
}
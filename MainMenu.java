// import Gdx class to get user input
import com.badlogic.gdx.Gdx;
// import constants that represent each key
import com.badlogic.gdx.Input.Keys;

// working with text
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

// value-based animations
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

// music and sounds
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MainMenu extends BaseScreen
{

    public void initialize()
    {
        new Background(0,0,mainStage);
        
        //title
        Title title = new Title(150,500,mainStage);
        title.addAction(
           Actions.forever(
           Actions.sequence(
           Actions.scaleTo(1.1f,1.1f,0.5f),
           Actions.scaleTo(0.9f,0.9f,0.5f)
             )
           )
        );
        
        Label who = new Label("Made by Isaiah Sterling",BaseGame.labelStyle);
        mainStage.addActor(who);
        who.setPosition(150,450);
        who.addAction(
           Actions.forever(
           Actions.sequence(
           Actions.scaleTo(1.1f,1.1f,0.5f),
           Actions.scaleTo(0.9f,0.9f,0.5f)
             )
           )
        );
        
        Label next = new Label("Press 'C' for controls",BaseGame.labelStyle);
        mainStage.addActor(next);
        next.setPosition(50,100);
        
        next.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.color(Color.WHITE,1),
                    Actions.color(Color.BLACK,1)
                )
            )
        );
        
        Start start = new Start(50,50,mainStage);
   
        
        start.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.color(Color.RED,1),
                    Actions.color(Color.BLACK,1)
                )
            )
        );
        
        // Sound s = Gdx.audio.newSound( Gdx.files.internal(fileName) );
    }

    public void update(float deltaTime)
    {
         if ( Gdx.input.isKeyPressed( Keys.SPACE ) )
            BaseGame.setActiveScreen( new LevelScreen());
         if(Gdx.input.isKeyPressed(Keys.C) )
            BaseGame.setActiveScreen(new ControlScreen() );
    }
    }
    

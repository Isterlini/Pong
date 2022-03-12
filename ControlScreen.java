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

public class ControlScreen extends BaseScreen
{

    public void initialize()
    {
        new Background(0,0,mainStage);
        
        Label P1Controls = new Label("P1:Use the W and S key to move \n up and down",BaseGame.labelStyle);
        mainStage.addActor(P1Controls);
        P1Controls.setPosition(0,480);
        P1Controls.setColor(Color.ROYAL);
        
        Label P2Controls = new Label("P2:Use the UP and DOWN key to \n move up and down",BaseGame.labelStyle);
        mainStage.addActor(P2Controls);
        P2Controls.setPosition(0,370);
        P2Controls.setColor(Color.SKY);
        
        Label instructions = new Label("Outscore the other player!!, first \n to 7 "
        + " wins! paddle1 is on the left while \n paddle 2 is on the right" ,BaseGame.labelStyle);
        mainStage.addActor(instructions);
        instructions.setPosition(0,170);
        instructions.setColor(Color.VIOLET);
        
        Label power = new Label("There are also power ups that will \n spwan"
        +"and you have until it reaches the \n bottom to hit it",BaseGame.labelStyle);
        mainStage.addActor(power);
        power.setPosition(0,0);
        power.setColor(Color.CYAN);
    }

    public void update(float deltaTime)
    {
        if ( Gdx.input.isKeyPressed( Keys.SPACE ) )
            BaseGame.setActiveScreen( new LevelScreen());
    }

}
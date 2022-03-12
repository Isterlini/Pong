import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;

public class Powerup extends BaseActor
{
    public String imageName;
    public Powerup(float x, float y, Stage s)
    {
        super(x, y, s);
        String[] imageNames = {"ball-speed-down", "ball-speed-up",
                                "ball-large", "ball-small","ball-gravity"};
        int randomIndex = MathUtils.random(0, imageNames.length - 1);
        this.imageName = imageNames[ randomIndex ];
        String fileName = "images/items/" + imageName + ".png";
        this.setAnimator( new Animator(fileName) );

        this.setSize(50,50);

        this.setPhysics( new Physics(0,100,0) );
        this.physics.setSpeed(100);
        this.physics.setMotionAngle(270);

        this.setScale(0.01f);
        this.addAction(
            Actions.scaleTo(1,1, 0.5f)
        );
    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);

        // destroy powerup if passes below bottom edge of screen
        if ( this.getY() + this.getHeight() < 0 )
            this.remove();
    }
}
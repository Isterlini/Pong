import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;


public class Paddle extends BaseActor
{
    public Paddle(float x, float y, Stage s)
    {
        super(x, y, s);
        this.setAnimator( new Animator("images/paddle.png") );
        
        this.setPhysics( new Physics(2400,800,2400) );
        
        // this.setBoundaryPolygon(8);
    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);
        
        this.boundToScreen(800,600);

    }
}
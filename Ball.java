import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;


public class Ball extends BaseActor
{
    public Ball(float x, float y, Stage s)
    {
        super(x, y, s);
        this.setAnimator( new Animator("images/ball.png") );
        this.setBoundaryPolygon(8);
        this.setPhysics( new Physics(0,600,0) );

    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);
    }
}
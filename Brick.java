import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;


public class Brick extends BaseActor
{
    public Brick(float x, float y, Stage s)
    {
        super(x, y, s);
        this.setAnimator( new Animator("images/brick.png") );
        // this.setBoundaryPolygon(8);
    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);

    }
}
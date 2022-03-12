import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class BaseScreen implements Screen
{
    // contains list of actors, calls their act() and draw() methods.
    public Stage mainStage;

    public BaseScreen()
    {
        mainStage = new Stage();

        initialize();
    }

    // contains game-specific code, implement in extending class
    public abstract void initialize();
    public abstract void update(float deltaTime);

    // called 60 times per second
    public void render(float deltaTime) 
    {
        // stop time from acculumating when window is moved
        if (deltaTime > 1/30f)
            deltaTime = 1/30f;

        // call actor act methods
        mainStage.act(deltaTime);

        // defined by user
        update(deltaTime);

        // clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // call actor draw methods (display graphics)
        mainStage.draw();
    }

    // methods required by Screen interface
    public void resize(int width, int height) {  }

    public void pause()   {  }

    public void resume()  {  }

    public void dispose() {  }

    public void show()    {  }

    public void hide()    {  }
}
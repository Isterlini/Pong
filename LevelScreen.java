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

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.MathUtils;

public class LevelScreen extends BaseScreen
{
    Paddle paddle1;
    Paddle paddle2;

    int player1Score;
    int player2Score;

    Label player1Label;
    Label player2Label;

    int lastPaddleHit; // 1 or 2

    Label gameMessage;
    Label winMessage;

    boolean gameOver;

    private float audioVolume; 
    private Sound explosion; 
    private Sound wallbump;
    private Sound paddleBump;
    private Sound brickBump;
    private Sound pwp;    
    private Sound ballPwp;
    public void initialize()
    {
        Background background = new Background(0,0, mainStage);

        Wall wallTop = new Wall(0,500, mainStage);
        wallTop.setSize(800,100);

        Wall wallBottom = new Wall(0,0, mainStage);
        wallBottom.setSize(800,20);

        for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
            wall.setColor( Color.PURPLE );

        paddle1 = new Paddle(0, 300, mainStage);
        paddle1.setRotation(270);
        paddle1.setColor(Color.PINK);

        paddle2 = new Paddle(780, 300, mainStage);
        paddle2.setRotation(90);
        paddle2.setColor(Color.CYAN);

        player1Score = 0;
        player2Score = 0;

        player1Label = new Label("0", BaseGame.labelStyle);
        mainStage.addActor( player1Label );
        player1Label.setPosition( 20, 520 );
        player1Label.setColor( paddle1.getColor() );

        player2Label = new Label("0", BaseGame.labelStyle);
        mainStage.addActor( player2Label );
        player2Label.setPosition( 750, 520 );
        player2Label.setColor( paddle2.getColor() );

        lastPaddleHit = 2;

        gameMessage = new Label("Get ready...", BaseGame.labelStyle);
        mainStage.addActor( gameMessage );
        gameMessage.setPosition( 280, 410 );
        gameMessage.setVisible(false);
        gameMessage.setColor( Color.VIOLET );

        //player win message 
        winMessage = new Label(" ",BaseGame.labelStyle);
        mainStage.addActor(winMessage);
        winMessage.setPosition(280,520);
        winMessage.setVisible(false);
        winMessage.setColor( Color.VIOLET);

        gameOver = false;

        //sounds
        wallbump = Gdx.audio.newSound( Gdx.files.internal("audio/boing.wav") );
        paddleBump = Gdx.audio.newSound( Gdx.files.internal("audio/bump.wav") );
        pwp = Gdx.audio.newSound( Gdx.files.internal("audio/pop.wav") );
        explosion = Gdx.audio.newSound( Gdx.files.internal("audio/explosion.ogg") );
        ballPwp = Gdx.audio.newSound( Gdx.files.internal("audio/sparkle.ogg")        );
        audioVolume = 1.00f;
    }

    public void update(float deltaTime)
    {
        if ( Gdx.input.isKeyPressed( Keys.W ) )
            paddle1.physics.accelerateAtAngle(90);
        if ( Gdx.input.isKeyPressed( Keys.S ) )
            paddle1.physics.accelerateAtAngle(270);

        if ( Gdx.input.isKeyPressed( Keys.UP ) )
            paddle2.physics.accelerateAtAngle(90);
        if ( Gdx.input.isKeyPressed( Keys.DOWN ) )
            paddle2.physics.accelerateAtAngle(270);

        // prevent overlap with paddle/wall
        for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
        {
            for (BaseActor paddle : BaseActor.getList(mainStage, "Paddle"))
            {
                if ( paddle.overlaps(wall) )
                {
                    paddle.preventOverlap(wall);
                    paddle.physics.velocity.set(0,0);
                    paddle.physics.acceleration.set(0,0);
                }
            }
        }

        // if no balls on screen, spawn a new ball
        if ( BaseActor.getList(mainStage, "Ball").size() == 0 && !gameOver)
        {
            Ball ball = new Ball(400,300, mainStage);
            int ballAngle;

            if (lastPaddleHit == 1)
            {
                // simulate serve by paddle 2
                lastPaddleHit = 2;
                ball.setColor( paddle2.getColor() );
                ballAngle = 180;
            }
            else // lastPaddleHit == 2
            {
                // simulate serve by paddle 1
                lastPaddleHit = 1;
                ball.setColor( paddle1.getColor() );
                ballAngle = 0;
            }

            Runnable code = () ->
                {
                    ball.physics.setSpeed(300);
                    ball.physics.setMotionAngle(ballAngle);
                };

            ball.addAction(
                Actions.sequence(
                    Actions.delay(2),
                    Actions.run(code)
                )
            );

            gameMessage.setVisible(true);
            gameMessage.addAction(
                Actions.sequence(
                    Actions.alpha(1),
                    Actions.fadeOut(2)
                )
            );

        }

        // ball is destroyed when off screen
        for ( BaseActor ball : BaseActor.getList(mainStage, "Ball") )
        {
            if (ball.isOffScreen(800, 600) )
            {
                if ( ball.getX() < 0 )
                {
                    player2Score++;
                    player2Label.setText( "" + player2Score );
                }
                if ( ball.getX() > 800 )
                {
                    player1Score++;
                    player1Label.setText( "" + player1Score );
                }
                ball.remove();
            }
        }

        for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
        {
            // ball bounces off paddles; 
            //   angle of bounce depends on part of paddle hit

            if (ball.overlaps(paddle1))
            {
                ball.preventOverlap(paddle1);
                float min = paddle1.getY() - paddle1.getWidth()/2;
                float max = paddle1.getY() + paddle1.getWidth()/2;
                float y = ball.getY() + ball.getHeight()/2;
                float percent = percent(min, max, y);
                float bounceAngle = lerp(315, 45+360, percent);
                ball.physics.setMotionAngle(bounceAngle);
                ball.setColor( paddle1.getColor() );
                lastPaddleHit = 1;

                paddleBump.play(audioVolume);
                //powerup spawn 
                double powerupProbability = 0.50;
                if (Math.random() < powerupProbability&&!gameOver)
                {
                    Powerup powerup = new Powerup(0,0,mainStage);
                    int x = MathUtils.random(300, 500);
                    int a = MathUtils.random(300,500);
                    powerup.setPosition(x,a);
                    pwp.play(audioVolume);
                }
            }

            if (ball.overlaps(paddle2))
            {
                ball.preventOverlap(paddle2);
                float min = paddle2.getY() - paddle2.getWidth()/2;
                float max = paddle2.getY() + paddle2.getWidth()/2;
                float y = ball.getY() + ball.getHeight()/2;
                float percent = percent(min, max, y);
                float bounceAngle = lerp(225, 135, percent);
                ball.physics.setMotionAngle(bounceAngle);
                ball.setColor( paddle2.getColor() );
                lastPaddleHit = 2;

                paddleBump.play(audioVolume);

                //powerup spawn 
                double powerupProbability = 0.50;
                if (Math.random() < powerupProbability&&!gameOver)
                {
                    Powerup powerup = new Powerup(0,0,mainStage);
                    int x = MathUtils.random(300, 500);
                    int a = MathUtils.random(300,500);
                    powerup.setPosition(x,a);
                    pwp.play(audioVolume);
                }
            }

            // ball-wall collisions
            for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
            {
                if (ball.overlaps(wall))
                {
                    wallbump.play(audioVolume);
                    Vector2 mtv = ball.preventOverlap(wall);
                    if (mtv != null)
                    {
                        float surfaceAngle = mtv.angle() + 90;
                        ball.physics.bounceAgainst(surfaceAngle);
                    }
                }
            }

        }

        for(BaseActor ball : BaseActor.getList(mainStage, "Ball"))
        {
            for(BaseActor powerup : BaseActor.getList(mainStage, "Powerup"))
            {
                if(ball.overlaps(powerup))
                {
                    ballPwp.play(audioVolume);
                    powerup.remove();

                    String type = (( Powerup)powerup).imageName;
                    if(type.equals("ball-speed-down") )
                    {                  
                        ball.physics.setSpeed(200);                        
                    }
                    else if(type.equals("ball-speed-up") )
                    {
                        ball.physics.setSpeed(600);
                    }                   
                    else if ( type.equals("ball-small") )
                    {                    
                        ball.addAction(
                            Actions.scaleTo(0.5f, 0.5f, 0.5f)
                        );                    
                    }
                    else if ( type.equals("ball-large") )
                    {                    
                        ball.addAction(
                            Actions.scaleTo(2, 2, 0.5f)
                        );                    
                    }
                    else if(type.equals("ball-gravity"))
                    {                       
                        ball.addAction(
                            Actions.sequence(
                                Actions.fadeOut(3),
                                Actions.delay(3),
                                Actions.fadeIn(3)
                            )
                        );
                    }
                }
            }
        }

        //win conditions for paddle1 
        if(player1Score == 7)
        {
            winMessage.setVisible(true);
            winMessage.setText("Player 1 WINS!!!!");
            explosion.play(audioVolume);
            gameOver = true;
        }

        if(player2Score == 7)
        {
            winMessage.setVisible(true);
            winMessage.setText("Player 2 WINS!!!!");
            explosion.play(audioVolume);
            gameOver = true;
        }
    }
    // helper methods for calculating bounce angle for ball off paddle
    //   example: min = 2, max = 10
    //            percent = 0.50   ->   lerp = 6
    //            percent = 0.25   ->   lerp = 4

    public float lerp(float min, float max, float percent)
    {
        return (min + percent * (max - min));
    }

    public float percent(float min, float max, float x)
    {
        return ( (x - min) / (max - min) );
    }

}
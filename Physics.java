// store (x,y) values
import com.badlogic.gdx.math.Vector2;

public class Physics
{
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 acceleration;

    // helpful values to store
    public float accelerationValue;
    public float decelerationValue;
    public float maximumSpeed;

    public Physics()
    {
        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2();

        accelerationValue = 800;
        maximumSpeed = 400;
        decelerationValue = 800;
    }

    public Physics(float acc, float maxSpeed, float dec)
    {
        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2();

        accelerationValue = acc;
        maximumSpeed = maxSpeed;
        decelerationValue = dec;   
    }

    public void setSpeed(float speed)
    {
        // when speed is 0, setLength does nothing -- no default direction.
        if ( getSpeed() == 0 )
        // default direction/angle to 0 degrees (to the right)
            velocity.set( speed, 0 );
        else
            velocity.setLength(speed);
    }

    public float getSpeed()
    {
        return velocity.len();
    }
    
    // This object is considered to be moving if speed is > 0.001.
    public boolean isMoving()
    {
        return getSpeed() > 0.001;
    }
    
    // note: direction is measured by LibGDX in degrees.
    public void setMotionAngle(float angle)
    {
        velocity.setAngle(angle);
    }

    public float getMotionAngle()
    {
        return velocity.angle();
    }

    // this method assumes that physics uses constant acceleration
    //   previously stored in accelerationValue
    public void accelerateAtAngle(float angle)
    {
        // create a vector with length accelerationValue and direction angle.

        // initial direction is to the right;
        //   this is necessary because setLength does not work on zero vector.
        acceleration.set( accelerationValue, 0 );
        acceleration.setAngle( angle );

    }

    // update the values of all the vectors
    public void update(float elapsedTime)
    {
        // update velocity based on acceleration
        velocity.add( acceleration.x * elapsedTime, acceleration.y * elapsedTime );

        // determine current speed and adjust if necessary.
        float speed = getSpeed();

        // enforce the max speed condition
        if ( speed > maximumSpeed )
            speed = maximumSpeed;

        // if acceleration is (close to) zero, decrease the speed
        if ( acceleration.len() < 0.0001 )
            speed = speed - decelerationValue * elapsedTime;

        // minimum possible speed is 0.
        if ( speed < 0 )
            speed = 0;

        // use the new speed value
        setSpeed( speed );

        // update the position based on velocity
        position.add( velocity.x * elapsedTime, velocity.y * elapsedTime );

        // we have "used up" acceleration vector contents; reset to 0,0.
        acceleration.set( 0,0 );
    }

    /**
     *  Change the angle of motion so this object "bounces" against
     *  a surface at the angle surfaceAngle.
     */
    public void bounceAgainst(float surfaceAngle)
    {
        float relativeCollisionAngle = getMotionAngle() - surfaceAngle;
        float finalBounceAngle = surfaceAngle - relativeCollisionAngle;
        setMotionAngle(finalBounceAngle);
    }

    
}
// building on the Actor class from LibGDX.
import com.badlogic.gdx.scenes.scene2d.Actor;
// Texture Region allows us to crop part of an image file
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// access files on the system
import com.badlogic.gdx.Gdx;
// load data from image file
import com.badlogic.gdx.graphics.Texture;

// use Batch objects to render images to the screen
import com.badlogic.gdx.graphics.g2d.Batch;

// every actor is automatically added to a stage
import com.badlogic.gdx.scenes.scene2d.Stage;

// for generating lists of actors
import java.util.ArrayList;

// used for improved collision detection
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

public abstract class BaseActor extends Actor
{
    public TextureRegion textureRegion;

    public Physics physics;

    public Animator animator;

    public Polygon boundary;
    public int numberSides;

    public BaseActor(float x, float y, Stage stage)
    {
        // call constructor of Actor class
        super();
        // set position of object
        this.setPosition(x, y);

        // set up size / collision boundary shape after animation loaded
        numberSides = 0; // defaults to rectangle

        // Physics is optional
        physics = null;

        // add the actor to a stage
        stage.addActor(this);
    }

    public void setPhysics(Physics p)
    {
        physics = p;
    }

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimator(Animator anim)
    {
        this.animator = anim;
        TextureRegion tr = animator.getKeyFrame();
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize( w, h );
        setBoundaryRectangle();
    }

    /**
     * Sets the size and origin point of this actor.
     * @param width Desired width of actor
     * @param height Desired height of actor
     */
    public void setSize(float width, float height)
    {
        super.setSize(width, height);
        // need to recenter the origin so that rotation is around center
        setOrigin( width/2, height/2 );

        if (numberSides == 0)
            setBoundaryRectangle();
        else
            setBoundaryPolygon(numberSides);
    }

    // ----------------------------------------------
    // Collision polygon methods
    // ----------------------------------------------

    /**
     *  Set rectangular-shaped collision polygon.
     *  This method is automatically called when animation is set,
     *   provided that the current boundary polygon is null.
     *  @see #setAnimation
     */
    public void setBoundaryRectangle()
    {
        float w = getWidth();
        float h = getHeight(); 

        float[] vertices = {0,0, w,0, w,h, 0,h};
        boundary = new Polygon(vertices);
    }

    /**
     *  Replace default (rectangle) collision polygon with an n-sided polygon. <br>
     *  Vertices of polygon lie on the ellipse contained within bounding rectangle.
     *  Note: one vertex will be located at point (0,width);
     *  a 4-sided polygon will appear in the orientation of a diamond.
     *  @param numSides number of sides of the collision polygon
     */
    public void setBoundaryPolygon(int numSides)
    {
        numberSides = numSides;
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2*numSides];
        for (int i = 0; i < numSides; i++)
        {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
            // y-coordinate
            vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
        }

        boundary = new Polygon(vertices);
    }

    /**
     *  Returns bounding polygon for this BaseActor, adjusted by Actor's current position and rotation.
     *  @return bounding polygon for this BaseActor
     */
    public Polygon getBoundary()
    {
        boundary.setPosition( getX(), getY() );
        boundary.setOrigin( getOriginX(), getOriginY() );
        boundary.setRotation( getRotation() );
        boundary.setScale( getScaleX(), getScaleY() );        
        return boundary;
    }

    /**
     *  Determine if this BaseActor overlaps other BaseActor (according to collision polygons).
     *  @param other BaseActor to check for overlap
     *  @return true if collision polygons of this and other BaseActor overlap
     *  @see #setCollisionRectangle
     *  @see #setCollisionPolygon
     */
    public boolean overlaps(BaseActor other)
    {
        Polygon poly1 = this.getBoundary();
        Polygon poly2 = other.getBoundary();

        // initial test to improve performance
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;

        return Intersector.overlapConvexPolygons( poly1, poly2 );
    }

    /**
     *  Implement a "solid"-like behavior:
     *  when there is overlap, move this BaseActor away from other BaseActor
     *  along minimum translation vector until there is no overlap.
     *  @param other BaseActor to check for overlap
     *  @return direction vector by which actor was translated, null if no overlap
     */
    public Vector2 preventOverlap(BaseActor other)
    {
        Polygon poly1 = this.getBoundary();
        Polygon poly2 = other.getBoundary();

        // initial test to improve performance
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return null;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if ( !polygonOverlap )
            return null;

        this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
        return mtv.normal;
    }

    // bound actor position to window
    public void boundToScreen(int screenWidth, int screenHeight)
    {
        // if the actor moved past the ... edge, then move it back

        // left edge
        if ( getX() < 0 )
            setX(0);

        // bottom edge
        if ( getY() < 0 )
            setY(0);

        // right edge
        if ( getX() + getWidth() > screenWidth )
            setX( screenWidth - getWidth() );

        // top edge
        if ( getY() + getHeight() > screenHeight )
            setY( screenHeight - getHeight() );
    }

    /**
     *  Processes all Actions and related code for this object; 
     *  automatically called by act method in Stage class.
     *  @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float deltaTime)
    {
        super.act(deltaTime);

        // update collision data
        this.boundary.setPosition( getX(), getY() );

        // if physics data is present, use it to update position of this actor.
        if ( physics != null )
        {
            // send actor position to physics object
            physics.position.set( getX(), getY() );
            // do physics calculations
            physics.update( deltaTime );
            // copy new calculated position back to actor
            this.setPosition( physics.position.x, physics.position.y );
        }

        // update the animator attached to this object
        if (animator != null)
            animator.update(deltaTime);
    }

    /**
     *  Draws current frame of animation; automatically called by draw method in Stage class. <br>
     *  If color has been set, image will be tinted by that color. <br>
     *  If no animation has been set or object is invisible, nothing will be drawn.
     *  @param batch (supplied by Stage draw method)
     *  @param parentAlpha (supplied by Stage draw method)
     *  @see #setColor
     *  @see #setVisible
     */
    public void draw(Batch batch, float alpha)
    {

        // take into account tint color and transparency / alpha value
        batch.setColor( this.getColor() );

        if ( this.isVisible() )
        // batch.draw( textureRegion, getX(), getY() );
            batch.draw( animator.getKeyFrame(), 
                getX(),       getY(),
                getOriginX(), getOriginY(), 
                getWidth(),  getHeight(), 
                getScaleX(),  getScaleY(), 
                getRotation() 
            );

        super.draw( batch, alpha );

    }

    // return a list of BaseActors on a given stage from a given class
    // example: BaseActor.getList( mainStage, "Starfish" ); 
    public static ArrayList<BaseActor> getList( Stage stage, String className )
    {
        ArrayList<BaseActor> list = new ArrayList<BaseActor>();

        // scan through list of all actors on this stage;
        // if they are an instance of the named class, add to list

        // convert String to Class object
        Class theClass = null;

        try
        {
            theClass = Class.forName(className);
        }
        catch (Exception error)
        {
            System.err.println("There is no class named: " + className);
        }

        for (Actor a : stage.getActors() )
        {
            if ( theClass.isInstance(a) )
            // need to cast Actor object to a BaseActor object
                list.add( (BaseActor)a );
        }

        // return the list
        return list;
    }

    public void wrapToScreen(float screenWidth, float screenHeight)
    {
        // check left boundary
        if ( this.getX() < -this.getWidth() )
            this.setX(screenWidth);

        // check right boundary
        if ( this.getX() > screenWidth )
            this.setX( -this.getWidth() );

        // check bottom boundary
        if ( this.getY() < -this.getHeight() )
            this.setY(screenHeight);

        // check top boundary
        if ( this.getY() > screenHeight )
            this.setY( -this.getHeight() );

    }
    
    public void centerAtPosition(float x, float y)
    {
        this.setPosition( x - this.getWidth()/2, 
                          y - this.getHeight()/2 );
    }
    
    public void centerAtActor(BaseActor other)
    {
        this.centerAtPosition( other.getX() + other.getWidth()/2, 
                               other.getY() + other.getHeight()/2 );
    }
    
    // calculate the distance between this actor and other actor
    public double distance(BaseActor other)
    {
        // distance from (x1,y1) to (x2,y2) is
        //  sqrt( (x2-x1)^2 + (y2-y1)^2 )
        return Math.sqrt( Math.pow(this.getX() - other.getX(), 2) + 
                          Math.pow(this.getY() - other.getY(), 2) );
    }
    
    public boolean isOnStage()
    {
        // check what stage this actor is assigned to;
        // if it is not null, then it is still on some stage.
        return (this.getStage() != null);
    }

    public boolean isOffScreen(double screenWidth, double screenHeight)
    {
        return ( this.getX() + this.getWidth() < 0 ) ||
               ( this.getX() > screenWidth ) ||
               ( this.getY() + this.getHeight() < 0 ) ||
               ( this.getY() > screenHeight );
    }
}
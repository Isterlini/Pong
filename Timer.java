 /**
 *  Timer objects simplify actions that happen either once after a delay
 *     or multiple times after a regular interval.
 *  Lambda expression notation recommended for specifying the Runnable.
 *  Usage: to print "Hello world" every 2 seconds, initialize as:
 *  
 *  Timer printTimer 
 *      = new Timer( 2, true, () -> 
 *                   { 
 *                      System.out.println("Hello world!"); 
 *                   } 
 *                 );
 *                  
 *  Then, during an update loop that runs 60 times per second:
 *  
 *  printTimer.update( 1/60f );
 *  
 */

public class Timer
{
    float elapsedTime;
    float timeLimit;
    Runnable function;
    boolean repeat;
    boolean enabled;
    
    // constructor
    public Timer(float timeLimit, boolean repeat, Runnable function)
    {
        this.elapsedTime = 0;
        this.timeLimit = timeLimit;
        this.function = function;
        this.repeat = repeat;
        this.enabled = true;
    }
    
    public void update(float deltaTime)
    {
        if ( !this.enabled )
            return;
            
        this.elapsedTime += deltaTime;
        if ( this.elapsedTime >= this.timeLimit )
        {
            this.function.run();
            if ( this.repeat )
                this.elapsedTime = 0;
            else // not repeating, then disable
                this.enabled = false;
        }
    }
    
    
}
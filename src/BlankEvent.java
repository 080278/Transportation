
/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines an event, It contains their attributes needed for the 
 * simulation, and the actions that can be performed by any event.
 */
public class BlankEvent implements Comparable 
{
    //holds the get to bus stop tick
    private int firstTicks;
    //holds the type of object to create
    private Object objectType;
    
    //constructor
    public BlankEvent(int firstTicks, Object objectType)
    {
        //set the time to run
        this.firstTicks = firstTicks;
        //set the type of object to create
        this.objectType = objectType;
    }
    
    //get the object time
    public int GetFirstTick()
    {
        return firstTicks;
    }
    
    //get the object to create
    public Object GetObjectTypeToCreate()
    {
        return objectType;
    }
    
    //implements the comparable interface
    public int compareTo(Object tmp )
    {
        int result;
        
        if (tmp instanceof Person)
            result = (firstTicks < ((Person)tmp).GetFirstTicks() ? -1 : (firstTicks == ((Person)tmp).GetFirstTicks() ? 0 : 1));
        else if (tmp instanceof BlankEvent)
            result = (firstTicks < ((BlankEvent)tmp).GetFirstTick() ? -1 : (firstTicks == ((BlankEvent)tmp).GetFirstTick() ? 0 : 1));
        else
            result = (firstTicks < ((Bus)tmp).GetFirstTicks() ? -1 : (firstTicks == ((Bus)tmp).GetFirstTicks() ? 0 : 1));
        
    return result;
    }
}

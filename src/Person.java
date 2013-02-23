
/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines a person. It contains their attributes needed for the 
 * simulation, and the actions that can be performed by any person.
 */
public class Person implements Comparable
{
    //holds the priority of the person
    int priority;
    //holds the get to bus stop tick
    private int firstTicks;
    //holds the on bus tick
    private int onBusTicks;
    //holds the off bus tick
    private int offBusTicks;
    //holds the bus stop to wait for the bus
    private int startBustop;
    //holds the bus stop to get off the bus
    private int stopBustop;
    //wait time on the stop
    private int waitTime;
    //the number of time(s) person left by the bus because of bus capacity
    private int leftByBus;
    //hold person number
    int personNumber;
    
    
    //default constructor
    public Person()
    {
        
    }
    
    //constructor for a person
    public Person(int firstTicks, int priority)
    {
        //set the time the person should be sent to the bus stop
        this.firstTicks = firstTicks;  
        //set the priority of the person
        this.priority = priority;
        //set the passenger wait time
        this.waitTime = 0;
        //set the number of times person left by the bus
        this.leftByBus = 0;
        
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
    
    //set person number
    public void SetPersonNumber(int personNumber)
    {
        this.personNumber = personNumber;
    }
    
    //get person number
    public int GetPersonNumber()
    {
        return personNumber;
    }
    
    //set the number of times a person was left by the bus
    public void IncrementLeftByBus()
    {
        this.leftByBus += 1;
    }
    
    //get the number of times a person was left by the bus
    public int GetLeftByBus()
    {
        return leftByBus;
    }
    
    //set the wait time for the bus
    public void SetWaitTime(int waitTime)
    {
        //set the wait time
        this.waitTime = waitTime;
    }
    
    //get the wait time a person waited for the bus
    public int GetWaitTime()
    {
        //get the wait time
        return waitTime;
    }
    
    //set the person priority for the bus stop
    public void SetPriority(int priority)
    {
        //set the priority of the person for the stop
        this.priority = priority;
    }
    
    //get the person priority for the bus stop
    public int GetPriority()
    {
        //return the priority of the person for the stop
        return priority;
    }
    
    //set the tick time the person got to the stop for the bus
    public void SetFirstTicks(int firstTicks)
    {
        //set the tick time the person got to the stop for the bus
        this.firstTicks = firstTicks;
    }
    
    //get the tick time the person got to the stop for the bus
    public int GetFirstTicks()
    {
        //get the tick time the person got to the stop for the bus
        return firstTicks;
    }
        
    //set the tick time the person got on the bus
    public void SetOnBusTicks(int onBusTicks)
    {
        //set the tick time person got on the bus
        this.onBusTicks = onBusTicks;
    }
    
    //get the tick time the person got on the bus
    public int GetOnBusTicks()
    {
        //get the tick time person got on the bus
        return onBusTicks;
    }
    
    //set the tick time the person got off the bus
    public void SetOffBusTicks(int offBusTicks)
    {
        //set the tick time person got off the bus
        this.offBusTicks = offBusTicks;
    }
    
    //get the tick time the person got off the bus
    public int GetOffBusTicks()
    {
        //get the tick time person got off the bus
        return offBusTicks;
    }
    
    //set the person start bus stop
    public void SetStartBusStop(int startBusStop)
    {
        //set the start bus stop
        this.startBustop = startBusStop;
    }
    
    //get the person start bus stop
    public int GetStartBusStop()
    {
        //get the person start bus stop
        return startBustop;
    }
    
    //set the person stop bus stop
    public void SetStopBusStop(int stopBusStop)
    {
        //set the person stop bus stop
        this.stopBustop = stopBusStop;
    }
    
    //get the person stop bus stop
    public int GetStopBusStop()
    {
        //get the person stop bus stop
        return stopBustop;
    }
    
    //get the person travel time
    public int GetTravelTime()
    {
        //return the travel time
        return this.offBusTicks - this.onBusTicks;
    }
}

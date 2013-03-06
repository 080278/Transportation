
/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines a bus, It contains their attributes needed for the 
 * simulation, and the actions that can be performed by any bus.
 */

import java.util.*;


public class Bus implements Comparable
{
    //holds the verbose status
    boolean verbose;
    
    //holds the persons on the bus
    LinkedList <Person> passengers;
    //holds the get to first bus stop tick
    private int firstTicks;
    //holds the get to the last bus stop tick
    private int toLastBusStopTicks;
    //holds the number of passengers
    private int passengersOnBoard;
    //holds total passangers carried per trip
    private int totalPassengersTransported;
    //holds accumulated passangers carried
    private int accumulatedPassangers;
    //holds the capacity of the bus
    private int busCapacity;
    //holds the bus current stop
    private BusStop currentBusStop;
    //holds the next bus stop to visit
    private int nextBusStop;
    //holds minimum passangers carried
    private int minimumPassenger;
    //holds maximum passangers carried
    private int maximumPassenger;
    //holds empty trip flag
    private boolean empty;
    //holds trips on route
    private int trips;
    //holds empty trips
    private int emptyTrips;
    //persons left because the bus is full
    private int totalLeftAtBusStop;
    //holds the bus number
    private int busNumber;
    
    //default constructor
    public Bus()
    {
        //set bus Number 
        busNumber = 0;
    }
    
    //constructor for a bus
    public Bus(int firstTicks, int busCapacity, int busNumber,boolean verbose)
    {
        //set the verbose status
        this.verbose = verbose;
        //set empty flag
        this.empty = true;
        //set bus Number 
        this.busNumber = busNumber;
        //set the time the bus get to the first stop on route
        this.firstTicks = firstTicks;
        //set the passangers on board
        passengersOnBoard = 0;
        //set the total passengers transported
        totalPassengersTransported = 0;
        //set the accumulated passengers transported
        this.accumulatedPassangers = 0;
        //set the bus capacity
        this.busCapacity = busCapacity;
        //initialize the passenger manifest
        passengers = new LinkedList<Person>();
        //set the current bus stop
        nextBusStop = 0;
        //set minimum passanger
        minimumPassenger = Integer.MAX_VALUE;
        //set maximum passanger
        maximumPassenger = Integer.MIN_VALUE;
        //set empty trips
        emptyTrips = 0;
        //set left at bus stop
        totalLeftAtBusStop = 0;
    }
    
    //set empty flag
    public void SetEmpty(boolean empty)
    {
        this.empty = empty;
    }
    
    //get empty flag
    public boolean GetEmpty()
    {
        
        return empty;
    }
    
    //get bus number
    public int GetBusNumber()
    {
        return busNumber;
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
        //return (firstTicks < ((BlankEvent)tmp).firstTicks ? -1 : (firstTicks == ((BlankEvent)tmp).firstTicks ? 0 : 1));
    }
    
    //set left at bus stop
    public void SetTotalLeftAtBusStop(int leftAtBusStop)
    {
        //increment counter of person left at a stop
        this.totalLeftAtBusStop += leftAtBusStop;
    }
    
    //get left at bus stop
    public int GetTotalLeftAtBusStop()
    {
        //get person left at a stop
        return totalLeftAtBusStop;
    }
    
    //increment empty trips
    public void IncrementEmptyTrips()
    {
        emptyTrips += 1;
    }
    
    
    //get trips on route
    public int GetTrips()
    {
        return trips;
    }
    
    //get empty trips
    public int GetEmptyTrips()
    {
        return emptyTrips;
    }
    
    //set the minimum passengers carried
    public void SetMinimumPassenger(int minimumPassenger)
    {
        if(minimumPassenger < this.minimumPassenger)
            this.minimumPassenger = minimumPassenger;
    }
    
    //get the minimum passengers carried
    public int GetMinimumPassenger()
    {
        return minimumPassenger;
    }
    
    //set the maximum passengers carried
    public void SetMaximumPassenger(int maximumPassenger)
    {
        if(maximumPassenger > this.maximumPassenger)
            this.maximumPassenger = maximumPassenger;
    }
    
    //get the maximum passengers carried
    public int GetMaximumPassenger()
    {
        return maximumPassenger;
    }
    
    
    //set the nextBusStop
    public void SetNextbusStop(int nextBusStop)
    {
        //set the next bus stop to visit
        this.nextBusStop = nextBusStop;
    }
    
    //get the currentBusStop
    public BusStop GetCurrentbusStop()
    {
        //get the current bus stop
        return currentBusStop;
    }
    
    //get the nextBusStop
    public int GetNextbusStop()
    {
        //get the next bus stop to visit
        return nextBusStop;
    }
    
    //set the tick time the bus got to the a bus stop
    public void SetFirstTicks(int firstTicks)
    {
        //set the tick time the bus got to a bus stop
        this.firstTicks = firstTicks;
    }
    
    //get the tick time the bus got to the a bus stop
    public int GetFirstTicks()
    {
        //get the tick time the bus got to the first bus stop
        return firstTicks;
    }
    
    //set the tick time the bus got to the last bus
    public void SetToLastBusStopTicks(int toLastBusStopTicks)
    {
        //set the tick time bus got to the last bus stop
        this.toLastBusStopTicks = toLastBusStopTicks;
    }
    
    //get the tick time the bus got to the last bus stop
    public int GetToLastBusStopTicks()
    {
        //get the tick time person got off the bus
        return toLastBusStopTicks;
    }
    
    //get the bus travel time
    public int GetTravelTime()
    {
        //return the travel time
        return this.toLastBusStopTicks - this.firstTicks;
    }
    
    //increment passengers onboard the bus
    public boolean Increment()
    {
        //set the status flag of onboarding a passenger
        boolean status = false;
        
        //ensure passangers onboard is within the bus capacity
        if ((passengersOnBoard + 1) <= busCapacity)
        {
            
            //increment passenger onboard
            passengersOnBoard += 1;
            
            //set the status flag for the boarding of a passanger
            status = true;
        }
        
        //return the status of onboardinng a passenger
        return status;
    }
    
    //increment total passengers transported on the bus
    public void IncrementTotalPassangersTransported()
    {
        //increment passenger onboard
        totalPassengersTransported += 1;
    }
    
    //get accumulated passengers transported on the bus
    public int GetAccumulatedPassangersTransported()
    {
        return accumulatedPassangers;
    }
    
    //get total passengers transported on the bus
    public int GetTotalPassangersTransported()
    {
        //copy the amount transported
        int tmp=totalPassengersTransported;
        //holds the running total
        accumulatedPassangers += totalPassengersTransported;
        //reset total passangers transported
        totalPassengersTransported = 0;
        
        return tmp;
    }
    
    
    //get number of passangers on board
    public int GetPassengerQuantity()
    {
        
        return passengersOnBoard;
    }
    
    //decrement passengers onboard the bus
    public boolean Decrement()
    {
        //set the status flag of offboarding a passanger
        boolean status = false;
        
        //ensure there are passengers on the bus
        if ((passengersOnBoard - 1) >= 0)
        {
            //decrement passenger onboard
            passengersOnBoard -= 1;
            //set the status flag for the off boarding of a passanger
            status = true;
        }
        
        //return the status of offboarding a passenger
        return status;
    }
    
    //get the capacity of the bus
    public int GetBusCapacity()
    {
        //get the capacity of the bus
        return busCapacity;
    }
    
    //get the bus passanger list iterator
    public ListIterator GetPassangerIterator()
    {
        //get the listing of passengers
        return passengers.listIterator();
    }
    
    //get passangers on the bus
    public int Count()
    {
        //get the number of passangers on the bus
        return passengers.size();
    }
    
    //onboarding of passangers
    public void PassengersOnBoarding(BusStop busStop,OutputFile screen)
    {
        //set the current bus stop
        currentBusStop = busStop;
        //set the average persons for the bus stop
busStop.SetAverageNumberOfPersons();
        //set the average person wait time
        busStop.SetAveragePersonsWaitTime(this);  //check
        
        //person getting on the bus
        Person got;
        while((busStop.Count() > 0) && (Increment()))
        {
            got = busStop.GetPerson();
            //set time person got on the bus                
            got.SetOnBusTicks(this.firstTicks); 
            //set person wait time
            got.SetWaitTime(this.GetFirstTicks()-got.GetFirstTicks());
            passengers.add(got);
//set empty flag to false
this.SetEmpty(false);
            
screen.WriteToFile("Time:,"+GetFirstTicks()+", --> ON"+", GetOn: "+ (got.GetStartBusStop()+1)+", Bus #"+GetBusNumber()+", person #"+(got.GetPersonNumber()+1)+", GetOff: "+ (got.GetStopBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());            
if(verbose)
    System.out.println("Time:,"+GetFirstTicks()+", --> ON"+", GetOn: "+ (got.GetStartBusStop()+1)+", Bus #"+GetBusNumber()+", person #"+(got.GetPersonNumber()+1)+", GetOff: "+ (got.GetStopBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());
        }
        //get iterator for the BusStop
        Iterator iter = busStop.GetIterator();
        //update person left at stop
        while(iter.hasNext())
        {
            ((Person)iter.next()).IncrementLeftByBus();
        }
        
        //indicate how much left at the stop
        SetTotalLeftAtBusStop(busStop.Count());
        //set minimum capacity
        this.SetMinimumPassenger(this.GetPassengerQuantity());
        //set maximum capacity
        this.SetMaximumPassenger(this.GetPassengerQuantity());
    }
    
    //offboarding of passangers
    public void PassengersOffBoarding(BusStop busStop,OutputFile outBusStopActivity,OutputFile screen)
    {
        //person getting off the bus
        Person got;
        //linklist iterator
        ListIterator iter = passengers.listIterator();

        //iterate all passangers on the bus
        while(iter.hasNext())
        {
            //get current person selected on the bus
            got = (Person)iter.next();
            
            //check if the person needs to get off the bus
            if(got.GetStopBusStop() == busStop.GetBusStopNumber())
            {
//set time person got off the bus                
got.SetOffBusTicks(this.firstTicks);  
screen.WriteToFile("Time:,"+GetFirstTicks()+", --> OFF"+", GetOff: "+ (got.GetStopBusStop()+1)+", Bus #"+GetBusNumber()+", person #"+got.GetPersonNumber()+", GetOn: "+ (got.GetStartBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());
if(verbose)
    System.out.println("Time:,"+GetFirstTicks()+", --> OFF"+", GetOff: "+ (got.GetStopBusStop()+1)+", Bus #"+GetBusNumber()+", person #"+(got.GetPersonNumber()+1)+", GetOn: "+ (got.GetStartBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());
//System.out.println("Time:"+GetFirstTicks()+" Bus #"+GetBusNumber()+", PASSENGERS: "+ passengers.size()+", person #"+got.GetPersonNumber()+", GetOff: "+ busStop.GetBusStopNumber());                
//write statistics to file
outBusStopActivity.WriteToFile((got.GetPersonNumber()+1)+","+got.GetFirstTicks()+","+(got.GetStartBusStop()+1)+","+(got.GetStopBusStop()+1)+","+got.GetOnBusTicks()+","+got.GetOffBusTicks()+","+got.GetWaitTime()+","+got.GetLeftByBus());

                //remove passenger
                iter.remove();
                //update total transported
                IncrementTotalPassangersTransported();
                //update passengersOnBoard counter
                Decrement();
                
            }
        }
    }
    
    //offboarding of passangers, onStop == true  drop on stop
    //                           onStop == false kick everyone off the bus
    public void PassengersKickedOff(OutputFile outBusStopActivity,OutputFile screen)
    {

        //check if trip was empty
        if(GetEmpty() == true)
        {
            //increment empty trip counter
            IncrementEmptyTrips();
        }
        

        //counting of trips
        trips += 1;
        //person getting off the bus
        Person got;
        //linklist iterator
        ListIterator iter = passengers.listIterator();
        
        
        
        
        //iterate all passangers on the bus
        while(iter.hasNext())
        {
// ADD CODE TO GET PERSON STATISTICS HERE
            //get current person selected on the bus
            got = (Person)iter.next();
//set time person got off the bus                
got.SetOffBusTicks(this.firstTicks);   
screen.WriteToFile("Time:,"+GetFirstTicks()+", --> END OF ROUTE"+" Bus #"+GetBusNumber()+", person #"+(got.GetPersonNumber()+1)+", GetOn: "+ (got.GetStartBusStop()+1)+", GetOff: "+ (got.GetStopBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());
if(verbose)
    System.out.println("Time:,"+GetFirstTicks()+", --> END OF ROUTE"+" Bus #"+GetBusNumber()+", person #"+(got.GetPersonNumber()+1)+", GetOn: "+ (got.GetStartBusStop()+1)+", GetOff: "+ (got.GetStopBusStop()+1)+", Priority: "+got.GetPriority()+", PASSENGERS: "+ passengers.size());
//System.out.println("Time: "+GetFirstTicks()+", PASSENGERS-> Kick: "+ passengers.size()+", person #"+got.GetPersonNumber()+", Getoff: "+ (got.GetStopBusStop()+1)+", Priority: "+got.GetPriority()+", waitTime: "+got.GetWaitTime());                
outBusStopActivity.WriteToFile((got.GetPersonNumber()+1)+","+got.GetFirstTicks()+","+(got.GetStartBusStop()+1)+","+(got.GetStopBusStop()+1)+","+got.GetOnBusTicks()+","+got.GetOffBusTicks()+","+got.GetWaitTime()+","+got.GetLeftByBus());
            //drop passengers
            iter.remove();
            //update total transported
            IncrementTotalPassangersTransported();
            //update passengersOnBoard counter
            Decrement();
        }
        
    }
    
    //bus completes the route
    public void BusJourneyComplete()
    {
        /*
         * robert add your journey completion code here
          
         */
        
        /*
         * This is clean up code for the bus in preperation for the next trip
         */
        //clear the passangers on board
        this.passengersOnBoard = 0;
    }
}

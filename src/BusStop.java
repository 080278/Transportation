/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines a busstop. It contains the attributes needed for the 
 * simulation, and the actions that can be performed by any busstop.
 */
import java.util.*;


public class BusStop {
    //bustop number
    private int busStopNumber;
    //priority queue for the bus stop
    private PriorityQueue<Person> busStop;
    //priority at stop
    private int priority;
    //minimum passenger at stop
    private int minimumPassenger;
    //maximum passenger at stop
    private int maximumPassenger;
    //average number of people waiting on the stop
    private int averageNumberOfPersons;
    //average wait time 
    private int averagePersonsWaitTime;
    //holds total persons that waited on this stop
    private int totalPersonsWaited;
    
    
    //constructor to create a bus stop
    public BusStop(int busStopNumber)
    {
        //initialize the bus stop
        busStop = new PriorityQueue<Person>();
        //set the initial priority at stop
        priority = 0;
        //set the bus stop number
        this.busStopNumber = busStopNumber;
        //set minimum passenger
        this.minimumPassenger = Integer.MAX_VALUE;
        //set maximum passenger
        this.maximumPassenger = Integer.MIN_VALUE;
        //set the average number of person on the stop
        averageNumberOfPersons = 0;
        //set the average person wait time
        averagePersonsWaitTime = 0;
    }
    
    //increment total persons waited on the stop
    public void IncrementTotalPersonsWaited()
    {
        totalPersonsWaited += 1;
    }
    
    //get the total persons waited on the stop
    public int GetTotalPersonsWaited()
    {
        return totalPersonsWaited;
    }
    
    //set the average number of persons, when the bus arrived
    public void SetAverageNumberOfPersons()
    {
        if(averageNumberOfPersons != 0)
        {
            if(Count() !=0)
            {
                averageNumberOfPersons = (averageNumberOfPersons + Count())/2;
            }
//System.out.println("Bus-Stop: " +this.busStopNumber+", Average number of Persons: "+averageNumberOfPersons);                    
        }
        else
        {
            if(Count() != 0)
            {
                averageNumberOfPersons = Count();
            }
//System.out.println("Bus-Stop: " +this.busStopNumber+", Average number of Persons: "+averageNumberOfPersons);                    
        }
    }
    
    //get the average number of persons, when the bus arrived
    public int GetAverageNumberOfPersons()
    {
        return averageNumberOfPersons;
    }
    
    //set the average person wait time
    public void SetAveragePersonsWaitTime(Bus currentBus)
    {
        /*
         * Get the bus arrival time, for each person 
         * (Bus 'firstTicks' - passenger 'firstTicks'), sum all difference,
         * and find the average
         */
        //holds total wait times of persons
        int totalPassengerWaitTimes = 0;
        //get the bus stop iterator
        Iterator iter = busStop.iterator();
        //iterate the bus stop
        while(iter.hasNext())
        {
            //get the person
            Person per = ((Person)iter.next());
            
            //sum up each person wait time
            totalPassengerWaitTimes += 
                    (currentBus.GetFirstTicks()-per.GetFirstTicks());
//System.out.println("Bus firstTick: "+currentBus.GetFirstTicks()+", Person firstTick: "+per.GetFirstTicks());
        }
        
        if(Count()>0)
        {
            //get the average of wait times
            int tmpAverage = (totalPassengerWaitTimes / Count());

            if(averagePersonsWaitTime != 0)
            {
                //accumulate the old and new averages and find the average
                averagePersonsWaitTime = 
                        (averagePersonsWaitTime + tmpAverage)/2;
            }
            else
            {
                //first average generated
                averagePersonsWaitTime = tmpAverage;
            }
        
//System.out.println("Average person wait time: "+averagePersonsWaitTime);
        }
    }
    
    //get the average person wait time
    public int GetAveragePersonsWaitTime()
    {
        return averagePersonsWaitTime;
    }
    
    //set the minimum passenger at bus stop
    public void SetMinimumPassenger(int minimumPassenger)
    {
        if(minimumPassenger < this.minimumPassenger)
            this.minimumPassenger = minimumPassenger;
    }
    
    //get the minimum passenger at bus stop
    public int GetMinimumPassenger()
    {
        return minimumPassenger;
    }
    
    //set the maximum passenger at bus stop
    public void SetMaximumPassenger(int maximumPassenger)
    {
        if(maximumPassenger > this.maximumPassenger)
            this.maximumPassenger = maximumPassenger;
    }
    
    //get the maximum passenger at bus stop
    public int GetMaximumPassenger()
    {
        return maximumPassenger;
    }
    
    
    /*
    //get the bus stop
    public PriorityQueue<Person> GetBusStop()
    {
        return busStop;
    }
    */
    /*
    //set bus stop number
    public void SetBusStopNumber(long busStopNumber)
    {
        //Set bus stop number
        this.busStopNumber = busStopNumber;
    }
    */
    //get bus stop number
    public int GetBusStopNumber()
    {
        //get bus stop number
        return busStopNumber;
    }
    
    //update last person priority level at the stop
    public void IncrementPriority()
    {
        //increment priority level at the stop
        priority += 1;
    }
    
    //add a person to the bus stop
    public void AddPerson(Person tmp)
    {
        
        //set new priority
        IncrementPriority();
        //set the person priority at the stop
        tmp.SetPriority(priority);
        //put a person on the stop
        busStop.add(tmp);
        //set minimum passangers
        this.SetMinimumPassenger(busStop.size());
        //set maximum passangers
        this.SetMaximumPassenger(busStop.size());
    }
    
    //get the number of passangers waiting
    public int Count()
    {
        //get the number of person on the stop
        return ((int)busStop.size());
    }
    
    //get the iterator for the Bus Stop
    public Iterator GetIterator()
    {
        return busStop.iterator();
    }
    
    //get the person at the front of the priority queue
    public Person GetPerson()
    {
        //first person in line
        Person tmp = null;
        
        if(Count() != 0)
        {
            //pickup priority person
            tmp = busStop.poll();  
            if(Count() == 0)
            {
                //reset priority
                priority = 0;
            }
        }
        
        
        //person going to the bus
        return tmp;
    }
    
       
    
    
    public static void main(String[] args)
    {
        BusStop bstop = new BusStop(5);
        
        
            bstop.AddPerson(new Person(4,9));
            bstop.AddPerson(new Person(1,12));
            bstop.AddPerson(new Person(2,2));
            bstop.AddPerson(new Person(3,1));
            bstop.AddPerson(new Person(5,6));
            bstop.AddPerson(new Person(1,11));
            bstop.AddPerson(new Person(2,5));
            bstop.AddPerson(new Person(3,4));
            bstop.AddPerson(new Person(4,7));
            bstop.AddPerson(new Person(5,3));
        
        
        if(bstop.Count() != 0)
        {
            System.out.println("persons count: "+bstop.Count());
            Person got = bstop.GetPerson();
            System.out.println("person #"+got.GetFirstTicks()+" persons count: "+bstop.Count());
            got = bstop.GetPerson();
            System.out.println("person #"+got.GetFirstTicks()+" persons count: "+bstop.Count());
        }
        else
        {
            System.out.println("Bustop empty");
        }
    }
}

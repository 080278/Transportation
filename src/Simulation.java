/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines the simulation main application. It contains the attributes needed for the 
 * simulation, and the actions that can be performed by the simulation.
 */

/*
 * 1. When a BUS has completed the route it is sent to PRIORITY, finishQueue
 * 2. 
 */
/*
 * When a bus stop is not selected 
 * selectedBusStop = -1
 * Ounce all passanger(s) have been collected and delivered to a stop, if
 * there are busses still on route without passangers the simulation stops
 * 
 * The total number of busses is accured in : Simulation.java
 */
/*
 * THINGS THAT NEEDS TO BE DONE
 * 
 * 1. implement the distribution algorithms in: GetNextSelectedBusStop()
 * 2. 
 * 3. 
 * 4. GetNextSelectedBusStop() Needs to handle the various distribution types 
 *    here for persons arrival at the bus stop    
 * 5. 
    
    public int  
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Simulation {
    //holds verbose status
    boolean verbose = false;
    //holds the seed value
    long seed;
    //start bustop random generator
    Random rndStart;
    //stop bustop random generator
    Random rndStop;
    //people Temp exponential random generator
    Random tmpExp;
            
    //holds the Person CREATED events
    OutputFile outPerson;
    //holds the Bus CREATED events
    OutputFile outBus;
    //holds the BusStop CREATED events
    OutputFile outBusStop;
    //holds the BusStop ACTIVITY events
    OutputFile outBusStopActivity;
    //holds the SCREEN display
    OutputFile screen;
    //holds the summary display
    OutputFile summary;
    
    //holds the Bus-Stop summary
    OutputFile summaryBusStop;
    //holds the Bus summary 
    OutputFile summaryBus;
    
    //holds the configuration for the simulation
    private static String[] config;
    //holds total number of bus stops
    private static int totalBusStops;
    //private bus numbering system
    private static int busNumber;
    //holds total population
    private static int totalPopulation;
    //holds total transported
    private static int totalTransported;
    //holds person arrival interval
    private static int personArrivalInterval;
    //holds distribution of people at the bus stops
    private static int[] distPeopleAtBusStops;
    //holds bus arrival interval
    private static int[] busArrivalInterval;
    //holds bus capacity
    private static int busCapacity;
    //holds bus stop tick distance
    private static int[] busStopTickDistances;
    //holds peaks travel times
    private static int[] peakTravelTimes;
    
    
    //the bus stop the next person created needs to be delivered to
    private int selectedBusStop;
    //the bus stop the person stops at
    private int stopBusStop;
    
    //new busses created
    private int bussesCreated;
    //new person created
    private int personsCreated;
    
    /*
     * Simulation timer
     */
    public int time;
    
    /*
     * Priority Ready Queue
     */
    private PriorityQueue<Object> readyQueue;
    /*
     * Priority Ready Queue
     */
    private PriorityQueue<Object> finishQueue;
    
    
    /*
     * ENTITIES
     */
    //busstops
    BusStop [] busStop;
    //Person's Arrival
    Exponential [] peopleArrival;
    
    
    //constructor
    public Simulation(String[] config, boolean verbose)
    {
        //set the output Person file
        outPerson = new OutputFile("personCreated.txt");
        //set the output BusStop file
        outBusStop = new OutputFile("busstopCreated.txt");
        //set the output Person file
        outBus = new OutputFile("busCreated.txt");
        //set the output BusStop file
        outBusStopActivity = new OutputFile("busstopActivity.txt");
        //set the output BusStop file
        screen = new OutputFile("output.txt");
        //set the summary file
        summary = new OutputFile("summary.txt");
        //set the Bus-Stop summary file
        summaryBusStop = new OutputFile("summaryBusStop.txt");
        //set the Bus summary file
        summaryBus = new OutputFile("summaryBus.txt");
        
        //person file header
        outPerson.WriteToFile("Time, Person, Bus-Stop, Arrival Interval, Arrival time ");
        //Busstop file header
        outBusStop.WriteToFile("BusStop");
        //Bus file header
        outBus.WriteToFile("Time, Bus");
        //person file header
        outBusStopActivity.WriteToFile("Person,Arrival-BusStop,Start-BusStop,Stop-BusStop,TimeonBus,TimeoffBus,WaitTime,NumberOfTimesLeftbyBus");
        
        //Bus-Stop file header
        summaryBusStop.WriteToFile("Bus-Stop, Avg-Person, Avg-WaitTime, Min-Per(s), Max-Per(s)");
        //Bus file header
        summaryBus.WriteToFile("Bus, Trips, Empty-Trip(s), Left-At-BusStop, Transported, Min-Per(s), Max-Per(s)");
        
        //set verbose
        this.verbose = verbose;
        //set time
        time = 0;
        //set selected bus stop to none
        selectedBusStop = -1;
        //set the bus number
        busNumber = 0;
        //set the configuration list
        this.config = config;
        
        try
        {
            //configure the simulation
            ConfigSetup();
            //create all BusStops, 
            CreateEntities();
            //start Simulation
            RunSimulation();
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }
        
        
    }
//******************************************************************************
//                          BUS-STOP Selection
    public int GetNextSelectedBusStop() 
    {
         
        //set a random bus stop to Start at
        SetSelectedBusStop(rndStart.nextInt(busStop.length));
//System.out.println("Start: "+(GetSelectedBusStop()+1));
        //return the stop chosen
        return GetSelectedBusStop();
    }

    
    public int GetNextStopBusStop() 
    {

     
        //set a random bus stop to get off at
        SetStopBusStop((GetSelectedBusStop()+1)+rndStop.nextInt(busStop.length-GetSelectedBusStop()));
        //SetStopBusStop(rndStop.nextInt(busStop.length+1)+GetSelectedBusStop());
//System.out.println("Stop: "+(GetStopBusStop()+1))        ;
        //return the stop chosen
        return GetStopBusStop();
    }
//******************************************************************************    
    
    
    
    //get verbose
    public boolean GetVerbose()
    {
        return verbose;
    }
    
    //get time
    public int GetTime()
    {
        return time;
    }
    
    //increment number of persons created
    public void IncrementPersonsCreated()
    {
        personsCreated += 1; 
    }
    
    //get the number of persons created
    public int GetNumberOfPersonsCreated()
    {
        return personsCreated;
    }
    
    
    //increment number of busses created
    public void IncrementBussesCreated()
    {
        bussesCreated += 1; 
    }
    
    //get the number of busses created
    public int GetNumberOfBussesCreated()
    {
        return bussesCreated;
    }
    
    //set selected bus stop
    private void SetSelectedBusStop(int selectedBusStop)
    {
        this.selectedBusStop = selectedBusStop;
    }
    
    //set stop bus stop
    private void SetStopBusStop(int stopBusStop)
    {
        this.stopBusStop = stopBusStop;
    }
    
    //get selected bus stop
    private int GetSelectedBusStop()
    {
        return selectedBusStop;
    }
    
    //get stop bus stop
    private int GetStopBusStop()
    {
        return stopBusStop;
    }
    
    //create entities
    public void CreateEntities()
    {
        //initialize random start Bus-Stop
        rndStart = new Random(seed);
        //initialize random stop Bus-Stop
        rndStop = new Random(seed);
        //initialize random used for the Exponential distribution
        tmpExp = new Random(seed);
        
        //create the bus stops
        busStop = new BusStop[totalBusStops];    
        //create paeole at bus stops arrival distibution
        peopleArrival = new Exponential[totalBusStops];
        
        //generate distribution total
        int total=0;
        for(int x=0; x< totalBusStops; x++)
        {
            //add each distribution number
            total += distPeopleAtBusStops[x];
        }
        
        //instantiate each bus stop
        for(int x=0; x< totalBusStops; x++)
        {
            //initialize each bus stop
            busStop[x] = new BusStop(x);
            //initialize each bus stop arrival mean at person creation interval
            peopleArrival[x] = new Exponential(personArrivalInterval, tmpExp);
            
            if((x+1) != totalBusStops)
            {
                //set distribution for the total - 1 distributions
                distPeopleAtBusStops[x] = (int)Math.ceil((double)totalPopulation*((double)distPeopleAtBusStops[x]/(double)total));
            }
            else
            {
                //generate distribution difference for the last distribution
                int tot=0;
                for(int x1=0; x1< totalBusStops-1; x1++)
                {
                    //add each distribution number
                    tot += distPeopleAtBusStops[x1];
                }
                //set the last distribution to the remaining persons
                distPeopleAtBusStops[x] = totalPopulation-tot;
            }
            //log Bus Stop created
            outBusStop.WriteToFile(String.valueOf(x+1));
        }
    }
    
    //get an array of data
    public void ConfigSetup() throws Exception
    {   
        //set default for seed specified
        boolean seedSpecified = false;
        
        //iterate options
        for(int x=0;x<config.length;x++)
        {
            //split each value
            String[] s = config[x].split(" ");
            
            if(s.length>1)
            {
                switch (s[0].charAt(0))
                {
                    case 'B':
                        break;
                    case 'P':
                        break;
                    case 'p':
                        break;
                    case 'D':
                        break;
                    case 'b':
                        break;
                    case 'C':
                        break;
                    case 'M':
                        break;
                    case 'T':
                        break;
                    case 'S':
                    {
                        seedSpecified = true;
                        break;
                    }
                    default:
                        throw new Exception("Invalid Option specified");
                }
            }
        }
        
        if(config.length >= 9)
        {
            //iterate options
            for(int x=0;x<config.length;x++)
            {
                //split each value
                String[] s = config[x].split(" ");
                //if not blank array
                if(s[0].compareTo("B") == 0)
                {
                    //ensure that Total number of bus stops are included
                    if(s.length-1 == 1)
                    {
                        //get total bus stops
                        totalBusStops = Integer.parseInt(s[1]);
                    }
                    else
                    {
                        throw new Exception("Check single integer eg. 'B 12' for: Total number of bus stops(single number)");
                    }
                }
                else if(s[0].compareTo("P") == 0)
                {
                    //ensure that Total population of simulation is included
                    if(s.length-1 == 1)
                    {
                        //get total population
                        totalPopulation = Integer.parseInt(s[1]);
                    }
                    else
                    {
                        throw new Exception("Check single integer eg. 'P 12000' for: Total population(single number)");
                    }
                }
                else if(s[0].compareTo("p") == 0)
                {
                    //ensure that person arrival interval is included
                    if(s.length-1 == 1)
                    {
                        //get person arrival interval
                        personArrivalInterval = Integer.parseInt(s[1]);
                    }
                    else
                    {
                        throw new Exception("Check single integer eg. 'p 5' for: Person arrival interval(single number)");
                    }
                }
                else if(s[0].compareTo("D") == 0)
                {
                    //ensure that Distribution of people at bus stop is included
                    if(s.length-1 == totalBusStops)
                    {
                        //set distribution of people at the bus stops
                        distPeopleAtBusStops = new int[s.length-1];
                        //put values in range list
                        SetRanges(s,distPeopleAtBusStops,s.length);
                    }
                    else
                    {
                        throw new Exception("Check Range of integer eg. 'D 5 7 200 etc' for: "
                                + "Distribution of people at bus stops(range of distribution values). "
                                + "Number of ranges must equal to the number of Bus-Stops.");
                    }
                }
                else if(s[0].compareTo("b") == 0)
                {
                    //ensure that Bus arrival interval is included
                    if(s.length-1 == 3)
                    {
                        //set bus arrival interval
                        busArrivalInterval = new int[s.length-1];
                        //put values in range list
                        SetRanges(s,busArrivalInterval,s.length);
                    }
                    else
                    {
                        throw new Exception("Check Range of integer eg. 'b 5 7 200 etc' for: "
                                + "Bus arrival interval(three numbers: peak, slow period, peak)"
                                +" range of distribution values.");
                    }
                }
                else if(s[0].compareTo("C") == 0)
                {
                    //ensure that Bus capacity is included
                    if(s.length-1 == 1)
                    {
                        //get bus capacity
                        busCapacity = Integer.parseInt(s[1]);
                    }
                    else
                    {
                        throw new Exception("Check single integer eg. 'C 5' for: Bus capacity(single number)");
                    }
                }
                else if(s[0].compareTo("M") == 0)
                {
                    //ensure that Distribution of people at bus stop is included
                    if(s.length-1 == totalBusStops)
                    {
                        //set bus stop tick distance
                        busStopTickDistances = new int[s.length-1];
                        //put values in range list
                        SetRanges(s,busStopTickDistances,s.length);
                    }
                    else
                    {
                        throw new Exception("Check Range of integer eg. 'M 5 7 200 10 etc' for: "
                                + "Bus-Stop movement interval(range of numbers). "
                                + "Number of ranges must equal to the number of Bus-Stops.");
                    }
                }
                else if(s[0].compareTo("T") == 0)
                {
                    //ensure that Bus arrival interval is included
                    if(s.length-1 == 3)
                    {
                        //set peaks travel times
                        peakTravelTimes = new int[s.length-1];
                        //put values in range list
                        SetRanges(s,peakTravelTimes,s.length);
                     }
                    else
                    {
                        throw new Exception("Check Range of integer eg. 'T 500 1700 6000 etc' for: "
                                + "Time range tick(three numbers: peak, slow period, peak)"
                                +" range of distribution values. Each time specify the three "
                                + "peak, slow period, peak times");
                    }

                }
                else if(s[0].compareTo("S") == 0)
                {
                    //ensure that seed value for the random generator is included
                    if(s.length-1 == 1)
                    {
                        //get seed value
                        seed = Integer.parseInt(s[1]);
                    }
                    else
                    {
                        throw new Exception("Check single integer eg. 'S 123456' for: Seed Value(single number)");
                    }
                }
                
                
            }
        }
        else
        {
            throw new Exception("Error in the config file. ");
        }
        
        //create a seed value, if seed not specified
        if(seedSpecified == false)
        {
            //default behavior for random seed generation
            seed = System.currentTimeMillis();
        }
    }
    
    //put the range in the array
    private void SetRanges(String[] strRange, int[] range, int length)
    {
        for(int x=1; x<length; x++)
            //convert string to number
            range[x-1] = Integer.parseInt(strRange[x]);
    }
    
    //run the simulation
    public void RunSimulation()
    {
        
        
        //initialize the ready queue
        readyQueue = new PriorityQueue<Object>();
        //initialize the finish queue
        finishQueue = new PriorityQueue<Object>();
        
        //current event being simulated
        Object current;
        
        //add Bus creation event
        readyQueue.add(new BlankEvent((time+busArrivalInterval[0]),new Bus()));
        //add Person creation event
        readyQueue.add(new BlankEvent((time+personArrivalInterval),new Person()));
        
        //begin the simulation
        while((readyQueue.size() !=0)   || totalTransported < totalPopulation) 
                
        {
            //get next item to be processed
            current = GetNextReady();
            
            if(current instanceof Bus)
            {
//*****************write code to perform bus action*****************************


                //ensure all the bus stop has been traversed, or sent to finishQueue
                if(((Bus)current).GetNextbusStop() < busStop.length)
                {
                  
                    //drop off the people who needs to be dropped on the bus stop
                    ((Bus)current).PassengersOffBoarding(busStop[((Bus)current).GetNextbusStop()],outBusStopActivity,screen);
                    //pickup the people on the bus stop
                    ((Bus)current).PassengersOnBoarding(busStop[((Bus)current).GetNextbusStop()],screen);
                    //set the next time event for the bus, to get to next stop
                    ((Bus)current).SetFirstTicks(((Bus)current).GetFirstTicks()+ 
                            busStopTickDistances[((Bus)current).GetNextbusStop()]);
                    //set the next bus stop to visit
                    ((Bus)current).SetNextbusStop(((Bus)current).GetNextbusStop()+1);
                    //put the bus back in the ready queue, if journey not complete
                    readyQueue.add(((Bus)current));
                }
                else
                {
                    //kick everyone off the bus stop
                    ((Bus)current).PassengersKickedOff(outBusStopActivity,screen);
                    //check if the trip was empty
//if(((Bus)current).GetEmpty() == true)
//{
    //((Bus)current).IncrementEmptyTrips();
//}
                    //set the empty flag
                    ((Bus)current).SetEmpty(true);
                    //put the bus into the FINISH queue, if journey complete
                    finishQueue.add(((Bus)current));
                    //update total transported                    
                    totalTransported +=((Bus)current).GetTotalPassangersTransported();   
                    
                }
            }
            else if(current instanceof Person)
            {
if(verbose)
    System.out.println("Time:," + time + ", Stop #: " + (((Person)current).GetStartBusStop()+1)+", Person("+(((Person)current).GetPersonNumber()+1)+") Arrived");

                //put the person at their bus stop
                busStop[((Person)current).GetStartBusStop()].AddPerson((Person)current);
//update number of person waited at the Bus-Stop
busStop[((Person)current).GetStartBusStop()].IncrementTotalPersonsWaited();
               
            }
            //time to create a new Bus or Person
            else if(current instanceof BlankEvent)
            {
                if (((BlankEvent)current).GetObjectTypeToCreate() instanceof Bus)
                {
                    //bus interval creation
                    int busInt = 0;
                    
                    if (time < peakTravelTimes[0])
                    {
                        busInt = 0;
                    }
                    else if (time < peakTravelTimes[1])
                    {
                        busInt = 1;
                    }
                    else if (time < peakTravelTimes[2])
                    {
                        busInt = 2;
                    }
                    else
                        busInt =1;
                    
                   
                    //update timer
                    time = ((BlankEvent)current).GetFirstTick();
                    

                    if(totalTransported < totalPopulation)
                    {
                        //add Bus creation event
                        readyQueue.add(new BlankEvent((time+busArrivalInterval[busInt]),new Bus()));
                    }
                    //holds the current bus
                    Bus busTmp;
                    
                    //check if there is a completed bus before, making a new one
                    if(finishQueue.size() == 0)
                    {
                        //increment the number of busses created
                        this.IncrementBussesCreated();
                        //change bus number
                        busNumber += 1; 
                        //create a Bus according to the busArrivalInterval
                        busTmp = new Bus(time,busCapacity,busNumber, GetVerbose());
                        //set the bus start bus stop
                        busTmp.SetNextbusStop(0);
                        //set the empty flag
                        busTmp.SetEmpty(true);
                        //add bus to the ready queue
                        readyQueue.add(busTmp);
screen.WriteToFile("Time:, "+time+", Create BUS "+busNumber); 
if(verbose)
    System.out.println("Time:, "+time+", Create BUS "+busNumber);  
outBus.WriteToFile(time+","+busNumber);                        
                    }
                    else
                    {
                        //reuse a bus
                        busTmp = (Bus)finishQueue.poll();
                        //set the firstTick for the bus
                        busTmp.SetFirstTicks(time);
                        //set the next bus stop
                        busTmp.SetNextbusStop(0);
                        //set the empty flag
                        busTmp.SetEmpty(true);
                        //add bus to the ready queue
                        readyQueue.add(busTmp);
                    }
                    
                }
                else if (((BlankEvent)current).GetObjectTypeToCreate() instanceof Person)
                {
                    //update timer
                    time = ((BlankEvent)current).GetFirstTick();
                    
                    //limit the number of people to create
                    if(personsCreated < totalPopulation-1)                    
                    {
                        //add Person creation event
                        readyQueue.add(new BlankEvent(time+personArrivalInterval,new Person()));
                    }                   
                    
                    //create a person according to the personArrivalInterval
                    Person personTmp = new Person(time,Integer.MAX_VALUE);

//get next bus stop to deliver people to                    
int nextStop = GetNextSelectedBusStop();
//ensure people distribution numbers are maintained
//search for a bus stop that should receive people
while(distPeopleAtBusStops[nextStop] == 0)
{
    nextStop = GetNextSelectedBusStop();
}

if(distPeopleAtBusStops[nextStop] > 0)
{
    //set the person start bus stop
    personTmp.SetStartBusStop(nextStop);
    //decrement people distribution
    distPeopleAtBusStops[nextStop] -= 1;
}
//need to reduce distribution count when a stop is used                    

//distPeopleAtBusStops                    
                    
                    
//set the person Exponential Distribution for the Bus-Stop
int arr = peopleArrival[personTmp.GetStartBusStop()].next();     
//set arrival time
personTmp.SetFirstTicks(time+arr);





                    
                    //set the person stop bus stop
                    personTmp.SetStopBusStop(GetNextStopBusStop()); 
                    //set the person number
                    personTmp.SetPersonNumber(personsCreated);
                    //add person to ready queue
                    readyQueue.add(personTmp);
screen.WriteToFile("Time:,"+time+", Create PERSON "+(personTmp.GetPersonNumber()+1));      
if(verbose)
    System.out.println("Time:,"+time+", Create PERSON "+(personTmp.GetPersonNumber()+1));  
outPerson.WriteToFile(time+","+(personTmp.GetPersonNumber()+1)+","+
        personTmp.GetStartBusStop()+","+arr+","+personTmp.GetFirstTicks());
                    //increment the number of persons created
                    IncrementPersonsCreated();
                    //readyQueue.add(new Person(personArrivalInterval,Long.MAX_VALUE));
                }
            }   
        }
        
        //get the config file
        ConfigFile config = new ConfigFile("config.txt");
        //read the cofig file and extract options
        config.ReadFile();
        
        
        System.out.println("\n\n\n                          S U M M A R Y\n");
        summary.WriteToFile("\n\n\n                          S U M M A R Y\n");
        summary.WriteToFile("\n");
        summary.WriteToFile(config.GetStringData()+"\n");
        summary.WriteToFile("\n");
        System.out.println("Population      = "+totalPopulation);
        summary.WriteToFile("Population      = "+totalPopulation);
        System.out.println("Transported     = "+totalTransported);
        summary.WriteToFile("Transported     = "+totalTransported);
        System.out.println("Busses Created  = "+GetNumberOfBussesCreated()+"\n\n");
        summary.WriteToFile("Busses Created  = "+GetNumberOfBussesCreated()+"\n\n");
        summary.WriteToFile("\n");
        
        for(BusStop b:busStop)
        {
            System.out.println("Bus-Stop: "+(b.GetBusStopNumber()+1));
            summary.WriteToFile("Bus-Stop: "+(b.GetBusStopNumber()+1));
            
System.out.println("    Total-Person(s): "+b.GetTotalPersonsWaited());
summary.WriteToFile("    Total-Person(s): "+b.GetTotalPersonsWaited());
            
            System.out.println("    Average-Person(s): "+b.GetAverageNumberOfPersons());
            summary.WriteToFile("    Average-Person(s): "+b.GetAverageNumberOfPersons());
            System.out.println("    Average-WaitTime : "+b.GetAveragePersonsWaitTime());
            summary.WriteToFile("    Average-WaitTime : "+b.GetAveragePersonsWaitTime());
            if(b.GetMinimumPassenger() <= b.GetTotalPersonsWaited())
            {
                System.out.println("    Min-Per(s)       : "+b.GetMinimumPassenger());
                summary.WriteToFile("    Min-Per(s)       : "+b.GetMinimumPassenger());
            }
            else
            {
                System.out.println("    Min-Per(s)       : 0");
                summary.WriteToFile("    Min-Per(s)       : 0");
                
            }
            
            if(b.GetMaximumPassenger() > -1)
            {
                System.out.println("    Max-Per(s)       : "+b.GetMaximumPassenger()+"\n");
                summary.WriteToFile("    Max-Per(s)       : "+b.GetMaximumPassenger()+"\n");
            }
            else
            {
                System.out.println("    Max-Per(s)       : 0"+"\n");
                summary.WriteToFile("    Max-Per(s)       : 0"+"\n");                
            }
            summaryBusStop.WriteToFile(b.GetBusStopNumber()+","+b.GetAverageNumberOfPersons()
            +","+b.GetAveragePersonsWaitTime()+","+b.GetMinimumPassenger()
            +","+b.GetMaximumPassenger()+"\n");
            
        }
        
        System.out.println("Bus-Stop: ("+(busStop.length+1)+") is the T E R M I N A L --> End of Route");
        
        System.out.println("\n");
        summary.WriteToFile("\n");
        
        Iterator bu = finishQueue.iterator();
        
        
        Bus b;
        int cnt =1;
        //iterate the bus listing
        while(bu.hasNext())
        {
            //print sorted list of busses
            Iterator bu1 = finishQueue.iterator();
            while(bu1.hasNext())
            {   
                Bus bc = ((Bus)bu1.next());
                if(bc.GetBusNumber() == cnt)
                {
                    b = bc;
                    System.out.println("Bus: "+b.GetBusNumber());
                    summary.WriteToFile("Bus: "+b.GetBusNumber());
                    System.out.println("    Trip(s)             : "+b.GetTrips());
                    summary.WriteToFile("    Trip(s)             : "+b.GetTrips());
                    System.out.println("    Empty-Trip(s)       : "+b.GetEmptyTrips());
                    summary.WriteToFile("    Empty-Trip(s)       : "+b.GetEmptyTrips());
                    System.out.println("    Left at Stop        : "+b.GetTotalLeftAtBusStop());
                    summary.WriteToFile("    Left at Stop        : "+b.GetTotalLeftAtBusStop());
                    System.out.println("    Per(s) Transported  : "+b.GetAccumulatedPassangersTransported());
                    summary.WriteToFile("    Per(s) Transported  : "+b.GetAccumulatedPassangersTransported());
                    System.out.println("    Min-Per(s)          : "+b.GetMinimumPassenger());
                    summary.WriteToFile("    Min-Per(s)          : "+b.GetMinimumPassenger());
                    System.out.println("    Max-Per(s)          : "+b.GetMaximumPassenger()+"\n");
                    summary.WriteToFile("    Max-Per(s)          : "+b.GetMaximumPassenger()+"\n");

                    summaryBus.WriteToFile(b.GetBusNumber()+","+b.GetTrips()+","+b.GetEmptyTrips()
                    +","+b.GetTotalLeftAtBusStop()+","+b.GetAccumulatedPassangersTransported()
                    +","+b.GetMinimumPassenger()+","+b.GetMaximumPassenger()+"\n");
                }
            }
            cnt += 1;
            //check all busses stats printed
            if(cnt>GetNumberOfBussesCreated())
            {
                break;
            }
        }
        //close Person CREATED output files
        outPerson.CloseFile();
        //close BusStop CREATED output files
        outBusStop.CloseFile();
        //close Bus CREATED output files
        outBus.CloseFile();
        
        //close BusStopActivity output files
        outBusStopActivity.CloseFile();
        //close screen output files
        screen.CloseFile();
        //close summary files
        summary.CloseFile();
        //close BusStop summary file
        summaryBusStop.CloseFile();
        //close Bus summary file
        summaryBus.CloseFile();
    }
    
    
    //deliver person to bus stop
    public void DeliverToBusStop(Person person)
    {
        busStop[person.GetStartBusStop()].AddPerson(person);
    }
    
    //get the next item at the front of the priority queue
    public Object GetNextReady()
    {
        //holds first object in the ready queue
        Object tmp = null;
        
            //pickup priority Object
            tmp = readyQueue.poll();
            
        //return the object  
        return tmp;
    }
    
    public static void main(String[] args)
    {
        //reads config file
        ConfigFile config;
        //holds verbose flag
        boolean verbose = false;
        
        if(args.length < 1)
        {
            System.out.println("Usage: java Simulation <filename> <option: verbose> eg. java Simulation test.txt -v");
        }
        else if(args.length ==1)
        {
            
            //get the config file
            config = new ConfigFile(args[0]);
            //read the cofig file and extract options
            if(config.ReadFile() == true)
            {
                System.out.println(config.GetStringData());
                //pass the options to the simulation
                new Simulation(config.GetData(), verbose);

            }
                
        }
        else if (args.length == 2)
        {
            if(args[0].compareToIgnoreCase("-v") != 0)
            {
                //get the config file
                config = new ConfigFile(args[0]);
                if(args[1].compareToIgnoreCase("-v") == 0)
                    //set verbose
                    verbose = true;
                else
                {
                    System.out.println("Error: Invalid option specified!");
                    return;
                }
            }
            else
            {
                //get the config file
                config = new ConfigFile(args[1]);
                if(args[0].compareToIgnoreCase("-v") == 0)
                    //set verbose
                    verbose = true;
                else
                {
                    System.out.println("Error: Invalid option specified!");
                    return;
                }
            }
            
            
            //read the cofig file and extract options
            if(config.ReadFile() == true)
            {
                System.out.println(config.GetStringData());
                //pass the options to the simulation
                new Simulation(config.GetData(), verbose);

            }
        }
        else
        {
            System.out.println("Error:  *** Too many parameters *** --> Usage: java Simulation <filename> <option: verbose> eg. java Simulation test.txt verbose");
        }
    }
    
}

/*
 System.out.println("Time: " + ((Bus)current).GetFirstTicks() +", Bus #"+((Bus)current).GetBusNumber() + ", Passengers:"+((Bus)current).GetPassengerQuantity()
        + ", Bus Stop #"+((Bus)current).GetNextbusStop()+", Persons Transported: "+ ((Bus)current).GetSccumulatedPassangersTransported()+", Left at Stop: "+((Bus)current).GetTotalLeftAtBusStop()
        + ", Empty trips: "+((Bus)current).GetEmptyTrips()+", BusMin Per(s): "+((Bus)current).GetMinimumPassenger() + ", BusMax Per(s): "+((Bus)current).GetMaximumPassenger()
        + ", Stp #"+((Bus)current).GetBusNumber()+" Min per(S):"+((Bus)current).GetCurrentbusStop().GetMinimumPassenger()
        + ", Stp #"+((Bus)current).GetBusNumber()+" Max per(S):"+((Bus)current).GetCurrentbusStop().GetMaximumPassenger());                    
         
 */
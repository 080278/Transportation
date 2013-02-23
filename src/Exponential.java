/*
 * Author Name: David A. Flemming
 * Date: 2013-02-20
 * 
 * This class defines an Exponential Distribution, It contains their attributes 
 * needed for the simulation, and the actions that can be performed by any 
 * Exponential Distribution.
 */
import java.util.Random;

/**
 * Exponential object generates random integers from 
 * an exponential distribution with mean * scale
 *
 * where scale defaults to 1.
 */
public class Exponential 
{
    private int scale = 1; 
    private Random rnd;
    private int mean;

    //Construct a new Exponential object.
    public Exponential(int mean) 
    {
	this.mean = mean;
	rnd = new Random();
    }
    
    //Generate the next random selection.
    public int next() 
    {
	return (int)(-scale*Math.log(rnd.nextDouble())*((double)mean));
    }

    
     public static void main (String[] args) 
    {
        int meanGoal = 5; //arrival interval of people
        int m = 10; //15; //amount of numbers needed
        
	int n = m; //3000;
	
	
	Exponential p0 = new Exponential(meanGoal);

	System.out.println("First " + m + " of " + n + 
			   " samples from an Exponential distribution\n" +
			   "with mean " + meanGoal);
	double total = 0;
	double totalSquares = 0;
	for (int i=0; i < n; i++) {
	    int next = p0.next();
	    if ( i < m ) {
		System.out.println(next);
	    }
	    total        += next;
	    totalSquares += next*next;
	}
	double mean      = total/n;
	double variance  = totalSquares/n - mean*mean;
	System.out.println("mean goal:          " + meanGoal + 
		         "\nachieved:           " + mean +
			 "\nstandard deviation: " + Math.sqrt(variance));


	
    }
}






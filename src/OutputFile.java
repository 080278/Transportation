/*
 * Author Name: David A. Flemming
 * Date: 2013-02-09
 * 
 * This class defines a config file. It contains their attributes needed for the 
 * simulation, and the operations needed to get the configuration information
 * for the simulation.
 */

import java.io.*;
import java.util.*;

public class OutputFile {
    
    //holds the configuration file filename
    private String fileName;
    //set the data stream for the file
    DataOutputStream out;
    //open write stream to file
    BufferedWriter br;
    
                    
    //constructor 
    public OutputFile(String fileName)
    {
        //set the config file filename
        this.fileName = fileName;
        //open the file for output
        OpenFile();
        
    }
    
    //open file for writing
    private boolean OpenFile()
    {
        //read status
        boolean status = false;
        
        try
        {
            //open the file output stream
            FileOutputStream outStream = new FileOutputStream(System.getProperty("user.dir")+"\\"+fileName);
            //set the data stream for the file
            out = new DataOutputStream(outStream);
            //open write stream to file
            br = new BufferedWriter(new OutputStreamWriter(out));
            
        }
        catch(Exception e){
            //get the error message
            System.out.println("Error opening file: " + e.getMessage());
            status = false;
        }
                
        //return status of file read
        return status;
    }
    
    //close the open file
    public boolean CloseFile()
    {
        boolean result = false;
        
        try
        {
            //flush the buffer
            br.flush();
            //close the file
            out.close();
            //set status flag
            result = true;
        }
        catch(Exception e){}
        
        //return status
        return result;
    }
    
    //write information to file
    public boolean WriteToFile(String data)
    {
        boolean result = false;
        
        try
        {
            //write data to file
            br.write(data);
            br.newLine();
            //set the status flag
            result = true;
        }
        catch(Exception e){}
        
        return result;
    }
    
    
    
    public static void main(String[] args)
    {
        OutputFile f = new OutputFile("test.txt");
        f.OpenFile();
            f.WriteToFile("this is a test");
            f.WriteToFile("second line written");
        f.CloseFile();
        
        
    }
}

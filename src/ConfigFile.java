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

public class ConfigFile {
    
    //holds the configuration file filename
    private String fileName;
    //hold the line read from the file
    private String data;
    private String data1;
    //config array
    String[] dataPieces;
    
    //constructor 
    public ConfigFile(String fileName)
    {
        //set the config file filename
        this.fileName = fileName;
        //set data to empty string
        //data = "";
        
    }
    
    //open and read the config file
    public boolean ReadFile()
    {
        //read status
        boolean status = false;
        
        try
        {
            //open the file input stream
            FileInputStream inStream = new FileInputStream(System.getProperty("user.dir")+"\\"+fileName);
            //get the data stream for the file
            DataInputStream in = new DataInputStream(inStream);
            //read data from the file
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                  
            //iterate the file line by line
            while((data = br.readLine()) != null)
            {
                
                data1 = data;
                //get each option
                dataPieces = data.split("-");
                //read was successful
                status = true;
            }
            
            //close input
            in.close();
        }
        catch(Exception e){
            //get the error message
            System.out.println("Error: " + e.getMessage());
            status = false;
        }
                
        //return status of file read
        return status;
    }
    
    //get data pieces
    public String GetStringData()
    {
        return data1;
    }
    
    //get data pieces
    public String[] GetData()
    {
        return dataPieces;
    }
    
    public static void main(String[] args)
    {
        ConfigFile f = new ConfigFile("config.txt");
        f.ReadFile();
        
        
    }
}

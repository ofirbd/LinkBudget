package ofirbd.linkbudget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	// method to convert string to date
	public static Date stringToDate(String str_date)
	{
		Date date1 = null;
		try {  
		    //String str_date="11-June-07";
		    DateFormat formatter ; 
		    //Date date ; 
		    formatter = new SimpleDateFormat("yy-MM-dd"); // "dd-MM-yy"
		    date1 = (Date)formatter.parse(str_date);  
		    //System.out.println("Today is " + date );
		 
		  }
		catch (ParseException e)
		  {System.out.println("Exception :"+e);  }
		
		return date1;
	} // end method

	// method to convert Date to String
	public static String dateToString(Date date)
	{
		String s = null;
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd"); // "dd-MM-yy"
		s = formatter.format(date);
		
		return s;
	} // end method
	
	// method to create new directory in the current path if does not exists
	public static void createDirectory(String directoryName){
		
		try{
			boolean success = (
				  new File(directoryName)).mkdir();
				  if (success) {
					  System.out.println("Directory: " 
					   + directoryName + " created");
				  }
						  
		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
	
	// method to write text into a file
	public static void fprintf(String file_name,String str, boolean append)
	{
		File file = new File(file_name);
		FileWriter fstream;
		BufferedWriter out;
		boolean exist;
		try {
			exist = file.createNewFile();
			if (!exist)
			{
			  //System.out.println("File already exists.");
			  fstream = new FileWriter(file,append); // append to log file if true
			  out = new BufferedWriter(fstream);
			  out.newLine();
			}
			else
			{	  
				//System.out.println("Create new File.");
				fstream = new FileWriter(file); // create new log file
				out = new BufferedWriter(fstream);
				
			}

			out.write(str);
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} // end method
	
	// method to delete a file
	public static void deleteFile(String file_name)
	{
		File file = new File(file_name);
		boolean exist;
		try {
			exist = file.createNewFile();
			//System.out.println("exist file: " + exist);
			if (!exist)
			{
				file.delete();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} // end method
	
	public static double[] convertFloatsToDoubles(Float[] input)
	{
	    if (input == null)
	    {
	        return null; // Or throw an exception - your choice
	    }
	    double[] output = new double[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        output[i] = input[i];
	    }
	    return output;
	}

	/**
	 * parse up to three integers from string with up to two commas
	 * if more than two commas exists then it will take the first comma and the last comma, i.e. will parse the
	 * first number before first comma, the second number after first comma and last number after last comma
	 * 
	 * @param str
	 * @return Nx[] the three numbers N1, N2, N3 (if found less then three numbers then others will be set to zero
	 */
	public static int[] parseMultipleInt(String str){
		int[] Nx = new int[3];
		int N1=0;
		int N2=0;
		int N3=0;
		
		int comma1 = str.indexOf(",");
		int comma2 = str.lastIndexOf(",");
		
		// if no comma then convert to integer only one number
		if(comma1==-1){
			N1 = Integer.parseInt(str);
			// else if only one comma exists then convert two numbers
		}else if(comma1==comma2){
			String str1 = str.substring(0, comma1);
			N1 = Integer.parseInt(str1);
			String str2 = str.substring(comma1+1, str.length());
			N2 = Integer.parseInt(str2);
			// else if two commas exists then convert three numbers
		}else if(comma1 != comma2){
			String str1 = str.substring(0, comma1);
			N1 = Integer.parseInt(str1);
			String str2 = str.substring(comma1+1, comma2);
			N2 = Integer.parseInt(str2);
			String str3 = str.substring(comma2+1, str.length());
			N3 = Integer.parseInt(str3);
			
		}
		
		Nx[0]=N1;
		Nx[1]=N2;
		Nx[2]=N3;
		//System.out.println("The numbers are " + N1 + ", " + N2 + ", " + N3);
		
		return Nx;
	}
	
} // end class
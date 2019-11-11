package com.cheweishi.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringorDate {

	public static Date getStringorDate(String date){
		
		try  
		{  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");  
		    Date date1 = sdf.parse(date);  
		    return date1;
		}  
		catch (ParseException e)  
		{  
		}
		
		return null;
	}
}

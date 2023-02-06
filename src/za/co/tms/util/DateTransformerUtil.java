package za.co.tms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransformerUtil {
	

	public static String removeTimeStamp(String transferTime) throws ParseException {
		
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        
        String formattedDate = null;
        
        if(transferTime.length()>10) {
            Date d = input.parse(transferTime);
            formattedDate = output.format(d).toString();
        } 
        return formattedDate;
	}

}

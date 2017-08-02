package org.globalkinetic.jwt.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.globalkinetic.jwt.domain.AppUser;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;


@Component
public class TimeProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	
	long second = 1000l;
    long minute = 60l * second;
    long hour = 60l * minute;

	public Date now() {
        return new Date();
    }
	
	 private long getCurrentTimeMillis() {
	        return DateTime.now().getMillis();
	  }

	 public Date generateExpirationDate() {
	     return new Date(getCurrentTimeMillis() + 180 * 1000);
	 }
	 
	 public Date generateCurrentDate() {
	 	return new Date(getCurrentTimeMillis());
	 }
	 
	 public long lastLogin(AppUser user) {
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		 String dateString = user.getLoginDate();
		 Date loginDate = null;
		 long lastLogggedIn = 6l;/*make this more that five minutes*/
		 if(dateString != null) {/*usesr has not logged in yet*/
			 try {
					loginDate = sdf.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 } else { 
			 return lastLogggedIn;
		 }
		
		 long diff =  generateCurrentDate().getTime() - loginDate.getTime();
		 return (diff%hour)/minute;
	 }
	 
	 public String formatDate(Date inputDate){
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String dateString = dateFormat.format(inputDate);		
		 return dateString;
	 }
}

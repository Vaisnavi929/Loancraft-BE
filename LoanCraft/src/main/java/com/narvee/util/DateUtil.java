package com.narvee.util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
	public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
	
	
	public static LocalDateTime convertToLocalDateTime(Date date) {
	    return date.toInstant()
	               .atZone(ZoneId.systemDefault())
	               .toLocalDateTime();
	}

}

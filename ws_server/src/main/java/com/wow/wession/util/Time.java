package com.wow.wession.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Time {
	private static DateFormat simple_df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
	private static TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
	static {
		simple_df.setTimeZone(tz);
	}
	
	public static String getTime(){
		return simple_df.format(new Date());
	}
	
}

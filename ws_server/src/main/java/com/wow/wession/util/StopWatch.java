package com.wow.wession.util;

import java.util.concurrent.TimeUnit;

public class StopWatch {
	private long start_time;

	private final TimeUnit time_unit;
	public StopWatch(TimeUnit unit){
		this.time_unit  = unit;
		this.start_time = getNowTime();
	}

	public void start(){
		this.start_time = getNowTime();
	}

	public long stop(){
		return getNowTime() - start_time;
	}

	public void reset(){
		this.start_time = getNowTime();
	}

	private long getNowTime(){
		long time = 0;
		switch (this.time_unit) {
		case SECONDS:
			time = System.currentTimeMillis()/1000;
			break;
		case MILLISECONDS:
			time = System.currentTimeMillis();
			break;
		case NANOSECONDS:
			time = System.nanoTime();
			break;
		}
		return time;
	}

}

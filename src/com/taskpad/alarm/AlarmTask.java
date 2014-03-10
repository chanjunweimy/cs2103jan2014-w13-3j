package com.taskpad.alarm;

import java.util.TimerTask;

/**
 * 
 * @author Jun
 *
 * AlarmTask is the task that should be done
 * after the timer delay, which is stop playing sound. :D
 */

public class AlarmTask extends TimerTask {
	
	protected AlarmTask(){
	}
	
    public void run() {
    	try {
			AlarmManager.turnOffAlarm();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
    }

}

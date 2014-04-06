package com.taskpad.alarm;

import java.util.TimerTask;

/**
 * 
 * Force user to wait to load sound
 *
 */

//@author A0112084U

public class ForceWaitTask extends TimerTask {
	private int _time = -1;
	
	protected ForceWaitTask(int time){
		_time = time;
	}
	
    public void run() {
    	AlarmExecutor.launchAlarm(_time);
    }
}

package com.TaskPad.tests;

import java.util.Timer;
import java.util.TimerTask;

import com.TaskPad.inputproc.InputMain;
import com.TaskPad.ui.GuiManager;
import com.TaskPad.ui.InputFrame;
import com.TaskPad.ui.OutputFrame;

public class TestGuiManager {
	Timer timer;
	public static void main(String[] args){
		new InputMain();
		new InputFrame();
		new OutputFrame();
		
		GuiManager.callOutput("a");

		new Reminder(5);
		
	}
}


class Reminder {
    Timer timer;

    public Reminder(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000, seconds*1000);
        
	}

    static class RemindTask extends TimerTask {
    	private int t = 0;
        public void run() {
        	GuiManager.callOutput("b");
        	t++;
        	if(t == 3){
            	GuiManager.callExit();
            }
        }
    }
}

/**
 * test if the input shown in displayBox. (true)
 * test if everything is closed. (true)
 */
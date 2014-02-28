package com.taskpad.ui;

import com.taskpad.input.InputMain;

public class TaskPadLauncher implements Runnable {
	private static InputFrame _inputFrame;
	private static FlexiFontOutputFrame _outputFrame;
	
	@Override
	public void run() {
		setUpInputProcessor();
		setUpGui();
	}

	private void setUpGui() {
		initialFrames();
		GuiManager.initialGuiManager(_inputFrame, _outputFrame);
		GuiManager.callOutput("Welcome to Taskpad! Type a command or type \"help\"");
		GuiManager.remindUser("HELLO! Reminder: ");
	}

	private void initialFrames() {
		_inputFrame = new InputFrame();
		//_outputFrame = new OutputFrame(); //DEPRECATED!!!
		_outputFrame = new FlexiFontOutputFrame();
	}

	private void setUpInputProcessor() {
		new InputMain();
	}

}

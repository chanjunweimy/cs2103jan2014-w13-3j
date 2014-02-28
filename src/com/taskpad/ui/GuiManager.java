package com.taskpad.ui;

import com.taskpad.input.InputManager;

public class GuiManager {
	private static final String NEWLINE = "\n";
	private static InputFrame _inputFrame;
	private static FlexiFontOutputFrame _outputFrame;
	
	public GuiManager(){
	}

	public static void initialGuiManager(InputFrame inputFrame,
		FlexiFontOutputFrame outputFrame) {
		setInputFrame(inputFrame);
		setOutputFrame(outputFrame);
	}
	
	public static void callExit(){
		closeAllWindows();
		
	}

	private static void closeAllWindows() {
		_inputFrame.close();
		_outputFrame.close();
	}
	
	public static void callOutput(String out){
		GuiManager._outputFrame.addLine(out + NEWLINE);
	}
	
	public static void remindUser(String out){
		GuiManager._outputFrame.addReminder(out + NEWLINE);
	}
	
	public static void passInput(String in){
		InputManager.receiveFromGui(in);
	}

	private static void setInputFrame(InputFrame _inputFrame) {
		GuiManager._inputFrame = _inputFrame;
	}

	public static void setOutputFrame(FlexiFontOutputFrame _outputFrame) {
		GuiManager._outputFrame = _outputFrame;
	}
	
	public static void clearOutput(){
		GuiManager._outputFrame.clearOutputBox();
	}
}

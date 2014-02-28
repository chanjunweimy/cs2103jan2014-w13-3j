package com.taskpad.ui;

import java.awt.Color;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;


public class InputFrame extends GuiFrame{

	/**
	 * default
	 */
	private static final long serialVersionUID = 1L;  
	
	private final Color INPUTBOX_BACKGROUND_COLOR = 
			new Color(219, 219, 219);
	
	//inputTextBox
	private static JTextField input = new JTextField(15);
	
	private final static int INPUTFRAME_WIDTH = 350;
	private final static int INPUTFRAME_HEIGHT = 30;
	
	public InputFrame(){
		super();
		initializeInputFrame();
	}

	private void initializeInputFrame() {		
		setUpFrame();
		
		initializeInputBox();
		input.requestFocus();        // start with focus on this field
		this.getContentPane().add(input);
	}

	private void setUpFrame() {
		setSize(INPUTFRAME_WIDTH,INPUTFRAME_HEIGHT);
		
		int leftShift = INPUTFRAME_WIDTH - OutputFrame.showWidth();
		leftShift /= 2;
		
		setLocation((int)(COMPUTER_WIDTH/2 - leftShift),
					(int)(COMPUTER_HEIGHT/2));
	}

	private void initializeInputBox() {
		makeInputboxReadyForEvent();
		
		input.setBackground(INPUTBOX_BACKGROUND_COLOR);//grey color
	}

	private void makeInputboxReadyForEvent() {
		TextFieldListener tfListener = new TextFieldListener();
		input.addActionListener(tfListener);
	}
	
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		input.requestFocus();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		super.nativeKeyPressed(arg0);
		
		boolean isCtrlI = arg0.getKeyCode() == NativeKeyEvent.VK_I
	            && NativeInputEvent.getModifiersText(arg0.getModifiers()).equals(
	                    "Ctrl");
		
		if (isCtrlI){
			requestFocusOnInputBox();
		} 
	}
	
	private void requestFocusOnInputBox() {
		Runnable inputBoxFocus = new Runnable(){
			public void run(){
				input.requestFocus();
			}
		};
		SwingUtilities.invokeLater(inputBoxFocus);
	}
	
	protected static String getText(){
		return input.getText();
	}
	
	protected static void reset(){
		input.setText("");
	}
}

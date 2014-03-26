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
			//new Color(219, 219, 219); //this is grey color
			new Color(255,248,220);		//Cornsilk
	
	//inputTextBox
	private static JTextField _input = new JTextField(15);
	
	private final static int INPUTFRAME_WIDTH = 480;
	private final static int INPUTFRAME_HEIGHT = 30;
	
	private TextFieldListener _seeText = new TextFieldListener();
	private ComponentMover _moveInputBox = new ComponentMover(this);
	
	protected InputFrame(){
		super();
		initializeInputFrame();
	}

	private void initializeInputFrame() {		
		setUpFrame();
		
		initializeInputBox();
		_input.requestFocus();        // start with focus on this field
		this.getContentPane().add(_input);
	}

	private void setUpFrame() {
		setSize(INPUTFRAME_WIDTH, INPUTFRAME_HEIGHT);
		setLocation((int)(COMPUTER_WIDTH / 2 - INPUTFRAME_WIDTH / 2),
					(int)(COMPUTER_HEIGHT / 2 + OutputFrame.getInitialHeight() / 2 - INPUTFRAME_HEIGHT / 2));
	}

	private void initializeInputBox() {
		//ready to receive input
		_input.addActionListener(_seeText);
		
		//ready to move
		_moveInputBox.registerComponent(_input);
		//_input.addMouseListener(_mousePress);      DEPRECATED
		//_input.addMouseMotionListener(_mouseMove); DEPRECATED
		
		_input.setBackground(INPUTBOX_BACKGROUND_COLOR);
	}
	
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		_input.requestFocus();
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
			@Override
			public void run(){
				_input.requestFocus();
			}
		};
		SwingUtilities.invokeLater(inputBoxFocus);
	}
	
	protected static String getText(){
		return _input.getText();
	}
	
	protected static void reset(){
		_input.setText("");
	}
	
	protected JTextField getInputBox(){
		return _input;
	}
	
	//@Override
	protected static int getInitialWidth(){
		return INPUTFRAME_WIDTH;
	}
	
	//@Override
	protected static int getInitialHeight(){
		return INPUTFRAME_HEIGHT;
	}
	
	@Override
	protected void endProgram(){
		super.endProgram();
		
		//clear every listener before closing
		_input.removeActionListener(_seeText);
		
		_moveInputBox.deregisterComponent(_input);
		//_input.removeMouseListener(_mousePress);      DEPRECATED
		//_input.removeMouseMotionListener(_mouseMove); DEPRECATED
	}
}

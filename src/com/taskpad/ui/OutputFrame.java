/**
 * ===============DEPRECATED================
 * 
 * This class is deprecated as this class can only 
 * show text font with only one color.
 * 
 * But we need different font color for reminder...
 * JTextArea doesn't support this functionality 
 * so we have to implement a new class......
 * 
 */

package com.taskpad.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

public class OutputFrame extends GuiFrame{

	/**
	 * default
	 */
	private static final long serialVersionUID = 1L;
	
	protected final Color OUTPUTBOX_BORDER_COLOR = 
			new Color(112, 48, 160);
	protected final Color OUTPUTBOX_BACKGROUND_COLOR = 
			new Color(242, 242, 242);
	
	private final static int OUTPUTFRAME_WIDTH = 350;
	private final static int OUTPUTFRAME_HEIGHT = 150;
	
	private final Color DEFAULT_FONT_COLOR = Color.black;
	
	//outputTextBox
	private JTextArea _output = new JTextArea(5, 15);
	
	//children should have scroll bar too
	protected JScrollPane _scrollBox  = new JScrollPane();
	
	public OutputFrame(){
		super();
		initializeOutputFrame();
	}
	
	//things that I want to be inherited by FlexiFontOutputFrame;
	//as long as there is a boolean parameter
	//then it is the child class
	protected OutputFrame(boolean inherit){
		super();
	}

	protected void setUpFrame() {
		setSize(OUTPUTFRAME_WIDTH,OUTPUTFRAME_HEIGHT);
		setLocation((int)(COMPUTER_WIDTH/2),
					(int)(COMPUTER_HEIGHT/2 - OUTPUTFRAME_HEIGHT));
	}

	private void initializeOutputFrame() {				
		setUpFrame();
		
		initializeOutputBox();
		
		setUpScrollBar();

		this.getContentPane().add(_scrollBox);
	}

	protected void setUpScrollBar() {
		//JScrollPane provides scroll bar, so I add outputbox inside it.
		_scrollBox = new JScrollPane(_output);
		disableHorizontalScrollBar();
	}

	protected void disableHorizontalScrollBar() {
		_scrollBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}


	protected void initializeOutputBox() {
		// Don't let the user change the output.
		_output.setEditable(false);
		
		// Fix the maximum length of the line
		_output.setLineWrap(true);
		
		_output.setBackground(OUTPUTBOX_BACKGROUND_COLOR);
		_output.setBorder(BorderFactory.createLineBorder(OUTPUTBOX_BORDER_COLOR));
		
		initializeFont();
	}

	private void initializeFont() {
		Font font = new Font("Verdana", Font.BOLD, 12);
		_output.setFont(font);
		_output.setForeground(DEFAULT_FONT_COLOR);
	}
	
	protected void clearOutputBox() {
		_output.setText("");
	}
	
	protected void addLine(String line) {
		_output.append(line);
	}
	
	protected void addReminder(String line) {
		_output.append(line);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		super.nativeKeyPressed(arg0);
		
		boolean isCtrlW= arg0.getKeyCode() == NativeKeyEvent.VK_W
				&& NativeInputEvent.getModifiersText(arg0.getModifiers()).equals(
	                    "Ctrl");
		boolean isCtrlS = arg0.getKeyCode() == NativeKeyEvent.VK_S				
				&& NativeInputEvent.getModifiersText(arg0.getModifiers()).equals(
	                    "Ctrl");

		
		if (isCtrlW){
			scrollUp();
			
		} else if (isCtrlS){
			scrollDown();
		}
	}

	private void scrollDown() {
		Runnable downScroller = new BarScroller(false, _scrollBox.getVerticalScrollBar());
		SwingUtilities.invokeLater(downScroller);
	}

	private void scrollUp() {
		Runnable upScroller = new BarScroller(true, _scrollBox.getVerticalScrollBar());
		SwingUtilities.invokeLater(upScroller);
	}
	
	protected static int showWidth(){
		return OUTPUTFRAME_WIDTH;
	}
}
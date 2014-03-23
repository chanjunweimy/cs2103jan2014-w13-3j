/* 
 * Abstract class for processing the commands 
 */

package com.taskpad.input;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Command {

	protected static Input inputObject;
	protected static Map<String, String> inputParameters;
	protected static String input;
	protected static String fullInput;
	
	protected static int NUMBER_ARGUMENTS;
	protected static String COMMAND;
	
	protected static final String MESSAGE_EMPTY_INPUT = "Error: Empty Input";
	protected static final String MESSAGE_INVALID_INPUT = "Error: Invalid input: %s";
	protected static final String MESSAGE_INVALID_PARAMETER_NUMBER = "Error: Invalid number of parameters.\nType help if you need! :)";
	    
	protected static Logger logger = Logger.getLogger("TaskPad");
	
	public Command(String input, String fullInput){
		Command.fullInput = fullInput;
		Command.input = input;
		inputParameters = new HashMap<String,String>();
	}
	
	public void run() {
		try {
			checkIfEmptyString();
		} catch (EmptyStringException e) {
			showEmptyString();
			return;
		}
		
		try {
			checkIfIncorrectArguments();
		} catch (TaskIDException | InvalidParameterException e) {
			showIncorrectArguments();
			return;
		}
		
//		if (checkIfEmptyString() || checkIfIncorrectArguments()){
//			return;
//		} 
		clearInputParameters();
		initialiseParametersToNull();
		if (commandSpecificRun()){
			createInputObject();
			passObjectToExecutor();
		} else {
			return;
		}
	}
	
	private void showIncorrectArguments(){
		String errorMessage = MESSAGE_INVALID_PARAMETER_NUMBER;
		InputManager.outputToGui(errorMessage);
	}
	
	private void showEmptyString(){
		String errorMessage = String.format(MESSAGE_EMPTY_INPUT);
		InputManager.outputToGui(errorMessage);
	}

	protected void showNoDesc() {
		String errorMessage = String.format(MESSAGE_INVALID_INPUT, input);
		InputManager.outputToGui(errorMessage);
	}
	
	protected abstract boolean commandSpecificRun();
	
	protected boolean checkIfEmptyString() throws EmptyStringException {
		
		if(isEmptyString()){
			throw new EmptyStringException();
//			InputManager.outputToGui(MESSAGE_EMPTY_INPUT);
//			return true;
		}
		return false;
	}
	
	protected abstract void initialiseParametersToNull();
	
	protected boolean checkIfIncorrectArguments() throws TaskIDException, InvalidParameterException{
		String inputString[] = input.split(" ");
		
		if (isNotNumberArgs(inputString)){
			throw new InvalidParameterException(input);
		}
		
		/* deprecated
		if (isNotNumberArgs(inputString)){
			invalidParameterError();
			return true;
		}
		*/
		
		if(isNotValidTaskID(inputString[0])){
			throw new TaskIDException(inputString[0]);
		}
		
		/* deprecated
		if (isNotValidTaskID(inputString[0])){
			return true;
		}
		*/
		
		return false;
	}
	
	protected boolean isNotValidTaskID(String taskID){
		if(isNotInteger(taskID) || isInvalidID(taskID)){
			//outputIdError();
			return true;
		}	
		return false;
	}
	
	/**
	 * Lynnette: clearInputParameters() cannot be put
	 * in here.
	 * For example, inside Add.java,
	 * you putParametes before you createInputObject
	 * in that case, your input parameters will be wrong!
	 * so I help you move it, in that place I will comment also.
	 * 
	 * You see, whether the place I move is suitable or not la :) 
	 * @return
	 */
	protected Input createInputObject() {
		//clearInputParameters();
		putInputParameters();
		inputObject = new Input(getCOMMAND(), inputParameters);	
		
		/**
		 * for debugging!
		 */
		/*
		InputManager.outputToGui(getCOMMAND());
		ArrayList<String> arr = new ArrayList<String>(inputParameters.values());
		for (String str : arr){
			InputManager.outputToGui(str);
		}
		 */
		
		logger.info("Input object created, command: " + inputObject.getCommand());
		return inputObject;
	}

	protected abstract void putInputParameters();
	
	protected static void clearInputParameters(){
		inputParameters.clear();
	}
	
	protected boolean isEmptyString(){
		if (input.isEmpty()){
			return true;
		}
		return false;
	}
	
	protected static void putOneParameter(String parameter, String input){
		inputParameters.put(parameter, input);
	}
	
	protected void passObjectToExecutor(){
		assert (inputObject.getParameters().size() != 0);
		InputManager.passToExecutor(inputObject, fullInput);
		
		clearInputParameters();
		
		logger.info("Input object passed to executor");
	}
	
	protected boolean isNotInteger(String input){
		try{
			Integer.parseInt(input);
		} catch (NumberFormatException e){
			return true;
		}
		return false;
	}
	
	protected boolean isInvalidID(String input){
		input = input.trim();
		int inputNum = Integer.parseInt(input.trim());
		if (inputNum > InputManager.retrieveNumberOfTasks() || inputNum < 0){
			return true;
		}
		return false;
	}
	
	protected boolean isNotNumberArgs(String[] inputString){
		if (inputString.length != getNUMBER_ARGUMENTS()){
			return true;
		}
		return false;
	}
	
	protected void invalidParameterError(){
		String errorMessage = String.format(MESSAGE_INVALID_PARAMETER_NUMBER);
		InputManager.outputToGui(errorMessage);
	}
	
	protected void outputIdError() {
		String errorMessage = String.format(MESSAGE_INVALID_INPUT, input);
		InputManager.outputToGui(errorMessage);
	}

	protected int getNUMBER_ARGUMENTS() {
		return NUMBER_ARGUMENTS;
	}

	protected void setNUMBER_ARGUMENTS(int nUMBER_ARGUMENTS) {
		NUMBER_ARGUMENTS = nUMBER_ARGUMENTS;
	}

	protected String getCOMMAND() {
		return COMMAND;
	}

	protected void setCOMMAND(String cOMMAND) {
		COMMAND = cOMMAND;
	}
	
}

package com.taskpad.input;

import java.util.HashMap;
import java.util.Map;

import com.taskpad.input.CommandTypes.CommandType;

public class InputMain {

	private static final String MESSAGE_CONFIRMATION_CLEAR_SCREEN = "Confirm clear screen? (Y/N)";
	private static final String MESSAGE_CONFIRMATION_CLEAR_DATA = "Confirm clear data? (Y/N)";
	private static final String MESSAGE_EMPTY_INPUT = "Error: Empty input";
	private static final String MESSAGE_INVALID_COMMAND = "Invalid Command: %s ";
	
	private static final String COMMAND_CLEAR = "CLEAR";
	private static final String COMMAND_CLEAR_SCREEN = "CLEARSCREEN";
			
	private static CommandTypes commandTypes = new CommandTypes();
	private static Input inputObject;
	private static boolean isConfirmation = false;
	private static boolean hasCheckedFlexi = false;
	private static String currentCommand = "";
	
	private static Map<String, String> inputParameters = new HashMap<String, String>();
	
	public static void receiveInput(String input){
		hasCheckedFlexi = false;
		if (isConfirmation){
			if (isConfirmClear(input)){
				processConfirmation();
			}
			resetConfirmationVariable();
		} else {
			String inputCopy = input;
			if (errorIfNoInput(input)){
				return;
			}
			String commandTypeString = parseInput(inputCopy);
			CommandTypes.CommandType commandType = determineCommandType(commandTypeString);
	
			if (isValidCommandType(commandType)){		
				commandTypeString = removeFirstWord(input);
				performCommand (commandType, commandTypeString, input);
			} else {
				invalidCommand(input);
			}
		}

	}
	
	private static boolean isConfirmClear(String input){
		if (input.equalsIgnoreCase("Y")){
			return true;
		} else {
			return false;
		}
	}
	
	private static void processConfirmation(){
		if (currentCommand.equals(COMMAND_CLEAR)){
			clearAllTasks();
			
		} else if (currentCommand.equals(COMMAND_CLEAR_SCREEN)){
			InputManager.clearScreen();
		}
	}
	
	private static void resetConfirmationVariable(){
		isConfirmation = false;
		currentCommand = "";
	}
	
	private static boolean errorIfNoInput(String input) {
		if (input.equals("")){
			InputManager.outputToGui(String.format(MESSAGE_EMPTY_INPUT));
			return true;
		}
		return false;
	}

	private static boolean isValidCommandType(CommandType commandType) {
		if (commandType.equals(commandType.INVALID)){
			return false;
		}
		return true;
	}

	private static void performCommand(CommandType commandType, String commandTypeString, String input) {
		switch(commandType){
			case ADD:
				addTask(commandTypeString);
				break;
			case ADD_INFO:
				addInfoTask(commandTypeString);
				break;
			case ADD_REM:
				addRemTask(commandTypeString);
				break;
			case ADD_PRI:
				addPriTask(commandTypeString);
				break;
			case LIST:
				listTask(commandTypeString);
				break;
			case CLEAR_ALL:
				isConfirmation = true;
				currentCommand = COMMAND_CLEAR;
				clearAllTasksConfirmation();
				break;
			case CLEAR_SCREEN:
				isConfirmation = true;
				currentCommand = COMMAND_CLEAR_SCREEN;
				clearScreen();
				break;
			case DELETE:
				deleteTask(commandTypeString);
				break;
			case DONE:
				doneTask(commandTypeString);
				break;
			case EDIT:
				editTask(commandTypeString);
				break;
			case SEARCH:
				searchTask(commandTypeString);
				break;
			case HELP:
				help();
				break;
			case EXIT:
				exitProgram();
				break;
			case UNDO:
				undoLast();
				break;
			case INVALID:
				if (hasCheckedFlexi){
					invalidCommand(input);
				} else {
					hasCheckedFlexi = true;
					flexiCommand(input);
				}
				break;
			default:
				invalidCommand(commandTypeString);
		}
	}

	/* Methods to perform commands */
	
	private static void addTask(String input) {
		Add add = new Add(input);
		add.run();
	}
	
	private static void addPriTask(String input){
		AddPri addPri = new AddPri(input);
		addPri.run();
	}

	private static void addInfoTask(String input) {
		Addinfo addinfo = new Addinfo(input);
		addinfo.run();
	}
	
	private static void addRemTask(String input){
		Addrem addRem = new Addrem(input);
		addRem.run();
	}
	
	private static void listTask(String input){
		List list = new List(input);
		list.run();
	}
	
	private static void deleteTask(String input) {
		Delete delete = new Delete(input);
		delete.run();
	}

	private static void doneTask(String input) {
		Done done = new Done(input);
		done.run();
	}
	
	private static void passObjectToExecutor(){
		InputManager.passToExecutor(inputObject);
	}
	
	private static boolean isEmptyInput(String input){
		if (input.equals("")){
			return true;
		}
		return false;
	}
	
	private static void clearInputParameters(){
		inputParameters.clear();
	}
	
	private static void putInputParameters(String parameter, String input){
		inputParameters.put(parameter, input);
	}

	private static void clearAllTasksConfirmation(){
		InputManager.outputToGui(MESSAGE_CONFIRMATION_CLEAR_DATA);
	}
	
	private static void clearAllTasks() {
		ClearTasks clearTask = new ClearTasks("");
		clearTask.run();
	}
	
	private static void clearScreen(){
		InputManager.outputToGui(MESSAGE_CONFIRMATION_CLEAR_SCREEN);
	}
	
	private static void undoLast() {
		Undo undo = new Undo("");
		undo.run();
	}
	
	private static void editTask(String input) {
		Edit edit = new Edit(input);
		edit.run();
	}

	private static void searchTask(String input) {
		Search search = new Search(input);
		search.run();	
	}
	
	private static void help() {
		Help help = new Help();
		help.outputHelp();
	}

	private static void exitProgram() {
		InputManager.callGuiExit();
	}
	
	private static void flexiCommand(String input){
		hasCheckedFlexi = true;
		CommandType command = CommandTypes.findFlexi(input);
	
		String commandTypeString = replaceCommandWord(input, command);
		performCommand(command, commandTypeString, input);
	}
	
	private static String replaceCommandWord (String input, CommandType command){
		return input.replaceFirst("(?i)"+command.toString(), "");
	}
	
	private static void invalidCommand(String input) {
		InputManager.outputToGui(String.format(MESSAGE_INVALID_COMMAND, input));	
	}

	private static CommandTypes.CommandType determineCommandType(String commandTypeString) {
		String commandToFind = getFirstWord(commandTypeString);
		CommandTypes.CommandType commandType = CommandTypes.find(commandToFind);
		
		return commandType;
	}

	private static String parseInput(String input) {
		if (isInvalidCommand(input)){
			return invalidInput(input);
		}
		
		String commandTypeString = getFirstWord(input);
		return commandTypeString;			
	}
	
	/* Helper methods for parsing commands */ 
	private static boolean isInvalidCommand(String userCommand) {
		if (userCommand.trim().equals("")){
			return true;
		}
		return false;
	}
	
	private static String invalidInput(String input) {
		return String.format(MESSAGE_INVALID_COMMAND, input);
	}
	
	private static String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
	
	private static String getFirstWord(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		return commandTypeString;
	}

}

package com.taskpad.input;

import java.util.Scanner;

public class Add extends Command{
	
	private static String COMMAND_ADD = "ADD";
		
	private static String PARAMETER_DEADLINE = "DEADLINE";
	private static String PARAMETER_START_DATE = "START DATE";
	private static String PARAMETER_START_TIME = "START TIME";
	private static String PARAMETER_END_DATE = "END DATE";
	private static String PARAMETER_END_TIME = "END TIME";
	private static String PARAMETER_CATEGORY = "CATEGORY";
	private static String PARAMETER_DESCRIPTION = "DESC";
	private static String PARAMETER_VENUE = "VENUE";
	
	private static String MESSAGE_ERROR_TIME = "Error: Invalid variables for time: %d";
	
	private static int LENGTH_TIME = 2;
	
	private static boolean _invalidParameters = false;
	
	private static Scanner sc;

	public Add(String input) {
		super(input);
		setNUMBER_ARGUMENTS(1);
		setCOMMAND(COMMAND_ADD);
		sc = new Scanner(System.in);
	}
	
	@Override
	protected boolean commandSpecificRun() {
		splitInputParameters();
		
		if (invalidParameters){
			return false;
		}
		
		return true;		
	}

	@Override
	protected void initialiseParametersToNull() {
		putOneParameter(PARAMETER_CATEGORY, "");
		putOneParameter(PARAMETER_DEADLINE, "");
		putOneParameter(PARAMETER_DESCRIPTION, "");
		putOneParameter(PARAMETER_START_DATE, "");
		putOneParameter(PARAMETER_END_DATE, "");
		putOneParameter(PARAMETER_END_TIME, "");
		putOneParameter(PARAMETER_START_TIME, "");
		putOneParameter(PARAMETER_VENUE, "");
	}

	@Override
	protected void putInputParameters() {		
	}

	@Override
	protected boolean checkIfIncorrectArguments(){
		return false;
	}
	
	private void splitInputParameters(){
		int count = 0;
		sc = new Scanner(input).useDelimiter("\\s-");
		while(sc.hasNext()){
			String nextParam = sc.next();
			if (count == 0){
				putOneParameter(PARAMETER_DESCRIPTION, nextParam);
			} else {
				parseNextParam(nextParam);
			}
			count++;
		}
	}
	
	private void parseNextParam(String param){
		String firstChar = getFirstChar(param);
		param = removeFirstChar(param);

		switch (firstChar){
		case "d":
			getDeadline(param);
			break;
		case "v":
			inputVenue(param);
			break;
		case "s":
			inputStartTime(param);
			break;
		case "e": 
			inputEndTime(param);
			break;
		case "c":
			inputCategory(param);
			break;
		default:
			invalidParam();
		}
	}
	
	private void getDeadline(String param) {
		inputDeadline(param);
		
		/* deprecated
		String[] splitParam = param.split("/", -1);
		String day = splitParam[0];
		String month = splitParam[1];
		String year = splitParam[2];
		inputDeadlines(day,month, year);
		*/
	}
	
	private void inputDeadline(String deadline){
		putOneParameter(PARAMETER_DEADLINE, deadline);
	}
	
	private void inputVenue(String param) {
		putOneParameter(PARAMETER_VENUE, param);		
	}

	//TODO: Check if splitParam[0] is valid time and splitParam[1] is valid Date
	private void inputStartTime(String param) {
		String[] splitParam = param.split(",");
		
		if (isValidTimeArgs(splitParam)){
			putOneParameter(PARAMETER_START_TIME, splitParam[0]);
			if (splitParam.length == LENGTH_TIME){
				putOneParameter(PARAMETER_START_DATE, splitParam[1]);
			}
		}
	}

	private void inputEndTime(String param) {
		String[] splitParam = param.split(",");
		
		if (isValidTimeArgs(splitParam)){
			putOneParameter(PARAMETER_END_TIME, splitParam[0]);
			if (splitParam.length == LENGTH_TIME){
				putOneParameter(PARAMETER_END_DATE, splitParam[1]);
			}
		}
	}

	private void inputCategory(String param){
		putOneParameter(PARAMETER_CATEGORY, param);
	}
	
	private void invalidParam() {
		_invalidParameters = true;
	}
	
	private String removeFirstChar(String input) {
		return input.replaceFirst(getFirstChar(input), "").trim();
	}
	
	private String getFirstChar(String input) {
		String firstChar = input.trim().split("\\s+")[0];
		return firstChar;
	}
	
	private boolean isValidTimeArgs(String[] args){
		if (args.length != LENGTH_TIME){
			outputTimeArgsError(args.length);
			return false;
		} else {
			return true;
		}
	}
	
	private String outputTimeArgsError(int length){
		String errorMessage = String.format(MESSAGE_ERROR_TIME, length);
		InputManager.outputToGui(errorMessage);
		return errorMessage;
	}

}

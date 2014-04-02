package com.taskpad.dateandtime;

import java.util.Scanner;

/**
 * This class is for us to find the existence of Date and Time in an input String
 * 
 * Supposed to put all the protected methods from DateAndTimeManager here
 * 
 * @author Lynnette, Jun
 *
 */

public class DateAndTimeRetriever {
	
	private static final String STRING_EMPTY = "";

	/**
	 * In an input string, check if there is valid time
	 * @param inputString
	 * @return time
	 */
	
	protected static TimeObject findTime(String inputString){
		TimeObject timeObject = null;
		
		String parsedTime = isValidTime(inputString);
		if (isNotEmptyParsedString(parsedTime)){
			timeObject = createNewTimeObject(parsedTime, inputString);
		}
		return timeObject;
	}
	
	/* Helper methods for checking valid time in a String */
	private static String isValidTime(String input){
		input = trimInput(input);
		try {
			return TimeParser.parseTimeInput(input);
		} catch (TimeErrorException | InvalidTimeException e) {
			return STRING_EMPTY;
		}
	}
	
	private static TimeObject createNewTimeObject(String parsedTime, String inputTime){
		return new TimeObject(parsedTime.trim(), inputTime.trim());
	}
	
	/**
	 * In an input string, check if there is valid date 
	 * @param inputString
	 * @return date 
	 */
	protected static DateObject findDate(String inputString){
		DateObject dateObject = null;
		
		String parsedDate = isValidDate(inputString);
		if (isNotEmptyParsedString(parsedDate)){
			dateObject = createDateObject(parsedDate, inputString);
		}
		return dateObject;
	}
	
	/* Helper method for checking valid date in a String */
	private static String isValidDate(String input){
		input = trimInput(input);
		DateParser dateParser = DateParser.getInstance();
		try {
			return dateParser.parseDate(input);
		} catch (DatePassedException | InvalidDateException e) {
			return STRING_EMPTY;
		}
	}

	private static String trimInput(String input) {
		input = input.trim();
		return input;
	}
	
	private static DateObject createDateObject(String parsedDate, String input){
		return new DateObject(parsedDate, input.trim());
	}
	
	private static boolean isNotEmptyParsedString(String parsedString) {
		return !parsedString.equals(STRING_EMPTY);
	}
	
	/**
	 * FOR JUNWEI: 
	 * formatDateAndTimeInString
	 * This method takes in an input string and returns the string 
	 * with all the number words converted to numbers, special words converted to date
	 * and time words converted to time
	 */

	protected static String formatDateAndTimeInString(String desc) {
		//split all nonAlphaNumerics character
		String alphaNumericSpaceDesc = getAlphaNumericSpaceDesc(desc);
		
		//step one: convert all number words to numbers using number parser		
		String numberedInput = parseNumber(alphaNumericSpaceDesc);
		
		//step two: find holiday words and replace with date
		String holidayString = parseHolidayDates(numberedInput);

		//step three: find dayParser words and find words before (i.e. next/prev) and replace with date
		String dayString = parseDay(holidayString);
		
		//step four: find dates -- find month words & find number before and after
		//step four b: find dates -- find three consecutive numbers and try parse as date
		//step five: find PM or AM words and find time unit before and replace with time
		
		//return that string to parse in respective Add/Addrem/Alarm classes - already done with return input
		return desc;
	}

	/**
	 * @param input
	 */
	private static String getAlphaNumericSpaceDesc(String input) {
		Scanner sc = new Scanner(input);
		StringBuffer alphaNumericSpaceString = new StringBuffer();
		while (sc.hasNext()){
			String token = sc.next();
			token = splitNonAlphaNumericCharacter(token);
			alphaNumericSpaceString.append(token + " ");
		}
		sc.close();
		return alphaNumericSpaceString.toString().trim();
	}

	/**
	 * @param token
	 */
	private static String splitNonAlphaNumericCharacter(String token) {		
		//characters in date and time cannot be omitted
		if (isDate(token) || isTime(token)){
			return token;
		}
		
		//token = token.replaceAll("!", "");
		//token = token.replaceAll(".", "");
		//token = token.replaceAll(",", " ");
		//token = token.replaceAll(";", " ");
		//token = token.replaceAll("?", "");
		//token = token.replaceAll("\"", " \" ");
		//token = token.replaceAll("\'", " \' ");
		//token = token.replaceAll("(", "");
		//token = token.replaceAll(")", "");
		//token = token.replaceAll("~", " until ");
		//token = token.replaceAll("*", "");
		
		Scanner sc = new Scanner(token);
		//sc.useDelimiter("[^A-Za-z0-9]");
		StringBuffer tokenBuilder = new StringBuffer();
		String anyCharacter;
		while ((anyCharacter = sc.findInLine("[^A-Za-z0-9]")) != null){
			int splitIndex = token.indexOf(anyCharacter);
			String tempTokens = token.substring(0, splitIndex);
			token = token.substring(splitIndex + 1, token.length());
			tokenBuilder.append(tempTokens + " " + anyCharacter + " ");
		}
		if (token != null){
			tokenBuilder.append(token);
		}
		sc.close();
		return tokenBuilder.toString().trim();
	}

	private static boolean isTime(String input){
		input = trimInput(input);
		try {
			TimeParser.parseTimeInput(input);
		} catch (TimeErrorException | InvalidTimeException e) {
			return false;
		} catch (Exception e){
			return false;
		}
		return true;
	}
	
	private static boolean isDate(String input){
		input = trimInput(input);
		DateParser dateParser = DateParser.getInstance();
		try {
			dateParser.parseDate(input);
		} catch (InvalidDateException e) {
			return false;
		} catch (DatePassedException e) {
			return true;
		} 
		return true;
	}
	
	/**
	 * @param holidayString
	 */
	private static String parseDay(String holidayString) {
		String[] dayTokens = holidayString.split(" ");
		StringBuffer dayString = new StringBuffer();
		SpecialWordParser swp = SpecialWordParser.getInstance();
		DateAndTimeManager datm = DateAndTimeManager.getInstance();
		DayParser dp = DayParser.getInstance();
		boolean[] isModified = new boolean[dayTokens.length];
		
		initializeArray(isModified);
		
		for (int i = 0; i < dayTokens.length; i++){
			String firstToken = dayTokens[i];
			StringBuffer changedTokens = new StringBuffer();
			
			if (dp.isDay(firstToken)){
				isModified[i] = true;				
				for (int j = i - 1 ; j >= 0; j--){
					if (isModified[j]){
						break;
					}
					
					isModified[j] = true;
					String token = dayTokens[j];
					if (swp.isSpecialWord(token)){
						changedTokens.append(token + " ");
						dayTokens[j] = null;
						isModified[j] = true;
					} else {
						break;
					}
				}
				changedTokens.append(firstToken);
				
				try {
					//System.err.println(changedTokens.toString());
					dayTokens[i] = datm.parseDayToDate(changedTokens.toString().trim());
				} catch (InvalidDayException | DatePassedException e) {
					assert (false);
				}
			}
		}
		
		dayString = buildString(dayTokens, dayString);
		
		return dayString.toString().trim();
	}

	/**
	 * 
	 * @param numberedInput
	 * @return
	 */
	private static String parseHolidayDates(String numberedInput) {
		String[] numberInputTokens = numberedInput.split(" ");
		boolean[] isModified = new boolean[numberInputTokens.length];
		StringBuffer holidayString = new StringBuffer();
		
		initializeArray(isModified);
		
		HolidayDates holidayParser = HolidayDates.getInstance();
		for (int i = 2; i < numberInputTokens.length; i++){
			String token = numberInputTokens[i];
			
			String holidayInput;
			String pastOneToken, pastTwoToken;
			
			//search 3 words:
			if (!isModified[i - 2] && !isModified[i - 1] && !isModified[i]){
				pastOneToken = numberInputTokens[i - 1];
				pastTwoToken = numberInputTokens[i - 2];
				holidayInput = holidayParser.replaceHolidayDate(
						pastTwoToken + " " + 
						pastOneToken + " " + token);
				if (holidayInput != null){
					numberInputTokens[i] = holidayInput;
					numberInputTokens[i - 1] = null;
					numberInputTokens[i - 2] = null;
					isModified[i] = true;
					isModified[i - 1] = true;
					isModified[i - 2] = true;
					//holidayString.append(holidayInput + " ");
				}
			}
		}
		
		for (int i = 1; i < numberInputTokens.length; i++){
			String token = numberInputTokens[i];
			String holidayInput;
			String pastOneToken;
				
			//search 2 words:
			if (!isModified[i - 1] && !isModified[i]){
				pastOneToken = numberInputTokens[i - 1];
				holidayInput = holidayParser.replaceHolidayDate(pastOneToken + " " + token);
				if (holidayInput != null){
					numberInputTokens[i] = holidayInput;
					numberInputTokens[i - 1] = null;
					isModified[i] = true;
					isModified[i - 1] = true;
					//holidayString.append(holidayInput + " ");
				}
			}
		}
		
		for (int i = 0; i < numberInputTokens.length; i++) {
			String token = numberInputTokens[i];

			if (isModified[i]){
				continue;
			}
			String holidayInput;
			// search 1 word
			holidayInput = holidayParser.replaceHolidayDate(token);
			if (holidayInput != null) {
				numberInputTokens[i] = holidayInput;
				isModified[i] = true;
				// holidayString.append(holidayInput + " ");
			}
		}
		
		buildString(numberInputTokens, holidayString);
		
		return holidayString.toString().trim();
	}

	/**
	 * @param numberInputTokens
	 * @param holidayString
	 */
	private static StringBuffer buildString(String[] anyTokens,
			StringBuffer anyString) {
		for (String token : anyTokens){
			if (token != null){
				anyString.append(token + " ");
			}
		}
		return anyString;
	}

	/**
	 * @param isModified
	 */
	private static void initializeArray(boolean[] isModified) {
		for (int i = 0; i < isModified.length; i++){
			isModified[i] = false;
		}
	}

	/**
	 * @param input
	 * @param datmParser
	 */
	private static String parseNumber(String input) {
		DateAndTimeManager datmParser = DateAndTimeManager.getInstance();
		Scanner sc = new Scanner(input);
		StringBuffer changedString = new StringBuffer();
		StringBuffer numberString = new StringBuffer();
		boolean isNumberContinue = false;
		while (sc.hasNext()){
			String token = sc.next();
			if (!datmParser.isNumber(token)){
				if (isNumberContinue){
					String realNumber = datmParser.parseNumber(numberString.toString().trim());
					changedString.append(realNumber + " ");
					numberString = new StringBuffer();
				}
				
				changedString.append(token + " ");
				isNumberContinue = false;
			} else {
				if (!isNumberContinue){
					isNumberContinue = true;
				}
				numberString.append(token + " ");
			}
		}
		
		String realNumber = datmParser.parseNumber(numberString.toString().trim());
		if (realNumber != null){
			changedString.append(realNumber + " ");
		}

		sc.close();
		return changedString.toString().trim();
	}

	/**
	 * @deprecated
	 * @param input
	 */
	@SuppressWarnings("unused")
	private static String createDesc(String input) {
		String desc = input.trim();
		
		if (!desc.startsWith("\"")){
			desc = "\"" + desc;
		}
		
		if (!input.endsWith("\"")){
			desc = desc + "\"";
		}
		
		return desc;
	}
	
	public static void main (String[] args){  
		System.out.println(getAlphaNumericSpaceDesc("I am looking for Lynnette. She is going home on Monday."));
		System.out.println(getAlphaNumericSpaceDesc("I am looking for Lynnette. She is going home on 1/4/15 12:00."));
		System.out.println(parseNumber("one one one aaa one one one"));
		System.out.println(parseNumber("one one one aaa"));
		System.out.println(parseNumber("aaa"));
		System.out.println(parseHolidayDates("last Christmas I gave you my heart Christmas"));
		System.out.println(parseHolidayDates("last New Year I gave you my heart Christmas"));
		System.out.println(parseHolidayDates("last April Fool Day I gave you my heart Christmas"));
		System.out.println(parseDay("Monday I want to eat Monday"));
		System.out.println(parseDay("next nxt NXT prev Monday I want to catch Pokemon!"));
		System.out.println(parseDay("next ASH nxt Monday I want to catch Pokemon nxt Fri !"));

	}

	
}

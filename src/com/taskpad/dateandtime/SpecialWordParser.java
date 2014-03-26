package com.taskpad.dateandtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * SpecialWordParser: parses special words like next and previous
 * 
 * @author Jun, Lynnette
 *
 */
public class SpecialWordParser {
	
	private static final String SPACE = " ";

	private static final String DAY_INVALID = "Not a valid day!";

	private static final int DAY_WEEK = 7;

	private static final String EMPTY = "";

	//private static Map<Integer, String[]> MAP_SPECIAL_WORD = new HashMap<Integer, String[]>();
	private static Map<String, Integer> MAP_SPECIAL_WORD = new HashMap<String, Integer>();
	
	private static SpecialWordParser _specialWordParser = new SpecialWordParser();
	
	private static final String[] MAP_TMR = {
		"TMR", "TOMORROW", "TOMORRO"
	};
	
	private static final String[] MAP_YTD = {
		"YTD", "YESTERDAY"
	};
	
	private static final String[] MAP_WK = {
		"WK", "WEEK"
	};
	
	private static final String[] MAP_NXT = {
		"NEXT", "NXT", "FOLLOWING", "COMING"
	};
	
	private static final String[] MAP_PREV = {
		"PREVIOUS", "PREV", "PAST", "LAST", "YESTERDAY"
	};
	
	private static final String[] MAP_THIS = {
		"THIS"
	};
	
	private SpecialWordParser(){
		initialiseSpecialWordMap();
	}
	
	protected static SpecialWordParser getInstance(){
		return _specialWordParser;
	}
	
	protected String parseSpecialDay(String specialDay, String lastWord) throws DatePassedException, InvalidDayException{
		DateAndTimeManager datm = DateAndTimeManager.getInstance();
		String todayDay = datm.getTodayDay();
		int userDay = -1;
		
		userDay = getTodayDay(todayDay);
		for (String myWk : MAP_WK){
			if (myWk.equals(lastWord)){
				return parseSpecialDay(specialDay, userDay);
			}
		}
		
		for (String myYtd : MAP_YTD){
			if (myYtd.equals(lastWord)){
				userDay--;
				return parseSpecialDay(specialDay, userDay);
			}
		}
		
		for (String myTmr : MAP_TMR){
			if (myTmr.equals(lastWord)){
				userDay++;
				return parseSpecialDay(specialDay, userDay);
			}
		}
		
		throw new InvalidDayException(DAY_INVALID);
	}
	
	protected String parseSpecialDay(String specialDay, int userDay) throws DatePassedException{
		DateAndTimeManager datm = DateAndTimeManager.getInstance();
		TimeWordParser twp = TimeWordParser.getInstance();
		
		String todayDay = datm.getTodayDay();
		int todayDayStat = getTodayDay(todayDay);
		int nxt = 1;
		
		if (specialDay == SpecialWordParser.EMPTY){
			return getNextDay(userDay, twp, todayDayStat, nxt, "d");
		}
		
		specialDay = specialDay.toUpperCase();
		
		nxt = calculateNext(specialDay, nxt);
		
		if (nxt <= 0){
			throw new DatePassedException();
		}
		
		return getNextDay(userDay, twp, todayDayStat, nxt, "d");
	}

	/**
	 * @param specialDay
	 * @param nxt
	 * @return
	 */
	private int calculateNext(String input, int nxt) {
		Scanner sc = new Scanner(input);
		
		while (sc.hasNext()){
			String specialToken = sc.next();
			
			Integer ans = MAP_SPECIAL_WORD.get(specialToken);
			
			if (ans == null){
				break;
			}
			
			nxt += ans.intValue();
		}
		sc.close();
		return nxt;
	}

	/**
	 * @param userNum
	 * @param twp
	 * @param systemNum
	 * @param nxt
	 */
	private String getNextDay(int userNum, TimeWordParser twp, int systemNum, int nxt, String unit) {
		systemNum -= userNum;
		
		if (systemNum <= 0){
			systemNum += DAY_WEEK;
		}
		
		nxt--;
		systemNum += DAY_WEEK * nxt;
		
		try {
			return twp.timeWord(systemNum + unit);
		} catch (NullTimeUnitException | NullTimeValueException e) {
			assert (false);
		}
		return null;
	}

	private int getTodayDay(String todayDay) {
		DayParser dp = DayParser.getInstance();
		int todayDayStat = 0;
		try {
			todayDayStat = dp.parseDayToInt(todayDay);
		} catch (InvalidDayException e) {
			assert (false);
		}
		return todayDayStat;
	}
	
	/**
	 * parseSpecialWord parses special sentence like
	 * next next hour.
	 * 
	 * It can solve types like 
	 * next next hour
	 * (special words... + TimeUnit)
	 * 
	 * or
	 * 
	 * next next 1 hour 
	 * (sepcial words... + integer + TimeUnit)
	 * 
	 * It is the only method that parses words like next and prev.
	 * 
	 * @author Jun
	 * @param specialWord String
	 * @return int
	 */
	protected String parseSpecialWord(String specialWord, int seconds){
		int nxt = 0;
		nxt = calculateNext(specialWord, nxt);
		
		seconds *= nxt;
		
		return seconds + EMPTY;
	}
	
	/**
	 * getTimeWordWithoutNext split special words
	 * with TimeWord
	 * @param input
	 * @return
	 */
	protected String getTimeWordWithoutSpecialWords(String input){
		String[] inputs = input.split(SPACE);
		
		for (int i = inputs.length - 1; i >= 0 ; i--){
			if (MAP_SPECIAL_WORD.containsKey(inputs[i])){
				return inputs[i];
			}
		}
		
		return null;
	}
	
	private void initialiseSpecialWordMap() {
		initializeNextMap();
		initializePrevMap();
		initializeThisMap();
		initializeYtdMap();
		initializeTmrMap();
	}

	private void initializeTmrMap() {
		for (String myTmr : MAP_TMR){
			MAP_SPECIAL_WORD.put(myTmr, +1);
		}
	}

	private void initializeYtdMap() {
		for (String myYtd : MAP_YTD){
			MAP_SPECIAL_WORD.put(myYtd, -1);
		}
	}

	private void initializeThisMap() {
		for (String myThis : MAP_THIS){
			MAP_SPECIAL_WORD.put(myThis, 0);
		}
		
	}

	private void initializePrevMap() {
		//MAP_SPECIAL_WORD.put(+1, MAP_NXT);
		
		for (String next : MAP_NXT){
			MAP_SPECIAL_WORD.put(next, +1);
		}
		
	}

	private void initializeNextMap() {
		//MAP_SPECIAL_WORD.put(-1, MAP_PREV);		
		
		for (String prev : MAP_PREV){
			MAP_SPECIAL_WORD.put(prev, -1);
		}
	}
	
	
}

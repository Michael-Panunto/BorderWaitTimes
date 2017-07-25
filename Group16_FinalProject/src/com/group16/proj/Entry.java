package com.group16.proj;

import java.util.ArrayList;

/**
 * Formats and stores the data from the boarder wait times of ONE
 * BOARDER CROSSING per Entry Object. 
 * <br>
 * Performs basic operations on the
 * data for better use in the Controller class
 *
 * @author alexgotsko
 */
public class Entry {

	
	/**
	 * Array list to store all the entry dates from the data set MM-DD format.
	 */
	private ArrayList<String> eDate;
	/**
	 */
	private ArrayList<int[][]> wTime;
	/**
	 * 2d Integer array used to temporarily store wait times for each hour interval in a given day. [i][] - hour of day (0 to 23), [][i] - wait time for vehicle type (0 gives commercial, 1 gives normal).
	 */
	private int[][] tempTime;

	// ==========constructors=================

	/**
	 * Default Entry Constructor Sets up the data structure
	 * <p>
	 * Initiate new array list of entry dates and set the first entry to "01-01"
	 * <br>
	 * Initiate new array list of wait times.
	 * <br>
	 * Create new 2d array for storing wait times for each hour interval.
	 * <br>
	 * Add 2d array to the array list of wait times.
	 * <p>
	 * One entry object per border crossing location.
	 */
	public Entry() {
		this.eDate = new ArrayList<String>();
		this.eDate.add("01-01");
		this.wTime = new ArrayList<int[][]>();
		int[][] newT = new int[24][2];
		this.wTime.add(newT);
		this.tempTime = new int[24][2];
	}

	/**
	 * Entry Constructor that takes an array list of strings for entry dates, and and array list of 2d integers for wait times and creates and entry object based off them.
	 * 
	 * @param d
	 * ArrayList String - A list of all the entry dates from the
	 * data set
	 * @param t
	 * ArrayList int[][] - A List of 2d Integer arrays for the wait
	 * times on each hour of each day
	 */
	public Entry(ArrayList<String> d, ArrayList<int[][]> t) {
		this.eDate = d;
		this.wTime = t;
	}

	// ==========setters=====================

	/**
	 * Takes a date as a string a recorded time as an integer 
	 * and the logged wait time for that time as an integer. 
	 * Checks to see if the last index of the entry objects day is the same
	 * if it is not then it adds a new day to the eDate arraylist, adds the complete temp array 
	 * of wait times to the wTime list and adds
	 * empty arrays for the wait times. It then adds the hour and the wait time 
	 * to the temporary wait time array.
	 * 
	 * @param d
	 * String representation of the Date
	 * @param h
	 * Integer representation of the recorded hour
	 * @param t
	 * Integer representation of the logged wait time
	 */
	public void addEntry(String d, int h, int t) {
		int n = this.eDate.size();
		if (this.eDate.get(n - 1).compareTo(d) != 0) {
			this.eDate.add(d);
			int[][] newT = new int[24][2];
			this.wTime.add(newT);
			this.wTime.set(n - 1, this.tempTime);
			this.tempTime = new int[24][2];
		}
		this.tempTime[h][0] = h;
		this.tempTime[h][1] = t;
	}
	
	/* ===========OLD_ADD_ENTRY===============
	public void addEntry(String d, int h, int t) {
		int n = this.eDate.size();
		if (this.eDate.get(n - 1).compareTo(d) != 0) {
			this.eDate.add(d);
			int[][] newT = new int[24][2];
			this.wTime.add(newT);
			this.wTime.set(n - 1, this.tempTime);
			this.tempTime = new int[24][2];
		}
		this.tempTime[h][0] = h;
		this.tempTime[h][1] = t;
	}
	*/

	// ===========getters=====================

	/**
	 * Returns a complete list of entry dates for this particular Boarder
	 * Crossing Should be every day of the year 01-01 to 12-31
	 * 
	 * @return
	 * ArrayList of Strings - all of the entry dates in ONE BOARDER
	 * CROSSINGS data set
	 */
	public ArrayList<String> getEntryDates() {
		return this.eDate;
	}

	/**
	 * returns a complete list of wait times for every date in a year and every
	 * hour for this particular Boarder Crossing. Should be every day of the
	 * year 01-01 to 12-31 and every hour 00 to 23
	 * 
	 * @return
	 * ArrayList of Strings - all of the Wait times for every day and
	 * every hour
	 */
	public ArrayList<int[][]> getWaitTimes() {
		return this.wTime;
	}

	// ========operations on entries===========

	/**
	 * Takes a day of the year as in 1 to 365 returns a calendar date
	 * representation of that day
	 * 
	 * @param dayOfYear
	 * Integer - Takes an integer day of the year as in 1 to 365
	 * @return 
	 * Returns a String of the calendar day from the day of the year
	 */
	public String getEntryDate(int dayOfYear) {
		return this.eDate.get(dayOfYear - 1);
	}

	/**
	 * Takes a date and returns a 2d array of wait times for that day in the specific entry object.
	 * 
	 * @param date
	 * String representation of a date MM-DD
	 * @return
	 * 2d array of integers with the wait times for each hour interval. [i][] - hour interval 00 to 23, [][i] - wait time in minutes as an integer.
	 */
	public int[][] getWaitTimes(String date) {
		int i = this.eDate.indexOf(date);
		return this.wTime.get(i);
	}

	/**
	 * Takes a String representation of the calander day and a int hour of the
	 * day Performs a search of the array and returns the wait time for that day
	 * and that hour
	 * 
	 * @param date
	 * Takes the calendar day "01-01" to "12-31" as a String
	 * @param h
	 * Integer representation of the hour interval 00 to 23
	 * @return 
	 * Integer representation of the wait time of on date and at hour
	 */
	public int getWaitTime(String date, int h) {
		int i = this.eDate.indexOf(date);
		return this.wTime.get(i)[h][1];
	}

	/**
	 * Takes a String date and returns an array of dates close to input date 10
	 * days in the future 10 days in the past
	 * 
	 * @param d
	 * Date in string format MM-dd
	 * @return 
	 * 2d array of Strings, [i][] == month in shorthand , [][i] == day in the month in numeric format
	 */
	public String[][] getNearByDates(String d) {
		String[][] tempA = new String[21][2];
		String[] tempS = new String[2];
		int i = this.eDate.indexOf(d);
		int ovfl = 0;

		for (int x = 0; x < 10; x++) {
			if (i > 0)
				i--;
			else
				ovfl++;
		}

		for (int x = 0; x < (21 + ovfl); x++) {
			tempS = new String[2];
			String tDay = this.eDate.get(i);
			String tDS[] = tDay.split("-");
			switch (Integer.parseInt(tDS[0])) {
			case 1:
				tempS[0] = "Jan.";
				break;
			case 2:
				tempS[0] = "Feb.";
				break;
			case 3:
				tempS[0] = "Mar.";
				break;
			case 4:
				tempS[0] = "Apr.";
				break;
			case 5:
				tempS[0] = "May";
				break;
			case 6:
				tempS[0] = "Jun.";
				break;
			case 7:
				tempS[0] = "Jul.";
				break;
			case 8:
				tempS[0] = "Aug.";
				break;
			case 9:
				tempS[0] = "Sep.";
				break;
			case 10:
				tempS[0] = "Oct.";
				break;
			case 11:
				tempS[0] = "Nov.";
				break;
			case 12:
				tempS[0] = "Dec.";
				break;
			}
			tempS[0] = tempS[0] + " " + tDS[1];
			tempS[1] = tDay;
			tempA[x][0] = tempS[0];
			tempA[x][1] = tempS[1];
			i++;
		}

		return tempA;
	}

	// ============================================

	/**
	 * Check if this Entry array is empty and return boolean true of false.
	 * 
	 * @return 
	 * Boolean - If the entry date array is empty or not
	 */
	public boolean isEmpty() {
		return (this.eDate.size() < 2) ? true : false;
	}

}

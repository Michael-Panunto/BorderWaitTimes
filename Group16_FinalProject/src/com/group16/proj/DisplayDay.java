/**
 * 
 */
package com.group16.proj;

/**
 * Formats and stores values related to the displaying of wait time information for a given date and at a given border crossing.
 * <br>
 * Also contains Methods for sorting this information by time interval and sorting by wait times.
 * 
 * @author alexgotsko
 */
public class DisplayDay {
	/**
	 * array of Display time objects storing formated wait time data
	 */
	private DisplayTime[] dayHours = new DisplayTime[24];
	/**
	 * Integer location index ID that time is to displayed at
	 */
	private int location; 
	/**
	 * 2d array of wait times every hour for 24 hours and both commercial and passenger wait times
	 */
	private int[][] waitTimes; 
	/**
	 *  Sorted by flag 0 == sorted by hour interval 1 == sorted by wait times
	 */
	private int sortedBy; 

	/**
	 * Default Constructor for DisplayDay Object
	 */
	public DisplayDay(){
		this.location = -1;
	}
	
	/**
	 * Constructor for DisplayDay Object takes date and array of all the wait times in that day for both types of vehicles.
	 * 
	 * @param curLocation
	 * String representation of the date going to be displayed
	 * @param waitTimes
	 * 2D integer array of wait times, [i][] -- the hours in the day 0 -- 23, [][i] vehicle type [0] | [1] commercial or normal
	 */
	public DisplayDay(int curLocation, int[][] waitTimes) {
		this.location = curLocation;
		this.waitTimes = waitTimes;
		// iterate through hours in day and initialize
		for (int h = 0; h < 24; h++) {
			dayHours[h] = new DisplayTime(h, waitTimes[h][1]);
		}
		this.sortedBy = 0;
	}

	/**
	 * Gets the location associated with the object
	 * 
	 * @return Integer representation of the objects Border Crossing index ID
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * Gets an array of all the wait times in this Display Day Object
	 * 
	 * @return An array of wait times for every hour of the day and sub arrayed
	 *         into index 0 truck wait times and index 1 car wait times
	 */
	public int[][] wTimes() {
		return waitTimes;
	}

	/**
	 * Gets all the DisplayTimes in this object Object 
	 * 
	 * @return An array of DisplayTime objects sorted by either the hours of the
	 *         day or by the length of the wait times
	 */
	public DisplayTime[] getDisplayTimes() {
		return dayHours;
	}

	/**
	 * Used to get the flag that represents what the current DisplayDayObject is sorted by
	 * 
	 * @return Integer - Sorted by 0 means by hours, sorted by 1 means by wait times
	 */
	public int getSortedBy(){
		return sortedBy;
	}
	
	/**
	 * Sort the array of Display times by the hours of the day 00 - 23 if already
	 * sorted perform a reversal of order.
	 * <p>
	 * First check to see if the sort by hours of the day flag is not set, 
	 * if not, sort the display time object using insertion sort with direct compare
	 * ordering it start of day hour 00 to end of day hour 23.
	 * <p>
	 * If the Object is already sorted by hours of the day, perform a order reversal by iterating
	 * through half the objects indexes while also keeping track of the indexes from the other
	 * end and swapping the Display time objects at those indexes.
	 */
	public void sortByHours() {
		if (sortedBy != 0) {
			for (int j = 1; j < 24; j++){
				for (int k = j; (k > 0) && (dayHours[k].forTime < dayHours[k - 1].forTime); k--) {
					DisplayTime temp = dayHours[k];
					dayHours[k] = dayHours[k-1];
					dayHours[k-1] = temp;
				}
			}
			sortedBy = 0;
		} else {
			int i = 0;
			for (int j = 23; j > 11; j--) {
				DisplayTime temp = dayHours[i];
				dayHours[i] = dayHours[j];
				dayHours[j] = temp;
				i++;
			}
		}
	}

	/**
	 * Sort the array of Display times by the estimated wait times if already
	 * sorted perform a reversal of order.
	 * <p>
	 * First check to see if the sort by wait time flag is not set, 
	 * if not sort the display time object using insertion sort with direct compare
	 * ordering it highest wait time to lowest. If the wait time is the same the order 
	 * wont change.
	 * <p>
	 * If the Object is already sorted by wait time, perform a order reversal by iterating
	 * through half the objects indexes while also keeping track of the indexes from the other
	 * end and swapping the Display time objects at those indexes.
	 */
	public void sortByWaitTimes() {
		if (sortedBy != 1) {
			for (int j = 1; j < 24; j++){
				for (int k = j; (k > 0) && (dayHours[k-1].wTime < dayHours[k].wTime); k--) {
					DisplayTime temp = dayHours[k];
					dayHours[k] = dayHours[k-1];
					dayHours[k-1] = temp;
				}
			}
			sortedBy = 1;
		} else {
			int i = 0;
			for (int j = 23; j > 11; j--) {
				DisplayTime temp = dayHours[i];
				dayHours[i] = dayHours[j];
				dayHours[j] = temp;
				i++;
			}
		}
	}

}

package com.group16.proj;

/**
 * Used for storing and formating the hour interval and wait time associated 
 * with it to be inserted into the results.jsp output page.
 * 
 * @author alexgotsko
 */
public class DisplayTime {
	/**
	 * Time interval as an int 0 to 23
	 */
	public final int forTime;
	/**
	 * Time interval as a string in format hh(12hclock) pp(AM/PM)
	 */
	public final String forTimeString;
	/**
	 * Wait time as a percentage (wait time greater or equal 30 min) == 100% as an int 0 == 0% 100 == 100%
	 */
	public final int wTimePerc;
	/**
	 * Time intervals wait time over %100 true or false
	 */
	public final boolean overPerc;
	/**
	 * Wait time for this time interval as an int
	 */
	public final int wTime;
	/**
	 * Wait time for time interval as a string which includes mins
	 */
	public final String wTimeString;
	
	//===============Constructor=====================
	
	/**
	 * Construct an object to store the displayed wait time information for each hour interval
	 * <p>
	 * At creation:
	 * <br>
	 * Convert and store wait time to integer representation as a percentage between 0% at 0 min and 100% at 30 min.
	 * <br>
	 * Convert and store hour interval value to friendly string version "hhAM" or "hhPM"
	 * <br>
	 * Convert and store wait time to a friendly string version "mm mins"
	 * <p>
	 * Also store the raw values of both hour interval and wait time interval.
	 * 
	 * @param forTime
	 * Integer hour interval at which wait time is recorded
	 * @param wT
	 * Integer wait time for the given hour interval
	 */
	public DisplayTime(int forTime, int wT){
		int temp = (int) (((double) wT/30)*100);	//wait time represented as percent between 0% at 0 min and 100% at 30 min
		this.forTime = forTime;
		this.forTimeString = 
				((forTime%12 == 0)? "12":forTime%12) 
				+ ((forTime>=12)?" PM":" AM");
		this.wTimePerc = (temp>100)? 100:temp;		//cap percent at 100%
		this.overPerc = (temp>100)? true:false;
		this.wTime = wT;
		this.wTimeString = wT + " mins";
	}
}

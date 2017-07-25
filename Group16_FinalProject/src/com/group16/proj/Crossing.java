package com.group16.proj;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
//==================Constants for all the boarder crossings=============================
/**
 * Constants for each Border Crossing storing values that never change at each boarder crossing.
 * <p>
 * Each border constant include final values for:
 * <br>
 * int - crossing index, String - name of customs office, int - node ID (graphing), Double Longatude (Google Maps API), Double Latitude (Google Maps API)
 * 
 * @author alexgotsko
 */
public enum Crossing {
	
	/**
	 * Crossing has final values for index #, name of location, intersection node id, Latitude and Longitude
	 */
	ABBOTSFORD_HUNTINGDON(0, "Abbotsford-Huntingdon", 8989, 49.003004, -122.265222),
	ALDERGROVE(1, "Aldergrove",	9221, 49.002897, -122.485031),
	AMBASSADOR_BRIDGE(2, "Ambassador Bridge", 36, 42.301024, -83.066730),
	BLUE_WATER_BRIDGE(3, "Blue Water Bridge", 0, 42.992226, -82.410958),
	BOUNDARY_BAY(4, "Boundary Bay", 18240, 49.002152, -123.068369),
	CORNWALL(5, "Cornwall", 24044, 45.010714, -74.737157),
	COUTTS(6, "Coutts", 0, 48.998723, -111.960072),
	DOUGLAS_PEACE_ARCH(7, "Douglas (Peace Arch)", 215, 49.004951, -122.757498),
	EDMUNDSTON(8, "Edmundston", 0, 47.362187, -68.328857),
	EMERSON(9, "Emerson", 0, 49.000719, -97.237133),
	FORT_FRANCES_BRIDGE(10, "Fort Frances Bridge", 0, 48.608664, -93.399372),
	NORTH_PORTAL(11, "North Portal",	0, 48.999232, -102.554195),
	PACIFIC_HIGHWAY(12, "Pacific Highway", 12000, 49.003002, -122.734128),
	PEACE_BRIDGE(13, "Peace Bridge", 6694, 42.907777, -78.912183),
	PRESCOTT(14, "Prescott", 155, 44.745296, -75.470347),
	QUEENSTON_LEWISTON_BRIDGE(15, "Queenston-Lewiston Bridge", 23085, 43.153890, -79.048625),
	RAINBOW_BRIDGE(16, "Rainbow Bridge", 11220, 43.091794, -79.071157),
	SAULT_STE_MARIE(17, "Sault Ste. Marie", 0, 46.519126, -84.34926),
	ST_ARMAND_PHILIPSBURG(18, "St-Armand/Philipsburg", 12101, 45.016196, -73.083862),
	ST_BERNARD_DE_LACOLLE(19, "St-Bernard-de-Lacolle", 7316, 45.009334, -73.452103),
	ST_STEPHEN(20, "St. Stephen", 22878, 45.192461, -67.283380),
	ST_STEPHEN_3RD_BRIDGE(21, "St. Stephen 3rd Bridge", 26132, 45.160978, -67.309191),
	STANSTEAD(22, "Stanstead", 0, 45.006546, -72.087768),
	THOUSAND_ISLANDS_BRIDGE(23, "Thousand Islands Bridge", 71, 44.348340, -75.983882),
	WINDSOR_AND_DETROIT_TUNNEL(24, "Windsor and Detroit Tunnel", 7919, 42.315972, -83.036820),
	WOODSTOCK_ROAD(25, "Woodstock Road", 0, 46.137909, -67.777711);
	
	
	/**
	 * For this Crossing Store constant Index # as an integer.
	 */
	public final int index;
	/**
	 * For this Crossing Store constant crossing name as a string
	 */
	public final String stringName;
	/**
	 * For this Crossing Store constant intersection node id as an integer
	 */
	public final int node;
	/**
	 * For this Crossing Store constant latitude coordinates as a double
	 */
	public final double lat;
	/**
	 * For this Crossing Store constant longitude coordinates as a double
	 */
	public final double lng;
	
	/**
	 * Crossing constructor
	 * @param i
	 * index of type int of the border crossing
	 * @param nm
	 * string name of the boarder crossing
	 * @param id
	 * node id of type int for the boarder crossing used in graphing
	 * @param lat
	 * latitude coordinate of type double used for drawing on google maps API
	 * @param lng
	 * longitude coordinate of type double used for drawing on google maps API
	 */
	Crossing(int i, String nm, int id, double lat, double lng){
		this.index = i;
		this.stringName = nm;
		this.node = id;
		this.lat = lat;
		this.lng = lng;
	}
	
	//====static methods for SEARCHING constants based off values without performance loss using HashMaps=====
	
	/**
	 * Initialize static Hash Map of Node id Keys with corresponding index number for each border crossing constant
	 */
	private static Map<Integer, Integer> mapN = new HashMap<Integer, Integer>();
	/**
	 * Initialize static Hash Map of Node id Keys with corresponding Crossing enum for each border crossing constant
	 */
	private static Map<Integer, Crossing> mapFrN = new HashMap<Integer, Crossing>();
	/**
	 * Initialize static Hash Map of string Crossing Name keys with corresponding Crossing enum for each border crossing constant
	 */
	private static Map<String, Integer> mapI = new HashMap<String, Integer>();
	/**
	 * Initialize static Hash Map of index key integers with corresponding node IDs for each border crossing constant
	 */
	private static Map<Integer, Integer> mapNaI = new HashMap<Integer, Integer>();
	
    static {
        for (Crossing crossingEnum : Crossing.values()) {
            mapI.put(crossingEnum.stringName, crossingEnum.index);
            mapN.put(crossingEnum.node, crossingEnum.index);
            mapFrN.put(crossingEnum.node, crossingEnum);
            mapNaI.put(crossingEnum.index, crossingEnum.node);
        }
    }
    
    //===========static getters=============
	
	/**
	 * Search Hash Map of Node Keys for corresponding index number
	 * @param node
	 * Node id integer of Boarder crossing
	 * @return 
	 * Index id of the corresponding Boarder Crossing
	 */
	public static int indexAtNode(int node){
		return mapN.get(node);
	}
	
	/**
	 * Search Hash Map of Node Keys for corresponding Crossing enum
	 * @param node
	 * Node id integer of Boarder crossing
	 * @return 
	 * Crossing enum of the corresponding Boarder Crossing
	 */
	public static Crossing fromNode(int node){
		return mapFrN.get(node);
	}
	
	/**
	 * Search Hash Map of name keys for corresponding Crossing enum
	 * @param name
	 * String name of the Boarder crossing
	 * @return 
	 * Integer of the corresponding index of the border crossing
	 */
	public static int indexFromString(String name){
		return mapI.get(name);
	}
	
	/**
	 * Search Hash Map of index keys for corresponding node id
	 * @param name
	 * String name of the Boarder crossing
	 * @return 
	 * Integer of the corresponding index of the border crossing
	 */
	public static int nodeFromIndex(int name){
		return mapNaI.get(name);
	}
	
	//===========getters=============
	
	/**
	 * Take the Coordinates of the border crossing 
	 * and return it as a key value string pair
	 * 
	 * @return
	 * String of the Coordinates of the border crossing, formated as a pair of key values
	 */
	public String coord(){
		return String.format("{lat: %f, lng: %f}", lat, lng);
	}
}
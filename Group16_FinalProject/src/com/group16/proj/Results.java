package com.group16.proj;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class. Acts as the controller of the Application taking input from the view (jsp and html files) and calling on the model (java files) when needed.
 * <br>
 * This class also retreives the data from the datasets and calls on the appropriate classes to format and store this data.
 * <br>
 * All the logic for the user interface is done through this class.
 * 
 * @author Alex Gotsko
 */
@WebServlet("/results")
public class Results extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ServletContext context;
	
	/**
	 * Crossing here used to find related constant values for border crossing currently being displayed
	 */
	private Crossing here;
	/**
	 * Crossing there used to find related constant values for border crossing that was located to be the nearest crossing to the current one being displayed
	 */
	private Crossing there;
	
	/**
	 * Array of 26 Entry objects where each entry represents a crossing
	 */
	private static Entry[] crossing = new Entry[26];
	
	/**
	 * Setup Shortestpath object used to store and perform graphing processing on our road map
	 */
	private static ShortestPaths sp;
	
	
	/**
	 * Hash Map to store all the nodes from our road map and their corresponding coordinates in a Double array [0] longitude [1] latitude
	 */
	private static HashMap<Integer, Double[]> nodeCoords = new HashMap<Integer, Double[]>();
	
	
	/**
	 * Storing the users requested Location
	 */
	private static String requestedLoc;
	/**
	 * Storing the users requested Date
	 */
	private static String requestedDate;
	
	
	/**
	 * storing displayed information about wait times
	 */
	private static DisplayDay curDayDisplay = new DisplayDay();
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Results() {
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//try and retrieve the selected location from form
		if (request.getParameter("location") != null)
			requestedLoc = request.getParameter("location");
		else
			requestedLoc = here.toString();
		
		//Try and retrieve date from form
		if (request.getParameter("date") != null)
			requestedDate = request.getParameter("date");
		
		//In the case of form retrieved date is null
		//Set default selected date to current date
		if (requestedDate == null || request.getParameter("show_today") != null){
			DateFormat df = new SimpleDateFormat("MM-dd");
			Date dobj = new Date();
			requestedDate = df.format(dobj);
		}
			
		
		if (request.getParameter("showing_nearby") != null){
			here = there;
			there = null;
		} else {
			here = Crossing.valueOf(requestedLoc); //getting appropriate enum boarder crossing constant for selected location
		}
		
		int curIndex = here.index;
		
		if (curDayDisplay.getLocation() != curIndex || request.getParameter("date") != null)
			curDayDisplay = 						
					new DisplayDay(curIndex, 
							crossing[curIndex].getWaitTimes(requestedDate));
		
		if (request.getParameter("sort") != null){
			if (request.getParameter("sort").equals("interval_ascending"))
				{ curDayDisplay.sortByHours(); request.setAttribute("sortDir", 0); }
			else if (request.getParameter("sort").equals("wait_time_ascending"))
				{ curDayDisplay.sortByWaitTimes(); request.setAttribute("sortDir", 3); }
			else if (request.getParameter("sort").equals("wait_time_descending"))
				{ curDayDisplay.sortByWaitTimes(); request.setAttribute("sortDir", 2); }
			else
				{ curDayDisplay.sortByHours(); request.setAttribute("sortDir", 1); }
		} else if (request.getParameter("sort") == null && curDayDisplay.getSortedBy() != 0)
				{ curDayDisplay.sortByHours(); request.setAttribute("sortDir", 0); }
		
		sp.pathsFrom(here.node);
		
		String path = "{}";
		
		//Setting attributes related to mapping depending on button states
		if (request.getParameter("find_nearby") != null){
			there = null;
			int toNode = sp.closestCrossing(here.node);
			if (toNode != -1){
				there = Crossing.fromNode(toNode);
				path = sp.nodeCoordsConnecting(nodeCoords, here.node, toNode);
				request.setAttribute("btnActivated", "true");
				request.setAttribute("distCrossing", String.valueOf(sp.lengthBetweenKM(here.node, toNode)));
				request.setAttribute("nextCrossing", there.stringName);
			} else {
				request.setAttribute("btnActivated", "error");
			}
		} else {
			request.setAttribute("btnActivated", "false");
		}
		
		
		//Set rest of attributes to be passed to index.jsp
		request.setAttribute("curCoord", here.coord() );
		request.setAttribute("path", path);
		request.setAttribute("dates", crossing[curIndex].getNearByDates(requestedDate));
		request.setAttribute("curDate", requestedDate);
		request.setAttribute("curLocation", here.stringName);
		request.setAttribute("DisplayTimes", curDayDisplay.getDisplayTimes());
		
		//pass responsibility to index.jsp
		request.getRequestDispatcher("results.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		context=getServletContext();
        
		doInit();					//initialization of data
		
		doGet(request, response);
	}
	
	/**
	 * Called once, after do post from index html page form submission, to initialize data on server
	 * initializes array "crossing" sized 26 (26 Boarder Crossings) of type Entry
	 * finish by calling the readFiles method.
	 */
	private void doInit(){
		
		for (int i = 0; i < 26; i++){	//Entry initializer empty constructor
			crossing[i] = new Entry();
		}
		
		curDayDisplay = new DisplayDay();
		
		readFiles();					//call function to read all data files
	}
	
	/**
	 * Reads data from csv files
	 * <p>
	 * Takes data roads.csv file and initializes Shortest Path object.
	 * <p>
	 * Takes data nodes.csv file and initializes a hash map of where Key is the node id
	 * and value is the coordinates of that note in latitude and longitude. Stored as a Array of doubles.
	 * <p>
	 * Takes data Q1.csv to Q4.csv and stores each entry into the Entry array where the array elements 
	 * are Entry objects representing a specific boarder crossing.
	 */
	private void readFiles(){
		//Read from road dataset
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(context.getResourceAsStream("/WEB-INF/data/roads.csv")))){
			ArrayList<String[]> mapIn = new ArrayList<String[]>();
			//get number of vertices and edges
			int mapNumN = Integer.parseInt(bReader.readLine());
			int mapNumR = Integer.parseInt(bReader.readLine());
			
			//read line by line
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] temp = line.split(",");
				String[] tempVal = new String[3];
				tempVal[0] = temp[0];
				tempVal[1] = temp[1];
				tempVal[2] = temp[2];
				mapIn.add(tempVal);
			}
			sp = new ShortestPaths(new RoadMap(mapNumN, mapNumR, mapIn));	//Initialize Shortest Path with map data roadByCrossings
		//catch file not found exceptions
		} catch (FileNotFoundException e) {
			System.out.println("Roads File Not Found");
		//Catch any other io exception
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read from road dataset
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(context.getResourceAsStream("/WEB-INF/data/nodes.csv")))){
			//get number of nodes set array of coords to that size
			int numN = Integer.parseInt(bReader.readLine());
			
			//read line by line track index
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] temp = line.split(",");
				//1st val in line == node, 2nd == latitude, 3rd == longitude
				int nodeID = Integer.parseInt(temp[0]);
				Double[] coords = new Double[] {Double.parseDouble(temp[2]),Double.parseDouble(temp[1])};
				
				//put key and value into hashmap
				nodeCoords.put(nodeID, coords);
			}
		//catch file not found exceptions
		} catch (FileNotFoundException e) {
			System.out.println("Nodes File Not Found");
		//Catch any other io exception
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read all data sets for boarder wait times
		for(int i = 1; i < 5; i++){
	    	try (BufferedReader bReader = new BufferedReader(new InputStreamReader(context.getResourceAsStream("/WEB-INF/data/Q"+ i +".csv")))){
	    		String line = bReader.readLine();
	    		//Step through lines of all boarder wait time datasets
	    		while ((line = bReader.readLine()) != null) {
	    			//split up the data appropriately
	    			String [] temp = line.split(",");
	    			String [] timeTemp = temp[3].split("[^a-zA-Z0-9']+");
	    			String eDate = timeTemp[1] + "-" + timeTemp[2]; 
	    			//if wait time data a number store the value
	    			//else must be missed entry or no wait time set to 0
	    			int waitTime;
	    			try {
	    				waitTime = Integer.parseInt(temp[5]);
	    			} catch (NumberFormatException e) {
	    				waitTime = 0;
	    			}
	    			
	    			//get index of selection using enum
	    			int crossIndex = Crossing.indexFromString(temp[0]);
	    			
	    			//Update entry for selected location, for read date, time and wait time
	    			crossing[crossIndex].addEntry(eDate, Integer.parseInt(timeTemp[3]), waitTime);
	    			
	    		}
	    	//Catch file not found exception
	    	} catch (FileNotFoundException e) {
				System.out.println("Q" + i +"File Not Found");
			//Catch any other io exception
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

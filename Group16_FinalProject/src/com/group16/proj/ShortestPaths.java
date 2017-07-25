/**
 * 
 */
package com.group16.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.IndexMinPQ;

/**
 * Class for performing shortest path opperations on custom undirected road map
 * Using Dijkstras algorithm for shortest path based on algorithm taught in class text book
 * Algorithms 4th Edition
 * 
 * @author alexgotsko
 */
public class ShortestPaths {
	
	/**
	 * 2D array of roads to origin(Edge type) weighted edge bidirectional
	 */
	private Edge[][] roadTo;
	/**
	 * 2D array of lengths to orgin(Double) 
	 */
	private double[][] lengthTo;
	/**
	 * Priority Queue imported from the ALGS4 library
	 * Algorithms 4th edition is 
	 */
	private IndexMinPQ<Double> pq;
	/**
	 * RoadMap (EdgeWeightedGraph) stores adjacency lists of the roads(edges) from the data set.
	 * RoadMap is heavily based off of the EdgeWeightedGraph class from the Algorithms Fourth Edition by Robert Sedgewick 
	 * and Kevin Wayne, a text book used in class.
	 */
	private final RoadMap RM;
	
	/**
	 * Constructor for ShortestPaths
	 * <br>
	 * Takes a RoadMap Graph of roads connecting to each other, and initializes a Shortest path object 
	 * to be later used in processing the shortest path from each BorderCrossing Node.
	 * <p>
	 * Saves input RoadMap rm to RoadMap RM
	 * <br>
	 * Initiates roadsTo 2d array of Edges, where [i][] is the index of the boarder crossing, [][i] is the array of roads and where where they lead to, optamized for the shortest path to the original node.
	 * <br>
	 * Initiates LengthTo 2d array of lengths, corresponding to the road to 2d array, where [i][] is the index of the border crossing, [][i] is the array of the lengths of each road corrisponding to the roadsTo 2d array
	 * <br>
	 * Steps through the lengths to array and initializes the length values to Infinity to start.
	 * @param rm
	 * RoadMap type - graph of all the roads and the adjacency lists of what they connect to. 
	 */
	public ShortestPaths(RoadMap rm){
		//set map
		RM = rm;
		
		//init Road arrays and Double arrays to store all roads and the collected lengths stemming from origin
		roadTo = new Edge[26][RM.N()+1];
		lengthTo = new double[26][RM.N()+1];
		
		//init all lengths to origin to infinity
		for (int i = 0; i < 26; i++)
			for (int n = 0; n < RM.N(); n++)
				lengthTo[i][n] = Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Method that takes int origin, a origin node id, and searches and stores the shortest paths from that node to all reachable nodes in the RoadMap graph.
	 * <p>
	 * First it checks to see if the node is on the map since some Border crossings where not included in the custom map data as they are not located close to any of the other nodes, and including them would make the custom graph data set very large.
	 * <br>
	 * If node is on map, create a new priority queue to store a list of roads lowest weights first.
	 * <br>
	 * get index of the origin node from the index of the border crossing, used since we are storing paths from each crossing.
	 * <br>
	 * Start building the pathsTo origin list for current crossing by initializing the priority queue for that node. Stepping through the edges adjacent to that node using vertex relaxation to find the eligible roads.
	 * 
	 * @param origin
	 * Integer Node ID of the origin node
	 */
	public void pathsFrom(int origin){
		//not when node is not on map
		if (origin != 0) {
			//init Priority queue
			pq = new IndexMinPQ<Double>(RM.N());
			
			//get index from node id
			int indx = Crossing.indexAtNode(origin);
			
			lengthTo[indx][origin] = 0.0;

	        // relax vertices in order of distance from s
	        pq.insert(origin, lengthTo[indx][origin]);
	        while (!pq.isEmpty()) {
	            int min = pq.delMin();
	            for (Edge r : RM.adj(min))
	                relax(r, min, indx);
	        }
		}
		
	}
	
	/**
	 * Vertex relaxation based off of the Relax Method found in Algorithms 4th edition by robert sedgewick textbook.
	 * <br>
	 * Checks to see if the path to the node, on the other end of the current edge, length is greater than the path length to the current node.
	 * <br>
	 * If it is it will add the edge to the pathTo array and the length to the end node to the lengthsTo array.
	 * Then it will update the priority queue accordingly.
	 * 
	 * @param r
	 * Edge type road
	 * @param n
	 * Integer node which represents what end of the edge we are starting at
	 * @param indx
	 * Integer index representing what border crossing we are looking at
	 */
	private void relax(Edge r, int n, int indx) {
        int b = r.other(n);
        if (lengthTo[indx][b] > lengthTo[indx][n] + r.weight()) 
        {
        	lengthTo[indx][b] = lengthTo[indx][n] + r.weight();
        	roadTo[indx][b] = r;
        	
            if (pq.contains(b)) pq.decreaseKey(b, lengthTo[indx][b]);
            else                pq.insert(b, lengthTo[indx][b]);
        }
    }
	
	/**
	 * Return the total length of the shortest road path between origin node and destination node in meters
	 * 
	 * @param origin
	 * Integer Node ID of the origin node
	 * @param dest
	 * Integer Node ID of the destination node
	 * @return
	 * Double type length in meters of the path between the origin node and the destination node
	 */
	private double lengthBetweenM(int origin, int dest){ return lengthTo[Crossing.indexAtNode(origin)][dest]; }
	/**
	 * Return the total length of the shortest road path between origin node and destination node in kilometers
	 * 
	 * @param origin
	 * Integer Node ID of the origin node
	 * @param dest
	 * Integer Node ID of the destination node
	 * @return
	 * Double type length in kilometers of the path between the origin node and the destination node
	 */
	public double lengthBetweenKM(int origin, int dest){ return ((int) lengthTo[Crossing.indexAtNode(origin)][dest]/1000.00); }
	
	/**
	 * Checks to see if the origin and the destination node has a road path that connects them
	 * 
	 * @param origin
	 * Integer Node ID of the origin node
	 * @param dest
	 * Integer Node ID of the destination node
	 * @return
	 * Boolean true or false if the nodes are connected by a shortest path
	 */
	private boolean roadsConnect(int origin, int dest) { return (lengthTo[Crossing.indexAtNode(origin)][dest] < Double.POSITIVE_INFINITY); }
	
	//====================PATH OUTPUTS==============================
	
	
	/**
	 * First checks to see if the origin node and destination node are connected by a path. 
	 * <br>
	 * If they are connected it then calls the nodes connecting function that returns a list of nodes along the shortest path.
	 * <br>
	 * It then steps through the list of nodes, searches a set of nodes and corresponding coordinates and concatenates the coordinates from the iterated node to the output string using a format which will allow for easy javascript integration on the results page.
	 * 
	 * @param nodeCoords
	 * A set of Node Id - Keys (type - Integer), and 
	 * @param origin
	 * Integer Node ID of the origin node
	 * @param dest
	 * Integer node id of the destination node
	 * @return
	 * Formated string of nodes that contain all the coordinates of the nodes on the shortest path between the origin and destination nodes.
	 */
	public String nodeCoordsConnecting(HashMap<Integer, Double[]> nodeCoords, int origin, int dest){
		//Start building a string to store each node along the paths coordinites
		String sFormat = "{lat: %f, lng: %f},\n";
		String sBuild = "[";
		
		if (!roadsConnect(origin, dest)) return null;	
		for (int node : nodesConnecting(origin, dest)){
			//SEARCH hashmap for current node
			Double[] curCoords = nodeCoords.get(node);
			
			sBuild = sBuild + String.format(sFormat, curCoords[0], curCoords[1]);
		}
		
		sBuild = sBuild.substring(0, sBuild.length() - 2) + "]";	//complete string ready for insert into javascript code
		
		return sBuild;
	}
	
	
	/**
	 * Checks and returns what nodes lie between the origin node and the destination node on the shortest path between them.
	 * <p>
	 * First it creates a array list to store the nodes, adding the destination node to the list.
	 * <br>
	 * Then it steps through each edge in the array of roadTo adding adding the end node of that road to the list of nodes and starting the next iteration at the edge that connects that node.
	 * <br>
	 * After gathering the list it reverses the order of the list getting an origin to destination list of nodes.
	 * <br>
	 * And returns the list of nodes. 
	 * 
	 * 
	 * @param origin
	 * Integer node id of the origin node from which the shortest path tree is stemming from
	 * @param dest
	 * Integer node id of the destination node
	 * @return
	 * Array List of Integers 
	 */
	private ArrayList<Integer> nodesConnecting(int origin, int dest){
		ArrayList<Integer> nodes = new ArrayList<Integer>();	//array list to store list of nodes along the path
		
        int x = dest;
        nodes.add(x);
        for (Edge	r = roadTo[Crossing.indexAtNode(origin)][dest]; 
        			r != null;
        			r = roadTo[Crossing.indexAtNode(origin)][x]) 
        {
            nodes.add(r.other(x));
            x = r.other(x);
        }
        Collections.reverse(nodes);
        return nodes;
	}
	
	
	/**
	 * Checking what Boarder Crossing is the closest to the origin node and returning the index id of that crossing
	 * 
	 * @param origin
	 * Integer of origin node id
	 * @return
	 * Integer Index id, of the Boarder Crossing with a node that has the shortest path to origin
	 */
	public int closestCrossing(int origin){
		int curIndex = -1;
		Double foundDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i<26;i++){	//iterate through all boarder crossing indexes
			if ( (origin != Crossing.nodeFromIndex(i)) && roadsConnect(origin, Crossing.nodeFromIndex(i)))
				if (lengthBetweenM(origin, Crossing.nodeFromIndex(i)) < foundDist)
					{curIndex = Crossing.nodeFromIndex(i); foundDist = lengthBetweenM(origin, Crossing.nodeFromIndex(i));}
		}
		return curIndex; //if no path is found return -1
	}
	
}

/**
 * 
 */
package com.group16.proj;

import java.util.ArrayList;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Edge;

/**
 * HEAVILY BASED OFF OF CODE FOR EdgeWieghtedGraph CODE FROM ALGORITHMS 4th EDITION by ROBERT SEDGEWICK, KEVIN WAYNE
 * TEXT BOOK USED IN CLASS
 * <p>
 * Takes an arraylist of roads (weighted edges) and steps through the data adding the road (edge) to an
 * adjacency list of edges since its an undirected data set it will add the edge to the adjacency list on both ends.
 * <br>
 * This roadmap(graph) will be used by the ShortestPaths Class
 * <p>
 * HAD TO MODIFY FOR USE WITH MY CUSTOM GRAPH DATASET AS ALGS4 INLUDES ONLY A SPECIFIC WAY TO INPORT DATA INTO THE
 * CLASS. WORKING WITH SERVLETS I WAS ONLY ABLE TO RELIABLY RETREAVE AND READ DATA NO MATTER THE LOCATION OF THE DATA 
 * AS A INPUT STEAM AND I WAS NOT ABLE TO MODIFY THE DATA TO MATCH 
 * 
 * @author ALGORITHMS 4th EDITION by ROBERT SEDGEWICK, KEVIN WAYNE - Modified by Alex Gotsko
 */
public class RoadMap {
	private final int N;
    private int R;
    private Bag<Edge>[] adj;
    
    @SuppressWarnings("unchecked")
	public RoadMap(int N, int R, ArrayList<String[]> roads){
	    this.N = N;
	    this.R = R;
	    adj = (Bag<Edge>[]) new Bag[N+1];
	    for (int n = 0; n < N+1; n++)
	    	adj[n] = new Bag<Edge>();
	    
	    for (int r = 0; r < R; r++) {
	       int a = Integer.parseInt(roads.get(r)[0]);
	       int b = Integer.parseInt(roads.get(r)[1]);
	       double weight = Double.parseDouble(roads.get(r)[2]);
	       Edge newR = new Edge(a, b, weight);
	       addRoad(newR);
	    }
    }
    
    public int N() {  return N;  }
    public int R() {  return R;  }
    
    public void addRoad(Edge r){
	   int a = r.either(), b = r.other(a);
	   adj[a].add(r);
	   try{
	   adj[b].add(r);
	   } catch (NullPointerException e) {
		   System.out.println(a + "    " + b);
	   }
	   R++;
    }
    
    public Iterable<Edge> adj(int n)
    {  return adj[n];  }
}

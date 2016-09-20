/* Charles Christen & Adrian Valente
This class implements a HashMaps-based representation of Graphs.
Vertices of the Graph are numbered from 0 to nbVertices-1.
Each vertex is mapped with a HashMap representing its favorite paths, sorted by order of preference.
Directed edges are represented by mapping the source-vertex's number to the target-vertex's one.
Each vertex may be attributed a path, identified by its position in the vertex's "wish list".
For convenience, we add functions to make a copy of a GraphB, to add a path to it, to test its consistency and stability, and to output it.
*/

import java.util.*;

class Graph {

	int nbVertices; //0 is included in the number of vertices. However, 0 is not a key in the following maps
	HashMap<Integer,Integer> edges;  //indicates the only vertex j to which i points
	HashMap<Integer,Integer> paths; //indicates the number of the path affected to i in i's preference list.
	HashMap<Integer,Path> routes;  //gives a pointer to the path currently affected to each vertex
	HashMap<Integer,HashMap<Integer,Path>> tuples;  //the inner HashMap gives all paths in a vertex's preference list, sorted by increasing order of preference
	
	//Constructor
	Graph(int k, HashMap<Integer,Integer> map, HashMap<Integer,Integer> p, HashMap<Integer,Path> r, HashMap<Integer,HashMap<Integer,Path>> t){
		nbVertices = k;
		edges = map;
		paths = p;
		routes = r;
		tuples = t;
	}

	public Graph copy(){
		int k = nbVertices;
		HashMap<Integer,Integer> map = (HashMap<Integer,Integer>) edges.clone();
		HashMap<Integer,Integer> p = (HashMap<Integer,Integer>) paths.clone();
		HashMap<Integer,Path> r = (HashMap<Integer,Path>) routes.clone();
		Graph res = new Graph(k,map,p,r,tuples);
		return res;
	}
	
	//Method that enables us to reduce the information on a graph, allowing for a faster treatment 
	public Graph pathCompression(){
		HashMap<Integer,HashMap<Integer,Path>> tup = new HashMap<Integer,HashMap<Integer,Path>>();
		for (int i=1; i<nbVertices; i++)
			tup.put(i, new HashMap<Integer,Path>());
		
		//We look at all the paths of each point
		for (int i=1; i<nbVertices; i++){
			int j=0;
			for (int k=0; k<tuples.get(i).size(); k++){
				Path p = tuples.get(i).get(k);
				//We first check if path p has a meaning
				boolean existingPath = true;
				Path c = p.suivant;
				while (c.current != 0 && existingPath){
					existingPath = false;
					//Check existence of path c
					for (int k2=0; k2<tuples.get(c.current).size(); k2++){
						Path d = tuples.get(c.current).get(k2);
						if (d.equals(c)){
							existingPath = true;
							break;
						}
					}
					c = c.suivant;
				}
				
				//We add the path to the tuples structure
				if (existingPath){
					if (p.suivant.current == 0){
						tup.get(i).put(j, p);
						j++;
					}
					else{
						//We transform p.suivant into a pointer to the path structure of the vertex p.suivant.current
						for (int k2=0; k2<tuples.get(p.suivant.current).size(); k2++){
							Path d = tuples.get(p.suivant.current).get(k2);
							if (d.equals(p.suivant)){
								p.suivant = d;
								break;
							}
						}
						tup.get(i).put(j,p);
						j++;
					}
				}
			}
		}
		return new Graph(nbVertices,edges,paths,routes,tup);
	}

	
	//affects a path to a vertex
	public void add(Path p, int k){
		Path c = p;
		paths.put(p.current,k); //the path number k is attributed to the vertex p.current
		while (p!=null) {       //and the corresponding edges are added to the GraphB
			if (p.suivant!=null){
				edges.put(p.current,p.suivant.current);
				routes.put(p.current, p);
			}
			p = p.suivant;
		}
		p = c;
	}
	
	
	//2 conditions in order to be consistent : 
	//current point must have a path either undefined or equal to the path we're testing
	//the same for next point in the path we're testing (which can also be 0)
	public Boolean consistent(Path p){
		if (p==null) return true;
		//We first check if current point hasn't got another path defined yet
		if (routes.containsKey(p.current) && routes.get(p.current) != p)
			return false;
		//If the next point is 0, the path is consistent
		if (p.current == 0 || p.suivant.current == 0)
			return true;
		//We check the next point's defined route (if it exists) that must be equal to p.suivant
		if (routes.containsKey(p.suivant.current) && routes.get(p.suivant.current) == p.suivant)
			return true;
		//If the next point hasn't got a defined route yet, we have to test the consistency of p.suivant
		if (paths.get(p.suivant.current) == -1)
			return consistent(p.suivant);
		//The last possibility is that the next point has another defined route than p.suivant
		return false;
	}


	//We test the stability by testing the consistency of the more preferred paths in the preference list of each vertex
	public Boolean stability(){
		for (int v : edges.keySet()){ //for all vertices
			int k = paths.get(v);
			if (k!=0) { //if they were not attributed their most preferred path
				for (int j=k-1; j>=0; j--){
					if (consistent(tuples.get(v).get(j).suivant)) return false;
				}
			}
		}
		return true;
	}
	
	
	public void output(){
		System.out.println("Stable solution found :D");
		for (int v : edges.keySet())
		{
			System.out.println("For Vertex "+v+" path "+routes.get(v));
		}
	}
	
}




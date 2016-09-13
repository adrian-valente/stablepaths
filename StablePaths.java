/* Charles Christen & Adrian Valente
This is the main class, where the algorithm is implemented. We make use of a recursive auxiliary function in order to make a partial tree-like routing.
The main function then transforms the .txt file passed in argument into a graph as defined in the Graph class.
*/

import java.util.*;
import java.io.*;

class StablePaths {
	static int compteur = 0; //is used to know how many consistency tests we make
	static long begin = System.currentTimeMillis();

	
	public static void stablePathsFinder(Graph g) {
   		Graph solution = Aux(g,1);
    		if (solution != null)	
    			solution.output();
    		else 
    			System.out.println("no stable solution found");
    		System.out.println(compteur+" consistency tests made.");
	}

	
	public static Graph Aux(Graph entry, int i) {
		Graph g = entry.copy();
   		Graph solution = null;
		HashMap<Integer,Path> tab = g.tuples.get(i);
		//We try each path in the vertex i's preference list out
    		for (int k=0; k<tab.size(); k++) {
    			compteur++;
    			Path p = tab.get(k);
        		if (g.consistent(p)) {
       				g.add(p,k);
            		if (i < (g.nbVertices-1)) {  //if i is not the last vertex
                		solution = Aux(g,i+1);
            		}
           			else {  //if i is the last vertex
      			        if (g.stability()) solution = g;
               		    break;  //Indeed for the last vertex, if there is a stable solution, then it's the best consistent one.
           			}
           			if (solution != null)
           				break;
           			g = entry.copy();  //removes the path we've just tried out
        		}
		}
		return solution;
	}

	public static void main(String[] args) {
		//transforms the .txt file representing a graph in a Graph object and launches StablePathsFinder
		try {
			File f = new File(args[0]);
    			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			try {
    			String line = br.readLine();
				HashMap<Integer,HashMap<Integer,Path>> stock= new HashMap<Integer,HashMap<Integer,Path>>();
				line = br.readLine();
				int k=0;

				while (line!=null){
					Path p = new Path(0,null);
					String number = "";
					for (int i=line.length()-3; i>=0; i--){
						if (line.charAt(i)!=' ') number = line.charAt(i)+number;
						else 
						{ 
							p.add(Integer.parseInt(number));
							number = "";
						}
					}
					p.add(Integer.parseInt(number));
					//System.out.println(p);
					HashMap<Integer,Path> m;
					HashMap<Integer,Path> previousm = stock.get(Integer.parseInt(number));
					if (previousm!=null){
						m = previousm;
					}
					//Starting a new vertex's preference list.
					else{
						m = new HashMap<Integer,Path>();
						k=0;
					}
					m.put(k,p);
					k++;
					stock.put(Integer.parseInt(number),m);
					line = br.readLine();
				}
				
				int s = stock.size();
				HashMap<Integer,Integer> e = new HashMap<Integer,Integer>();
				for (int i : stock.keySet()){
					e.put(i,i);
				}
				HashMap<Integer,Integer> p = new HashMap<Integer,Integer>();
				for (int i : stock.keySet()){
					p.put(i,-1);
				}
				HashMap<Integer,Path> routes = new HashMap<Integer,Path>();
				Graph g = new Graph(s+1,e,p,routes,stock);
				
				
				Graph gb = g.pathCompression();
				stablePathsFinder(gb);
				
			}
			catch (IOException exception) {
    				System.out.println ("Reading error :" + exception.getMessage());
			}
		}
		catch (FileNotFoundException exception) {
    			System.out.println ("File not found");		
		}
		System.out.println("time "+(System.currentTimeMillis()-begin));
	}
	
}

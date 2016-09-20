/* Charles Christen & Adrian Valente
This class is used to test out our algorithm by randomly generating a Graph of given number of vertices as well as maximal length of the vertices whish lists.
*/

import java.util.*;
import java.io.*;

public class RandomGraphGenerator {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);  //Number of vertices
		int l = Integer.parseInt(args[1]);  //Maximal number of paths per vertex
		HashSet<Integer> integers = new HashSet<Integer>();  //integers from 1 to n-1
		for (int i=1; i<n; i++)
		{
			integers.add(i);
		}
		
		try{
			FileWriter fw = new FileWriter(args[2]);
			fw.write("Graphe genere par RandomGraphGenerator\n");
			
			//Creation of the paths for vertex i
			for (int i=1; i<n; i++)
			{
				int li = (int) Math.floor(Math.random()*(l+1.)); //Number of preferred paths for this vertex
				LinkedList<Path> list = new LinkedList<Path>();
				//Creation of each path :
				//We pick a vertex at random (and don't put it back) in the whole pack of available vertices until
				//finding vertex i, therefore defining a path from i to 0
				//We moreover demand that the li paths are distinct.
				for (int j=0; j<li; j++)
				{
					HashSet<Integer> verticesLeft = new HashSet<Integer>(integers);
					Path p = new Path(0,null);
					int v = 0;
					boolean cheminCree = true;
					while (p.current != i)
					{
						while (!verticesLeft.contains(v))
						{
							v = 1 + ((int) (Math.floor(Math.random()*(n-1.)))); //Choice of a random vertex among the remaining ones
						}
						p = new Path(v,p);
						verticesLeft.remove(v);
					}
					for (Path q : list)
						if (p.equals(q)){
							cheminCree = false;
							break;
						}
					if (!cheminCree)
						j--; //Inelegant but operational
					else
						list.add(p);
				}
				//We now write down paths into the .txt file.
				for (Path p : list)
				{
					fw .write(p.toString()+"\n");
				}
			}
			fw.close();
		}catch(IOException e){
			System.out.println("Erreur "+e);
		}
	}

}

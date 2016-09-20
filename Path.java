/* Charles Christen & Adrian Valente
This class implements a simple list-representation of paths.
For convenience, functions to add a vertex, test equality and convert a path into a String are added.
*/

class Path {

	int current;
	Path suivant;

	Path(int v){
		current = v;
		suivant = null;
	}

	Path(int v, Path s){
		current = v;
		suivant = s;
	}

	public void add(int v){
		suivant = new Path(current,suivant);
		current = v;
	}
	
	public String toString(){
		Path c = this;
		String str = new String();
		while (c != null)
		{
			str += c.current;
			c=c.suivant;
			if (c != null)
				str += " ";
		}
		return str;
	}
	
	public boolean equals(Path p){
		Path a = this;
		Path b = p;
		while (a.current == b .current && a.suivant != null && b.suivant != null)
		{
			a = a.suivant;
			b = b.suivant;
		}
		if (a.suivant != null || b.suivant != null)
			return false;
		return true;
	}
}

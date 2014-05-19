
public class Word implements Comparable<Word> {
	String name;
	double tfIdf;
	Word(String name,double tfIdf){
		this.name=name;
		this.tfIdf=tfIdf;
	}
	public int compareTo(Word other){
		if (this.tfIdf>other.tfIdf)
			return 1;
		else if(other.tfIdf>this.tfIdf)
			return -1;
		else return 0;
		
	}
	
}
import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocParser {
	
	static List words[]= new List[(int) Math.pow(10, 6)];
	static Word finalWord[];
	static int wordCount=0;
	static double frequencyTotal=0;
	public  static String[] parseDoc(String url) throws IOException {
		
		//we check paragraphs, anchors and h1, h2, h3
		
		Document doc = Jsoup.connect(url).get();
		Elements anchor = doc.select("a");
		
		Elements paragraphs = doc.select("p");
		Elements head1 = doc.select("h1");
		Elements head2 = doc.select("h2");
		Elements head3 = doc.select("h3");
		Elements button = doc.select("button");
		Elements placeholder = doc.select("input[placeholder]");
		String elementsText[];
	
		WordDatabase.initDatabase();
		
	
		
		int i = 0;
		elementsText = new String[paragraphs.size()+anchor.size()+head1.size()+head2.size()+head3.size()+button.size()+placeholder.size()];
		
		for (Element p : anchor) {
			elementsText[i++] = p.text();
		}
		
		for (Element p : button) {
			elementsText[i++] = p.text();
		}
		
		for (Element p : placeholder) {
			System.out.println(p.attr("placeholder"));
			elementsText[i++] = p.attr("placeholder");
		}
		
		
		for (Element p : paragraphs) {			
			elementsText[i++] = p.text();
			
		}
		for (Element p : head1) {
				elementsText[i++] = p.text();	
		}
		for (Element p : head2) {			
				elementsText[i++] = p.text();
		}
		for (Element p : head3) {			
				elementsText[i++] = p.text();
		}
		
		split(elementsText);
		finalWord=new Word[wordCount];
		int count=0;
		for (int j=0;j<words.length;j++){
			if (words[j]!=null && WordDatabase.idf[j]>0){ //make sort quicker
				double value=(1+Math.log10(words[j].size))*WordDatabase.idf[j];
				finalWord[count]=new Word(words[j].first.name,value);
				count++;
				
			}
		}
		
		Arrays.sort(finalWord,0,count);

		String result[]=new String[5]; 
//		for (int j=0;j<count;j++){
//			System.out.println(finalWord[j].name+" "+finalWord[j].tfIdf);
//		}
		int k=0;
		for (int j=count-1;j>=count-5;j--){		
			result[k]=finalWord[j].name;
			k++;
		}	
		return result;
		
		
	}

	


	
	private static void split(String[] arr){
		for(int i = 0; i < arr.length; i++){
			String temp = "";
			for(int j = 0; j < arr[i].length(); j++){
				if((arr[i].charAt(j)>=65 && arr[i].charAt(j)<=90) || (arr[i].charAt(j)>=97 && arr[i].charAt(j)<=122) || arr[i].charAt(j)=='\''){
					temp += arr[i].charAt(j);
				}
				else{
					if(!temp.equals("")){
						temp=temp.toLowerCase();
					
						
						int hash=(int) ((temp.hashCode()& 0x7fffffff)%words.length);
						if (words[hash]==null){
							words[hash]=new List();						
							wordCount++;
						}
						words[hash].add(temp);
						
					}
					temp = "";
				}
			}
		}
	}

}
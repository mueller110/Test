import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocParser {

	static Word finalWord[];
	static double frequencyTotal = 0;

	public static String[] parseDoc(String url) throws IOException {

		// we check paragraphs, anchors and h1, h2, h3

		Document doc = Jsoup.connect(url).get();
		Elements anchor = doc.select("a");

		Elements title = doc.select("title");
		Elements paragraphs = doc.select("p");
		Elements head1 = doc.select("h1");
		Elements head2 = doc.select("h2");
		Elements head3 = doc.select("h3");
		Elements button = doc.select("button");
		Elements placeholder = doc.select("input[placeholder]");
		Elements th = doc.select("th");
		Elements td = doc.select("td");
		String elementsText[];
		String titleHeadText[];

		WordDatabase.initDatabase();

		int size = td.size() + th.size() + paragraphs.size() + anchor.size()
				+ head2.size() + head3.size() + button.size()
				+ placeholder.size();

		int i = 0;
		int o = 0;
		elementsText = new String[size];
		titleHeadText = new String[title.size() + head1.size()];
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
			titleHeadText[o++] = p.text();
		}
		for (Element p : head2) {
			elementsText[i++] = p.text();
		}
		for (Element p : head3) {
			elementsText[i++] = p.text();
		}

		for (Element p : th) {
			elementsText[i++] = p.text();
		}
		for (Element p : td) {
			elementsText[i++] = p.text();
			int j = i - 1;
			for (Element x : p.children()) {
				if (x.tagName().equals("p") || x.equals("a")) {
					int y = elementsText[j].indexOf(x.text());
					String tmp = "";
					for (int k = 0; k < elementsText[j].length(); k++) {
						if (k < y || k > y + x.text().length()) {
							tmp += "" + elementsText[j].charAt(k);
						}
					}
					elementsText[j] = tmp;
				}
			}
		}
		for (Element p : title) {
			titleHeadText[o++] = p.text();
		}
		String result[] = new String[5];
		List elementSplit[] = new List[(int) Math.pow(10, 6)];
		List titleHeadSplit[] = new List[(int) Math.pow(10, 6)];

		int wordCount = split(elementsText, elementSplit);
		finalWord = new Word[wordCount];
		int count = 0;
		for (int j = 0; j < elementSplit.length; j++) {
			if (elementSplit[j] != null && WordDatabase.idf[j] > 0) { // make
																		// sort
				// quicker
				double value = (1 + Math.log10(elementSplit[j].size))
						* WordDatabase.idf[j];
				// System.out.println(words[j].first.name+" "+WordDatabase.idf[j]+" "+words[j].size+" "+value);
				finalWord[count] = new Word(elementSplit[j].first.name, value);
				count++;
			}
		}
		System.out.println(count);
		Arrays.sort(finalWord, 0, count);
		int k = 0;
		
		for (int j = count - 1; j >= count - 5; j--) {
			result[k] = finalWord[j].name;
			k++;
		}

		count = 0;
		wordCount = split(titleHeadText, titleHeadSplit);
		finalWord = new Word[wordCount];
		for (int j = 0; j < titleHeadSplit.length; j++) {
			if (titleHeadSplit[j] != null && WordDatabase.idf[j] > 0) { // make
																		// sort
				// quicker
				double value = (1 + Math.log10(titleHeadSplit[j].size))
						* WordDatabase.idf[j];
				// System.out.println(words[j].first.name+" "+WordDatabase.idf[j]+" "+words[j].size+" "+value);
				finalWord[count] = new Word(titleHeadSplit[j].first.name, value);
				System.out.println(finalWord[count].name+" "+finalWord[count].tfIdf );
				count++;
				
			}
			
		}
		
		
		
		Arrays.sort(finalWord, 0, count);
		String tmp = "";
		if (count != 0) {
			for (int x = count - 1; x >= 0; x--) {
				boolean found = false;
				tmp = finalWord[x].name;
				for (int m = 0; m < 4; m++) {
					if (tmp.equals(result[m])) {
						found = true;
					}
				}
				if (!found)
					break;
			}
			result[4] = tmp;
		}
		// for(int x = 0; x < elementsText.length; x++){
		// System.out.println(elementsText[x]);
		// }

		// for (int j=0;j<count;j++){
		// System.out.println(finalWord[j].name+" "+finalWord[j].tfIdf);
		// }

		return result;

	}

	private static int split(String[] arr, List[] words) {
		int wordCount = 0;
		for (int i = 0; i < arr.length; i++) {
			String temp = "";
			for (int j = 0; j < arr[i].length(); j++) {
				if ((arr[i].charAt(j) >= 65 && arr[i].charAt(j) <= 90)
						|| (arr[i].charAt(j) >= 97 && arr[i].charAt(j) <= 122)
						|| arr[i].charAt(j) == '\'') {
					temp += arr[i].charAt(j);
				} else {
					if (!temp.equals("")) {
						temp = temp.toLowerCase();

						int hash = (int) ((temp.hashCode() & 0x7fffffff) % words.length);
						if (words[hash] == null) {
							words[hash] = new List();
							wordCount++;
						}
						words[hash].add(temp);

					}
					temp = "";
				}
			}
		}
		return wordCount;
	}

}
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Board extends JPanel implements ActionListener {

	Timer timer;
	JButton parseDoc, search;
	JTextField url;
	JLabel outKeys;
	JTextArea outSearch,bestMatch;
	String keys[];
	JScrollPane scrollpane;
	
	Board(Dimension d) throws UnsupportedEncodingException, IOException {
		this.setLayout(null);

		parseDoc = new JButton("Check Document");
		search = new JButton("Search");
		parseDoc.setLocation(100, 100);
		parseDoc.setSize(200, 40);
		parseDoc.addActionListener(this);
		search.addActionListener(this);
		search.setLocation(100, 500);
		search.setSize(100, 50);
		this.add(parseDoc);
		this.add(search);

		url = new JTextField();
		url.setLocation(350, 110);
		url.setSize(250, 25);
		this.add(url);

		outKeys = new JLabel("Found Keys: ");
		outKeys.setLocation(100, 160);
		outKeys.setSize(500, 20);
		this.add(outKeys);
		
		bestMatch= new JTextArea("");
		bestMatch.setLocation(50, 180);
		bestMatch.setSize(700, 70);
		bestMatch.setLineWrap(true);
		bestMatch.setEditable(false);
		this.add(bestMatch);

		outSearch = new JTextArea("");
		outSearch.setLocation(100, 250);
		outSearch.setSize(600, 250);
		outSearch.setLineWrap(true);
		outSearch.setEditable(false);

		scrollpane = new JScrollPane(outSearch);
		scrollpane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setSize(600, 250);
		scrollpane.setLocation(100, 250);

		this.add(scrollpane);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == parseDoc) {
			try {
				keys = DocParser.parseDoc(url.getText());
				outKeys.setText("Found Keywords: " + keys[0] + " " + keys[1]
						+ " " + keys[2] + " " + keys[3] + " " + keys[4]);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (source == search) {
			try {
				if (keys.length > 0) {
					String result[] = search(0);
					for (int i = 0; i < result.length; i++) {
						if(result[i]!=null){
							outSearch.append(result[i] +" "+"\n");
							if (result[i].equals(url.getText())){
								bestMatch.setText("Match: "+result[i]);
							}
						}		
					}
					
				} else {
					outSearch.setText("No keywords found!!!");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String[] search(int mode) throws UnsupportedEncodingException,
			IOException {
		String google = "http://www.google.com/search?q=";
		String yahoo = "http://search.yahoo.com/search?p=";
		String search = "";
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";
		int numberOfResults = 100;
		int linkCount = 0;
		String topResults[] = new String[numberOfResults];

		for (int i = 0; i < keys.length; i++) {
			search += keys[i] + " ";
		}

		Elements links = null;
		switch (mode) {
		case 0:
			links = Jsoup
					.connect(
							google + URLEncoder.encode(search, "UTF-8")
									+ "&num=" + (numberOfResults))
					.userAgent(userAgent).get().select("li.g>h3>a");
			break;

		case 1:
			links = Jsoup
					.connect(
							yahoo + URLEncoder.encode(search, "UTF-8") + "&n="
									+ (numberOfResults)).userAgent(userAgent)
					.get().select("span.url");
			break;
		}
		if (links != null) {
			for (Element link : links) {
				String url;
				if (mode == 0) {
					url = link.absUrl("href");
					url = URLDecoder.decode(
							url.substring(url.indexOf('=') + 1,
									url.indexOf('&')), "UTF-8");
				} else {
					url = link.text();
				}
				topResults[linkCount] = url;
				linkCount++;

			}
		} else {
			System.out.println("No results found");
		}
		return topResults;
	}

}
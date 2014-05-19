import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.JFrame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String[] args) throws Exception {
		JFrame myFrame = new JFrame();
		Dimension d = new Dimension(800,600);
		myFrame.setSize(d);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		
		Board board = new Board(d);
		
		board.setSize(d);
		
		board.setVisible(true);
		board.setFocusable(true);
		myFrame.add(board);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		
		
	}
}
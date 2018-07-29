package jiexi;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class main {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Book book = new Book();
		book.setBookName("Thinking in Java");
		book.setPages(880);
		book.setPrice(68);
		book.setAuthor("Bruce Eckel");
		book.setIsbn("9787111213826");

		//Ä£°å·½·¨
		
		XMLFormatter xmlFormatter = new XMLFormatter();
		String result = xmlFormatter.formatBook(book);
		System.out.println(result);
		/*
		 * JSONFormatter jsonFormatter = new JSONFormatter(); result =
		 * jsonFormatter.formatBook(book); System.out.println(result);
		 */
	}
}

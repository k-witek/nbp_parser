package pl.parser.nbp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.InputStreamReader;
import java.lang.Math;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {

	public static void main(String args[]) {

		try {
			String date1 ="";
			String date2 ="";
			ArrayList<String> arrayOfPages = new ArrayList<String>();
			ArrayList<Double> arrayOfSellingRates = new ArrayList<Double>();
			double counter = 0 ;
			double sumOfBuyingRates = 0;
			double sumOfSellingRates = 0;
			double variance = 0;
			
			date1 = date1 + args[1].charAt(2) + args[1].charAt(3) + args[1].charAt(5)
					      + args[1].charAt(6) + args[1].charAt(8) + args[1].charAt(9);
			date2 = date2 + args[2].charAt(2) + args[2].charAt(3) + args[2].charAt(5)
					      + args[2].charAt(6) + args[2].charAt(8) + args[2].charAt(9);
			
			Scanner sc = new Scanner(new InputStreamReader(new URL("http://www.nbp.pl/kursy/xml/dir.txt").openStream()));
			
			while (sc.hasNextLine()) {
				String lineFromFile = sc.nextLine();
				if(Integer.parseInt(lineFromFile.substring(5)) >= Integer.parseInt(date1) &&
				   Integer.parseInt(lineFromFile.substring(5)) <= Integer.parseInt(date2)) {
						arrayOfPages.add(lineFromFile);
					}
			}
						
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			for (int i = 0; i < arrayOfPages.size(); i++) {
				if (arrayOfPages.get(i).charAt(0) == 'c' ) {
				Document doc = dBuilder.parse(new URL("http://www.nbp.pl/kursy/xml/" + arrayOfPages.get(i) + ".xml").openStream());
				NodeList nList = doc.getElementsByTagName("pozycja");
				for (int j = 0; j < nList.getLength(); j++) {
					Node nNode = nList.item(j);
					if (((Element) nNode).getElementsByTagName("kod_waluty").item(0).getTextContent().equals(args[0])) {
						sumOfBuyingRates += Double.parseDouble((((Element) nNode).getElementsByTagName("kurs_kupna").item(0).getTextContent().replace(',', '.')));
						sumOfSellingRates += Double.parseDouble((((Element) nNode).getElementsByTagName("kurs_sprzedazy").item(0).getTextContent().replace(',', '.')));
						arrayOfSellingRates.add(Double.parseDouble((((Element) nNode).getElementsByTagName("kurs_sprzedazy").item(0).getTextContent().replace(',', '.'))));
						counter++;
					}
					}
				}
			}
			
			for (int i = 0; i < arrayOfPages.size(); i++) {
				System.out.println(arrayOfPages.get(i));
			}
			
			double averageOfBuyingRates = sumOfBuyingRates / counter; 
			double averageOfSellingRates = sumOfSellingRates / counter;
			
			for (int i = 0; i < arrayOfSellingRates.size(); i++) {
				variance += ((arrayOfSellingRates.get(i)-averageOfSellingRates) 
						   * (arrayOfSellingRates.get(i)-averageOfSellingRates));
			}
			
			System.out.printf("%.4f", averageOfBuyingRates);
			System.out.printf("%n%.4f", Math.sqrt(variance/arrayOfSellingRates.size()));
			
			sc.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

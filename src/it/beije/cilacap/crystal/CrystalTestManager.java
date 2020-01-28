package it.beije.cilacap.crystal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class CrystalTestManager {
	public static final char[] CARATTERI_AMMESSI = {'A','a','B','b','C','c','D','d','E','e','F','f','G','g','H','h','I','i',
													'J','j','K','k','L','l','M','m','N','n','O','o','P','p','Q','q','R','r'
													,'S','s','T','t','U','u','V','v','W','w','X','x','Y','y','Z','z',
													'0','1','2','3','4','5','6','7','8','9'
													,'.','+','-','*','\\','/','%','(',')','=',' ','[',']','{','}',':',';','<','>',','};
	public TestData readTestFile(String pathFile) throws Exception{
		
		File f = new File(pathFile);
				
		TestData tests = new TestData();
		String[] arrayElementi = null;
		
		int inReadSeq=0, inWriteSeq=0;
		int inReadRan=0, inWriteRan=0;
		
		try {
			FileReader fileReader = new FileReader(f);		
			BufferedReader reader = new BufferedReader(fileReader);
			
			String row;
			String[] setAttribute;
		
			int indexWhile = 0;
			int indexRead = 0;
			int indexWrite = 0;
			
			while ((row = reader.readLine()) != null) {
				
				
				row = pulisciSpazi(row.trim());
				setAttribute = row.split(Character.toString((char)Character.SPACE_SEPARATOR));
				
				setAttribute=setAttribute[0].split(" ");
				
				
				if (setAttribute[0].contentEquals("CrystalDiskMark")) {
					
					tests.setVersion(setAttribute[1]  + " " + setAttribute[2]);
					System.out.println("Version: " + tests.getVersion());
				}
				
				if (setAttribute[0].contentEquals("Test:")) {
					
					tests.setType(setAttribute[1]  + " " + setAttribute[2]);
					System.out.println("Type: " + tests.getType());
					
					tests.setIterations((int)setAttribute[3].charAt(2));
					System.out.println("Iterations: " + tests.getIterations());
				
					tests.setInterval(setAttribute[5]  + " " + setAttribute[6].replace("]", ""));
					System.out.println("Interval: " + tests.getInterval());
				}
				
				if (setAttribute[0].contentEquals("Date:")) {
					
					tests.setDate(setAttribute[1]  + " " + setAttribute[2]);
					System.out.println("Date: " + tests.getDate());
					tests.setIdComputer(tests.getDate().trim().replace("/", "").replace(":", "").replace(" ", ""));
					System.out.println("Id Computer: " + tests.getIdComputer());
				}
				
				if (setAttribute[0].contentEquals("OS:")) {
					
					tests.setOs(setAttribute[1]  + " " + setAttribute[2]);
					System.out.println("OS: " + tests.getOs());
				}
				
				if (setAttribute[0].contentEquals("Sequential") && inReadSeq > 0) {
					
					tests.getRead().add(new TestRow());
					
					tests.getRead().get(indexRead).setType(setAttribute[0]  + " " + setAttribute[1]);
					System.out.println("Read"+indexRead+" -Type: " + tests.getRead().get(indexRead).getType());
					
					tests.getRead().get(indexRead).setQ(Integer.parseInt(row.split("Q=")[1].trim().split(",")[0].trim()));
					System.out.println("Read"+indexRead+"-Q: " + tests.getRead().get(indexRead).getQ());
					
					tests.getRead().get(indexRead).setT(Integer.parseInt(row.split("T=")[1].trim().split("\\)")[0].trim()));
					System.out.println("Read"+indexRead+" -T: " + tests.getRead().get(indexRead).getT());
					
					tests.getRead().get(indexRead).setMbs(Double.parseDouble(row.split(":")[1].trim().split("MB")[0].trim()));
					System.out.println("Read"+indexRead+" -MB/s: " + tests.getRead().get(indexRead).getMbs());
					
					tests.getRead().get(indexRead).setIops(Double.parseDouble(row.split("\\[")[1].trim().split("IOPS")[0].trim()));
					System.out.println("Read"+indexRead+" -IOPS: " + tests.getRead().get(indexRead).getIops());
					
					tests.getRead().get(indexRead).setUs(Double.parseDouble(row.split("<")[1].trim().split("us>")[0].trim()));
					System.out.println("Read"+indexRead+" -us: " + tests.getRead().get(indexRead).getUs());
					
					indexRead++;
					inReadSeq--;
				}
				
				if (setAttribute[0].contentEquals("Sequential") && inWriteSeq > 0) {
					
					tests.getWrite().add(new TestRow());
					
					tests.getWrite().get(indexWrite).setType(setAttribute[0]  + " " + setAttribute[1]);
					System.out.println("Write "+indexWrite+" -Type: " + tests.getWrite().get(indexWrite).getType());
					
					tests.getWrite().get(indexWrite).setQ(Integer.parseInt(row.split("Q=")[1].trim().split(",")[0].trim()));
					System.out.println("Write "+indexWrite+"-Q: " + tests.getWrite().get(indexWrite).getQ());
					
					tests.getWrite().get(indexWrite).setT(Integer.parseInt(row.split("T=")[1].trim().split("\\):")[0].trim()));
					System.out.println("Write"+indexWrite+" -T: " + tests.getWrite().get(indexWrite).getT());
					
					tests.getWrite().get(indexWrite).setMbs(Double.parseDouble(row.split(":")[1].trim().split("MB")[0].trim()));
					System.out.println("Write"+indexWrite+" -MB/s: " + tests.getWrite().get(indexWrite).getMbs());
					
					tests.getWrite().get(indexWrite).setIops(Double.parseDouble(row.split("\\[")[1].trim().split("IOPS")[0].trim()));
					System.out.println("Write"+indexWrite+" -IOPS: " + tests.getWrite().get(indexWrite).getIops());
					
					tests.getWrite().get(indexWrite).setUs(Double.parseDouble(row.split("<")[1].trim().split("us>")[0].trim()));
					System.out.println("Write"+indexWrite+" -us: " + tests.getWrite().get(indexWrite).getUs());
					
					indexWrite++;
					inWriteSeq--;
				}
				
				if (setAttribute[0].contentEquals("Random") && inReadRan > 0) {
					
					tests.getRead().add(new TestRow());
					
					tests.getRead().get(indexRead).setType(setAttribute[0]  + " " + setAttribute[1]);
					System.out.println("Read"+indexRead+" -Type: " + tests.getRead().get(indexRead).getType());
					
					tests.getRead().get(indexRead).setQ(Integer.parseInt(row.split("Q=")[1].trim().split(",")[0].trim()));
					System.out.println("Read"+indexRead+"-Q: " + tests.getRead().get(indexRead).getQ());
					
					tests.getRead().get(indexRead).setT(Integer.parseInt(row.split("T=")[1].trim().split("\\):")[0].trim()));
					System.out.println("Read"+indexRead+" -T: " + tests.getRead().get(indexRead).getT());
					
					tests.getRead().get(indexRead).setMbs(Double.parseDouble(row.split(":")[1].trim().split("MB")[0].trim()));
					System.out.println("Read"+indexRead+" -MB/s: " + tests.getRead().get(indexRead).getMbs());
					
					tests.getRead().get(indexRead).setIops(Double.parseDouble(row.split("\\[")[1].trim().split("IOPS")[0].trim()));
					System.out.println("Read"+indexRead+" -IOPS: " + tests.getRead().get(indexRead).getIops());
					
					tests.getRead().get(indexRead).setUs(Double.parseDouble(row.split("<")[1].trim().split("us>")[0].trim()));
					System.out.println("Read"+indexRead+" -us: " + tests.getRead().get(indexRead).getUs());
					
					indexRead++;
					inReadRan--;
				}
				
				if (setAttribute[0].contentEquals("Random") && inWriteRan > 0) {
					
					tests.getWrite().add(new TestRow());
					
					tests.getWrite().get(indexWrite).setType(setAttribute[0]  + " " + setAttribute[1]);
					System.out.println("Write "+indexWrite+" -Type: " + tests.getWrite().get(indexWrite).getType());
					
					tests.getWrite().get(indexWrite).setQ(Integer.parseInt(row.split("Q=")[1].trim().split(",")[0].trim()));
					System.out.println("Write "+indexWrite+"-Q: " + tests.getWrite().get(indexWrite).getQ());
					
					tests.getWrite().get(indexWrite).setT(Integer.parseInt(row.split("T=")[1].trim().split("\\):")[0].trim()));
					System.out.println("Write"+indexWrite+" -T: " + tests.getWrite().get(indexWrite).getT());
					
					tests.getWrite().get(indexWrite).setMbs(Double.parseDouble(row.split(":")[1].trim().split("MB")[0].trim()));
					System.out.println("Write"+indexWrite+" -MB/s: " + tests.getWrite().get(indexWrite).getMbs());
					
					tests.getWrite().get(indexWrite).setIops(Double.parseDouble(row.split("\\[")[1].trim().split("IOPS")[0].trim()));
					System.out.println("Write"+indexWrite+" -IOPS: " + tests.getWrite().get(indexWrite).getIops());
					
					tests.getWrite().get(indexWrite).setUs(Double.parseDouble(row.split("<")[1].trim().split("us>")[0].trim()));
					System.out.println("Write"+indexWrite+" -us: " + tests.getWrite().get(indexWrite).getUs());
					
					indexWrite++;
					inWriteRan--;
				}
				
				if (setAttribute[0].contentEquals("[Write]")) {
					inWriteSeq = 2;
					inWriteRan = 2;
					
				}
				if (setAttribute[0].contentEquals("[Read]")) {
					inReadSeq = 2;
					inReadRan = 2;
				}
				
			}indexWhile++;
		
			
		} catch(Exception exception) {
			throw exception;
		}
		
		return tests;
	}
	
	public List<TestData> readTestFromXML(String pathFile) throws Exception{
		
		File file = new File(pathFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        List<TestData> tests = new ArrayList<>();        
        // Load the input XML document, parse it and return an instance of the
        // Document class.
	    try { 
        	Document document = builder.parse(file);
        	Element element = document.getDocumentElement(); 
	        
	        if (!element.hasAttributes()) {
		//      System.out.println(element.getTagName());
		        
		        //System.out.println(element.getChildNodes().getLength());
		        NodeList testNode = element.getElementsByTagName("test");
		       
		//        System.out.println("contatti : " + contatti.getLength());
		
		        for (int i = 0; i < testNode.getLength(); i++) {
		        	
		        	TestData test = new TestData();
		        	
		        	Element itemTest = (Element)testNode.item(i);
		        		
			        	test.setIdComputer(itemTest.getAttribute("id_computer"));
			        	test.setVersion(itemTest.getAttribute("version"));
			        	test.setOs(itemTest.getAttribute("os"));
			        	test.setType(itemTest.getAttribute("type"));
			        	test.setIterations(Integer.parseInt(itemTest.getAttribute("iterations")));
			        	test.setInterval(itemTest.getAttribute("interval"));
			        	test.setDate(itemTest.getAttribute("type"));
			        	
			        	NodeList nodeRead = itemTest.getElementsByTagName("read");
			        	
			        	for (int x = 0; x < nodeRead.getLength(); x++) {
			        		
			        		Element read = (Element)nodeRead.item(x);
			        		
			        		NodeList readMib = read.getElementsByTagName("Sequential_1MiB");
			        		
			        		for (int y = 0; y < readMib.getLength(); y++) {
				        		TestRow testRow = new TestRow();
				        		
				        		Element row = (Element)readMib.item(y);
				        		
				        		Element mbs= (Element)row.getElementsByTagName("MBs").item(0);
				        		Element iops= (Element)row.getElementsByTagName("IOPS").item(0);
				        		Element us= (Element)row.getElementsByTagName("us").item(0);
			        			
			        			testRow.setMbs(Double.parseDouble(mbs.getTextContent()));
			        			testRow.setIops(Double.parseDouble(iops.getTextContent()));
			        			testRow.setUs(Double.parseDouble(us.getTextContent()));
				        		
				        		testRow.setQ(Integer.parseInt(row.getAttribute("q")));
				        		testRow.setT(Integer.parseInt(row.getAttribute("t")));
				        		testRow.setType(row.getNodeName());
				        		
				        		test.getRead().add(testRow);
			        		}	
			        		
			        		NodeList readKib = read.getElementsByTagName("Random_4KiB");
			        		
			        		for (int y = 0; y < readKib.getLength(); y++) {
				        		TestRow testRow = new TestRow();
				        		
				        		Element row = (Element)readKib.item(y);

				        		Element mbs= (Element)row.getElementsByTagName("MBs").item(0);
				        		Element iops= (Element)row.getElementsByTagName("IOPS").item(0);
				        		Element us= (Element)row.getElementsByTagName("us").item(0);
			        			
			        			testRow.setMbs(Double.parseDouble(mbs.getTextContent()));
			        			testRow.setIops(Double.parseDouble(iops.getTextContent()));
			        			testRow.setUs(Double.parseDouble(us.getTextContent()));
				        		
				        		testRow.setQ(Integer.parseInt(row.getAttribute("q")));
				        		testRow.setT(Integer.parseInt(row.getAttribute("t")));
				        		testRow.setType(row.getNodeName());
				        		
				        		test.getRead().add(testRow);
			        		}
				        }
			        		
			        	
			        	NodeList nodeWrite = itemTest.getElementsByTagName("write");
			        	
			        	for (int x = 0; x < nodeRead.getLength(); x++) {
			        		
			        		Element write = (Element)nodeWrite.item(x);
			        		
			        		NodeList readMib = write.getElementsByTagName("Sequential_1MiB");
			        		
			        		for (int y = 0; y < readMib.getLength(); y++) {
				        		TestRow testRow = new TestRow();
				        		
				        		Element row = (Element)readMib.item(y);
				        		
				        		Element mbs= (Element)row.getElementsByTagName("MBs").item(0);
				        		Element iops= (Element)row.getElementsByTagName("IOPS").item(0);
				        		Element us= (Element)row.getElementsByTagName("us").item(0);
			        			
			        			testRow.setMbs(Double.parseDouble(mbs.getTextContent()));
			        			testRow.setIops(Double.parseDouble(iops.getTextContent()));
			        			testRow.setUs(Double.parseDouble(us.getTextContent()));
				        		
				        		testRow.setQ(Integer.parseInt(row.getAttribute("q")));
				        		testRow.setT(Integer.parseInt(row.getAttribute("t")));
				        		testRow.setType(row.getNodeName());
				        		
				        		test.getWrite().add(testRow);
			        		}	
			        		
			        		NodeList readKib = write.getElementsByTagName("Random_4KiB");
			        		
			        		for (int y = 0; y < readKib.getLength(); y++) {
				        		TestRow testRow = new TestRow();
				        		
				        		Element row = (Element)readKib.item(y);

				        		Element mbs= (Element)row.getElementsByTagName("MBs").item(0);
				        		Element iops= (Element)row.getElementsByTagName("IOPS").item(0);
				        		Element us= (Element)row.getElementsByTagName("us").item(0);
			        			
			        			testRow.setMbs(Double.parseDouble(mbs.getTextContent()));
			        			testRow.setIops(Double.parseDouble(iops.getTextContent()));
			        			testRow.setUs(Double.parseDouble(us.getTextContent()));
				        		
				        		testRow.setQ(Integer.parseInt(row.getAttribute("q")));
				        		testRow.setT(Integer.parseInt(row.getAttribute("t")));
				        		testRow.setType(row.getNodeName());
				        		
				        		test.getWrite().add(testRow);
			        		}
				        }
		        	
			        tests.add(test);
		        }
	        }
	    }
	    catch(Exception exception) {
			throw exception;
		}
	    return tests;
	}
	
	
	public void writeTestToXML(List<TestData> tests, List<TestData> oldTests, String pathFile) throws Exception{
		
		File file = new File(pathFile);
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(file);
			Element element = document.getDocumentElement();
			
			int i = 0;
			
			for (TestData test : tests) {
				
			boolean equal = false;
//				
//				for (TestData oldTest : oldTests) {
//					if (oldTest.getIdComputer().contentEquals(test.getIdComputer())) {
//						equal=true;
//					}
//				}
				
				if(!equal) { 
					Element testChild = document.createElement("test");
					
					testChild.setAttribute("id_computer", test.getIdComputer());
					testChild.setAttribute("version", test.getVersion());
					testChild.setAttribute("os", test.getOs());
					testChild.setAttribute("type", test.getType());
					testChild.setAttribute("iterations", Integer.toString(test.getIterations()));
					testChild.setAttribute("interval", test.getInterval());
					testChild.setAttribute("date", test.getDate());
					
					Element read = (Element)document.createElement("read");
					
					for (TestRow rowRead : tests.get(i).getRead()) {
						
						Element rowReadItem = (Element)document.createElement(rowRead.getType().replace(" ", "_"));
						
						rowReadItem.setAttribute("q", Integer.toString(rowRead.getQ()));
						rowReadItem.setAttribute("t", Integer.toString(rowRead.getT()));
						
						Element mbsReadItem = (Element)document.createElement("MBs");
						Element iopsReadItem = (Element)document.createElement("IOPS");
						Element usReadItem = (Element)document.createElement("us");
						
						mbsReadItem.setTextContent(Double.toString(rowRead.getMbs()));
						iopsReadItem.setTextContent(Double.toString(rowRead.getIops()));
						usReadItem.setTextContent(Double.toString(rowRead.getUs()));
					
						rowReadItem.appendChild(mbsReadItem);
						rowReadItem.appendChild(iopsReadItem);
						rowReadItem.appendChild(usReadItem);
						read.appendChild(rowReadItem);
					}
					
	
					Element write = (Element)document.createElement("write");
					
					for (TestRow rowWrite : tests.get(i).getWrite()) {
						
						Element rowWriteItem = (Element)document.createElement(rowWrite.getType().replace(" ", "_"));
						
						rowWriteItem.setAttribute("q", Integer.toString(rowWrite.getQ()));
						rowWriteItem.setAttribute("t", Integer.toString(rowWrite.getT()));
						
						Element mbsWriteItem = (Element)document.createElement("MBs");
						Element iopsWriteItem = (Element)document.createElement("IOPS");
						Element usWriteItem = (Element)document.createElement("us");
						
						mbsWriteItem.setTextContent(Double.toString(rowWrite.getMbs()));
						iopsWriteItem.setTextContent(Double.toString(rowWrite.getIops()));
						usWriteItem.setTextContent(Double.toString(rowWrite.getUs()));
						
						rowWriteItem.appendChild(mbsWriteItem);
						rowWriteItem.appendChild(iopsWriteItem);
						rowWriteItem.appendChild(usWriteItem);
						
						write.appendChild(rowWriteItem);
					}
	
					testChild.appendChild(read);
					testChild.appendChild(write);
					
					element.appendChild(testChild);
				}
				i++;
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(pathFile));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("CONTATTI SALVATI .XML CON SUCCESSO");
			
		}catch (Exception exception) {
			throw exception;
		}
	}
public void writeTestToXML(List<TestData> tests, List<TestData> oldTests, String pathFile, boolean isNew) throws Exception{
		
		
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.newDocument();
			Element docElement = document.createElement("CrystalDiskMark");
			
			int i = 0;
			
			for (TestData test : tests) {
				
				boolean equal = false;
				
//				for (TestData oldTest : oldTests) {
//					if (oldTest.getIdComputer().contentEquals(test.getIdComputer())) {
//						equal=true;
//					}
//				}
				
				if(!equal) { 
					Element testChild = document.createElement("test");
					
					testChild.setAttribute("id_computer", test.getIdComputer());
					testChild.setAttribute("version", test.getVersion());
					testChild.setAttribute("os", test.getOs());
					testChild.setAttribute("type", test.getType());
					testChild.setAttribute("iterations", Integer.toString(test.getIterations()));
					testChild.setAttribute("interval", test.getInterval());
					testChild.setAttribute("date", test.getDate());
					
					Element read = (Element)document.createElement("read");
					
					for (TestRow rowRead : tests.get(i).getRead()) {
						
						Element rowReadItem = (Element)document.createElement(rowRead.getType().replace(" ", "_"));
						
						rowReadItem.setAttribute("q", Integer.toString(rowRead.getQ()));
						rowReadItem.setAttribute("t", Integer.toString(rowRead.getT()));
						
						Element mbsReadItem = (Element)document.createElement("MBs");
						Element iopsReadItem = (Element)document.createElement("IOPS");
						Element usReadItem = (Element)document.createElement("us");
						
						mbsReadItem.setTextContent(Double.toString(rowRead.getMbs()));
						iopsReadItem.setTextContent(Double.toString(rowRead.getIops()));
						usReadItem.setTextContent(Double.toString(rowRead.getUs()));
					
						rowReadItem.appendChild(mbsReadItem);
						rowReadItem.appendChild(iopsReadItem);
						rowReadItem.appendChild(usReadItem);
						read.appendChild(rowReadItem);
					}
					
	
					Element write = (Element)document.createElement("write");
					
					for (TestRow rowWrite : tests.get(i).getWrite()) {
						
						Element rowWriteItem = (Element)document.createElement(rowWrite.getType().replace(" ", "_"));
						
						rowWriteItem.setAttribute("q", Integer.toString(rowWrite.getQ()));
						rowWriteItem.setAttribute("t", Integer.toString(rowWrite.getT()));
						
						Element mbsWriteItem = (Element)document.createElement("MBs");
						Element iopsWriteItem = (Element)document.createElement("IOPS");
						Element usWriteItem = (Element)document.createElement("us");
						
						mbsWriteItem.setTextContent(Double.toString(rowWrite.getMbs()));
						iopsWriteItem.setTextContent(Double.toString(rowWrite.getIops()));
						usWriteItem.setTextContent(Double.toString(rowWrite.getUs()));
						
						rowWriteItem.appendChild(mbsWriteItem);
						rowWriteItem.appendChild(iopsWriteItem);
						rowWriteItem.appendChild(usWriteItem);
						
						write.appendChild(rowWriteItem);
					}
	
					testChild.appendChild(read);
					testChild.appendChild(write);
					
					docElement.appendChild(testChild);
					
					
					
				}
				i++;
			}
			document.appendChild(docElement);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(pathFile));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("CONTATTI SALVATI .XML CON SUCCESSO");
			
		}catch (Exception exception) {
			throw exception;
		}
	}
	
	public static String pulisciSpazi(String str) {
		if (str.length()>0) {
		
			char[] arrString = str.toCharArray();
			StringBuilder strPulita = new StringBuilder("");
			
			for (int i = 0; i < arrString.length; i++) {
				if (isAmmesso(arrString[i]))
					strPulita.append(arrString[i]); 
			}
			
			return  strPulita.toString();
		}
		return str;
	}
	
	public static boolean isAmmesso(char c) {
		boolean responde = false;
		for (int i = 0; i < CARATTERI_AMMESSI.length; i++) {
			if (c == CARATTERI_AMMESSI[i]) {
				responde = true;
			}
		}
		return responde;
	}
	
	
}

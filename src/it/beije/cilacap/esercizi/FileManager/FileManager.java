package it.beije.cilacap.esercizi.FileManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;





public class FileManager {
	public ArrayList<Contatto1> getContattiFromFile(String pathfile) throws Exception {
		File file = new File(pathfile);
		
		return getContattiFromFile(file);
	}

	public ArrayList<Contatto1> getContattiFromFile(File file) throws Exception {
		ArrayList<Contatto1> listaContatti = new ArrayList<Contatto1>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
	    try { 
        	Document document = builder.parse(file);
        	Element element = document.getDocumentElement(); 
	        
	        if (!element.hasAttributes()) {
		//      System.out.println(element.getTagName());
		        
		        //System.out.println(element.getChildNodes().getLength());
		        NodeList contatti = element.getElementsByTagName("contatto");
		//        System.out.println("contatti : " + contatti.getLength());
		
		        for (int i = 0; i < contatti.getLength(); i++) {
		        	Element utente = (Element)contatti.item(i);
		//        	System.out.println(utente.getTagName() + " " + i);
		//        	System.out.println("\tanni = " + utente.getAttribute("anni"));
		 
		        	Element nome = (Element)utente.getElementsByTagName("nome").item(0);
		        	Element cognome = (Element)utente.getElementsByTagName("cognome").item(0);
		        	Element telefono = (Element)utente.getElementsByTagName("telefono").item(0);
		        	Element email = (Element)utente.getElementsByTagName("email").item(0);
		        	
		        	Contatto1 contatto = new Contatto1();
		        	contatto.setNome(nome.getTextContent());
		        	contatto.setCognome(cognome.getTextContent());
		        	contatto.setTelefono(telefono.getTextContent());
		        	contatto.setEmail(email.getTextContent());
		        	
		//        	System.out.println("\tnome = " + contatto.getNome());
		//        	System.out.println("\tcognome = " + contatto.getCognome());
		//        	System.out.println("\ttelefono = " + contatto.getTelefono());
		//        	System.out.println("\temail = " + contatto.getEmail());
		        	
		        	listaContatti.add(contatto);
		        }
	        } else {
	        	Element elementNull = document.createElement("rubrica");
	        	document.appendChild(elementNull);
	        }
		       
	    }catch (IllegalArgumentException exception) {
	    	writeRubricaXML(listaContatti, file.getPath(), true);
		}
        
        return listaContatti;
	}
	
	Scanner scn = new Scanner(System.in);
 
	public ArrayList<Contatto1> readRubricaCSV(String pathFile) throws Exception  {
		File f = new File(pathFile);
		
		ArrayList<Contatto1> contatti = new ArrayList<>();
		String[] arrayElementi = null;
		
		try {
			FileReader fileReader = new FileReader(f);		
			BufferedReader reader = new BufferedReader(fileReader);
			
			String row;
			int indexWhile = 0;
			
			while ((row = reader.readLine()) != null) {
				arrayElementi = row.split(";");
				
				contatti.add(new Contatto1());
				contatti.get(indexWhile).setNome(arrayElementi[0]);
				contatti.get(indexWhile).setCognome(arrayElementi[1]);
				contatti.get(indexWhile).setTelefono(arrayElementi[2]);
				contatti.get(indexWhile).setEmail(arrayElementi[3]);
				
				indexWhile++;
			}
		
			
		} catch(Exception exception) {
			throw exception;
		}
		return contatti;
	}
	
	public ArrayList<Contatto1> readRubricaXML(String pathFile){
		ArrayList<Contatto1> contatti = new ArrayList<>();
		
		return contatti;
	}
	
	public void writeRubrica(ArrayList<Contatto1> contatti, String pathFileCSV, String pathFileXML) throws IOException {
		
		writeRubricaCSV(contatti, pathFileCSV);
		
		try {
			writeRubricaXML(contatti, pathFileXML);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	public void writeRubricaCSV(ArrayList<Contatto1> contatti, String pathFileCSV) throws IOException {
		
		String [] contattiCsv = new String[contatti.size()];
		FileWriter fileWriter = new FileWriter(pathFileCSV);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		for (int i = 0; i < contattiCsv.length; i++) {
			contattiCsv[i]= "";
			contattiCsv[i] += contatti.get(i).getNome() + ";";
			contattiCsv[i] += contatti.get(i).getCognome() + ";";
			contattiCsv[i] += contatti.get(i).getTelefono() + ";";
			contattiCsv[i] += contatti.get(i).getEmail() + ";";
		}
		
		for (String row : contattiCsv) {
			bufferedWriter.append(row).append('\n');
		}
		
		bufferedWriter.flush();
		bufferedWriter.close();
		
		
		System.out.println("CONTATTI SALVATI .CSV CON SUCCESSO");
	}	
	
	public void writeRubricaXML(ArrayList<Contatto1> contatti, String pathFile) throws Exception {
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(pathFile);
			Element element = document.getDocumentElement();
			Element docElement = document.createElement("rubrica");
			document.removeChild(element);
			document.appendChild(docElement);
			
			for (Contatto1 c : contatti) {
				Element contatto = document.createElement("contatto");
				
				Element nome = (Element)document.createElement("nome");
				Element cognome = (Element)document.createElement("cognome");
				Element telefono = (Element)document.createElement("telefono");
				Element email = (Element)document.createElement("email");
				
				nome.setTextContent(c.getNome());
				cognome.setTextContent(c.getCognome());
				telefono.setTextContent(c.getTelefono());
				email.setTextContent(c.getEmail());
				
				contatto.appendChild(nome);
				contatto.appendChild(cognome);
				contatto.appendChild(telefono);
				contatto.appendChild(email);
				
				docElement.appendChild(contatto);
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
	
	public void writeRubricaXML(ArrayList<Contatto1> contatti, String pathFile,boolean isNew) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.newDocument();
			Element docElement = document.createElement("rubrica");
			document.appendChild(docElement);
			
			for (Contatto1 c : contatti) {
				Element contatto = document.createElement("contatto");
				
				Element nome = (Element)document.createElement("nome");
				Element cognome = (Element)document.createElement("cognome");
				Element telefono = (Element)document.createElement("telefono");
				Element email = (Element)document.createElement("email");
				
				nome.setTextContent(c.getNome());
				cognome.setTextContent(c.getCognome());
				telefono.setTextContent(c.getTelefono());
				email.setTextContent(c.getEmail());
				
				contatto.appendChild(nome);
				contatto.appendChild(cognome);
				contatto.appendChild(telefono);
				contatto.appendChild(email);
				
				docElement.appendChild(contatto);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(pathFile));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("CONTATTI ESPORTATI IN " + pathFile + "CON SUCCESSO");
			
		}catch (Exception exception) {
			throw exception;
		}
	}
	
	public void printRubrica(ArrayList <Contatto1> contatti) {
		if (contatti.size() > 0) {
			for (int i = 0; i < contatti.size(); i++) {
				System.out.println("Contatto n�" + (i+1));
				System.out.println("Nome: " + contatti.get(i).getNome());
				System.out.println("Cognome: " + contatti.get(i).getCognome());
				System.out.println("Telefono: " + contatti.get(i).getTelefono());
				System.out.println("Email: " + contatti.get(i).getEmail());
				System.out.println("\n");
			}
		}else {
			System.out.println("Nessun contatto registrato...");
		}
	}
	
	public ArrayList <Contatto1> addContatti(ArrayList <Contatto1> contatti) {
		boolean finish= false;
		String finishString = "";
	
		while (!finish) {
			contatti.add(new Contatto1());
			System.out.println("Inserire nome...\n");
			contatti.get(contatti.size()-1).setNome(scn.nextLine());
			System.out.println("Inserire cognome...\n");
			contatti.get(contatti.size()-1).setCognome(scn.nextLine());
			System.out.println("Inserire telefono...\n");
			contatti.get(contatti.size()-1).setTelefono(scn.nextLine());
			System.out.println("Inserire email...\n");
			contatti.get(contatti.size()-1).setEmail(scn.nextLine());
			
			System.out.println("Vuoi inserire un altro numero?\ts/n\n");
			finishString = scn.nextLine();
			
			if (finishString.contentEquals("n")) {
				finish = true;
			}
			
		}
		return contatti;
	}
	
		
}


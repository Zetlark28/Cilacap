package it.beije.cilacap.esercizi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScannerCalcolatrice {
	
	public static char[] ammessi = {'0','1','2','3','4','5','6','7','8','9','.','+','-','*','/','%','(',')','=','Q','q'};

	public static void main(String[] args) {
		System.out.println("Caratteri ammessi : " + Arrays.toString(ammessi));
		
		Scanner s = new Scanner(System.in);
		String st = s.next();
		while (!st.equalsIgnoreCase("Q")) {
			st = s.nextLine();
			String string = new String(st);
			System.out.println(string);
		}
		
		System.gc();
		System.out.println("BYE!!");
		s.close();

	}

}

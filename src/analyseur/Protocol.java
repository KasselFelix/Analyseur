package analyseur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.TraceReader;

public class Protocol {
	
	protected static List<String> trace;
	protected static List<Protocol> pListe;
	
	private List<String> octet;
	private StringBuilder sb=new StringBuilder();
	
	public Protocol() {
		String file = "data/udp.txt";
		TraceReader t= new TraceReader(file);
		trace=t.getOctet();
		pListe=new ArrayList<Protocol>();
	}
	
	public Protocol(List<String> octet) {
		this.octet=octet;
	}
	
	public void run() {
		Protocol.add(new Ethernet(trace.subList(0, 13)));
		Collections.reverse(pListe);
		for(Protocol protocole : pListe) 
			sb.append(protocole.toString());
	}
	
	public static void add(Protocol p) {
		pListe.add(p);
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
	public int octToDec(String oct) throws Exception {
		if(oct.length()==2) {
			try {
				return Integer.parseInt(oct,16);
			}catch(NumberFormatException e) {
				System.out.println("caractere non existant dans la base 16!");
			}
		}
		throw new Exception("format de trame incorrect :-(");
	}

}

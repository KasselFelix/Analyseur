package analyseur;

import java.util.ArrayList;
import java.util.List;

import util.TraceReader;

public class Protocol {

	protected List<Protocol> pListe;
	protected List<List<String>> Alltrace;


	private List<String> trace;
	private List<String> octet;

	private Protocol racine;

	public Protocol(TraceReader t ) {
		Alltrace=t.getOctet();
		pListe=new ArrayList<Protocol>();
	}

	public Protocol(List<String> trace) {
		pListe=new ArrayList<Protocol>();
		this.trace=trace;
		racine=this;
	}
	public Protocol(List<String> octet,Protocol racine,List<String> trace) {
		this.octet=octet;
		this.racine=racine;
		this.trace=trace;
	}

	public void run() throws Exception{
		for(List <String> l : Alltrace  ) {
			Protocol protocole=new Protocol(l);
			pListe.add(protocole);
			protocole.add(new Ethernet(protocole.getTrace().subList(0, 14),protocole,protocole.trace));
		}
	}


	public void add(Protocol p) {
		racine.pListe.add(0,p);
	}


	public int octToDec(String oct) throws Exception {//int begin, int end) throws Exception{
		//String s = String.join("", octet.subList(begin, end+1));
		if(oct.length()%2==0) {
			try {
				return Integer.parseInt(oct,16);
			}catch(NumberFormatException e) {
				System.err.println("Error : \""+oct+"\" caractere non existant dans la base 16!");
			}
		}
		throw new Exception("Error : \""+oct+"\" format de trame incorrect :-(");
	}

	
	public String octToBin(int begin, int end) {
		String s = String.join("", octet.subList(begin, end+1));
		if(s.length() < 8)
			return String.format("%" + s.length()*4 + "s", Integer.toBinaryString(Integer.parseInt(s, 16))).replace(" ", "0");
		else
			return String.format("%" + s.length()*4 + "s", Long.toBinaryString(Long.parseLong(s, 16))).replace(" ", "0");
	}
	
	public String octToAscii(int begin, int end) {
		StringBuilder s=new StringBuilder();
		List<String> l = octet.subList(begin, end+1);
		for(String oct:l)
			s.append((char) Integer.parseInt(oct, 16));
		return s.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(Protocol p :pListe)
			sb.append(p.toString());
		
		return sb.toString();
	}

	public List<String> getTrace() {
		return trace;
	}

	public Protocol getRacine() {
		return racine;
	}

}

package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TraceReader {
	private List<List<String>> octet = new ArrayList<>();

	public TraceReader(String file) {
		try {
			BufferedReader br =new BufferedReader(new FileReader(file));
			String line;
			int nbo=0;
			int i=0;
			int l=1;
			boolean b=true;
			while((line = br.readLine())!=null) {
				i=0;
				for(String word : line.split("\\s+")) {
					if(word.equals(""))continue;
					if(i==0) {
						if(word.equals("0000")) {
							octet.add(new ArrayList<String>());
							nbo=0;
						}
						if(word.length()<4 || !checkHexa(word)) b=false;
						else {
							int offset=octToDec(word);
							if(nbo!=offset) throw new Exception("Error ligne-"+l+" : offset \""+word+"\" non valide nb octet = "+nbo);
							b=true;
						}
					}else {
						if(word.length()==2 && checkHexa(word) && b) {
							octet.get(octet.size()-1).add(word.toLowerCase());
							nbo++;
						}
					}
					i++;
				}
				l++;
			}
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(octet.size()==0) System.err.println("ce fichier ne contient aucun octet de donnée");
	}


	public static int octToDec(String oct) throws Exception {
		if(oct.length()%2==0) {
			try {
				return Integer.parseInt(oct,16);
			}catch(NumberFormatException e) {
				System.err.println("Error : \""+oct+"\" caractere non existant dans la base 16!");
			}
		}
		throw new Exception("Error : \""+oct+"\" format de trame incorrect :-(");
	}

	private boolean checkHexa(String oct) {
		if(oct.length()%2==0) {
			try {
				Integer.parseInt(oct,16);
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public List<List<String>> getOctet() {
		return octet;
	}

}

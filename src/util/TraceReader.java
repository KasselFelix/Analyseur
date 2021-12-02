package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TraceReader {
	private List<String> octet = new ArrayList<>();

//	public TraceReader(String file) {
//		try {
//			BufferedReader br =new BufferedReader(new FileReader(file));
//			String line;
//			int cpt=0; 
//			int offset=0;
//			int nbo=0;
//			int i=0;
//			int l=0;
//			List<String> tmp=new ArrayList();
//			while((line = br.readLine())!=null) {
//				i=0;
//				for(String word : line.split("\\s+")) {
//					if(i==1 && word.length()!=4) {
//						i++;
//						continue;
//					}
//					if(word.length()==4) {
//						System.out.println(word);
//						offset=octToDec(word);
//						System.out.println(offset);
//						if(nbo!=offset) throw new Exception("Error ligne-"+l+" : offset \""+word+"\" non valide nb octet = "+nbo);
//						else octet.addAll(tmp);
//						tmp.clear();
//					}else {
//						if(word.length()==2 && checkHexa(word)) {
//							tmp.add(word);
//							nbo++;
//						}
//					}
//					cpt++;
//					i++;
//				}
//				l++;
//			}
//			octet.addAll(tmp);
//			br.close();
//		}catch(IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(octet);
//	}
	
	public TraceReader(String file) {
		try {
			BufferedReader br =new BufferedReader(new FileReader(file));
			String line;
			int nbo=0;
			int i=0;
			int l=0;
			boolean b=true;
			List<String> tmp=new ArrayList();
			while((line = br.readLine())!=null) {
				i=0;
				for(String word : line.split("\\s+")) {
					if(word.equals(""))continue;
					if(i==0) {
						if(word.length()<4 || !checkHexa(word)) b=false;
						else {
							int offset=octToDec(word);
							if(nbo!=offset) throw new Exception("Error ligne-"+l+" : offset \""+word+"\" non valide nb octet = "+nbo);
							b=true;
						}
					}else {
						if(word.length()==2 && checkHexa(word) && b) {
							octet.add(word);
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
		System.out.println(octet);
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
	
	public List<String> getOctet() {
		return octet;
	}
	
}

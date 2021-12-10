package analyseur;

import util.TraceReader;
import util.UtilFile;

public class Main {

	public static void main(String args[]) {
		try {
			//String file = "data/UDP_DNS.txt";
			String file=UtilFile.chooseFile();
			TraceReader t= new TraceReader(file);
			Protocol p=new Protocol(t);
			p.run();
			System.out.println(p);
			UtilFile.writeFile(p.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}

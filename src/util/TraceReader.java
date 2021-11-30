package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TraceReader {
	private List<String> octet = new ArrayList<>();
	

	public TraceReader(String file) {
		try {
			BufferedReader br =new BufferedReader(new FileReader(file));
			String line;
			int cpt=0;
			while((line = br.readLine())!=null) {
				cpt=0;
				for(String word : line.split(" ")) {
					cpt++;
					if(cpt<2 || cpt>19)continue;
					if(word.equals(""))continue;//retirer mot vide
					octet.add(word);
				}
			}
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getOctet() {
		return octet;
	}
	
}

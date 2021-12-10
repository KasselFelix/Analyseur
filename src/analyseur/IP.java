package analyseur;

import java.util.ArrayList;
import java.util.List;

public class IP extends Protocol {
	List<String> octet;

	private Character version;
	private String headerLength;
	private int hl;
	private String tos; 
	private int totalLength;
	private int identification;
	private String flags;
	private Character flagR;
	private Character flagDF;
	private Character flagMF;
	private int fragmentOffset;
	private String fo;
	private int timeToLive;
	private String protocol;
	private String headerChecksum;
	private String sourceIP="";
	private String destinationIP="";
	private boolean checksum;

	public IP(List<String> octet,Protocol racine,List<String> trace) throws Exception {
		super(octet,racine,trace);
		this.octet=octet;
		version = octet.get(0).charAt(0);
		headerLength=octet.get(0);
		hl = octToDec("0"+headerLength.charAt(0))*octToDec("0"+headerLength.charAt(1));
		tos = octet.get(1);
		totalLength=octToDec( octet.get(2) + octet.get(3));
		identification=octToDec(octet.get(4)+octet.get(5));
		flags=octet.get(6);
		String tmp=octToBin(6,7);
		flagR = tmp.charAt(0) ;
		flagDF = tmp.charAt(1);
		flagMF = tmp.charAt(2);
		fo = tmp.substring(3);
		fragmentOffset=octToDec(octet.get(6)+octet.get(7));
		timeToLive = octToDec(octet.get(8));
		for(int i=octet.size()-4;i<octet.size();i++) {
			if(sourceIP!="")sourceIP+=".";
			if(destinationIP!="")destinationIP+=".";
			try {
				sourceIP+=octToDec(octet.get(i-4));
				destinationIP+=octToDec(octet.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		protocol=octet.get(9);
		headerChecksum = octet.get(10) + octet.get(11);
		checksum = checkChecksum();

		if(protocol.equals("11"))add(new UDP(trace.subList(34,43),racine,trace));
		else throw new Exception("Error : Protocol "+protocol+" n'est pas un protocol UDP");
	}

	private boolean checkChecksum() {
		/** Addition des hexas de l'entête
		 * Si résultat supérieur à 16 bits :
		 * On additionne les 16 bits "high" avec les 16 bits "low"
		 * res = (somme & 0xFFFF) + (somme >> 16)
		 * pas d'erreurs si res = 0xFFFF
		 */
		List<String> header = new ArrayList<>();
		header.addAll(octet.subList(0, 20));
		// On crée la liste d'hexas de 2 octets
		List<String> hexs = new ArrayList<>();

		for(int i = 0; i < header.size(); i+=2) {
			hexs.add(header.get(i) + header.get(i + 1));
		}
		// On fait la somme
		int sum = 0;
		for (String hex : hexs) {
			sum += Integer.parseInt(hex, 16);
		}
		// On somme les bits de poids forts et faibles
		if (sum > 0xFFFF) {
			sum = (sum >> 16) + (sum & 0xFFFF) ;
		}
		return sum == 0xFFFF;
	}

	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		try {
			sb.append("Internet Protocol Version "+version+"\n");
			sb.append("\t"+octToBin(0,0).substring(0,4)+".... = Version: "+version+"\n");
			sb.append("\t.... "+octToBin(0,0).substring(4)+" = Header Length: "+hl+" bytes ("+octToDec("0"+octet.get(0).charAt(1))+")\n");
			sb.append("\tType of Service : 0x" + tos +"\n");
			sb.append("\tTotal Length : "+ totalLength +"\n" );
			sb.append("\tIdentification : 0x" + octet.get(4)+octet.get(5) + " (" + identification + ")\n" );
			sb.append("\tFlags: 0x"+flags+"\n");
			sb.append("\t\t"+flagR+"... .... = Reserved bit: "+((flagR.equals('1')) ? "Set\n" : "Not Set\n"));
			sb.append("\t\t."+flagDF+".. .... = Don't Fragment : "+((flagDF.equals('1')) ? "Set\n" : "Not Set\n"));
			sb.append("\t\t.."+flagMF+". .... = More Fragments : "+((flagMF.equals('1')) ? "Set\n" : "Not Set\n"));
			sb.append("\t..."+fo.charAt(0)+" "+fo.substring(1,5)+" "+fo.substring(5,9)+" "+fo.substring(9)+" = Fragment Offset : " + fragmentOffset +"\n");
			sb.append("\tTime to Live : " + timeToLive + "\n" ); 
			sb.append("\tProtocol UDP:  (" + protocol  +") \n");
			sb.append("\tHeader Checksum : 0x"+ headerChecksum + ((checksum) ? " [correct]\n" : " [incorrect]\n") );
			sb.append("\tSource Address : " + sourceIP +"\n");
			sb.append("\tDestination Address : " + destinationIP  +"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}

package analyseur;

import java.util.List;

public class Ethernet extends Protocol {
	List<String> octet;
	
	private String srcMac="";
    private String destMac="";
    private String type;
    private String crc;
    //https://hpd.gasmi.net/
	public Ethernet( List<String> octet) throws Exception {
		super(octet);
		this.octet=octet;
		
		for(int i=0;i<6;i++) {
			if(destMac!="")destMac+=":";
			destMac+=octet.get(i);
			if(srcMac!="")srcMac+=":";
			srcMac+=octet.get(i+6);
		}

		type=octet.get(12)+octet.get(13);
		if(type.equals("0800") || type.equals("0x86dd"))Protocol.add(new IP(trace.subList(14, 34)));
		else throw new Exception("Error : Trame "+type(type)+" non pris en charge.");
	}
	
	private String type(String type) throws Exception {
		switch(type){
			case "0800":
				return "IPv4";
			case "0805":
				return "X.25 niveau 3";
			case "0806":
				return "ARP";
			case "8035":
				return "RARP";
			case "8098":
				return "Appletalk";
			case "0x86dd":
				return "IPv6";
		}
		throw new Exception("Type non reconnue");
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		try {
			sb.append("Ethernet II, Src: "+srcMac+" ("+srcMac+"), Dst: "+destMac+" ("+destMac+")\n");
			sb.append("\tDestination: "+destMac+"\n");
			sb.append("\tSource: "+srcMac+"\n");
			sb.append("\tType: "+type(type)+" (0x"+type+")\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}

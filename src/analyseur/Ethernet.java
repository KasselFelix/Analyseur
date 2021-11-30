package analyseur;

import java.util.List;

public class Ethernet extends Protocol {
	List<String> octet;
	
	private String srcMac="";
    private String destMac="";
    private String type;
    private String crc;
    //https://hpd.gasmi.net/
	public Ethernet( List<String> octet) {
		super(octet);
		this.octet=octet;
		
		for(int i=0;i<6;i++) {
			if(destMac!="")destMac+=":";
			destMac+=octet.get(i);
		}
		
		for(int i=6;i<12;i++) {
			if(srcMac!="")srcMac+=":";
			srcMac+=octet.get(i);
		}
		
		try {
			type=octet.get(12)+octet.get(13);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Protocol.add(new IP(trace.subList(14, 34)));
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
		}
		throw new Exception("Type non reconnue");
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("ethernet\n");
		sb.append("Destination: "+destMac+"\n");
		sb.append("Source: "+srcMac+"\n");
		sb.append("type:"+type(type)+" (0x"+type+")\n");
		return sb.toString();
	}
	
}

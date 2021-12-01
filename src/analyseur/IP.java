package analyseur;

import java.util.List;

public class IP extends Protocol {
	List<String> octet;
	
	private String typeOfService;
    private String totalLength;
    private String identifier;
    private String flagR;
    private String flagDF;
    private String flagMF;
    private String fragmentOffset;
    private String timeToLive;
    private String protocol;
    private String headerChecksum;
    private String sourceIP="";
    private String destinationIP="";
    private List<String> options;
    private boolean checksum;
    
	public IP(List<String> octet) {
		super(octet);
		this.octet=octet;
		
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
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("Internet Protocol Version\n");
		sb.append("\tSource: "+sourceIP+"\n");
		sb.append("\tDestination: "+destinationIP+"\n");
		return sb.toString();
	}

	public List<String> getOctet() {
		return octet;
	}
	
	public String getTypeOfService() {
		return typeOfService;
	}

	public String getTotalLength() {
		return totalLength;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getFlagR() {
		return flagR;
	}

	public String getFlagDF() {
		return flagDF;
	}

	public String getFlagMF() {
		return flagMF;
	}

	public String getFragmentOffset() {
		return fragmentOffset;
	}

	public String getTimeToLive() {
		return timeToLive;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHeaderChecksum() {
		return headerChecksum;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public List<String> getOptions() {
		return options;
	}

	public boolean isChecksum() {
		return checksum;
	}

}

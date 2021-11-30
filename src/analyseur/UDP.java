package analyseur;

import java.util.List;

public class UDP extends Protocol{
	private List<String> octet;
	private String portSource;
	private String portDestination;
	private String length;
	private String checksum;
	
	
	public UDP( List<String> octet) {
		super(octet);
		this.octet=octet;
		Protocol.add(new IP(trace.subList(14, 24)));
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("UDP\n");
		return sb.toString();
	}
	
}

	

	



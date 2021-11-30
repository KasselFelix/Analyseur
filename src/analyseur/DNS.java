package analyseur;

import java.util.List;

public class DNS extends Protocol{
	
	List<String> octet;

	public DNS( List<String> octet) {
		super(octet);
		this.octet=octet;
		Protocol.add(new IP(trace.subList(14, 24)));
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("DNS\n");
		return sb.toString();
	}

}

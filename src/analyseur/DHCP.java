package analyseur;

import java.util.List;

public class DHCP extends Protocol {
	
	List<String> octet;

	public DHCP( List<String> octet) {
		super(octet);
		this.octet=octet;
		Protocol.add(new IP(trace.subList(14, 24)));
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("DHCP\n");
		return sb.toString();
	}
}

package analyseur;

import java.util.List;

public class UDP extends Protocol{
	private int portSource;
	private int portDestination;
	private int length;
	private String checksum;
	private int UDPpayload;
	
	public UDP( List<String> octet,Protocol racine,List<String> trace) throws Exception {
		super(octet,racine,trace);
		portSource=octToDec(octet.get(0)+octet.get(1));
		portDestination=octToDec(octet.get(2)+octet.get(3));
		length=octToDec(octet.get(4)+octet.get(5));
		checksum=octet.get(6)+octet.get(7);
		UDPpayload=length-octet.size()+1;
		if((portSource==67 && portDestination==68)||(portSource==68 && portDestination==67))add(new DHCP(trace.subList(trace.size()-length+8,trace.size()),racine,trace));
		else if(portSource==53 || portDestination==53 )add(new DNS(trace.subList(trace.size()-length+8,trace.size()),racine,trace,false));
		else if(portSource==5353 && portDestination==5353) add(new DNS(trace.subList(trace.size()-length+8,trace.size()),racine,trace,true));
		else  throw new Exception("Error : Port destination "+portDestination+" non pris en charge");
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("User Datagram Protocol, Src Port: "+portSource+", Dst Port: "+portDestination+"\n");
		sb.append("\tSource Port: "+portSource+"\n");
		sb.append("\tDestination Port: "+portDestination+"\n");
		sb.append("\tlength: "+length+"\n");
		sb.append("\tChecksum: 0x"+checksum+"\n");
		sb.append("\tUDP payload ("+UDPpayload+" bytes)\n");
		return sb.toString();
	}
	
}

	

	



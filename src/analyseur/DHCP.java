package analyseur;

import java.util.List;

public class DHCP extends Protocol {
	//https://datatracker.ietf.org/doc/html/rfc2131
	//https://www.iana.org/assignments/bootp-dhcp-parameters/bootp-dhcp-parameters.xhtml
	List<String> octet;
	
	private int messageType;
	private String hardwareType;
	private int hardwareAddressLength;
	private int hops;
	private String transaction;
	private int secondsElapsed;
	private String bootpFlags;
	private Character broadcastFlag;
	private String reservedFlags;
	private String clientIPaddress="";
	private String yourCIPa="";
	private String nextSIPa="";
	private String relayAgentIPa="";
	private String ClientmacAdress;
	private String ClientHardwareAddressPadding;
	private String ServerHostNameNotGiven;
	private String BootFileNameNotGiven;
	private String MagicCookie;
	private int opt;
	private StringBuilder sb= new StringBuilder();


	public DHCP( List<String> octet,Protocol racine,List<String> trace) throws Exception {
		super(octet,racine,trace);
		this.octet=octet;
		messageType=octToDec(octet.get(0));
		hardwareType=octet.get(1);
		hardwareAddressLength=octToDec(octet.get(2));
		hops=octToDec(octet.get(3));
		transaction=String.join("", trace.subList(46, 50));
		secondsElapsed=octToDec(trace.get(50)+trace.get(51));
		bootpFlags=trace.get(52)+trace.get(53);
		broadcastFlag=octToBin(52,53).charAt(0);
		reservedFlags=octToBin(52,53).substring(1);
		for(int i=0;i<4;i++) {
			if(clientIPaddress!="")clientIPaddress+=".";
			if(yourCIPa!="")yourCIPa+=".";
			if(nextSIPa!="")nextSIPa+=".";
			if(relayAgentIPa!="")relayAgentIPa+=".";
			try {
				clientIPaddress+=octToDec(octet.get(i+12));
				yourCIPa+=octToDec(octet.get(i+16));
				nextSIPa+=octToDec(octet.get(i+20));
				relayAgentIPa+=octToDec(octet.get(i+24));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ClientmacAdress=String.join(":", octet.subList(28, 34));
		ClientHardwareAddressPadding=String.join("",trace.subList(76, 86));
		ServerHostNameNotGiven=String.join("", trace.subList(86, 150));
		BootFileNameNotGiven=String.join("", trace.subList(150, 278));
		MagicCookie=String.join("",trace.subList(278, 282));
		if(!MagicCookie.equals("63825363"))System.err.println("Error Magic cookie ["+MagicCookie+"] != 63825363 : protocole non DHCP");
		
		
		sb.append("Dynamic Host Configuration Protocol ()\n");
		sb.append("\tMessage type: "+op(messageType)+"\n");
		sb.append("\tHardware type: "+hrd(hardwareType)+"\n");
		sb.append("\tHardware address length: "+hardwareAddressLength+"\n");
		sb.append("\tHops: "+hops+"\n");
		sb.append("\tTransaction ID: 0x"+transaction+"\n");
		sb.append("\tSeconds elapsed: "+secondsElapsed+"\n");
		sb.append("\tBootp flags: 0x"+bootpFlags+((broadcastFlag.equals('0')) ? " (Unicast)\n" : " (Broadcast)\n"));
		sb.append("\t\t"+broadcastFlag+"... .... .... .... = Broadcast flag: "+((broadcastFlag.equals('0')) ? " Unicast\n" : " Broadcast\n"));
		sb.append("\t\t."+reservedFlags.substring(0,3)+" "+reservedFlags.substring(3,7)+" "+reservedFlags.substring(7,11)+" "+reservedFlags.substring(11)+" = Reserved flags: 0x"+bootpFlags+"\n");
		sb.append("\tClient IP address: "+clientIPaddress+"\n");
		sb.append("\tYour (client) IP address: "+yourCIPa+"\n");
		sb.append("\tNext server IP address: "+nextSIPa+"\n");
		sb.append("\tRelay agent IP address: "+relayAgentIPa+"\n");
		sb.append("\tClient MAC address: "+ClientmacAdress+" ("+ClientmacAdress+")\n");
		sb.append("\tClient hardware address padding: "+ClientHardwareAddressPadding+"\n");
		sb.append((octToDec(ServerHostNameNotGiven)==0)? "\tServer host name not given\n" : "\t"+octToAscii(86, 149)+"\n");
		sb.append((octToDec(BootFileNameNotGiven)==0) ? "\tBoot file name not given\n" :  "\t"+octToAscii(150, 277)+"\n");
		sb.append((MagicCookie.equals("63825363")) ? "\tMagic cookie: DHCP\n" : "Error");
		if(trace.size()>281) {
			int i=282;
			opt=octToDec(trace.get(282));
			while(opt!=255) {
				sb.append("\tOption: "+opt(opt)+"\n");
				i+=octToDec(trace.get(i+1))+2;
				opt=octToDec(trace.get(i));
			}
			if(opt==255)sb.append("\tOption: "+opt(opt)+"\n");
		}
		sb.append("\n\n");
	}
	
	
	
	public String op (int op) throws Exception {
		switch(op) {
			case 1 :
				return "Boot Request (1)";
			case 2:
				return "Boot Reply (2)";
			case 3:
				return "Boot request Reverse (3)";
			case 4:
				return "Boot reply Reverse (4)";
			case 5:
				return "Boot DRARP-Request (5)";
			case 6:
				return "Boot DRARP-Reply (6)";
			case 7:
				return "Boot DRARP-Error (7)";
			case 8:
				return "Boot InARP-Request(8)";
			case 9:
				return "Boot InARP-Reply (9)";
			case 10:
				return "Boot ARP-NAK (10)";
		}
		throw new Exception("DHCP Number Operation Code "+op+" non reconnue");
	}
	
	public String hrd (String hrd) throws Exception {
		switch(hrd) {
			case "01":
				return "Ethernet (0x01)";
			case "02":
				return "Experimental Ethernet (0x02)";
			case "03":
				return "Amateur Radio AX.25 (0x03)";
			case "04":
				return "Proteon ProNET Token Ring (0x04)";
			case "05":
				return "Chaos (0x05)";
			case "06":
				return "IEEE 802 Networks (0x06)";
			case "07":
				return "ARCNET (0x07)";
			case "08":
				return "Hyperchannel (0x08)";
			case "09":
				return "Lanstar (0x09)";
			case "0a":
				return "Autonet Short Address (0x0a)";
			case "0b":
				return "LocalTalk (0x0b)";
			case "0c":
				return "LocalNet (IBM PCNet or SYTEK LocalNET) (0x0c)";
			case "0d":
				return "Ultra link (0x0d)";
			case "0e":
				return "SMDS";
			case "0f":
				return "Frame Relay (0x0f)";
			case "10":
				return "Asynchronous Transmission Mode (ATM) (0x10)";
			case "11":
				return "HDLC (0x11)";
			case "12":
				return "Fibre Channel (0x12)";
			case "13":
				return "Asynchronous Transmission Mode (ATM) (0x13)";
			case "14":
				return "Serial Line (0x14)";
			case "15":
				return "Asynchronous Transmission Mode (ATM) (0x15)";
						
		}
		throw new Exception("DHCP Number Hardware Type Code 0x"+hrd+" non reconnue");
	}
	
	public String opt(int opt) throws Exception {
		switch(opt) {
			case 0:
				return "Pad ("+opt+")";
			case 1:
				return "Subnet Mask ("+opt+")";
			case 2:
				return "Time Offset ("+opt+")";
			case 3:
				return "Router ("+opt+")";
			case 4:
				return "Time Server ("+opt+")";
			case 5:
				return "Time Server ("+opt+")";
			case 6:
				return "Domain Server ("+opt+")";
			case 12:
				return "Hostname ("+opt+")";
			case 15:
				return "Domain Name ("+opt+")";
			case 28:
				return "Broadcast Address ("+opt+")";
			case 51:
				return "Address Time ("+opt+")";
			case 53:
				return "DHCP Msg Type ("+opt+")";
			case 54:
				return "DHCP Server Id ("+opt+")";
			case 55:
				return "Parameter List ("+opt+")";
			case 58:
				return "Renewal Time ("+opt+")";
			case 59:
				return "Rebinding Time ("+opt+")";
			case 60:
				return "Class Id ("+opt+")";
			case 61:
				return "Client Id ("+opt+")";
			case 81:
				return "Client FQDN ("+opt+")";
			case 125:
				return "V-I Vendor-Specific Information ("+opt+")";
			case 255:
				return "End ("+opt+")";
		}
		if(opt<0 || opt>255)throw new Exception("DHCP Option ("+opt+") non reconnue");
		else return "option ("+opt+")";
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}

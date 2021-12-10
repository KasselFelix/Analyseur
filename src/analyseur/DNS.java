package analyseur;

import java.util.List;


public class DNS extends Protocol{

	private String TransactionId;
	private String flags;
	private Character response;
	private String opcode;
	private Character authoritative;
	private Character truncated;
	private Character recursionDesired;
	private Character recursionAvailable;
	private Character z;
	private Character answerAuthenticated;
	private Character nonAutenticatedData;
	private String replyCode;
	private int question;
	private int answerRRs;
	private int authorityRRs;
	private int additionalRRs;
	private String name="";
	private int nameLength;
	private int labelCount=0;
	private int type;
	private String Class;
	StringBuilder sb= new StringBuilder();

	public DNS( List<String> octet,Protocol racine,List<String> trace,boolean mult) throws Exception {
		super(octet,racine,trace);
		TransactionId=octet.get(0)+octet.get(1);
		flags=octet.get(2)+octet.get(3);
		String tmp=octToBin(2,3);
		response=tmp.charAt(0);
		opcode=tmp.substring(1,5);
		authoritative=tmp.charAt(5);
		truncated=tmp.charAt(6);
		recursionDesired=tmp.charAt(7);
		recursionAvailable=tmp.charAt(8);
		z=tmp.charAt(9);
		answerAuthenticated=tmp.charAt(10);
		nonAutenticatedData=tmp.charAt(11);
		replyCode=tmp.substring(12);
		question=octToDec(octet.get(4)+octet.get(5));
		answerRRs=octToDec(octet.get(6)+octet.get(7));
		authorityRRs=octToDec(octet.get(8)+octet.get(9));
		additionalRRs=octToDec(octet.get(10)+octet.get(11));
		
		sb.append(((mult) ? "Multicast " : "" )+"Domain Name System "+ ((response.equals('0')) ? "(query)\n" : "(response)\n" ));
		sb.append("\tTransaction ID: "+TransactionId+"\n");
		sb.append("\tFlags: 0x"+flags+" Standard query "+ ((response.equals('0')) ? "\n" : "response,"+rcode(replyCode)+"\n" ) );
		sb.append("\t\t"+response+"... .... .... .... = Response: "+ ((response.equals('0')) ? "Message is a query\n" : "Message is a response\n" ));
		sb.append("\t\t."+opcode.substring(0,3)+" "+opcode.charAt(3)+"... .... .... = Opcode: "+opcode(opcode)+"\n");
		sb.append("\t\t.... ."+authoritative+".. .... .... = Authoritative: "+ ((authoritative.equals('0')) ? "Server is not an authority for domain\n" : "Server is an authority for domain\n" ));
		sb.append("\t\t.... .."+truncated+". .... .... = Truncated: "+ ((truncated.equals('0')) ? "Message is not truncated\n" : "Message is truncated\n" ));
		sb.append("\t\t.... ..."+recursionDesired+" .... .... = Recursion desired: "+ ((recursionDesired.equals('1')) ? "Do query recursively\n" : "Don't do query recursively\n" ));
		sb.append(((recursionAvailable.equals('1')) ? "\t\t.... .... "+recursionAvailable+"... .... = Recursion available: Server can do recursive queries\n" : ""));
		sb.append("\t\t.... .... ."+z+".. .... = Z: "+ ((z.equals('0')) ? "reserved (0)\n" : "error\n" ));
		sb.append("\t\t.... .... .."+answerAuthenticated+". .... = Answer authenticated: "+ ((answerAuthenticated.equals('0')) ? "Answer/authority portion was not authenticated by the server\n" : "Answer/authority portion was authenticated by the server\n"));
		sb.append("\t\t.... .... ..."+nonAutenticatedData+" .... = Non-authenticated data: "+((nonAutenticatedData.equals('0')) ? "Unacceptable\n" : "\n" ));
		sb.append("\t\t.... .... .... "+replyCode+" = Reply code: "+rcode(replyCode)+" ("+octToDec("0"+octet.get(3).charAt(1))+")\n");
		sb.append("\tQuestions: "+question+"\n");
		sb.append("\tAnswer RRs: "+answerRRs+"\n");
		sb.append("\tAuthority RRs: "+authorityRRs+"\n");
		sb.append("\tAdditional RRs: "+additionalRRs+"\n");
		int p=0;
		int i=12;
		while(question>0) {
			nameLength=octToDec(octet.get(i));
			int d=i+1;
			int f=d+nameLength-1;
			while(nameLength!=0 && nameLength!=192 ) {
				if(!name.equals(""))name+='.';
				name+=octToAscii(d,f);
				d+=nameLength+1;
				nameLength=octToDec(octet.get(f+1));
				p=octToDec(octet.get(f+2));
				f=d+nameLength-1;
				labelCount+=1;
			}
			if(nameLength==192) {
				if(p==28) {
					name+="._tcp.local";
					nameLength=name.length();
					type=octToDec(octet.get(i+name.length()-8)+octet.get(i+name.length()-7));
					Class=octet.get(i+name.length()-5);
					i=i+nameLength-4;
					labelCount+=2;
				}else {
					if(p==33){
						name+=".local";
						nameLength=name.length(); 
						type=octToDec(octet.get(i+name.length()-3)+octet.get(i+name.length()-2));
						Class=octet.get(i+name.length());
						i=i+nameLength+1;
						labelCount++;
					}
				}
			}else {
				nameLength=name.length();
				type=octToDec(octet.get(i+name.length()+2)+octet.get(i+name.length()+3));
				Class=octet.get(i+name.length()+5);
				i=i+nameLength+6;
			}
			question--;		
		
			sb.append("\tQueries\n");
			sb.append("\t\t"+name+": type "+type(type)+", class "+Class(Class)+"\n");
			sb.append("\t\t\tName: "+name+"\n");
			sb.append("\t\t\t[Name Length: "+name.length()+"]\n");
			sb.append("\t\t\t[Label Count: "+labelCount+"]\n");
			sb.append("\t\t\tType: ("+type(type)+") ("+type+")\n");
			sb.append("\t\t\tClass: "+Class(Class)+" (0x"+Class+") \n");
			name="";
			labelCount=0;
		}
		if(answerRRs>0) sb.append("\tAnswers\n");
		if(authorityRRs>0) sb.append("\tAuthoritative nameservers\n");
		if(additionalRRs>0) sb.append("\tAdditional records\n");
		sb.append("\n\n");
	}
	
	
	public String type(int type) throws Exception {
		switch(type){
		case 1:
			return "Host Address";
		case 2:
			return "authoritative name server";
		case 3:
			return "mail destination (Obsolete - use MX)";
		case 4:
			return "mail forwarder (Obsolete - use MX)";
		case 5:
			return "canonical name for an alias";
		case 6:
			return "marks the start of a zone of authority";
		case 7:
			return "mailbox domain name (EXPERIMENTAL)";
		case 8:
			return "mail group member (EXPERIMENTAL)";
		case 9:
			return "mail rename domain name (EXPERIMENTAL)";
		case 10:
			return "null RR (EXPERIMENTAL)";
		case 11:
			return "well known service description";
		case 12:
			return "domain name pointer";
		case 13:
			return "host information";
		case 14:
			return "Mailbox or mail list information";
		case 15:
			return "Mail exchange";
		case 16:
			return "Text strings";
		case 252:
			return "request for a transfer of an entire zone";
		case 253:
			return "request for mailbox-related records (MB, MG or MR)";
		case 254:
			return "request for mail agent RRs (Obsolete - see MX)";
		case 255:
			return "request for all records";
		
		
		
		}
		throw new Exception("DNS Type non reconnue");
	}
	
	public String Class(String c) throws Exception {
		switch(c){
		case "01":
			return "IN (Internet)";
		case "02":
			return "CS (CSNET class)";
		case "03":
			return "CH (CHAOS class)";
		case "04":
			return "HS (Hesiod)";
		case "ff":
			return "any class";
		}
		throw new Exception("DNS class "+c+" non reconnue");
	}
	
	public String opcode(String opcode) throws Exception {
		switch(opcode){
		case "0000":
			return "Standard query (0)";
		case "0001":
			return "Inverse query (1)";
		case "0010":
			return "Server status request (2)";
		}
		throw new Exception("DNS opcode"+opcode+" non reconnue");
	}
	
	public String rcode(String rcode) throws Exception {
		switch(rcode){
		case "0000":
			return "No error condition";
		case "0001":
			return "Format error ";
		case "0010":
			return "Server failure ";
		case "0011":
			return "Name Error";
		case "0100":
			return "Not Implemented";	
		case "0101":
			return "Refused";	
		}
		throw new Exception("DNS rcode"+rcode+" non reconnue");
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

}

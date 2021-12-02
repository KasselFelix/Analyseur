package analyseur;

public class main {

	public static void main(String args[]) {
		try {
			Protocol p=new Protocol();
			p.run();
			System.out.println(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}

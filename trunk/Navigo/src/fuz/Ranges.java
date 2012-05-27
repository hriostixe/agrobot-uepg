package fuz;

public class Ranges {

	static double start=0;
	static double leftTop=0;
	static double rightTop=0;
	static double finish=0;
	
	public Ranges() {
		// TODO Auto-generated constructor stub
	}

	
	public static void main(String[] args) {
		double tam =  20/5;
		double i=0;
		while(i<tam){
			finish=tam;				
			start=leftTop;
			leftTop=finish;
			rightTop=leftTop;				
			i=i+tam;	
			System.out.println("In:"+start+" | L:"+leftTop+" | R:"+rightTop+" |"+finish);
		}
	}
	
}

	
	


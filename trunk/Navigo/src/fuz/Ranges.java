package fuz;

import java.util.ArrayList;

public class Ranges {

	
	public Ranges() {
		// TODO Auto-generated constructor stub
	}

	
	public ArrayList<VariavelLinguistica> createRanges(double line, int q, String[] rotulos){
		double tam = line/q;
		double start=0;
		double leftTop=0;
		double rightTop=0;
		double finish=0;
		int cont =0;
		ArrayList<VariavelLinguistica> myArr = new ArrayList<VariavelLinguistica>();
		for(double i=0;i<q;i++){
			start=leftTop;
			
			if(i+1>=q){
				finish=finish+tam;
				tam=0;
			}	
			leftTop=finish;
			rightTop=leftTop;			
			finish=leftTop+tam;			
			
			
			//System.out.println("("+(int)i+") In:"+start+" | L:"+leftTop+" | R:"+rightTop+" |"+finish);
			VariavelLinguistica vl = new VariavelLinguistica(start, leftTop, rightTop, finish,rotulos[cont]);
			myArr.add(cont,vl);
			cont++;
		}	
		return myArr;	
		
	}
}

	
	


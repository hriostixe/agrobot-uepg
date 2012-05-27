package fuz;

import java.util.ArrayList;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;



public class Fuzzy {
	public FuzzyEngine fuzzyEngine ;
	ArrayList<VariavelLinguistica> myArr = new ArrayList<VariavelLinguistica>();
	double start=0;
	double leftTop=0;
	double rightTop=0;
	double finish=0;
	
	public ArrayList<VariavelLinguistica> createRanges(double line, int q){
		double tam = line/q;
		for(double i=0;i<q;i++){
			start=leftTop;
			leftTop=finish;
			rightTop=leftTop;			
			finish=leftTop+tam;			
			if(finish>20)
				finish=20;
			
			//System.out.println("("+(int)i+") In:"+start+" | L:"+leftTop+" | R:"+rightTop+" |"+finish);
			VariavelLinguistica vl = new VariavelLinguistica(start, leftTop, rightTop, finish,String.valueOf(i));
			myArr.add((int)i,vl);
		}	
		return myArr;	
		
	}
	
	public Fuzzy() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("teste");
		
		LinguisticVariable direcao = new LinguisticVariable("direcao");
		//angulo.add(name, start, left_top, right_top, finish)
		direcao.add("ME",0,0,0,5); 
		direcao.add("E",0,5,5,10);
		direcao.add("N",5,10,10,15);
		direcao.add("D",10,15,15,20); 
		direcao.add("MD",15,20,20,20);		
		
		LinguisticVariable posicao = new LinguisticVariable("posicao"); 
		posicao.add("MB",0,0,0,5); 
		posicao.add("B",0,5,5,10);
		posicao.add("Ne",5,10,10,15);
		posicao.add("A",10,15,15,20); 
		posicao.add("MA",15,20,20,20);
		
		LinguisticVariable angulo = new LinguisticVariable("angulo"); 
		angulo.add("BE",-30,-30,-30,-15); 
		angulo.add("E",-30,-15,-15,0);
		angulo.add("M",-15,0,0,15);
		angulo.add("D",0,15,15,30); 
		angulo.add("BD",15,30,30,30);
		FuzzyEngine fuzzyEngine = new FuzzyEngine();
		fuzzyEngine.register(direcao); 
		fuzzyEngine.register(posicao);
		fuzzyEngine.register(angulo);

		
		FuzzyBlockOfRules fuzzyBlockOfRules = new FuzzyBlockOfRules("if direcao is ME and posicao is MB then angulo is M \n " +
				"if direcao is ME and posicao is B then angulo is E \n" +
				"if direcao is ME and posicao is Ne then angulo is BE \n" +
				"if direcao is ME and posicao is A then angulo is BE \n" +
				"if direcao is ME and posicao is MA then angulo is BE \n" +
				
				"if direcao is E and posicao is MB then angulo is BD \n " +
				"if direcao is E and posicao is B then angulo is M \n" +
				"if direcao is E and posicao is Ne then angulo is E \n" +
				"if direcao is E and posicao is A then angulo is BE \n" +
				"if direcao is E and posicao is MA then angulo is BE \n" +
				
				"if direcao is N and posicao is MB then angulo is BD \n " +
				"if direcao is N and posicao is B then angulo is D \n" +
				"if direcao is N and posicao is Ne then angulo is M \n" +
				"if direcao is N and posicao is A then angulo is E \n" +
				"if direcao is N and posicao is MA then angulo is BE \n" +
				
				"if direcao is D and posicao is MB then angulo is BD \n " +
				"if direcao is D and posicao is B then angulo is BD \n" +
				"if direcao is D and posicao is Ne then angulo is D \n" +
				"if direcao is D and posicao is A then angulo is M \n" +
				"if direcao is D and posicao is MA then angulo is E \n" +
				
				"if direcao is MD and posicao is MB then angulo is BD \n " +
				"if direcao is MD and posicao is B then angulo is BD \n" +
				"if direcao is MD and posicao is Ne then angulo is BD \n" +
				"if direcao is MD and posicao is A then angulo is D \n" +
				"if direcao is MD and posicao is MA then angulo is M " 
				);
		fuzzyEngine.register(fuzzyBlockOfRules);
		
		direcao.setInputValue(0);
		posicao.setInputValue(1);
		
		
		try {
			
			
			
			fuzzyBlockOfRules.parseBlock();
			
			System.out.println(fuzzyBlockOfRules.evaluateBlockText());
			double result = angulo.defuzzify();
			System.out.println(""+result);
		} catch (RulesParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fuzzyBlockOfRules.evaluateBlock(); 
		catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoRulesFiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		 
		
		
		
		
	}

}

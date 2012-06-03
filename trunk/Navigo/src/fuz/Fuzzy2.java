package fuz;


import java.util.ArrayList;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;



public class Fuzzy2 {
	public FuzzyEngine fuzzyEngine ;
	
	public Fuzzy2() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static String doFuzzy(ArrayList<VariavelLinguistica> dirX,ArrayList<VariavelLinguistica> dirY) {
		// TODO Auto-generated method stub
		//System.out.println("teste");
		
		LinguisticVariable direcao = new LinguisticVariable("direcao");
		//angulo.add(name, start, left_top, right_top, finish)
//		direcao.add("ME",0,0,0,5); 
//		direcao.add("E",0,5,5,10);
//		direcao.add("N",5,10,10,15);
//		direcao.add("D",10,15,15,20); 
//		direcao.add("MD",15,20,20,20);		
		for(int i =0;i<dirX.size();i++){
			direcao.add(dirX.get(i).getName(),dirX.get(i).getStart(),dirX.get(i).getLeftTop(),dirX.get(i).getRightTop(),dirX.get(i).getFinish());
		}

		LinguisticVariable posicao = new LinguisticVariable("posicao"); 
//		posicao.add("MB",0,0,0,5); 
//		posicao.add("B",0,5,5,10);
//		posicao.add("Ne",5,10,10,15);
//		posicao.add("A",10,15,15,20); 
//		posicao.add("MA",15,20,20,20);
		for(int i =0;i<dirY.size();i++){
			posicao.add(dirY.get(i).getName(),dirY.get(i).getStart(),dirY.get(i).getLeftTop(),dirY.get(i).getRightTop(),dirY.get(i).getFinish());
		}
		
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
	
		String result="a";
		
		try {
			
			fuzzyBlockOfRules.parseBlock();
			fuzzyBlockOfRules.evaluateBlockText();
			result = String.valueOf(angulo.defuzzify());
			
		} catch (RulesParsingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();//printStackTrace();
		}catch (NoRulesFiredException e) {
			// TODO Auto-generated catch block
			return e.getMessage();//printStackTrace();
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} 

		return result;
		 
		
		
		
		
	}

}

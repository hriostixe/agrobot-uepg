package fuz;


import java.util.ArrayList;

import agrobot.navigo.Point;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;



public class Fuzzy {
	public FuzzyEngine fuzzyEngine ;
	public static String rotulosX[] = {"ME", "E", "N", "D", "MD"};
	public static String rotulosY[] = {"MB", "B", "Ne", "A", "MA"};
	public static ArrayList<VariavelLinguistica> dirX;
	public static ArrayList<VariavelLinguistica> dirY;
	
	public Fuzzy() {
		// TODO Auto-generated constructor stub
	}

	
	
	public static String doFuzzy(double x,double y){
		
		LinguisticVariable direcao = new LinguisticVariable("direcao");
		
		for(int i =0;i<dirX.size();i++){
			direcao.add(dirX.get(i).getName(),dirX.get(i).getStart(),dirX.get(i).getLeftTop(),dirX.get(i).getRightTop(),dirX.get(i).getFinish());
		}

		LinguisticVariable posicao = new LinguisticVariable("posicao"); 
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
		
		if(x>dirX.get(dirX.size()-1).getFinish() )
			x=dirX.get(dirX.size()-1).getFinish() ;
		if(y>dirY.get(dirY.size()-1).getFinish())
			y=dirY.get(dirY.size()-1).getFinish() ;
		direcao.setInputValue(x);
		posicao.setInputValue(y);
	
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
	
	
	public static void createRules(){
		Ranges   r = new Ranges();
		dirX = r.createRanges((int) Point.getFirstCatetoAdjacente(), 5, rotulosX);
		dirY = r.createRanges((int) Point.getFirstCatetoOposto(), 5, rotulosY);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("teste");
		
	}

}

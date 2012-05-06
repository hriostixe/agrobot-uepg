package fuz;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;


public class Teste {
	public FuzzyEngine fuzzyEngine ;
	
	public Teste() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LinguisticVariable angulo = new LinguisticVariable("angulo");
		
		//angulo.add(name, start, left_top, right_top, finish)
		angulo.add("Nao_virar",0,0,0,30); 
		angulo.add("Virar_pouco",0,30,30,60);
		angulo.add("Virar_muito",30,60,60,90);
		angulo.add("Virar_completamente",60,90,90,90);
		FuzzyEngine fuzzyEngine = new FuzzyEngine();
		fuzzyEngine.register(angulo); 

		FuzzyBlockOfRules fuzzyBlockOfRules = new FuzzyBlockOfRules("if angulo is negativo then energia is aumentou \n" +
				"if angulo is positivo then energia is diminuiu");
		fuzzyEngine.register(fuzzyBlockOfRules);
		
		
		
		angulo.setInputValue(-1);
		
		
		try {
			fuzzyBlockOfRules.parseBlock();
			
			System.out.println(fuzzyBlockOfRules.evaluateBlockText());
			double result = angulo.defuzzify();
			System.out.println(result);
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

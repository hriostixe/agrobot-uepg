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
		System.out.println("teste");
		LinguisticVariable angulo = new LinguisticVariable("angulo");
		
		angulo.add("negativo",-3.14,-3.14,0,0); 
		angulo.add("positivo",0,0,3.14,3.14); 
		LinguisticVariable energia = new LinguisticVariable("energia"); 
		energia.add("diminuiu",-2,-1,-1,0); 
		energia.add("aumentou",0,1,1,2);
		FuzzyEngine fuzzyEngine = new FuzzyEngine();
		fuzzyEngine.register(angulo); 
		fuzzyEngine.register(energia);
		FuzzyBlockOfRules fuzzyBlockOfRules = new FuzzyBlockOfRules("if angulo is negativo then energia is aumentou \n" +
				"if angulo is positivo then energia is diminuiu");
		fuzzyEngine.register(fuzzyBlockOfRules);
		
		
		
		energia.setInputValue(-1);
		
		
		try {
			fuzzyBlockOfRules.parseBlock();
			
			System.out.println(fuzzyBlockOfRules.evaluateBlockText());
			double result = energia.defuzzify();
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

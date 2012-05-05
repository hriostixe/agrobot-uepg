package fuz;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;


public class Teste2 {
	public FuzzyEngine fuzzyEngine ;
	
	public Teste2() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("teste");
		LinguisticVariable fundos = new LinguisticVariable("fundos");
		//angulo.add(name, start, left_top, right_top, finish)
		fundos.add("inadequado",0,20,20,40); 
		fundos.add("normal",20,50,50,80);
		fundos.add("adequado",60,80,80,100);
		
		LinguisticVariable pessoal = new LinguisticVariable("pessoal"); 
		pessoal.add("pouco",0,30,30,70); 
		pessoal.add("muito",30,70,70,100);
		
		LinguisticVariable risco = new LinguisticVariable("risco"); 
		risco.add("baixo",0,20,20,40); 
		risco.add("normal",20,50,50,80);
		risco.add("alto",60,80,80,100);
		FuzzyEngine fuzzyEngine = new FuzzyEngine();
		fuzzyEngine.register(fundos); 
		fuzzyEngine.register(pessoal);
		fuzzyEngine.register(risco);

		
		FuzzyBlockOfRules fuzzyBlockOfRules = new FuzzyBlockOfRules("if fundos is adequado or pessoal is pouco then risco is baixo \n " +
				"if fundos is normal and pessoal is muito then risco is normal \n" +
				"if fundos is inadequado then risco is alto	");
		fuzzyEngine.register(fuzzyBlockOfRules);
		
		fundos.setInputValue(60);
		pessoal.setInputValue(80);
		
		
		try {
			fuzzyBlockOfRules.parseBlock();
			
			System.out.println(fuzzyBlockOfRules.evaluateBlockText());
			double result = risco.defuzzify();
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

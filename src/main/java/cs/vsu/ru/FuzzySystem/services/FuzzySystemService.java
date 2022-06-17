package cs.vsu.ru.FuzzySystem.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTrapetzoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethodMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;

@Service
public class FuzzySystemService {

	public double[] getValues() {
		/*
		 * FIS fis = new FIS();
		 * FunctionBlock functionBlock = new FunctionBlock(fis);
		 * fis.addFunctionBlock("tipper", functionBlock);
		 * 
		 * Variable service = new Variable("service");
		 * functionBlock.setVariable(service.getName(), service);
		 * 
		 * Value poorX[] = { new Value(0), new Value(4) };
		 * Value poorY[] = { new Value(1), new Value(0) };
		 * MembershipFunction poor = new MembershipFunctionPieceWiseLinear(poorX,
		 * poorY);
		 * 
		 * MembershipFunction good = new MembershipFunctionTrapetzoidal(new Value(1),
		 * new Value(4), new Value(6),
		 * new Value(9));
		 * 
		 * LinguisticTerm ltPoor = new LinguisticTerm("poor", poor);
		 * service.add(ltPoor);
		 * // for output
		 * tip.setDefuzzifier(new DefuzzifierCenterOfGravity(tip));
		 * 
		 * RuleBlock ruleBlock = new RuleBlock(functionBlock);
		 * ruleBlock.setName("No1");
		 * ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
		 * ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());
		 * 
		 * // RULE 1 : IF service IS poor OR food is rancid THEN tip IS cheap;
		 * Rule rule1 = new Rule("Rule1", ruleBlock);
		 * rule1.addAntecedent(service, "poor", false);
		 * ruleBlock.add(rule1);
		 * 
		 * // RULE 3 : IF ((service IS good) OR (service IS excellent)) AND food IS
		 * // delicious THEN tip is generous;
		 * Rule rule3 = new Rule("Rule3", ruleBlock);
		 * RuleTerm term1 = new RuleTerm(service, "good", false); // Create 'terms'
		 * RuleTerm term2 = new RuleTerm(service, "excellent", false);
		 * RuleTerm term3 = new RuleTerm(food, "delicious", false);
		 * 
		 * RuleExpression antecedentOr = new RuleExpression(term1, term2,
		 * RuleConnectionMethodOrMax.get());
		 * RuleExpression antecedentAnd = new RuleExpression(antecedentOr, term3,
		 * RuleConnectionMethodAndMin.get());
		 * rule3.setAntecedents(antecedentAnd); // Set antecedent
		 * 
		 * rule3.addConsequent(tip, "generous", false);
		 * ruleBlock.add(rule3);
		 * 
		 * HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
		 * ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
		 * functionBlock.setRuleBlocks(ruleBlocksMap);
		 * 
		 * // ---
		 * // Show generated FIS (FCL) and show animation
		 * // ---
		 * fis.getVariable("service").setValue(2);
		 * fis.getVariable("food").setValue(7);
		 * fis.evaluate();
		 * System.out.println(fis.getVariable("tip").getValue());
		 * return new double[] { fis.getVariable("tip").getValue() };
		 */
		return null;
	}
}

package cs.vsu.ru.FuzzySystem.services;

import java.util.HashMap;
import java.util.Map;

import cs.vsu.ru.FuzzySystem.model.*;

import org.springframework.stereotype.Service;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravitySingletons;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierLeftMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierRightMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfArea;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTrapetzoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethod;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodBoundedSum;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodNormedSum;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethod;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethodMin;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethodProduct;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;

@Service
public class FuzzySystemService {

	private void addTermsToVariable(Term[] terms, Variable var) throws Exception {
		// добавляем термины
		for (Term term : terms) {
			Function func = term.getTermFunction();
			MembershipFunction membershipFunction = switch (func.getName().toLowerCase()) {
				case ("треугольная") -> new MembershipFunctionTriangular(
						new Value(func.getPoints()[0]),
						new Value(func.getPoints()[1]),
						new Value(func.getPoints()[2]));
				case ("трапецеидальная") -> new MembershipFunctionTrapetzoidal(
						new Value(func.getPoints()[0]),
						new Value(func.getPoints()[1]),
						new Value(func.getPoints()[2]),
						new Value(func.getPoints()[3]));
				case ("прямоугольная") -> new MembershipFunctionTrapetzoidal(
						new Value(func.getPoints()[0]),
						new Value(func.getPoints()[0]),
						new Value(func.getPoints()[1]),
						new Value(func.getPoints()[1]));
				default -> throw new Exception("Переданна необрабатываемая функция.");
			};
			var.add(new LinguisticTerm(term.getName(), membershipFunction));
		}
	}

	public FIS getEvaluatedFIS(FuzzySystem fuzzySystem) throws Exception {
		// создаем нечеткую систему
		FIS fis = new FIS();
		FunctionBlock functionBlock = new FunctionBlock(fis);
		fis.addFunctionBlock("name", functionBlock);

		// добавление входных данных в функциональный блок
		for (InputVariable inputVariable : fuzzySystem.getInputVariables()) {
			Variable var = new Variable(inputVariable.getName());
			// задаем значение переменной
			var.setValue(inputVariable.getValue());
			functionBlock.setVariable(var.getName(), var);
			addTermsToVariable(inputVariable.getType().getTerms(), var);
		}

		// добавление выходящих данных в функциональный блок
		for (OutputVariable outputVariable : fuzzySystem.getOutputVariables()) {
			Variable var = new Variable(outputVariable.getName());
			functionBlock.setVariable(var.getName(), var);
			addTermsToVariable(outputVariable.getType().getTerms(), var);
			Defuzzifier defuzzifier = switch (outputVariable.getMethod().getName().toLowerCase()) {
				case ("центр тяжести") -> new DefuzzifierCenterOfGravity(var);
				case ("центр площади") -> new DefuzzifierCenterOfArea(var);
				case ("центр тяжести для синглтонов") -> new DefuzzifierCenterOfGravitySingletons(var);
				case ("крайний левый максимум") -> new DefuzzifierLeftMostMax(var);
				case ("крайний правый максимум") -> new DefuzzifierRightMostMax(var);
				default -> throw new Exception("Неверный метод деффазификации.");
			};
			var.setDefuzzifier(defuzzifier);
			var.setDefaultValue(outputVariable.getDef());
		}

		// Задаем блок с правилами
		RuleBlock ruleBlock = new RuleBlock(functionBlock);
		ruleBlock.setName("Блок правил");

		// задаем метод аккумулирования
		RuleAccumulationMethod ruleAccumulationMethod = switch (fuzzySystem.getAccumulation().toLowerCase()) {
			case ("максимум") -> new RuleAccumulationMethodMax();
			case ("ограниченная сумма") -> new RuleAccumulationMethodBoundedSum();
			case ("нормализированная сумма") -> new RuleAccumulationMethodNormedSum();
			default -> throw new Exception("Неверный метод аккумуляции.");
		};
		ruleBlock.setRuleAccumulationMethod(ruleAccumulationMethod);

		// задаем метод активации
		RuleActivationMethod ruleActivationMethod = switch (fuzzySystem.getActivator()) {
			case ("произведение") -> new RuleActivationMethodProduct();
			case ("минимум") -> new RuleActivationMethodMin();
			default -> throw new Exception("Неверный метод активации.");
		};
		ruleBlock.setRuleActivationMethod(ruleActivationMethod);

		// задаем правила
		for (RuleInput ruleInput : fuzzySystem.getRules()) {
			Rule rule = new Rule(ruleInput.getName(), ruleBlock);

			// задаем условия
			Condition[] conditions = ruleInput.getConditions();
			for (Condition condition : conditions) {
				// отрицание в условии
				boolean isNegative = switch (condition.getConnection()) {
					case ("=") -> false;
					case ("&#8800;") -> true;
					default -> throw new Exception("Неверный оператор условия.");
				};
				// Задаем условие
				RuleTerm ruleTerm = new RuleTerm(
						functionBlock.getVariable(condition.getVariable().getName()),
						condition.getTerm().getName(), isNegative);
				if (condition.getConditionConnector() == null) {
					rule.addAntecedent(ruleTerm.getVariable(), ruleTerm.getTermName(),
							isNegative);
				} else {
					switch (condition.getConditionConnector()) {
						case ("и") -> rule.setAntecedents(new RuleExpression(
								rule.getAntecedents(), ruleTerm,
								RuleConnectionMethodAndMin.get()));
						case ("или") -> rule.setAntecedents(new RuleExpression(
								rule.getAntecedents(), ruleTerm,
								RuleConnectionMethodOrMax.get()));
						default -> throw new Exception("Неверная связь между условиями.");
					}
				}

			}

			// Задали постусловие
			Action[] actions = ruleInput.getActions();
			for (Action action : actions) {
				rule.addConsequent(functionBlock.getVariable(action.getVariable().getName()),
						action.getTerm().getName(), false);
			}

			rule.setWeight(ruleInput.getWeight());
			ruleBlock.add(rule);
		}
		// Задаем блок правил
		HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
		ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
		functionBlock.setRuleBlocks(ruleBlocksMap);

		// Вычисляем выходные данные
		fis.evaluate();

		return fis;
	}

	public Map<String, Double> getOutputValues(FuzzySystem fs) throws Exception {
		Map<String, Double> values = new HashMap<>();
		FIS fis = getEvaluatedFIS(fs);
		for (OutputVariable outputVariable : fs.getOutputVariables()) {
			values.put(outputVariable.getName(),
					Math.ceil(fis.getVariable(outputVariable.getName()).getValue() * 1000) / 1000);
		}
		return values;
	}
}

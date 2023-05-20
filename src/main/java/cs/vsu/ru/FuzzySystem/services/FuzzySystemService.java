package cs.vsu.ru.FuzzySystem.services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs.vsu.ru.FuzzySystem.model.*;

import org.springframework.stereotype.Service;

import net.sourceforge.jFuzzyLogic.rule.*;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethoNormedSum;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodBoundedSum;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodProbOr;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodSum;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodOrMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravitySingletons;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierLeftMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierMeanMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierRightMostMax;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfArea;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionContinuous;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionDiscrete;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionFuncion;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGaussian;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenericSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionSigmoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTrapetzoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethodMin;
import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethodProduct;

@Service
public class FuzzySystemService {
	private final ModelConverterService modelConverterService;

	public FuzzySystemService(ModelConverterService modelConverterService) {
		this.modelConverterService = modelConverterService;
	}

	private Variable variableWithTerms(Term[] terms, CommonVariable iv) throws Exception {
		Variable var = new Variable(iv.getName());
		for (Term term : terms) {
			Function func = term.getTermFunction();
			MembershipFunction membershipFunction;
			switch (func.getName().toLowerCase()) {
				case ("треугольная"):
					membershipFunction = new MembershipFunctionTriangular(
							func.getLeft(), func.getMean(), func.getRight());
					break;
				case ("гаусса"):
					membershipFunction = new MembershipFunctionGaussian(
							func.getMean(),
							func.getStandardDeviation());
					break;
				case ("сигмоида"):
					membershipFunction = new MembershipFunctionSigmoidal(
							func.getMean(), func.getStandardDeviation());
					break;
				case ("кусочно-линейная"):
					membershipFunction = new MembershipFunctionPieceWiseLinear(
							func.getX(), func.getY());
					break;
				case ("трапецеидальная"):
					membershipFunction = new MembershipFunctionTrapetzoidal(
							func.getLeft(), func.getUpperLeft(), func.getUpperRight(), func.getRight());
					break;
				default:
					throw new Exception("Переданна необрабатываемая функция.");
			}
			LinguisticTerm lt = new LinguisticTerm(term.getName(), membershipFunction);
			var.add(lt);
		}
		return var;
	}

	public FIS getEvaluatedFIS(FuzzySystem fuzzySystem) throws Exception {
		// создаем нечеткую систему
		FIS fis = new FIS();

		// Задаем блок с правилами
		FuzzyRuleSet rs = new FuzzyRuleSet();
		List<Variable> vars = new ArrayList<>();
		// добавление входных данных в функциональный блок
		for (InputVariable inputVariable : fuzzySystem.getInputVariables()) {
			// создаем переменную
			Variable variable = variableWithTerms(inputVariable.getType().getTerms(), inputVariable);
			variable.setValue(inputVariable.getValue());
			// задаем метод аккумулирования
			switch (fuzzySystem.getAccumulation().toLowerCase()) {
				case ("максимум"):
					variable.setRuleAggregationMethod(new RuleAggregationMethodMax());
					break;
				case ("ограниченная разность"):
					variable.setRuleAggregationMethod(new RuleAggregationMethodProbOr());
					break;
				case ("алгебраическая сумма"):
					variable.setRuleAggregationMethod(new RuleAggregationMethodSum());
					break;
				case ("ограниченная сумма"):
					variable.setRuleAggregationMethod(new RuleAggregationMethodBoundedSum());
					break;
				case ("нормализированная сумма"):
					variable.setRuleAggregationMethod(new RuleAggregationMethoNormedSum());
					break;
				default:
					throw new Exception("Неверный метод аккумуляции.");
			}
			vars.add(variable);
		}

		// добавление выходящих данных в функциональный блок
		for (OutputVariable outputVariable : fuzzySystem.getOutputVariables()) {
			Variable var = variableWithTerms(outputVariable.getType().getTerms(), outputVariable);
			Defuzzifier defuzzifier;
			switch (outputVariable.getMethod().getName().toLowerCase()) {
				case ("центр тяжести"):
					defuzzifier = new DefuzzifierCenterOfGravity(var);
					break;
				case ("центр площади"):
					defuzzifier = new DefuzzifierCenterOfArea(var);
					break;
				case ("центр тяжести для синглтонов"):
					defuzzifier = new DefuzzifierCenterOfGravitySingletons(var);
					break;
				case ("крайний левый максимум"):
					defuzzifier = new DefuzzifierLeftMostMax(var);
					break;
				case ("крайний правый максимум"):
					defuzzifier = new DefuzzifierRightMostMax(var);
					break;
				case ("центр максимумов"):
					defuzzifier = new DefuzzifierMeanMax(var);
					break;
				default:
					throw new Exception("Неверный метод деффазификации.");
			}
			;
			var.setDefuzzifier(defuzzifier);
			var.setDefaultValue(outputVariable.getDef());
			vars.add(var);
		}

		// задаем метод активации
		switch (fuzzySystem.getActivator()) {
			case ("произведение"):
				rs.setRuleImplicationMethod(new RuleImplicationMethodProduct());
				break;
			case ("минимум"):
				rs.setRuleImplicationMethod(new RuleImplicationMethodMin());
				break;
			default:
				throw new Exception("Неверный метод активации.");
		}

		// задаем правила
		for (RuleInput ruleInput : fuzzySystem.getRules()) {
			FuzzyRule rule = new FuzzyRule(ruleInput.getName());
			// задаем условия
			Condition[] conditions = ruleInput.getConditions();
			for (Condition condition : conditions) {
				// отрицание в условии
				boolean isNegative;
				switch (condition.getConnection()) {
					case ("="):
						isNegative = false;
						break;
					case ("&#8800;"):
						isNegative = true;
						break;
					default:
						throw new Exception("Неверный оператор условия.");
				}

				Variable var = vars.stream()
						.filter(a -> condition.getVariable().getName().equals(a.getName()))
						.findFirst()
						.orElse(null);
				if (null == var) {
					throw new Exception("Ошибка поиска переменной.");
				}
				FuzzyRuleTerm ruleTerm = new FuzzyRuleTerm(
						var,
						condition.getTerm().getName(), isNegative);
				if (condition.getConditionConnector() == null) {
					rule.addAntecedent(ruleTerm.getVariable(), ruleTerm.getTermName(),
							isNegative);
				} else {
					switch (condition.getConditionConnector()) {
						case ("и"):
							// !!!!! не хватает методов доделать* добавить input еще один и
							rule.setAntecedents(new FuzzyRuleExpression(
									rule.getAntecedents(), ruleTerm,
									new RuleConnectionMethodAndMin()));
							break;
						case ("или"):
							rule.setAntecedents(new FuzzyRuleExpression(
									rule.getAntecedents(), ruleTerm,
									new RuleConnectionMethodOrMax()));
							break;
						default:
							throw new Exception("Неверная связь между условиями.");
					}
				}

			}

			// задание постусловия
			Action[] actions = ruleInput.getActions();
			for (Action action : actions) {
				Variable var = vars.stream()
						.filter(a -> action.getVariable().getName().equals(a.getName()))
						.findFirst()
						.orElse(null);
				rule.addConsequent(var,
						action.getTerm().getName(), false);
			}

			rule.setWeight(ruleInput.getWeight());
			rs.add(rule);
		}

		rs.evaluate();
		fis.addFuzzyRuleSet("ruleset", rs);
		return fis;
	}

	public Map<String, Double> getOutputValues(FuzzySystem fs) throws Exception {
		Map<String, Double> values = new HashMap<>();
		FIS fis = getEvaluatedFIS(fs);
		for (OutputVariable outputVariable : fs.getOutputVariables()) {
			System.out.println(fis.getFuzzyRuleSet().getVariable(outputVariable.getName()).getLatestDefuzzifiedValue());
			values.put(
					outputVariable.getName(),
					Math.ceil(fis.getFuzzyRuleSet().getVariable(outputVariable.getName()).getLatestDefuzzifiedValue()
							* 1000) / 1000);
		}
		return values;
	}

	public String toStringFCL(FuzzySystem fs) throws Exception {
		return getEvaluatedFIS(fs).toStringFCL();
	}

	public String toString(FuzzySystem fs) throws Exception {
		return getEvaluatedFIS(fs).toString();
	}

	public FuzzySystem fromString(String text) throws Exception {
		FIS fis = FIS.createFromString(text, false);
		return modelConverterService.getConvertedFuzzySystemFromFIS(fis);
	}
}

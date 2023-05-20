package cs.vsu.ru.FuzzySystem.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.stereotype.Service;

import cs.vsu.ru.FuzzySystem.model.Action;
import cs.vsu.ru.FuzzySystem.model.Condition;
import cs.vsu.ru.FuzzySystem.model.Function;
import cs.vsu.ru.FuzzySystem.model.FuzzySystem;
import cs.vsu.ru.FuzzySystem.model.InputVariable;
import cs.vsu.ru.FuzzySystem.model.Method;
import cs.vsu.ru.FuzzySystem.model.OutputVariable;
import cs.vsu.ru.FuzzySystem.model.RuleInput;
import cs.vsu.ru.FuzzySystem.model.Term;
import cs.vsu.ru.FuzzySystem.model.Type;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleExpression;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleTerm;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

@Service
public class ModelConverterService {

	// Преобразуем MembershipFunction -> Function
	private Function getModelFunction(MembershipFunction membershipFunction) {
		// обработка ф-ии принадлежности
		Function modelFunction = new Function();
		switch (membershipFunction.getName().toLowerCase()) {
			case ("triangular"):
				modelFunction.setName("Треугольная");
				modelFunction.setLeft(membershipFunction.getParameter(0));
				modelFunction.setMean(membershipFunction.getParameter(1));
				modelFunction.setRight(membershipFunction.getParameter(2));
				break;
			case ("gaussian"):
				modelFunction.setName("Гаусса");
				modelFunction.setMean(membershipFunction.getParameter(0));
				modelFunction.setStandardDeviation(membershipFunction.getParameter(1));
				break;
			case ("sigmoidal"):
				modelFunction.setName("Сигмоида");
				modelFunction.setMean(membershipFunction.getParameter(0));
				modelFunction.setStandardDeviation(membershipFunction.getParameter(1));
				break;
			case ("trapetzoidal"):
				modelFunction.setName("Трапецеидальная");
				modelFunction.setLeft(membershipFunction.getParameter(0));
				modelFunction.setUpperLeft(membershipFunction.getParameter(1));
				modelFunction.setUpperRight(membershipFunction.getParameter(1));
				modelFunction.setRight(membershipFunction.getParameter(2));
				break;
			case ("piecewiselinear"):
				modelFunction.setName("Кусочно-линейная");
				int size = membershipFunction.getParametersLength();
				double[] x = new double[size / 2];
				double[] y = new double[size / 2];
				int k = 0;
				int j = 0;
				String raw = "";
				for (int i = 0; i < size; i++) {
					if (i % 2 == 0) {
						x[j++] = membershipFunction.getParameter(i);
						raw += membershipFunction.getParameter(i) + ";";
					} else {
						y[k++] = membershipFunction.getParameter(i);
						raw += membershipFunction.getParameter(i) + " ";
					}
				}
				modelFunction.setRaw(raw);
				modelFunction.setY(y);
				modelFunction.setX(x);
				break;
			default:

		}
		return modelFunction;
	}

	// Преобразуем FuzzyRuleTerm -> Term
	private Term getModelTerm(FuzzyRuleTerm term) {
		Term modelTerm = new Term();
		modelTerm.setName(term.getTermName());
		modelTerm.setTermFunction(this.getModelFunction(term.getMembershipFunction()));
		return modelTerm;
	}

	// Преобразуем LinguisticTerm -> Term
	private Term getModelTerm(LinguisticTerm term) {
		Term modelTerm = new Term();
		modelTerm.setName(term.getTermName());
		modelTerm.setTermFunction(this.getModelFunction(term.getMembershipFunction()));
		return modelTerm;
	}

	// Преобразуем из Variable -> InputVariable
	private InputVariable getModelInputVariable(Variable variable) {
		InputVariable inputVariable = new InputVariable();
		inputVariable.setName(variable.getName());
		inputVariable.setValue(variable.getValue());
		HashMap<String, LinguisticTerm> types = variable.getLinguisticTerms();
		ArrayList<Term> listTerms = new ArrayList<>();
		for (String key : types.keySet()) {
			LinguisticTerm term = types.get(key);
			this.getModelTerm(term);
			listTerms.add(this.getModelTerm(term));
		}
		Term[] modelTerms = new Term[listTerms.size()];
		modelTerms = listTerms.toArray(modelTerms);
		Type type = new Type();
		type.setTerms(modelTerms);
		type.setName(variable.getName());
		inputVariable.setType(type);
		return inputVariable;
	}

	// Преобразуем из FuzzyRuleTerm,connector -> Condition
	private Condition getModelCondition(FuzzyRuleTerm term, String connector) {
		Condition modelCondition = new Condition();
		modelCondition.setTerm(this.getModelTerm(term));

		if (term.isNegated()) {
			modelCondition.setConnection("&#8800;");
		} else {
			modelCondition.setConnection("=");
		}
		if (connector != null) {
			if (connector == "and") {
				modelCondition.setConditionConnector("и");
			}
			if (connector == "or") {
				modelCondition.setConditionConnector("или");
			}

		} else {
			modelCondition.setConditionConnector(null);
		}
		modelCondition.setVariable(getModelInputVariable(term.getVariable()));
		return modelCondition;
	}

	// вспомогательный метод рекурсии для сокращения кода
	// Определяет FuzzyRuleTerm или FuzzyRuleExpression и добавляет в условие
	private void identificationTermOrExpression(Object object, String connection, ArrayList<Condition> conditions) {
		if (object == null) {
			return;
		}
		if (object.getClass() == FuzzyRuleTerm.class) {
			FuzzyRuleTerm term = FuzzyRuleTerm.class.cast(object);
			conditions.add(this.getModelCondition(term, connection));
		} else {
			recursionExpression(FuzzyRuleExpression.class.cast(object), conditions);
		}
	}

	// Рекурсивный метод добавление Term с connectionMethod в Conditions(условия).
	// Если FuzzyRuleExpression, то рекурсивный заход.
	private void recursionExpression(FuzzyRuleExpression expression, ArrayList<Condition> conditions) {
		this.identificationTermOrExpression(
				expression.getTerm1(),
				expression.getRuleConnectionMethod().getName(),
				conditions);
		this.identificationTermOrExpression(
				expression.getTerm2(),
				expression.getRuleConnectionMethod().getName(),
				conditions);
	}

	// Преобразуем из Variable -> OutputVariable
	private OutputVariable getModelOutputVariable(Variable variable) {
		OutputVariable outputVariable = new OutputVariable();

		outputVariable.setName(variable.getName());
		outputVariable.setDef(variable.getDefaultValue());
		String methodName = "";
		switch (variable.getDefuzzifier().getName().toLowerCase()) {
			case ("centerofgravity"):
				methodName = "Центр тяжести";
				break;
			case ("centerofgravitysingleton"):
				methodName = "Центр тяжести для синглтонов";
				break;
			case ("centerofarea"):
				methodName = "Центр площади";
				break;
			case ("rightmostmax"):
				methodName = "Крайний правый максимум";
				break;
			case ("leftmostmax"):
				methodName = "Крайний левый максимум";
				break;
			case ("meanmax"):
				methodName = "Центр максимумов";
				break;
		}
		Method modelMethod = new Method(methodName);
		outputVariable.setMethod(modelMethod);
		HashMap<String, LinguisticTerm> types = variable.getLinguisticTerms();
		ArrayList<Term> listTerms = new ArrayList<>();
		for (String key : types.keySet()) {
			LinguisticTerm term = types.get(key);
			this.getModelTerm(term);
			listTerms.add(this.getModelTerm(term));
		}
		Term[] modelTerms = new Term[listTerms.size()];
		modelTerms = listTerms.toArray(modelTerms);
		Type type = new Type();
		type.setTerms(modelTerms);
		type.setName(variable.getName());
		outputVariable.setType(type);
		return outputVariable;
	}

	// Преобразуем из FuzzyRuleTerm -> Action
	private Action getModelAction(FuzzyRuleTerm term) {
		Action modelAction = new Action();
		modelAction.setTerm(this.getModelTerm(term));
		if (term.isNegated()) {
			modelAction.setConnection("&#8800;");
		} else {
			modelAction.setConnection("=");
		}
		modelAction.setVariable(getModelOutputVariable(term.getVariable()));
		return modelAction;
	}

	private RuleInput getModelRule(FuzzyRule rule) {
		RuleInput modelRule = new RuleInput();
		modelRule.setName(rule.getName());
		modelRule.setWeight(rule.getWeight());

		ArrayList<Condition> conditions = new ArrayList<>();
		this.recursionExpression(rule.getAntecedents(), conditions);
		modelRule.setConditions(conditions.toArray(new Condition[conditions.size()]));

		ArrayList<Action> actions = new ArrayList<>();
		for (FuzzyRuleTerm term : rule.getConsequents()) {
			actions.add(this.getModelAction(term));
		}
		modelRule.setActions(actions.toArray(new Action[actions.size()]));
		return modelRule;
	}

	public FuzzySystem getConvertedFuzzySystemFromFIS(FIS fis) throws Exception {
		FuzzyRuleSet ruleSet = fis.getFuzzyRuleSet();
		LinkedList<FuzzyRule> rules = ruleSet.getRules();
		ArrayList<RuleInput> modelRules = new ArrayList<RuleInput>();

		for (FuzzyRule rule : rules) {
			modelRules.add(this.getModelRule(rule));
		}
		FuzzySystem modelSystem = new FuzzySystem();
		modelSystem.setRules(modelRules.toArray(new RuleInput[modelRules.size()]));
		modelSystem.setActivator(ruleSet.getRuleImplicationMethod().getName());

		ArrayList<InputVariable> modelInputVariables = new ArrayList<InputVariable>();
		ArrayList<OutputVariable> modelOutputVariables = new ArrayList<OutputVariable>();

		for (String name : ruleSet.getVariables().keySet()) {
			Variable variable = ruleSet.getVariable(name);
			if (variable.getDefuzzifier() == null) {
				modelInputVariables.add(this.getModelInputVariable(variable));
			} else {
				modelOutputVariables.add(this.getModelOutputVariable(variable));
			}
		}
		String accumulationName = null;
		for (String key : ruleSet.getVariables().keySet()) {
			if (ruleSet.getVariable(key).getRuleAggregationMethod() != null) {
				accumulationName = ruleSet.getVariable(key).getRuleAggregationMethod().getName();
			}
		}
		modelSystem.setAccumulation(accumulationName);
		modelSystem.setInputVariables(modelInputVariables.toArray(new InputVariable[modelInputVariables.size()]));
		modelSystem.setOutputVariables(modelOutputVariables.toArray(new OutputVariable[modelOutputVariables.size()]));

		return modelSystem;
	}
}

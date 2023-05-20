export default {
	state: () => ({
		rules:[],
    selected:'',
    // operators: [
    //   {
    //     name: "или",
    //     algorithms: [
    //       "максимум",
    //       "алгебраическая сумма",
    //       "ограниченная сумма",
    //     ],
    //   },
    //   {
    //     name: "и",
    //     algorithms: ["минимум", "произведение", "ограниченная разность"],
    //   },
    // ],
    activator:'минимум',
    activators: ["произведение", "минимум"],
    accumulation:'максимум',
    accumulations: [
      "максимум",
      "ограниченная сумма",
      "нормализированная сумма",
      "алгебраическая сумма",
      "ограниченная разность",
    ],
  }),
	mutations: {
		setRules (state, rules) {
			state.rules=rules
		},
		setAccumulation (state, accumulation) {
			state.accumulation=accumulation
		},
		setActivator (state, activator) {
			state.activator=activator
		},
		addRule (state, storeVariable) {
			if (storeVariable.inputVariables.length <= 0) {
				alert('Задайте входные переменные')
				return
			}
			if (storeVariable.outputVariables.length <= 0) {
				alert('Задайте выходные переменные')
				return
			}
			state.rules.push({
				name: "Правило № " + (state.rules.length + 1),
				conditions: [
					{
						variable: storeVariable.inputVariables[0],
						connection: "=",
						term: storeVariable.inputVariables[0].type.terms[0],
						conditionConnector: "и",
						indexVariable: 0,
						indexTerm:0
					},
				],
				actions: [
					{
						variable: storeVariable.outputVariables[0],
						connection: "=",
						term: storeVariable.outputVariables[0].type.terms[0],
						indexVariable: 0,
						indexTerm:0
					},
				],
				weight: 1,
			})
		},
		changeSelectedRule(state,rule) {
			if (state.selected == rule) {
				state.selected = null;
			} else {
				state.selected = rule;
			}
		},
		deleteLastClauseInConditionsSelectedRule(state) {
			if (state.selected.conditions.length > 1) {
				state.selected.conditions.pop();
			}else {
				alert('Нельзя удалить последнее условие')
			}
		},
		addClauseInConditionsSelectedRule(state,length) {
			if (state.selected.conditions.length < length) {
				state.selected.conditions.push({
					variable: state.selected.conditions[0].variable,
					connection: "=",
					term: state.selected.conditions[0].variable.type.terms[0],
					conditionConnector: "и",
					indexVariable: 0,
					indexTerm:0
				})
			} else {
				alert('У вас недостаточно входных переменных')
			}
		},
		addClauseInActionsSelectedRule(state,length) {
			if (state.selected.actions.length < length) {
				state.selected.actions.push({
					variable: state.selected.actions[0].variable,
					connection: "=",
					term: state.selected.actions[0].variable.type.terms[0],
					indexVariable: 0,
					indexTerm:0
				});
			} else {
				alert('У вас недостаточно выходных переменных')
			}
		},
		deleteLastClauseInActionsSelectedRule(state) {
			if (state.selected.actions.length > 1) {
				state.selected.actions.pop();
			} else {
				alert('Нельзя удалить последнее следствие')
			}
		},
		setClauseVariable (state, obj) {
			if (obj.isInput) {
				state.selected.conditions[obj.indexClause].indexVariable=obj.indexVariable
				state.selected.conditions[obj.indexClause].variable= obj.variable
				state.selected.conditions[obj.indexClause].term= obj.variable.type.terms[0]
			} else {
				state.selected.actions[obj.indexClause].indexVariable=obj.indexVariable
				state.selected.actions[obj.indexClause].variable = obj.variable
				state.selected.actions[obj.indexClause].term = obj.variable.type.terms[0]
			}
		},
		setClauseTerm (state, obj) {
			if (obj.isInput) {
				state.selected.conditions[obj.indexClause].term= obj.term
			} else {
				state.selected.actions[obj.indexClause].term= obj.term
			}
		},
		setClauseConnection (state, obj) {
			if (obj.isInput) {
				state.selected.conditions[obj.indexClause].connection= obj.connection
			} else {
				state.selected.actions[obj.indexClause].connection= obj.connection
			}
		}
  },
}

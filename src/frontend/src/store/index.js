import axios from 'axios'
import { createStore } from 'vuex'
import addableTerm from './addableTerm';
import editableTerm from './editableTerm';
import types from './type';
import variable from './variable';
import rule from './rule';
import chart from './chart';

export default createStore({
	state () {
		return {
			// url: 'http://localhost:8080',
			url: 'http://vizual-fuzzy-control-laguage.fiori-vrn.ru',
			isLoading: false,
			graphic: null,
			selectedType: null,
			selectedVariable: null,
			selectedOperator: null,
			selectedAlgorithm: null,
			selectedAccumulation: null,
			selectedActivator: null,
			selected: null,
		}
	},
	getters: {
		getGraphic (state) {
			return state.graphic
		},
		getRules (state) {
			return state.rules
		},
		selectedOperator (state) {
			return state.selectedOperator
		},
		selectedAlgorithm (state) {
			return state.selectedAlgorithm
		},
		selectedAccumulation (state) {
			return state.selectedAccumulation
		},
		selectedActivator (state) {
			return state.selectedActivator
		},
	},
	mutations: {
		selectChart (state, typeOrVariable) {
			state.selected = typeOrVariable
			// отображение графика
		},
		setIsColor (state, isColor) {
			state.isColor = isColor
		},
		setRules (state, rules) {
			state.rules = rules
		},
		setGraphic (state, graphic) {
			state.graphic = graphic
		},
		updateSelectedOperator (state, operator) {
			state.selectedOperator = operator
		},
		updateSelectedAlgorithm (state, val) {
			state.selectedAlgorithm = val
		},
		updateSelectedAccumulation (state, val) {
			state.selectedAccumulation = val
		},
		updateSelectedActivator (state, val) {
			state.selectedActivator = val
		},
		setLoading (state, isLoading) {
			state.isLoading = isLoading
		},
	},
	actions: {
		async getExample ({ state }, id) {
			state.isLoading = true
			return axios
				.get(state.url + '/api/examples/' + id)
				.then((response) => {
					const rules = response.data.rules
					console.log(rules);
					//добавляем типы
					// rules.forEach(rule => {
					// 	rule.conditions.forEach(condition => {
					// 		if (!state.type.types.includes(t => t.name == condition.variable.type.name)) {
					// 			state.variable.type.push(condition.variable.type)
					// 		}

					// 	})
					// 	rule.actions.forEach(action => {
					// 		if (!state.type.types.includes(t => t.name == action.variable.type.name)) {
					// 			state.variable.type.push(condition.variable.type)
					// 		}
					// 	})
					// })
					// rules.forEach(rule => {
					// 	rule.conditions.forEach(condition => {
					// 		condition.variable.type = state.type.types.find(type => type.name == condition.variable.type.name )
							
					// 	})
					// })


					// this.commit('setInputVariables', data.inputVariables)
					// this.commit('setOutputVariables', data.outputVariables)
					// rules.
					// 	data.rules.forEach((rule) => {
					// 		rule.actions.forEach((action) => {
					// 			action.variable = state.outputVariables.find((ov) => ov.name == action.variable.name)
					// 		})
					// 		rule.conditions.forEach((condition) => {
					// 			condition.variable = state.inputVariables.find(
					// 				(iv) => iv.name == condition.variable.name,
					// 			)
					// 		})
					// 	})
					// this.commit('setRules', data.rules)
				})
				.catch((error) => console.log(error))
				.finally(() => (state.isLoading = false))
		},
		async getOutputValues ({ state }) {
			state.isLoading = true
			return axios
				.post(
					state.url + '/api/get_output',
					JSON.stringify({
						rules: state.rule.rules,
						inputVariables: state.variable.inputVariables,
						outputVariables: state.variable.outputVariables,
						// operator: state.selectedOperator.name,
						// algorithm: state.selectedAlgorithm,
						accumulation: state.rule.accumulation,
						activator: state.rule.activator,
					}),
					{
						headers: { 'content-type': 'application/json' },
					},
				)
				.then((response) => {
					let variable
					Object.entries(response.data).forEach((entry) => {
						variable = state.variable.outputVariables.find((v) => v.name == entry[0])
						variable.value = entry[1]
					})
					this.commit('showChartByVariable',variable)
				})
				.catch((error) => console.log(error))
				.finally(() => (state.isLoading = false))
		},
		async importFCL ({ state }, file) {
			state.isLoading = true
			let fd = new FormData()
			fd.append('file', file)
			axios
				.post(state.url + '/api/input_file', fd, {
					headers: {
						'Content-Type': 'multipart/form-data',
					},
				})
				.then((response) => {
					const data = response.data
					console.log(data)
					this.commit('setInputVariables', data.inputVariables)
					this.commit('setOutputVariables', data.outputVariables)
					const types = []
					data.inputVariables.forEach((variable,index) => {
						variable.type.index = index
						types.push(variable.type)
					})
					data.outputVariables.forEach((variable,index) => {
						variable.type.index = index
						types.push(variable.type)
					})
					this.commit('setTypes',types)
					data.rules.forEach((rule) => {
						rule.actions.forEach((action) => {
							action.variable = state.variable.outputVariables.find((ov) => ov.name == action.variable.name)
						})
						rule.conditions.forEach((condition) => {
							condition.variable = state.variable.inputVariables.find(
								(iv) => iv.name == condition.variable.name,
							)
						})
					})
					this.commit('setRules', data.rules)
				})
				.catch((err) => {
					console.log(err)
				})
				.finally(() => {
					state.isLoading = false
				})
		},
		async exportFCL ({ state }) {
			state.isLoading = true
			return axios
				.post(
					state.url + '/api/export_fcl',
					JSON.stringify({
						rules: state.rule.rules,
						inputVariables: state.variable.inputVariables,
						outputVariables: state.variable.outputVariables,
						// operator: state.selectedOperator.name,
						// algorithm: state.selectedAlgorithm,
						accumulation: state.rule.accumulation,
						activator: state.rule.activator,
					}),
					{
						headers: { 'content-type': 'application/json' },
						responseType: 'blob',
					},
				)
				.then((response) => {
					const href = URL.createObjectURL(new Blob([response.data], { type: 'text/plain' }))
					const link = document.createElement('a')
					link.href = href
					link.setAttribute('download', 'fuzzySystem.fcl')
					document.body.appendChild(link)
					link.click()
					document.body.removeChild(link)
					URL.revokeObjectURL(href)
				})
				.catch((error) => console.log(error))
				.finally(() => (state.isLoading = false))
		},
		async export ({ state }) {
			state.isLoading = true
			return axios
				.post(
					state.url + '/api/export',
					JSON.stringify({
						rules: state.rule.rules,
						inputVariables: state.variable.inputVariables,
						outputVariables: state.variable.outputVariables,
						// operator: state.selectedOperator.name,
						// algorithm: state.selectedAlgorithm,
						accumulation: state.rule.accumulation,
						activator: state.rule.activator,
					}),
					{
						headers: { 'content-type': 'application/json' },
						responseType: 'blob',
					},
				)
				.then((response) => {
					const href = URL.createObjectURL(new Blob([response.data], { type: 'text/plain' }))
					const link = document.createElement('a')
					link.href = href
					link.setAttribute('download', 'fuzzySystem.txt')
					document.body.appendChild(link)
					link.click()
					document.body.removeChild(link)
					URL.revokeObjectURL(href)
				})
				.catch((error) => console.log(error))
				.finally(() => (state.isLoading = false))
		},
	},
	modules: {
		type: types,
		variable: variable,
		rule: rule,
		addableTerm: addableTerm,
		editableTerm: editableTerm,
		chart: chart,
	},
})

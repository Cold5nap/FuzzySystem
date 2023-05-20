export default {
	
  state: () => ({
    inputVariables: [
      // { name: 'Сервис', type: state.type.types[0], obj.value: 7 },
      // { name: 'Еда', type: state.type.types[1], obj.value: 8 },
    ],
    outputVariables: [
      // {
      //   name: 'Чаевые',
      //   type: state.type.types[2],
      //   def: 0,
      //   method: state.defuzzificationMethods[0],
      // },
    ],
    defuzzificationMethods: [
      { name: 'Центр тяжести' ,index:0},
      { name: 'Центр тяжести для синглтонов',index:1 },
      { name: 'Центр площади',index:2 },
      { name: 'Центр максимумов',index:3 },
      { name: 'Крайний левый максимум',index:4 },
      { name: 'Крайний правый максимум' ,index:5},
    ],
  }),
  mutations: {
    setNameInputVariable(state,obj) {
			state.inputVariables[obj.index].name = obj.value
			this.commit('rebuildByVariable')
    },
    setTypeInputVariable(state,obj) {
			state.inputVariables[obj.variableIndex].type = obj.type
			this.commit('rebuildByVariable')
    },
    setValueInputVariable(state,obj) {
			state.inputVariables[obj.index].value = obj.value
			this.commit('rebuildByVariable')
    },
    setValueOutputVariable(state,obj) {
			state.outputVariables[obj.index].value = obj.value
			this.commit('rebuildByVariable')
    },
    setNameOutputVariable(state,obj) {
			state.outputVariables[obj.index].name = obj.value
			this.commit('rebuildByVariable')
    },
    setDefOutputVariable(state,obj) {
      state.outputVariables[obj.index].def = obj.value
    },
    setTypeOutputVariable(state,obj) {
			state.outputVariables[obj.index].type = obj.type
			this.commit('rebuildByVariable')
    },
    setMethodOutputVariable(state,obj) {
      state.outputVariables[obj.index].method = obj.method
    },
    setInputVariables(state, vars) {
      state.inputVariables = vars
    },
    setOutputVariables(state, vars) {
      state.outputVariables = vars
    },
    deleteInputVariable(state, obj) {
			state.inputVariables.splice(obj.index, 1)
    },
    deleteOutputVariable(state, obj) {
			state.outputVariables.splice(obj.index, 1)
    },

		addInputVariable (state, obj) {
			state.inputVariables.push(Object.assign({}, obj))
    },
    addOutputVariable(state, obj) {
      state.outputVariables.push(Object.assign({},obj))
    },
  },
}

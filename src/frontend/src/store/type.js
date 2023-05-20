  export default {
    state: () => ({
      types: [
        {
					name: 'Сервис',
					index:0,
          terms: [
            { name: 'Плохой', termFunction: { name: 'Треугольная', left: '0', mean: '0', right: '2',index:0 } },
            { name: 'Хороший', termFunction: { name: 'Трапецеидальная', left: '1', upperLeft: '2', upperRight: '3', right: '4',index:1 } },
            { name: 'Великолепный', termFunction: { name: 'Треугольная', left: '4', mean: '5', right: '5',index:0 } },
          ],
        },
        {
					name: 'Еда',
					index:1,
          terms: [
            {
              name: 'Нормальная',
              termFunction: { name: 'Гаусса',mean:'3',standardDeviation:'0.7' ,index:2 },
						},
						{ name: 'Отвратительная', termFunction: { name: 'Треугольная', left: '0', mean: '0', right: '3' ,index:0} },
            { name: 'Вкусная', termFunction: { name: 'Треугольная', left: '3', mean: '5',right:'5',index:0 } },
          ],
        },
        {
					name: 'Чаевые',
					index:2,
          terms: [
            { name: 'Мало', termFunction: { name: 'Треугольная', left: '0', mean: '0', right: '300' ,index:0} },
            { name: 'Средние', termFunction: { name: 'Трапецеидальная', left: '200', upperLeft: '300', upperRight: '400',right:'600' ,index:1  } },
            { name: 'Щедро', termFunction: { name: 'Треугольная', left: '500', mean: '1000', right: '1000',index:0 } },
          ],
        },
      ],
      termFunctions: [
        { name: 'Треугольная', left: '0', mean: '0', right: '0',index:0 },
        { name: 'Трапецеидальная', left: '0', upperLeft: '0', upperRight: '0',right:'0' ,index:1  },
        { name: 'Гаусса',mean:'0', standardDeviation:'0',index:2},
        { name: 'Кусочно-линейная',x:[0],y:[0],raw:'0;0',index:3},
        { name: 'Сигмоида',mean:'0',standardDeviation:'0',index:4},
      ],
    }),
	mutations: {
		setTypes (state,types) {
			state.types=types
			},
      setTermName (state, obj) {
        state.types[obj.indexType].terms[obj.indexTerm].name=obj.name
      },
      setTypeName (state, obj) {
        state.types[obj.index].name = obj.name
      },
      setVisible (state,typeIndex) {
        state.types[typeIndex].visibility=true
      },
      setInvisible (state,typeIndex) {
        state.types[typeIndex].visibility=false
      },
		deleteTerm (state, obj) {
			state.types[obj.typeIndex].terms.splice(obj.termIndex, 1)
			this.commit('rebuildByType')
      },
      deleteType(state, index) {
        delete state.types.splice(index, 1)
      },
      setFunction(state, obj) {
        state.types[obj.typeIndex].terms[obj.termIndex].termFunction = state.termFunctions[obj.functionIndex]
      },
      addTypeName (state, name) {
				state.types.push({ name: name, terms: [] })
				alert(name+' добавлен')
      },
      addTerm (state, obj) {
        const term = obj.term
				const typeIndex = obj.typeIndex
				console.log(JSON.stringify(obj));
        if (term && term.name && term.termFunction) {
					state.types[typeIndex].terms.push(Object.assign({}, term))
					alert(term.name + " добавлен")
        } else {
          alert('Необходимо заполнить все поля')
          return
				}
				this.commit('rebuildByType')
      },
		addType (state, name) {
				state.types.push({ name: name, terms: [], index: state.types.length })
				
      },
    },
  }

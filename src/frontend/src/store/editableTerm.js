export default {
	state: () => ({
		term:'',
    //нет полей для значений терма, не задан 
	}),
	mutations: {
		setEditableTerm (state, term) {
      state.term = term
		},
		setRightEditableTerm (state, right) {
			
			state.term.termFunction.right = right
			this.commit('rebuildByType')
    },
    setMeanEditableTerm (state, mean) {
			
			state.term.termFunction.mean = mean
			this.commit('rebuildByType')
    },
    setLeftEditableTerm (state, left) {
			
			state.term.termFunction.left = left
			this.commit('rebuildByType')
    },
    setUpperLeftEditableTerm (state, upperLeft) {
			
			state.term.termFunction.upperLeft = upperLeft
			this.commit('rebuildByType')
    },
    setUpperRightEditableTerm (state, upperRight) {

			state.term.termFunction.upperRight = upperRight
			this.commit('rebuildByType')
    },
    setStandardDeviationEditableTerm (state, standardDeviation) {
			
			state.term.termFunction.standardDeviation = standardDeviation
			this.commit('rebuildByType')
    },
    setPointsEditableTerm (state, str) {
      state.term.termFunction.raw = str
      let x = []
      let y = []
      str.split(" ").forEach(p => {
				let xy = p.split(";")
				x.push(xy[0])
				if (Number.parseFloat(xy[1]) > 1) {
					alert('Нельзя задать y > 1')
					return
				}
				y.push(xy[1])
			})
      state.term.termFunction.x=x
			state.term.termFunction.y = y
			this.commit('rebuildByType')
    },
    setNameEditableTerm (state, name) {
			state.term.name = name
			this.commit('rebuildByType')
    },
		setFunctionEditableTerm (state, obj) {
			state.term.termFunction = Object.assign({}, obj.termFunctions[obj.index])
			this.commit('rebuildByType')
    },
  },
}

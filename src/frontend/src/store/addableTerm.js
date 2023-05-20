export default {
  state: () => ({
    name: "",
    termFunction:  {name:'Треугольная'}
    //нет полей для значений терма, не задан 
	}),
  mutations: {
    setRightAddableTerm (state, right) {
      state.termFunction.right = right
    },
    setMeanAddableTerm (state, mean) {
      state.termFunction.mean = mean
    },
    setLeftAddableTerm (state, left) {
      state.termFunction.left = left
    },
    setUpperLeftAddableTerm (state, upperLeft) {
      state.termFunction.upperLeft = upperLeft
    },
    setUpperRightAddableTerm (state, upperRight) {
      state.termFunction.upperRight = upperRight
    },
    setStandardDeviationAddableTerm (state, standardDeviation) {
      state.termFunction.standardDeviation = standardDeviation
    },
    setPointsAddableTerm (state, str) {
      state.termFunction.raw = str
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
      state.termFunction.x=x
      state.termFunction.y=y
    },
    setNameAddableTerm (state, name) {
      state.name = name
    },
		setFunctionAddableTerm (state, obj) {
			state.termFunction = Object.assign({}, obj.termFunctions[obj.index])
    },
  },
}

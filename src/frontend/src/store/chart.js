
import Chart from 'chart.js/dist/chart.min.js'
export default {
	state: () => ({
		chart: null,
		dataSets: null,
		randomNumber: null,
		isColor: null,
		rgb: null,
		type:null,
		variable:null,
	}),
	mutations: {
		setColor (state) {
			if (state.isColor) {
				state.isColor= false
			} else {
				this.commit('setRandomRGB')
				state.isColor= true
			}
			if (state.variable != null) {
				this.commit('rebuildByVariable')
			} else {
				this.commit('rebuildByType')
			}
		},
		showChartByVariable (state, variable) {
			if (variable.value != null) {
				this.commit('setDataSetsByVariable', variable)
			} else {
				this.commit('setDataSetsByType', variable.type)
			}
			this.commit('showChart')
		},
		showChartByType (state, type) {
			this.commit('setDataSetsByType', type)
			this.commit('showChart')
		},
		setRandomNumber (state) {
			state.randomNumber = Math.floor(Math.random() * 255)
		},
		setRandomRGB (state) {
			this.commit('setRandomNumber')
			const r = state.randomNumber
			this.commit('setRandomNumber')
			const g = state.randomNumber
			this.commit('setRandomNumber')
			const b = state.randomNumber
			state.rgb = `rgba(${r},${g},${b},0.4)`
		},
		setDataSetsByVariable (state, variable) {
			let data = []
			data.push({ x: variable.value, y: 0 })
			data.push({ x: variable.value, y: 1 })
			let dataSets = []
			dataSets.push({
				showLine: true,
				fill: false,
				data: data,
				borderColor: 'rgb(0,0,0)',
				label: 'Значение ' + variable.name.toLowerCase() + ' = ' + variable.value,
			})
			state.variable = variable
			this.commit('setDataSetsByType', variable.type)
			state.dataSets = [...dataSets, ...state.dataSets]
		},
		setDataSetsByType (state, type) {
			let dataSets = []
			if (type) {
				for (let i in type.terms) {
					let data = []
					if (type.terms[i].termFunction.name.toLowerCase() === 'треугольная') {
						data.push({ x: type.terms[i].termFunction.left, y: 0 })
						data.push({ x: type.terms[i].termFunction.mean, y: 1 })
						data.push({ x: type.terms[i].termFunction.right, y: 0 })
					}
					if (type.terms[i].termFunction.name.toLowerCase() === 'кусочно-линейная') {
						for (let j = 0; j < type.terms[i].termFunction.x.length; j++) {
							console.log(type.terms[i].termFunction.x[j]);
							data.push({ x: type.terms[i].termFunction.x[j], y: type.terms[i].termFunction.y[j] })
						}
					}
					if (type.terms[i].termFunction.name.toLowerCase() === 'трапецеидальная') {
						data.push({ x: type.terms[i].termFunction.left, y: 0 })
						data.push({ x: type.terms[i].termFunction.upperLeft, y: 1 })
						data.push({ x: type.terms[i].termFunction.upperRight, y: 1 })
						data.push({ x: type.terms[i].termFunction.right, y: 0 })
					}
					if (type.terms[i].termFunction.name.toLowerCase() === 'гаусса') {
						const mean = type.terms[i].termFunction.mean
						const dev = type.terms[i].termFunction.standardDeviation
						let x
						let y
						let step = Math.abs(mean / 30)
						if (mean < 0) {
							for (let j = mean * 2; j <= 0; j += step) {
								x = j
								y = Math.exp(-Math.pow(j - mean, 2) / (2 * Math.pow(dev, 2)))
								data.push({ x, y })
							}
						} else {
							for (let j = 0; j < mean * 2; j += step) {
								x = j
								y = Math.exp(-Math.pow(j - mean, 2) / (2 * Math.pow(dev, 2)))
								data.push({ x, y })
							}
						}
					}
					if (type.terms[i].termFunction.name.toLowerCase() === 'сигмоида') {
						const mean = type.terms[i].termFunction.mean
						const dev = type.terms[i].termFunction.standardDeviation
						let x
						let y
						let step = Math.abs(mean / 30)
						if (mean < 0) {
							for (let j = mean * 2; j <= -mean * 2; j += step) {
								x = j
								y = 1 / (1 + Math.exp(-mean * (j - dev)))
								data.push({ x, y })
							}
						} else {
							for (let j = -mean * 2; j < mean * 2; j += step) {
								x = j
								y = 1 / (1 + Math.exp(-mean * (j - dev)))
								data.push({ x, y })
							}
						}

					}
					let color
					if (state.isColor) {
						this.commit('setRandomRGB')
					} else {
						state.rgb = 'rgba(192,192,192,0.5)'
					}
					dataSets.push({
						showLine: true,
						fill: true,
						data: data,
						backgroundColor: state.rgb,
						borderColor: color,
						label: type.terms[i].name,
					})
					state.type=type
				}
			}
			state.dataSets = dataSets
		},
		rebuildByType (state) {
			if (state.type != null) {
				this.commit('showChartByType',state.type)
			}
		},
		rebuildByVariable (state) {
			if (state.variable != null) {
				this.commit('showChartByVariable',state.variable)
			}
		},
		showChart (state) {
			if (state.chart != null) {
				state.chart.destroy()
			}

			let ctx = document.getElementById('chart').getContext('2d')
			state.chart = new Chart(ctx, {
				type: 'scatter',
				data: {
					datasets: state.dataSets,
				},
				options: {
					responsive: true,
					animation: {
						duration: 0,
					},
				},
			})
		},
	},
}

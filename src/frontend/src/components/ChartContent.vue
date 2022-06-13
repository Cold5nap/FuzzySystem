<template>
  <div class="col-6 bg-black bg-opacity-10 p-2 ">
    <p><b>График</b></p>
    <canvas id="chart"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js/dist/chart.min.js'
// import axios from 'axios';

export default {
  name: "ChartContent",
  data() { return { chart: null, } },
  // props:['selectedType'],
  methods: {
    showChartByType(type) {
      if (this.chart != null) {
        this.chart.destroy()
      }
      let dataSets = [];
      for (let i in type.terms) {
        let data = []
        if (type.terms[i].termFunction.name.toLowerCase() === 'треугольная') {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 })
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 })
          data.push({ x: type.terms[i].termFunction.points[2], y: 0 })
        }
        if (type.terms[i].termFunction.name.toLowerCase() === 'прямоугольная') {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 })
          data.push({ x: type.terms[i].termFunction.points[0], y: 1 })
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 })
          data.push({ x: type.terms[i].termFunction.points[1], y: 0 })
        }
        if (type.terms[i].termFunction.name.toLowerCase() === 'трапецеидальная') {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 })
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 })
          data.push({ x: type.terms[i].termFunction.points[2], y: 1 })
          data.push({ x: type.terms[i].termFunction.points[3], y: 0 })
        }
        dataSets.push({
          showLine: true,
          fill: true,
          data: data,
          label: type.terms[i].name
        })
      }


      let ctx = document.getElementById('chart').getContext('2d');
      this.chart = new Chart(ctx, {
        type: 'scatter',
        data: {
          datasets: dataSets,
        },
        options: {
          responsive: true,
          animation: {
            duration: 0
          }
        }
      });

    }
  },
}
</script>
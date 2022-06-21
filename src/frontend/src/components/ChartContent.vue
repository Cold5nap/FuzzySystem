<template>
  <div class="col-6 bg-black bg-opacity-10 p-2">
    <p><b>График</b></p>
    <canvas id="chart"></canvas>
  </div>
</template>

<script>
import Chart from "chart.js/dist/chart.min.js";

export default {
  name: "ChartContent",
  data() {
    return { chart: null };
  },
  methods: {
    showChartByVariable(variable) {
      if (variable.value != null) {
        this.showChart(this.dataSetsByVariable(variable));
      } else {
        this.showChart(this.dataSetsByType(variable.type));
      }
    },
    showChartByType(type) {
      this.showChart(this.dataSetsByType(type));
    },
    randomNumber() {
      return Math.floor(Math.random() * 255);
    },
    randomRGB() {
      return `rgba(${this.randomNumber()},${this.randomNumber()},${this.randomNumber()},0.4)`;
    },
    dataSetsByVariable(variable) {
      let data = [];
      data.push({ x: variable.value, y: 0 });
      data.push({ x: variable.value, y: 1 });
      let dataSets = [];
      dataSets.push({
        showLine: true,
        fill: false,
        data: data,
        borderColor: "rgb(0,0,0)",
        label: "Значение " + variable.name.toLowerCase() + " = " + variable.value,
      });
      return [...dataSets, ...this.dataSetsByType(variable.type)];
    },
    dataSetsByType(type) {
      let dataSets = [];
      for (let i in type.terms) {
        let data = [];
        if (type.terms[i].termFunction.name.toLowerCase() === "треугольная") {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 });
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 });
          data.push({ x: type.terms[i].termFunction.points[2], y: 0 });
        }
        if (type.terms[i].termFunction.name.toLowerCase() === "прямоугольная") {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 });
          data.push({ x: type.terms[i].termFunction.points[0], y: 1 });
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 });
          data.push({ x: type.terms[i].termFunction.points[1], y: 0 });
        }
        if (type.terms[i].termFunction.name.toLowerCase() === "трапецеидальная") {
          data.push({ x: type.terms[i].termFunction.points[0], y: 0 });
          data.push({ x: type.terms[i].termFunction.points[1], y: 1 });
          data.push({ x: type.terms[i].termFunction.points[2], y: 1 });
          data.push({ x: type.terms[i].termFunction.points[3], y: 0 });
        }
        let color;
        if (this.$store.state.isColor) {
          color = this.randomRGB();
        } else {
          color = "rgba(192,192,192,0.5)";
        }
        dataSets.push({
          showLine: true,
          fill: true,
          data: data,
          backgroundColor: color,
          borderColor: color,
          label: type.terms[i].name,
        });
      }
      return dataSets;
    },
    showChart(datasets) {
      if (this.chart != null) {
        this.chart.destroy();
      }

      let ctx = document.getElementById("chart").getContext("2d");
      this.chart = new Chart(ctx, {
        type: "scatter",
        data: {
          datasets: datasets,
        },
        options: {
          responsive: true,
          animation: {
            duration: 0,
          },
        },
      });
    },
  },
};
</script>

<template>
  <div class="container border rounded p-2">
    <div class="my-2 row justify-content-end">
      <!-- <div class="col-auto">
        <input type="file" hidden class="form-file-input" id="customFile" />
        <label class="form-file-label btn-sm btn-success" for="customFile" style="cursor: pointer">
          Загрузить файл формата fcl
        </label>
      </div> -->
      <div class="form-check form-switch col-auto">
        <input
          v-model="isColor"
          class="form-check-input"
          type="checkbox"
          role="switch"
          id="isColor"
        />
        <label class="form-check-label" for="isColor">Цветные графики</label>
      </div>
    </div>
    <div class="row m-0">
      <VariablesContent
        :methods="defuzzificationMethods"
        :types="types"
        :parentInputVariables="inputVariables"
        :parentOutputVariables="outputVariables"
        @updateSelectedVariable="selectVariable"
        @updateParentInputVariables="updateInputVariables"
        @updateParentOutputVariables="updateOutputVariables"
      ></VariablesContent>
      <ChartContent v-show="!$store.state.isLoading" ref="chart"></ChartContent>
      <div v-show="$store.state.isLoading" class="spinner-border" role="status">
        <span class="visually-hidden">Загрузка...</span>
      </div>
      <TypesContent
        :termFunctions="termFunctions"
        :parentTypes="types"
        @updateTypes="updateTypes"
      ></TypesContent>
    </div>
    <RulesContent></RulesContent>

    <div id="buttons" class="row row-cols-auto my-2 justify-content-center">
      <div class="col">
        <button type="button" class="btn btn-success" @click="getOutputValues">
          Получить значения для выходных данных
        </button>
      </div>
      <!-- <div class="col">
        <button class="btn btn-success">Экспортировать файл FCL построенной системы.</button>
      </div> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TypesContent from "@/components/TypesContent";
import ChartContent from "@/components/ChartContent";
import VariablesContent from "@/components/VariablesContent";
import RulesContent from "@/components/RulesContent";

export default {
  name: "ContentComponent",
  components: {
    ChartContent,
    TypesContent,
    VariablesContent,
    RulesContent,
  },
  data() {
    return {
      termFunctions: [
        { name: "Треугольная", points: [1, 2, 3] },
        { name: "Прямоугольная", points: [1, 3] },
        { name: "Трапецеидальная", points: [1, 2, 3, 4] },
      ],
      defuzzificationMethods: [
        { name: "Центр тяжести" },
        { name: "Центр тяжести для синглтонов" },
        { name: "Центр площади" },
        { name: "Крайний левый максимум" },
        { name: "Крайний правый максимум" },
      ],
      types: null,
      inputVariables: null,
      outputVariables: null,
      selectedType: null,
      selectedVariable: null,
      rules: null,
    };
  },
  created() {
    //Вводим начальные данные
    this.types = [
      {
        name: "Сервис",
        terms: [
          { name: "Плохой", termFunction: { name: "Треугольная", points: [0, 0, 4] } },
          { name: "Хороший", termFunction: { name: "Трапецеидальная", points: [1, 4, 6, 9] } },
          { name: "Великолепный", termFunction: { name: "Треугольная", points: [6, 9, 9] } },
        ],
      },
      {
        name: "Еда",
        terms: [
          {
            name: "Отваратительная",
            termFunction: { name: "Трапецеидальная", points: [0, 0, 1, 3] },
          },
          { name: "Вкусная", termFunction: { name: "Треугольная", points: [6, 9, 9] } },
        ],
      },
      {
        name: "Чаевые",
        terms: [
          { name: "Мало", termFunction: { name: "Треугольная", points: [0, 5, 10] } },
          { name: "Средние", termFunction: { name: "Треугольная", points: [10, 15, 20] } },
          { name: "Щедро", termFunction: { name: "Треугольная", points: [20, 25, 30] } },
        ],
      },
    ];
    this.inputVariables = [
      { name: "Сервис", type: this.types[0], value: 7 },
      { name: "Еда", type: this.types[1], value: 8 },
    ];
    this.outputVariables = [
      { name: "Чаевые", type: this.types[2], def: 0, method: this.defuzzificationMethods[0] },
    ];
    this.rules = [
      {
        name: "Первое правило",
        conditions: [
          {
            variable: this.inputVariables[0],
            connection: "=",
            term: this.inputVariables[0].type.terms[0],
            conditionConnector: null,
          },
          {
            variable: this.inputVariables[1],
            connection: "=",
            term: this.inputVariables[1].type.terms[0],
            conditionConnector: "и",
          },
        ],
        actions: [
          {
            variable: this.outputVariables[0],
            connection: "=",
            term: this.outputVariables[0].type.terms[0],
          },
        ],
        weight: 1,
      },
      {
        name: "Второе правило",
        conditions: [
          {
            variable: this.inputVariables[0],
            connection: "=",
            term: this.inputVariables[0].type.terms[1],
            conditionConnector: null,
          },
        ],
        actions: [
          {
            variable: this.outputVariables[0],
            connection: "=",
            term: this.outputVariables[0].type.terms[1],
          },
        ],
        weight: 1,
      },
      {
        name: "Третье правило",
        conditions: [
          {
            variable: this.inputVariables[0],
            connection: "=",
            term: this.inputVariables[0].type.terms[2],
            conditionConnector: null,
          },
          {
            variable: this.inputVariables[1],
            connection: "=",
            term: this.inputVariables[1].type.terms[1],
            conditionConnector: "и",
          },
        ],
        actions: [
          {
            variable: this.outputVariables[0],
            connection: "=",
            term: this.outputVariables[0].type.terms[2],
          },
        ],
        weight: 1,
      },
    ];

    this.$store.commit("setRules", this.rules);
    this.$store.commit("setInputVariables", this.inputVariables);
    this.$store.commit("setOutputVariables", this.outputVariables);
  },
  methods: {
    async getOutputValues() {
      await this.$store.dispatch("getOutputValues");
      if (this.selectedVariable != null) {
        let updatedSelectedVariable = this.$store.state.outputVariables.find(
          (variable) => variable.name == this.selectedVariable.name
        );
        console.log(updatedSelectedVariable);
        if (typeof updatedSelectedVariable !== "undefined") {
          this.$refs.chart.showChartByVariable(updatedSelectedVariable);
        }
      }
    },
    selectVariable(variable) {
      this.selectedVariable = variable;
      this.selectedType = null;
      this.$refs.chart.showChartByVariable(variable);
    },
    updateOutputVariables(outputVariables) {
      this.outputVariables = outputVariables;
      console.log(this.outputVariables);
    },
    updateInputVariables(inputVariables) {
      this.inputVariables = inputVariables;
      console.log(this.inputVariables);
    },
    updateTypes(types) {
      this.types = types.types;
      this.selectedType = types.selectedType;
      if (this.selectedType != null) {
        this.selectedVariable = null;
      }
      this.$refs.chart.showChartByType(this.selectedType);
      console.log(this.selectedType.name);
    },
    sendInputFile() {
      let fd = new FormData();
      fd.append("input_file", this.$refs.inputFile.files[0]);
      axios
        .post("http://localhost:8080/api/input_file", fd, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getResult() {
      axios
        .post("http://localhost:8080/api/input_file", 0, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
  },
  computed: {
    isColor: {
      get() {
        return this.$store.state.isColor;
      },
      set(value) {
        this.$store.commit("setIsColor", value);
        if (this.selectedVariable != null) {
          this.$refs.chart.showChartByVariable(this.selectedVariable);
        } else if (this.selectedType != null) {
          this.$refs.chart.showChartByType(this.selectedType);
        }
      },
    },
  },
  mounted() {},
};
</script>

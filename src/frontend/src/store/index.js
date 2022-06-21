import axios from "axios";
import { createStore } from "vuex";

export default createStore({
  state() {
    return {
      rules: null,
      inputVariables: null,
      outputVariables: null,
      isLoading: false,
      graphic: null,
      selectedOperator: null,
      selectedAlgorithm: null,
      selectedAccumulation: null,
      selectedActivator: null,
      isColor: null,
    };
  },
  getters: {
    getGraphic(state) {
      return state.graphic;
    },
    getRules(state) {
      return state.rules;
    },
    selectedOperator(state) {
      return state.selectedOperator;
    },
    selectedAlgorithm(state) {
      return state.selectedAlgorithm;
    },
    selectedAccumulation(state) {
      return state.selectedAccumulation;
    },
    selectedActivator(state) {
      return state.selectedActivator;
    },
  },
  mutations: {
    setIsColor(state, isColor) {
      state.isColor = isColor;
    },
    setInputVariables(state, vars) {
      state.inputVariables = vars;
    },
    setOutputVariables(state, vars) {
      state.outputVariables = vars;
    },
    setRules(state, rules) {
      state.rules = rules;
    },
    setGraphic(state, graphic) {
      state.graphic = graphic;
    },
    updateSelectedOperator(state, operator) {
      state.selectedOperator = operator;
    },
    updateSelectedAlgorithm(state, val) {
      state.selectedAlgorithm = val;
    },
    updateSelectedAccumulation(state, val) {
      state.selectedAccumulation = val;
    },
    updateSelectedActivator(state, val) {
      state.selectedActivator = val;
    },
  },
  actions: {
    async getOutputValues({ state }) {
      return axios
        .post(
          "http://localhost:8080/api/get_output",
          JSON.stringify({
            rules: state.rules,
            inputVariables: state.inputVariables,
            outputVariables: state.outputVariables,
            operator: state.selectedOperator.name,
            algorithm: state.selectedAlgorithm,
            accumulation: state.selectedAccumulation,
            activator: state.selectedActivator,
          }),
          {
            headers: { "content-type": "application/json" },
          }
        )
        .then((response) => {
          state.isLoading = true;
          Object.entries(response.data).forEach((entry) => {
            state.outputVariables.find((v) => v.name == entry[0]).value = entry[1];
          });
        })
        .catch((error) => console.log(error))
        .finally(() => (state.isLoading = false));
    },
  },
  modules: {},
});

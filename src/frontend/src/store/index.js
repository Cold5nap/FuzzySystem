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
    };
  },
  getters: {
    getGraphic(state) {
      return state.graphic;
    },
    getRules(state) {
      return state.rules;
    },
  },
  mutations: {
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
  },
  actions: {
    async getGraphicFromApi({ commit, state }) {
      console.log(JSON.stringify(state.rules[0].actions[0].variable.method));
      return axios
        .post(
          "http://localhost:8080/api/evualation",
          { rules: JSON.stringify(state.rules[0].actions[0].variable.method) },
          {
            headers: { "content-type": "application/json" },
          }
        )
        .then((response) => {
          state.isLoading = true;
          console.log(response.data);
          commit("setGraphic", response.data);
        })
        .catch((error) => console.log(error))
        .finally(() => (this.isLoading = false));
    },
  },
  modules: {},
});

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
    };
  },
  getters: {
    getGraphic(state) {
      return state.graphic;
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
      return axios("/api/graphic", {
        headers: { "content-type": "JSON" },
      })
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

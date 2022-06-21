<template>
  <div id="rules" class="bg-info bg-success bg-opacity-10 p-3">
    <b>Правила</b>
    <i class="bi bi-plus-square text-success mx-3 h5" style="cursor: pointer" @click="addRule"></i>

    <!-- connection, accumulation, activator div-->
    <div class="row" v-if="!isEditUpperMethods">
      <div class="col-auto">Оператор: {{ selectedOperator.name }},</div>
      <div class="col-auto">алгоритм оператора: {{ selectedAlgorithm }},</div>
      <div class="col-auto">метод активации: {{ selectedActivator }},</div>
      <div class="col-auto d-flex justify-content-between">
        <div>метод накопления: {{ selectedAccumulation }}.</div>
        <div>
          <i
            @click="isEditUpperMethods = !isEditUpperMethods"
            class="bi bi-pencil-square text-info mx-3 h5"
            style="cursor: pointer"
          ></i>
        </div>
      </div>
    </div>
    <!-- edit div -->
    <div class="row" v-if="isEditUpperMethods">
      <div class="col-auto">
        <label for="operator">Выберите операцию</label>
        <select
          class="form-control"
          id="operator"
          v-model="selectedOperator"
          @change="selectedAlgorithm = selectedOperator.algorithms[0]"
        >
          <option v-for="operator in this.operators" :key="operator.name" :value="operator">
            {{ operator.name }}
          </option>
        </select>
      </div>

      <div class="col-auto" v-if="selectedOperator != null">
        <label for="algorithm">Выберите алгоритм операции {{ selectedOperator.name }}</label>
        <select class="form-control" v-model="selectedAlgorithm">
          <option v-for="name in selectedOperator.algorithms" :key="name" :value="name">
            {{ name }}
          </option>
        </select>
      </div>

      <div class="col-auto">
        <label for="algorithm">Выберите метод активации</label>
        <select class="form-control" v-model="selectedActivator">
          <option v-for="name in this.activators" :key="name" :value="name">{{ name }}</option>
        </select>
      </div>

      <div class="col-auto">
        <div class="d-flex justify-content-between">
          <div>
            <label for="algorithm">Выберите метод аккумуляции</label>
            <select class="form-control" v-model="selectedAccumulation">
              <option v-for="name in this.accumulations" :key="name" :value="name">
                {{ name }}
              </option>
            </select>
          </div>

          <div>
            <i
              @click="isEditUpperMethods = !isEditUpperMethods"
              class="bi bi-pencil-square text-info mx-3 h5"
              style="cursor: pointer"
            ></i>
          </div>
        </div>
      </div>
    </div>
    <!--end connection, accumulation, activator div-->

    <!-- conditions div -->
    <div class="row my-3" v-for="(rule, iRule) in this.$store.state.rules" :key="iRule">
      <div class="col-auto">{{ rule.name }}:</div>
      Если
      <div class="col-auto" v-for="(condition, index) in rule.conditions" :key="index">
        <div class="row">
          <div class="col-auto" v-if="index > 0">{{ condition.conditionConnector }}</div>
          <div class="col-auto">
            {{ condition.variable.name }} {{ condition.connection }} {{ condition.term.name }}
          </div>
        </div>
      </div>

      <div class="col-auto">То</div>

      <div class="col-auto">
        <span v-for="(action, index) in rule.actions" :key="index">
          <span v-if="index > 0">,</span>
          {{ action.variable.name }} {{ action.connection }} {{ action.term.name + " " }}
        </span>
      </div>

      <div class="col">
        <div class="d-flex justify-content-between">
          <span>Весовой коэффициент {{ rule.weight }}</span>
          <span>
            <i
              class="bi bi-dash-square text-danger h5"
              style="cursor: pointer"
              @click="this.$store.state.rules.splice(iRule, 1)"
            ></i>
            <i
              class="bi bi-pencil-square text-info h5"
              style="cursor: pointer"
              @click="changeSelectedRule(rule)"
            ></i>
          </span>
        </div>
      </div>

      <!-- selected rule div-->
      <div class="row my-3 bg-info bg-opacity-10" v-if="selectedRule == rule">
        <div class="col-auto">
          <label for="rule-name">Название</label>
          <input class="form-control" type="text" v-model="selectedRule.name" id="rule-name" />
        </div>

        <div class="col-auto">
          <br />
          <b>Если</b>
        </div>

        <div class="col-auto" v-for="(condition, index) in selectedRule.conditions" :key="index">
          <div class="row">
            <div class="col-auto" v-if="index > 0">
              <label for="condition-connector">Оператор</label>
              <select
                class="form-control"
                v-model="condition.conditionConnector"
                id="condition-connector"
              >
                <option>или</option>
                <option>и</option>
              </select>
            </div>

            <div class="col">
              <Clause
                :variables="this.$store.state.inputVariables"
                @updateTerm="condition.term = $event"
                @updateVariable="condition.variable = $event"
                @updateConnection="condition.connection = $event"
                :clause="condition"
              >
              </Clause>
            </div>
          </div>
        </div>

        <div class="col-auto">
          <br />
          <i
            class="bi bi-plus-square text-success h5"
            style="cursor: pointer"
            @click="addClauseInConditionsSelectedRule"
          ></i>
          <i
            class="bi bi-dash-square text-danger h5"
            style="cursor: pointer"
            @click="deleteLastClauseInConditionsSelectedRule"
          ></i>
        </div>

        <div class="col-auto">
          <br />
          <b>То</b>
        </div>

        <div class="col-auto p-0" v-for="(action, index) in selectedRule.actions" :key="index">
          <div class="row">
            <div class="col"></div>
            <div class="col" v-if="0 < index">
              <br />
              <b>,</b>
            </div>
            <div class="col-auto">
              <Clause
                :variables="this.$store.state.outputVariables"
                @updateTerm="action.term = $event"
                @updateVariable="action.variable = $event"
                @updateConnection="action.connection = $event"
                :clause="action"
              >
              </Clause>
            </div>
          </div>
        </div>

        <div class="col-auto">
          <br />
          <i
            class="bi bi-plus-square text-success h5"
            style="cursor: pointer"
            @click="addClauseInActionsSelectedRule"
          ></i>
          <i
            class="bi bi-dash-square text-danger h5"
            style="cursor: pointer"
            @click="deleteLastClauseInActionsSelectedRule"
          ></i>
        </div>

        <div class="col-auto">
          <label for="weight-rule">Весовой коэффициент</label>
          <input
            class="form-control"
            type="number"
            min="0"
            max="1"
            step="0.05"
            v-model="selectedRule.weight"
            id="weight-rule"
          />
        </div>
      </div>
      <!--end selected rule -->

      <hr />
    </div>
  </div>
</template>

<script>
import Clause from "./Clause.vue";

export default {
  name: "RulesContent",
  components: {
    Clause,
  },
  data() {
    return {
      operators: [
        { name: "или", algorithms: ["максимум", "алгебраическая сумма", "ограниченная сумма"] },
        { name: "и", algorithms: ["минимум", "произведение", "ограниченная разность"] },
      ],
      activators: ["произведение", "минимум"],
      accumulations: ["максимум", "ограниченная сумма", "нормализированная сумма"],
      isEditUpperMethods: false,

      selectedName: null,
      selectedWeight: 1,
      selectedRule: {
        name: "Первое правило",
        conditions: [
          {
            variable: this.$store.state.inputVariables[0],
            connection: "=",
            term: this.$store.state.inputVariables[0].type.terms[0],
            conditionConnector: null,
          },
        ],
        actions: [
          {
            variable: this.$store.state.outputVariables[0],
            connection: "=",
            term: this.$store.state.outputVariables[0].type.terms[0],
          },
        ],
        weight: 1,
      },
    };
  },
  created() {
    this.$store.commit("updateSelectedAlgorithm", this.operators[0].algorithms[0]);
    this.$store.commit("updateSelectedAccumulation", this.accumulations[0]);
    this.$store.commit("updateSelectedOperator", this.operators[0]);
    this.$store.commit("updateSelectedActivator", this.activators[1]);
  },
  methods: {
    test() {
      console.log(this.$store.state.rules);
    },
    addRule() {
      this.$store.state.rules.push({
        name: "Правило № " + (this.$store.state.rules.length + 1),
        conditions: [
          {
            variable: this.$store.state.inputVariables[0],
            connection: "=",
            term: this.$store.state.inputVariables[0].type.terms[0],
            conditionConnector: null,
          },
        ],
        actions: [
          {
            variable: this.$store.state.outputVariables[0],
            connection: "=",
            term: this.$store.state.outputVariables[0].type.terms[0],
          },
        ],
        weight: 1,
      });
    },
    changeSelectedRule(rule) {
      if (this.selectedRule == rule) {
        this.selectedRule = null;
      } else {
        this.selectedRule = rule;
      }
    },
    deleteLastClauseInConditionsSelectedRule() {
      if (this.selectedRule.conditions.length > 1) {
        this.selectedRule.conditions.pop();
      }
    },
    addClauseInConditionsSelectedRule() {
      if (this.selectedRule.conditions.length < this.$store.state.inputVariables.length) {
        this.selectedRule.conditions.push({
          variable: this.$store.state.inputVariables[0],
          connection: "=",
          term: this.$store.state.inputVariables[0].type.terms[0],
          conditionConnector: "и",
        });
      }
    },
    addClauseInActionsSelectedRule() {
      if (this.selectedRule.actions.length < this.$store.state.outputVariables.length) {
        this.selectedRule.actions.push({
          variable: this.$store.state.outputVariables[0],
          connection: "=",
          term: this.$store.state.outputVariables[0].type.terms[0],
        });
      }
    },
    deleteLastClauseInActionsSelectedRule() {
      if (this.selectedRule.actions.length > 1) {
        this.selectedRule.actions.pop();
      }
    },
  },
  computed: {
    selectedOperator: {
      get() {
        return this.$store.getters.selectedOperator;
      },
      set(value) {
        this.$store.commit("updateSelectedOperator", value);
      },
    },
    selectedAlgorithm: {
      get() {
        return this.$store.getters.selectedAlgorithm;
      },
      set(value) {
        this.$store.commit("updateSelectedAlgorithm", value);
      },
    },
    selectedAccumulation: {
      get() {
        return this.$store.getters.selectedAccumulation;
      },
      set(value) {
        this.$store.commit("updateSelectedAccumulation", value);
      },
    },
    selectedActivator: {
      get() {
        return this.$store.getters.selectedActivator;
      },
      set(value) {
        this.$store.commit("updateSelectedActivator", value);
      },
    },
  },
};
</script>

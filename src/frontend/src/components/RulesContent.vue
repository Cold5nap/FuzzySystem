<template lang="pug">
#rules.bg-info.bg-success.bg-opacity-10.p-3
  b Правила
  i.bi.bi-plus-square.text-success.mx-3.h4(@click="addRule($store.state.variable)")
  //  параметры для rule
  .row(v-if="!isEditUpperMethods")
    //- .col-auto
    //- 	label(for="operator") Выберите операцию
    //- 	select#operator.form-control(
    //- 		v-model="selectedOperator",
    //- 		@change="selectedAlgorithm = selectedOperator.algorithms[0]"
    //- 	)
    //- 		option(
    //- 			v-for="operator in this.operators",
    //- 			:key="operator.name",
    //- 			:value="operator"
    //- 		) {{ operator.name }}
    //- .col-auto(v-if="selectedOperator != null")
    //- 	label(for="algorithm") Выберите алгоритм операции {{ selectedOperator.name }}
    //- 	select.form-control(v-model="selectedAlgorithm")
    //- 		option(
    //- 			v-for="name in selectedOperator.algorithms",
    //- 			:key="name",
    //- 			:value="name"
    //- 		) {{ name }}
    .col-auto Метод активации: {{ruleStore.activator}}
    .col-auto Метод аккумуляции: {{ruleStore.accumulation}}
    .col-auto
      i.bi.bi-pencil-square.text-info.mx-3.h4(
        @click="isEditUpperMethods = !isEditUpperMethods"
      )
  .row(v-else)
    .col-auto
      label(for="activation") Метод активации
      select.form-control(
        @change="setActivator($event.target.value)",
        :value="ruleStore.activator"
      )
        option(
          v-for="name in ruleStore.activators",
          :key="name",
          :value="name"
        ) {{ name }}
    .col-auto
      .d-flex.justify-content-between
        div
          label(for="algorithm") Метод аккумуляции
          select.form-control(
            @change="setAccumulation($event.target.value)",
            :value="ruleStore.accumulation"
          )
            option(
              v-for="name in ruleStore.accumulations",
              :key="name",
              :value="name"
            ) {{ name }}
    .col-auto
      i.bi.bi-pencil-square.text-info.mx-3.h4(
        @click="isEditUpperMethods = !isEditUpperMethods"
      )
  // end connection, accumulation, activator div
  //  conditions div 
  .row.my-3(v-for="(rule, iRule) in ruleStore.rules", :key="iRule")
    .col-auto {{ rule.name }}:
    | Если
    .col-auto(v-for="(condition, index) in rule.conditions")
      .row
        .col-auto(v-if="index > 0") {{ condition.conditionConnector }}
        .col-auto {{ condition.variable.name }} {{ condition.connection }} {{ condition.term.name }}
    .col-auto То
    .col-auto
      span(v-for="(action, index) in rule.actions")
        span(v-if="index > 0") ,
        | {{ action.variable.name }} {{ action.connection }} {{ action.term.name + " " }}

    .col
      .d-flex.justify-content-between
        span Весовой коэффициент {{ rule.weight }}
        span
          i.bi.bi-dash-square.text-danger.h4(
            @click="ruleStore.rules.splice(iRule, 1)"
          )
          i.bi.bi-pencil-square.text-info.h4(@click="changeSelectedRule(rule)")
    //  selected rule div
    .row.my-3.bg-info.bg-opacity-10(v-if="ruleStore.selected == rule")
      .col-auto
        label(for="rule-name") Название
        input#rule-name.form-control(
          type="text",
          @input="setRuleName($event.target.value)",
          :value="ruleStore.selected.name"
        )
      .col-auto
        br
        b Если
      .col-auto(v-for="(condition, index) in ruleStore.selected.conditions")
        .row
          //- .col-auto(v-if="index > 0")
          //-   label(for="condition-connector") Оператор
          //-   select#condition-connector.form-control(
          //-     v-model="condition.conditionConnector"
          //-   )
          //-     option или
          //-     option и
          .col
            clause(
              :variables="$store.state.variable.inputVariables",
              :indexClause="index",
              :indexRule="iRule",
              :isInput="true"
            )
      .col-auto
        br
        i.bi.bi-plus-square.text-success.h4(
          @click="addClauseInConditionsSelectedRule($store.state.variable.inputVariables.length)"
        )
        i.bi.bi-dash-square.text-danger.h4(
          @click="deleteLastClauseInConditionsSelectedRule"
        )
      .col-auto
        br
        b То
      .col-auto.p-0(
        v-for="(action, index) in ruleStore.selected.actions",
        :key="index"
      )
        .row
          .col
          .col(v-if="0 < index")
            br
            b ,
          .col-auto
            clause(
              :variables="$store.state.variable.outputVariables",
              :indexClause="index"
              :indexRule="iRule"
              :isInput="false"
            )
      .col-auto
        br
        i.bi.bi-plus-square.text-success.h4(
          @click="addClauseInActionsSelectedRule($store.state.variable.outputVariables.length)"
        )
        i.bi.bi-dash-square.text-danger.h4(
          @click="deleteLastClauseInActionsSelectedRule"
        )
      .col-auto
        label(for="weight-rule") Весовой коэффициент
        input#weight-rule.form-control(
          type="number",
          min="0",
          max="1",
          step="0.05",
          @input="setRuleWeight($event.target.value)",
          :value="ruleStore.selected.weight"
        )
    hr
</template>

<script>
import Clause from "./Clause.vue";
import { mapMutations } from "vuex";

export default {
  name: "RulesContent",
  mounted(){
      this.setRules([
        {
          name: 'Первое правило',
          conditions: [
            {
              variable: this.$store.state.variable.inputVariables[0],
              connection: '=',
              term: this.$store.state.variable.inputVariables[0].type.terms[0],
              conditionConnector: null,
              indexVariable: 0,
						indexTerm:0
            },
            {
              variable: this.$store.state.variable.inputVariables[1],
              connection: '=',
              term: this.$store.state.variable.inputVariables[1].type.terms[0],
              conditionConnector: 'и',
              indexVariable: 1,
						indexTerm:0
            },
          ],
          actions: [
            
            {
              indexVariable: 0,
						indexTerm:0,
              variable: this.$store.state.variable.outputVariables[0],
              connection: '=',
              term: this.$store.state.variable.outputVariables[0].type.terms[0],
            },
          ],
          weight: 1,
        },
        {
          name: 'Второе правило',
          conditions: [
            {
              variable: this.$store.state.variable.inputVariables[0],
              connection: '=',
              term: this.$store.state.variable.inputVariables[0].type.terms[1],
              conditionConnector: null,
              indexVariable: 0,
						indexTerm:1
            },
          ],
          actions: [
            {
              variable: this.$store.state.variable.outputVariables[0],
              connection: '=',
              term: this.$store.state.variable.outputVariables[0].type.terms[1],
              indexVariable: 0,
						indexTerm:1
            },
          ],
          weight: 1,
        },
        {
          name: 'Третье правило',
          conditions: [
            {
              variable: this.$store.state.variable.inputVariables[0],
              connection: '=',
              term: this.$store.state.variable.inputVariables[0].type.terms[2],
              conditionConnector: null,
              indexVariable: 0,
						indexTerm:2
            },
            {
              variable: this.$store.state.variable.inputVariables[1],
              connection: '=',
              term: this.$store.state.variable.inputVariables[1].type.terms[1],
              conditionConnector: 'и',
              indexVariable: 1,
						indexTerm:1
            },
          ],
          actions: [
            {
              variable: this.$store.state.variable.outputVariables[0],
              connection: '=',
              term: this.$store.state.variable.outputVariables[0].type.terms[2],
              indexVariable: 0,
						indexTerm:2
            },
          ],
          weight: 1,
        },
      ])
  
  
  },
  components: {
    Clause,
  },
  methods: {
    ...mapMutations([
      "setRules",
      "addRule",
      "setAccumulation",
      "setActivator",
      "changeSelectedRule",
      "addClauseInConditionsSelectedRule",
      "addClauseInActionsSelectedRule",
      "deleteLastClauseInActionsSelectedRule",
      "deleteLastClauseInConditionsSelectedRule",
    ]),
  },
  data() {
    return {
      isEditUpperMethods: false,
      ruleStore: this.$store.state.rule,
    };
  },
};
</script>

<template lang="pug">
.row 
	.col-auto.p-0 
		label(for="variable-condition") Переменная
		select#variable-condition.form-control(
				@change="setClauseVariable({indexVariable:$event.target.value,indexClause:indexClause,variable:variables[$event.target.value],isInput:isInput})",
				:value="clause.indexVariable"
		) 
			option(
				v-for="(variable,index) in variables",
				:value="index"
			) {{ variable.name }}
	.col-auto.p-0 
		label(for="variable-term-connector") Оператор
		select#variable-term-connector.form-control(
			@change="setClauseConnection({indexClause:indexClause,connection:$event.target.value,isInput:isInput})",
      :value="clause.connection"
		) 
			option="=" 
			option ≠
	.col-auto.p-0
		label(for="term-condition") Термин
		select#term-condition.form-control(disabled, v-if="clause.variable == null") 
			option(selected) Переменная не выбрана
		select#term-condition.form-control(
			@change="setClauseTerm({indexClause:indexClause,term:variables[clause.indexVariable].type.terms[$event.target.value],isInput:isInput})",
      :value="clause.indexTerm"
			v-else
		) 
			option(
				v-for="(term,index) in clause.variable.type.terms",
				:value="index"
			) {{ term.name }}
</template>

<script>

import { mapMutations } from "vuex";
export default {
	name: "ClauseComponent",
	props: ["indexClause","variables","isInput"],
	data() {
		return {
			clause:{variable:null,connection:'',term:''}
			// clause:this.$store.state.rule.rules[this.indexRule].
		};
	},
	mounted(){
			const rule = this.$store.state.rule.selected
			if(this.isInput){
				this.clause=rule.conditions[this.indexClause]
			}else{
				this.clause=rule.actions[this.indexClause]
			}
	},
  methods: {
		
    ...mapMutations([
      "setClauseVariable",
      "setClauseConnection",
      "setClauseTerm",
    ]),
  },
};
</script>

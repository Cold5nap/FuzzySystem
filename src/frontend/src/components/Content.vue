<template lang="pug">
.p-2
  .my-2.row.justify-content-between.me-5
    .col-auto.d-flex
      //- .mx-3
      //-   label(for="example") Примеры
      //-   select#example.form-control(@change="$store.dispatch('getExample',$event.target.value)")
      //-     option(disabled,value="0",selected) Не выбран
      //-     option(value="1") Чаевые
      div
        input.form-file-input#customFile(type="file", hidden,
        @change="$store.dispatch('importFCL',$event.target.files[0])")
        label.form-file-label.btn.btn-success(for="customFile", style="cursor: pointer")  
          | Импортировать FCL (только латинский алфавит)
    
    .form-check.form-switch.col-auto
      input.form-check-input#isColor(@change="$store.commit('setColor')", type="checkbox", role="switch")
      label.form-check-label(for="isColor") Цветные графики
  .row.m-0
    VariablesContent
    ChartContent(v-show="!$store.state.isLoading", ref="chart")
    .spinner-border(v-show="$store.state.isLoading", role="status")
      span.visually-hidden Загрузка...
    TypesContent
  RulesContent
  #buttons.row.row-cols-auto.my-2.justify-content-center
    .col
      button.btn.btn-success(type="button", @click="getOutputValues") 
        | Получить значения для выходных данных
    .col
      button.btn.btn-success(type="button", @click="$store.dispatch('exportFCL')") 
        | Экспортировать в FCL
    .col
      button.btn.btn-success(type="button", @click="$store.dispatch('export')") Экспортировать в TXT

</template>

<script>
import { mapMutations } from "vuex";
import TypesContent from '@/components/TypesContent'
import ChartContent from '@/components/ChartContent'
import VariablesContent from '@/components/VariablesContent'
import RulesContent from '@/components/RulesContent'

export default {
  name: 'ContentComponent',
  components: {
    ChartContent,
    TypesContent,
    VariablesContent,
    RulesContent,
  },
  methods: {
    ...mapMutations([
      "setColor",
    ]),
    async getOutputValues() {
      await this.$store.dispatch('getOutputValues')
      if (this.selectedVariable != null) {
        let updatedSelectedVariable = this.$store.state.outputVariables.find(
          (variable) => variable.name == this.selectedVariable.name,
        )
        if (typeof updatedSelectedVariable !== 'undefined') {
          this.$refs.chart.showChartByVariable(updatedSelectedVariable)
        }
      }
    },
    // selectVariable(variable) {
    //   this.selectedVariable = variable
    //   this.selectedType = null
    //   this.$refs.chart.showChartByVariable(variable)
    // },
    // updateOutputVariables(outputVariables) {
    //   this.outputVariables = outputVariables
    // },
    // updateInputVariables(inputVariables) {
    //   this.inputVariables = inputVariables
    // },
    // updateTypes(types) {
    //   this.types = types.types
    //   this.selectedType = types.selectedType
    //   if (this.selectedType != null) {
    //     this.selectedVariable = null
    //   }
    //   this.$refs.chart.showChartByType(this.selectedType)
    // },
  },
  // mounted() {},
}
</script>
<style>
i {
  cursor: pointer;
}
.fade-enter-active,
.fade-leave-active {
  transition: transform .5s ease
}
.fade-enter,
.fade-leave-to {
  transform: translateX(50px)
}
</style>

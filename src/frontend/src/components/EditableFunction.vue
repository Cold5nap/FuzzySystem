<template lang="pug">
div
  label(for="termFunction") Выберите функцию принадлежности
  select#termFunction.form-control.form-select.my-1(
    required,
    :value="$store.state.editableTerm.term.termFunction.index"
    @change="setFunctionEditableTerm({index:$event.target.value,termFunctions:$store.state.type.termFunctions})"
  )
    option(
      v-for="(func,index) in $store.state.type.termFunctions",
      :value="index"
    ) {{ func.name }}
  .row(v-if='["Треугольная","Трапецеидальная"].includes($store.state.editableTerm.term.termFunction.name)')
    span Абсциссы:
    .col.pe-0
      input.form-control(
        type="number",
        required,
				
				:value="$store.state.editableTerm.term.termFunction.left",
        @input="setLeftEditableTerm($event.target.value)"
      )
    .col.px-0(v-if="'Трапецеидальная'==$store.state.editableTerm.term.termFunction.name")
      input.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.upperLeft",
        @input="setUpperLeftEditableTerm($event.target.value)"
      )
    .col.px-0(v-if="'Треугольная'==$store.state.editableTerm.term.termFunction.name")
      input.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.mean"
        @input="setMeanEditableTerm($event.target.value)"
      )
    .col.px-0(v-if="'Трапецеидальная'==$store.state.editableTerm.term.termFunction.name")
      input.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.upperRight",
        @input="setUpperRightEditableTerm($event.target.value)"
      )
    .col.ps-0
      input.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.right",
        @input="setRightEditableTerm($event.target.value)"
      ) 
  .row(v-if='["Гаусса","Сигмоида"].includes($store.state.editableTerm.term.termFunction.name)')
    .col
      br
      label(for='mean') Среднее
      input#mean.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.mean",
        @input="setMeanEditableTerm($event.target.value)"
      )
    .col
      label(for="stdDev") Стандартное отклонение
      input#stdDev.form-control(
        type="number",
        required,
				:value="$store.state.editableTerm.term.termFunction.standardDeviation",
        @input="setStandardDeviationEditableTerm($event.target.value)"
      ) 
  div(v-if="'Кусочно-линейная'==$store.state.editableTerm.term.termFunction.name")
    label(for="xy") Координаты в формате: x1;y1 x2;y2 ...
    input#xy.form-control(
        type="text",
        required,
				:value="$store.state.editableTerm.term.termFunction.raw"
        @change="setPointsEditableTerm($event.target.value)"
        placeholder="x1;y1 x2;y2 ..."
      )
</template>

<script>
import { mapMutations } from "vuex";
export default {
  name: "EditableFunction", 
  methods: {
    ...mapMutations([
      "setRightEditableTerm",
      "setLeftEditableTerm",
      "setMeanEditableTerm",
      "setUpperLeftEditableTerm",
      "setUpperRightEditableTerm",
      "setStandardDeviationEditableTerm",
      "setPointsEditableTerm",
      "setNameEditableTerm",
      "setFunctionEditableTerm",
    ]),
  },
};
</script>
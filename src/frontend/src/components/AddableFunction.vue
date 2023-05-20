<template lang="pug">
div
  label(for="termFunction") Выберите функцию принадлежности
  select#termFunction.form-control.form-select.my-1(
    required,
    @input="setFunctionAddableTerm({index:$event.target.value,termFunctions:$store.state.type.termFunctions})"
  )
    option(
      v-for="(func,index) in $store.state.type.termFunctions"
      :value="index"
    ) {{ func.name }}
  .row(v-if='["Треугольная","Трапецеидальная"].includes($store.state.addableTerm.termFunction.name)')
    span Абсциссы:
    .col
      input.form-control(
        type="number",
        required,
        @input="setLeftAddableTerm($event.target.value)"
      )
    .col(v-if="'Трапецеидальная'==$store.state.addableTerm.termFunction.name")
      input.form-control(
        type="number",
        required,
        @input="setUpperLeftAddableTerm($event.target.value)"
      )
    .col(v-if="'Треугольная'==$store.state.addableTerm.termFunction.name")
      input.form-control(
        type="number",
        required,
        @input="setMeanAddableTerm($event.target.value)"
      )
    .col(v-if="'Трапецеидальная'==$store.state.addableTerm.termFunction.name")
      input.form-control(
        type="number",
        required,
        @input="setUpperRightAddableTerm($event.target.value)"
      )
    .col
      input.form-control(
        type="number",
        required,
        @input="setRightAddableTerm($event.target.value)"
      ) 
  .row(v-if='["Гаусса","Сигмоида"].includes($store.state.addableTerm.termFunction.name)')
    .col
      label(for='mean') Среднее
      input#mean.form-control(
        type="number",
        required,
        @input="setMeanAddableTerm($event.target.value)"
      )
    .col
      label(for="stdDev") Стандартное отклонение
      input#stdDev.form-control(
        type="number",
        required,
        @input="setStandardDeviationAddableTerm($event.target.value)"
      ) 
  div(v-if="'Кусочно-линейная'==$store.state.addableTerm.termFunction.name")
    label(for="xy") Координаты в формате: x1;y1 x2;y2 ...
    input#xy.form-control(
        type="text",
        required,
        @change="setPointsAddableTerm($event.target.value)"
        placeholder="x1;y1 x2;y2 ..."
      )
</template>

<script>
import { mapMutations } from "vuex";
export default {
  name: "AddableFunction", 
  methods: {
    ...mapMutations([
      "setRightAddableTerm",
      "setLeftAddableTerm",
      "setMeanAddableTerm",
      "setUpperLeftAddableTerm",
      "setUpperRightAddableTerm",
      "setStandardDeviationAddableTerm",
      "setPointsAddableTerm",
      "setNameAddableTerm",
      "setFunctionAddableTerm",
    ]),
  },
};
</script>
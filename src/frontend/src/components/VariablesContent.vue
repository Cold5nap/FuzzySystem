<template lang="pug">
#types.col-3.bg-success.bg-opacity-10.p-2
  .d-flex.justify-content-between
    b Входные переменные
    i.bi.bi-plus-square.text-success.h4(
      data-bs-toggle="modal",
      data-bs-target="#modal",
      @click="isAddableInputVariable = true"
    )
  // список входных данных
  ul.list-group
    li.list-group-item.bg-success.bg-opacity-10.p-1.d-flex.justify-content-between(
      v-for="(variable, index) in $store.state.variable.inputVariables"
    )
      // отображение данных
      div(v-if="editableVariable !== variable")
        div
          label(:for="'selectedInputVariable' + index") 
          | Переменная: {{ variable.name }}
          input.mx-1.form-check-input.h4.my-0(
            :id="'selectedInputVariable' + index",
            type="radio",
            name="chart",
            @click="showChartByVariable(variable)"
          )
        div Тип: {{ variable.type.name }}
        div Значение: {{ variable.value }}

      // изменение данных
      div(v-if="editableVariable === variable")
        label(for="vName") Название переменной
        input#vName.form-control(
          type="text",
          :value="editableVariable.name",
          @input="setNameInputVariable({value:$event.target.value, index:index})"
        )
        label(for="vType") Выберете тип переменной
        select#vType.form-select(
          :value="editableVariable.type.index",
          @change="setTypeInputVariable({type:$store.state.type.types[$event.target.value], variableIndex:index})"
        )
          option(
            v-for="type in $store.state.type.types",
            :value="type.index"
          ) {{ type.name }}
        label(for="vValue") Значение
        input#vValue.form-control(
          type="number",
          :value="editableVariable.value",
          @input="setValueInputVariable({value:$event.target.value, index:index})"
        )
      //  Блок, если нет данных 
      .alert.alert-danger(
        v-if="$store.state.variable.inputVariables.length === 0"
      ) 
        | Отсутствуют входные данные
      //  Блок действий с переменными 
      span#actionsInputVariables(style="min-width:52px")
        i.bi.bi-dash-square.text-danger.h4.me-1(@click="deleteInputVariable(index)")
        i.bi.bi-pencil-square.text-info.h4(
          @click="changeEditable(variable)"
        )

  .d-flex.justify-content-between
    b Выходные переменные
    i.bi.bi-plus-square.text-success.h4(
      data-bs-toggle="modal",
      data-bs-target="#modal",
      @click="isAddableInputVariable = false"
    )
  // список выходных данных
  ul.list-group
    li.list-group-item.bg-success.bg-opacity-10.p-1.d-flex.justify-content-between(
      v-for="(variable, index) in $store.state.variable.outputVariables"
    )
      div(v-if="editableVariable !== variable")
        div
          label(:for="'selectedOutputVariable' + index") Переменная: {{ variable.name }}
          input.mx-1.form-check-input.h4.my-0(
            :id="'selectedOutputVariable' + index",
            type="radio",
						name="chart"
            @click="showChartByVariable(variable)"
          )
        div Тип: {{ variable.type.name }}
        div
          | Метод дефаззификации:
          br
          | {{ variable.method.name }}
        div Значение поумолчанию: {{ variable.def }}
        div(v-if="variable.value") Значение: {{ variable.value }}

      div(v-if="editableVariable === variable")
        input.form-control(
          type="text",
          :value="editableVariable.name",
          @input="setNameOutputVariable({value:$event.target.value, index:index})"
        )
        select.form-select(
          :value="editableVariable.type.index",
          @change="setTypeOutputVariable({type:$store.state.type.types[$event.target.value], index:index})"
        )
          option(
            v-for="varType in $store.state.type.types",
            :key="varType.name",
            :value="varType.index"
          ) {{ varType.name }}
        label(for="vmethod") Метод дефаззификации
        select#vmethod.form-select(
          :value="editableVariable.method.index",
          @change="setMethodOutputVariable({method:methods[$event.target.value], index:index})"
        )
          option(
            v-for="method in methods",
            :key="method.name",
            :value="method.index"
          ) {{ method.name }}
        input.form-control(
          type="text",
          :value="editableVariable.def",
          @input="setDefOutputVariable({value:$event.target.value, index:index})"
        )
      //  Блок Отсутствуют выходных переменных 
      .alert.alert-danger(v-if="$store.state.variable.outputVariables.length === 0")
        | Отсутствуют выходные данные
      //  Действия с выходными переменными 
      span#actionsOutputVariables(style="min-width:52px")
        i.bi.bi-dash-square.text-danger.h4.me-1(
          @click="deleteOutputVariable(index)"
        )
        i.bi.bi-pencil-square.text-info.h4(
          @click="changeEditable(variable)"
        )

  // модальное окно для добавления входных/выходных переменных
  form#modal.modal.fade(
    tabindex="-1",
    aria-labelledby="modalLabel",
    aria-hidden="true"
  )
    .modal-dialog
      form.modal-content(
        @submit.prevent="isAddableInputVariable ? addInputVariable(addableVariable) : addOutputVariable(addableVariable)"
      )
        .modal-header
          h4#modalLabel.modal-title(v-if="isAddableInputVariable") Добавление входной переменной
          h4#modalLabel.modal-title(v-else) Добавление выходной переменной
          button.btn-close(
            type="button",
            data-bs-dismiss="modal",
            aria-label="Close"
          )
        .modal-body
          .row
            .col
              label(for="nameOV") Название переменной
              input#nameOV.form-control(
                type="text",
                required,
                v-model="addableVariable.name",
                placeholder="Название"
              )

            .col(v-if="isAddableInputVariable")
              label(for="default") Значение 
              input#default.form-control(
                required,
                v-model="addableVariable.value",
                type="number"
              )

            .col(v-if="!isAddableInputVariable")
              label(for="default") Значение поумолчанию
              input#default.form-control(
                required,
                v-model="addableVariable.def",
                type="number"
              )
          .row
            .col(v-if="!isAddableInputVariable")
              label(for="method") Метод деффазификации
              select#method.form-select(v-model="addableVariable.method", required)
                option(
                  v-for="method in methods",
                  :key="method.name",
                  :value="method"
                ) {{ method.name }}
            .col
              label(for="typeOV") Тип переменной
              select#typeOV.form-select(v-model="addableVariable.type", required)
                option(
                  v-for="varType in $store.state.type.types",
                  :key="varType.name",
                  :value="varType"
                ) {{ varType.name }}
        .modal-footer
          button.btn.btn-success.container-fluid Добавить
</template>

<script>
import { mapMutations } from "vuex";
export default {
  name: "VariablesContent",
  data() {
    return {
      isAddableInputVariable: true,
      editableVariable: null,
      addableVariable: {
        name: "",
        method: this.$store.state.variable.defuzzificationMethods[0],
        type: this.$store.state.type.types[0],
        def: 0,
        value: 0,
      },
      methods: this.$store.state.variable.defuzzificationMethods,
    };
  },
	mounted(){
		this.addableVariable.name="Сервис"
		this.addableVariable.value =3
		this.addInputVariable(this.addableVariable)

		this.addableVariable.name="Еда"
		this.addableVariable.value =5
		this.addableVariable.type = this.$store.state.type.types[1]
		this.addInputVariable(this.addableVariable)
		
		this.addableVariable.name="Чаевые"
		delete this.addableVariable.value
		this.addableVariable.def =100
		this.addableVariable.type = this.$store.state.type.types[2]
		this.addOutputVariable(this.addableVariable)
	},
  methods: {
    changeEditable(variable){
      if(this.editableVariable==variable){
        this.editableVariable=null
      }else{
        this.editableVariable=variable
      }
    },
    ...mapMutations([
      "setNameInputVariable",
      "setTypeInputVariable",
      "setValueInputVariable",

      "setDefOutputVariable",
      "setNameOutputVariable",
      "setValueOutputVariable",
      "setMethodOutputVariable",
      "setTypeOutputVariable",

      "deleteInputVariable",
      "deleteOutputVariable",

      "addInputVariable",
      "addOutputVariable",
      "showChartByVariable",
    ]),
  },
};
</script>

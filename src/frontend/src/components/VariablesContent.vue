<template>
  <div id="types" class="col-3 bg-success bg-opacity-10 p-2">
    <div class="d-flex justify-content-between">
      <b>Входные данные</b>
      <i class="bi bi-plus-square text-success h5" style="cursor: pointer" data-bs-toggle="modal"
        data-bs-target="#inputVariableModal"></i>
    </div>

    <!--список входных данных-->
    <ul class="list-group">
      <li class="list-group-item bg-success bg-opacity-10 p-1 d-flex justify-content-between"
        v-for="(variable, index) in inputVariables" :key="index">

        <!--отображение данных-->
        <div v-if="editableInputVariable !== variable">
          <div>Переменная: {{ variable.name }}</div>
          <div>Тип: {{ variable.type.name }}</div>
        </div>

        <!--изменение данных-->
        <div v-if="editableInputVariable === variable">
          <input class="form-control" type="text" v-model="variable.name" @change="updateParentInputVariables">
          <select class="form-select" v-model="variable.type" @change="updateParentInputVariables">
            <option v-for="type in types" :key="type.name" :value="type">{{ type.name }}</option>
          </select>
        </div>

        <div class="alert alert-danger" v-if="inputVariables.length === 0">Отсутвуют входные данные</div>

        <span>
          <i class="bi bi-dash-square text-danger h5" style="cursor: pointer" @click="deleteInputVariable(index)"></i>
          <i class="bi bi-pencil-square text-info h5" style="cursor: pointer" @click="editInputVariable(variable)"></i>
        </span>
      </li>
    </ul>

    <div class="d-flex justify-content-between">
      <b>Выходные данные</b>
      <i class="bi bi-plus-square text-success h5" style="cursor: pointer" data-bs-toggle="modal"
        data-bs-target="#outputVariableModal"></i>
    </div>

    <!--список выходных данных-->
    <ul class="list-group">
      <li class="list-group-item bg-success bg-opacity-10 p-1 d-flex justify-content-between"
        v-for="(variable, index) in outputVariables" :key="index">
        <div v-if="editableOutputVariable !== variable">
          <div>Переменная: {{ variable.name }}</div>
          <div>Тип: {{ variable.type.name }}</div>
          <div>Тип: {{ variable.method.name }}</div>
          <div>Значение поумолчанию: {{ variable.def }}</div>
        </div>

        <div v-if="editableOutputVariable === variable">

          <input class="form-control" type="text" v-model="variable.name" @change="updateParentOutputVariables">

          <select class="form-select" v-model="variable.type" @change="updateParentOutputVariables">
            <option v-for="type in types" :key="type.name" :value="type">{{ type.name }}</option>
          </select>

          <input class="form-control" type="text" v-model="variable.def" @change="updateParentOutputVariables">

          <select class="form-select" v-model="variable.method" @change="updateParentOutputVariables">
            <option v-for="method in methods" :key="method.name" :value="method">{{ method.name }}</option>
          </select>

        </div>

        <div class="alert alert-danger" v-if="inputVariables.length === 0">Отсутвуют входные данные</div>

        <span>
          <i class="bi bi-dash-square text-danger h5" style="cursor: pointer" @click="deleteOutputVariable(index)"></i>
          <i class="bi bi-pencil-square text-info h5" style="cursor: pointer"
            @click="editOutputVariable(variable)"></i></span>
      </li>
    </ul>

    <!--модальное окно для добавления выходных переменных-->
    <div class="modal fade" id="outputVariableModal" tabindex="-1" aria-labelledby="oVModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="oVModalLabel">Добавление входнных данных</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <label for="nameOV">Введите название переменной</label>
            <input type="text" id="nameOV" v-model="addedOutputVariableName" class="form-control"
              placeholder="Название">

            <label for="method">Выберите метод для деффазификации</label>
            <select id="method" v-model="addedOutputMethod" class="form-select">
              <option v-for="method in methods" :key="method.name" :value="method">{{ method.name }}</option>
            </select>

            <label for="typeOV">Выберите тип переменной</label>
            <select id="typeOV" v-model="addedOutputType" class="form-select">
              <option v-for="type in types" :key="type.name" :value="type">{{ type.name }}</option>
            </select>

            <label for="default">Введите значение поумолчанию</label>
            <input v-model="addedOutputDefaultValue" class="form-control" type="number" id="default">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
            <button type="button" class="btn btn-success" @click="addOutputVariable">Добавить</button>
          </div>
        </div>
      </div>
    </div>


    <!--модальное окно для добавления входных переменных-->
    <div class="modal fade" id="inputVariableModal" tabindex="-1" aria-labelledby="iVModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="iVModalLabel">Добавление входнных данных</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <label for="name">Введите название переменной</label>
            <input type="text" id="name" v-model="addedInputVariableName" class="form-control" placeholder="Название">

            <label for="type">Выберите тип переменной</label>
            <select id="type" v-model="addedInputType" class="form-select">
              <option v-for="type in types" :key="type.name" :value="type">{{ type.name }}</option>
            </select>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
            <button type="button" class="btn btn-success" @click="addInputVariable">Добавить</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import InputVariable from "@/services/InputVariable";
import OutputVariable from "@/services/OutputVariable";

export default {
  name: "VariablesContent",
  props: ['methods', 'types', 'parentInputVariables', 'parentOutputVariables'],
  data() {
    return {
      inputVariables: this.parentInputVariables,
      outputVariables: this.parentOutputVariables,

      editableInputVariable: null,//форма для изменения
      editableOutputVariable: null,//форма для изменения

      addedInputVariableName: null,//форма для добавления переменных
      addedInputType: null,//форма для добавления переменных

      addedOutputVariableName: null,//форма для добавления переменных
      addedOutputType: null,//форма для добавления переменных
      addedOutputMethod: null,//форма для добавления переменных
      addedOutputDefaultValue: 1,//форма для добавления переменных
    }
  },
  methods: {
    updateParentOutputVariables() {
      this.$emit('updateParentInputVariables', this.outputVariables)
    },
    updateParentInputVariables() {
      this.$emit('updateParentOutputVariables', this.inputVariables)
    },
    deleteInputVariable(index) {
      this.inputVariables.splice(index, 1)
      this.updateParentInputVariables()
    },
    deleteOutputVariable(index) {
      this.outputVariables.splice(index, 1)
      this.updateParentOutputVariables()
    },
    editInputVariable(variable) {
      if (this.editableInputVariable == null) {
        this.editableInputVariable = variable;
      } else {
        this.editableInputVariable = null;
      }
    },
    editOutputVariable(variable) {
      if (this.editableOutputVariable == null) {
        this.editableOutputVariable = variable;
      } else {
        this.editableOutputVariable = null;
      }
    },
    addInputVariable() {
      this.inputVariables.push(
        new InputVariable(
          this.addedInputVariableName,
          this.addedInputType
        ));
      this.updateParentInputVariables();
    },
    addOutputVariable() {
      this.outputVariables.push(
        new OutputVariable(
          this.addedOutputVariableName,
          this.addedOutputType,
          this.addedOutputDefaultValue,
          this.addedOutputMethod
        ));
      this.updateParentOutputVariables();
    }
  }
}
</script>

<style scoped>
</style>
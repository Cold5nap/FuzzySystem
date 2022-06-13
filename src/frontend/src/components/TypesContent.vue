<template>
  <div id="types" class="col-3 bg-success bg-opacity-10 p-2 ">
    <p class="d-flex justify-content-between">
      <b>Типы</b>
      <i class="bi bi-plus-square text-success mx-1 h5" style="cursor: pointer" data-bs-toggle="modal"
        data-bs-target="#typeModal"></i>
    </p>

    <!--список типов-->
    <ul class="list-group">
      <li class="list-group-item bg-success bg-opacity-10 p-1" v-for="(type, index) in types" :key="index">

        <div class="justify-content-between d-flex">
          <span>Термины<span class="mx-1" v-if="editType !== type">{{ type.name }}</span>
            <input class="form-check-input" type="radio" name="typeRadio"
              @click="selectType(type)"></span>

          <span>
            <i class="bi bi-plus-square text-success h5" style="cursor: pointer" data-bs-toggle="modal"
              data-bs-target="#termModal" @click="addType = type"></i>
            <i class="bi bi-dash-square text-danger h5" style="cursor: pointer" @click="deleteType(index)"></i>
            <i class="bi bi-pencil-square text-info h5" style="cursor: pointer" @click="onEditTypeName(type)"></i>
          </span>
        </div>
        <input class="form-control " type="text" v-model="type.name" v-if="editType === type"
          @change="updateParentTypes">

        <!--список термов в типе-->
        <ul id="terms" class="list-group" v-if="type.terms.length > 0">
          <li class="list-group-item bg-success bg-opacity-10" v-for="(term, iTerm) in type.terms" :key="iTerm">
            <div class="d-flex justify-content-between">
              <span class="me-1" v-if="editTerm !== term">Термин: {{ term.name }}</span>
              <span></span>
              <span>
                <i class="bi bi-dash-square text-danger h5" style="cursor: pointer"
                  @click="deleteTerm(type.terms, iTerm)"></i>
                <i class="bi bi-pencil-square text-info h5" style="cursor: pointer" @click="onEditTerm(term)"></i>
              </span>
            </div>
            <div>
              <input class="form-control " v-if="editTerm === term" type="text" v-model="term.name"
                @change="updateParentTypes">
              <span v-if="editTerm !== term">Функция: {{ term.termFunction.name }}</span>
              <select class="form-control  form-select" v-if="editTerm === term" v-model="selectedTermFunc"
                @change="setSelectValues(term)">
                <option v-for="(func, indexf) in termFunctions" :value="func" :key="indexf">{{ func.name }}</option>
              </select>
              <!--переделать, адресса массива точек не создаются новые-->
            </div>
            <!--БАГ при изменении точек начинает пропадать функция, свзяанно с зависимостью точек от выбранной функции тегом select-->

            <span v-if="editTerm !== term">Ось х: {{ term.termFunction.points }}</span>
            <span v-if="editTerm === term">
              <input class="form-control " v-for="(point, ipoint) in term.termFunction.points" type="number"
                v-model="term.termFunction.points[ipoint]" :key="ipoint" @change="updateParentTypes">
            </span>

          </li>
        </ul>

        <div class="alert alert-danger" v-if="type.terms.length === 0">Отсутвуют термины</div>
      </li>
    </ul>

    <!--модальное окно для добавления типов-->
    <div class="modal fade" id="typeModal" tabindex="-1" aria-labelledby="typeModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="typeModalLabel">Добавление типа</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <label for="nameType">Введите название</label>
            <input type="text" id="nameType" v-model="addTypeName" class="form-control" placeholder="Название">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
            <button type="button" class="btn btn-primary" @click="onAddType">Добавить</button>
          </div>
        </div>
      </div>
    </div>

    <!--модальное окно для добавления терминов-->
    <div class="modal fade" id="termModal" tabindex="-1" aria-labelledby="termModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="termModalLabel">Добавление термина</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <label for="nameTerm">Введите название</label>
            <input type="text" v-model="addTermName" id="nameTerm" class="form-control" placeholder="Название">

            <label for="termFunction">Выберите функцию</label>
            <select id="termFunction" class="form-control form-select" v-model="addTermFunc"
              @change="addTermPoints = []">
              <option v-for="func in termFunctions" :key="func.name" :value="func">{{ func.name }}</option>
            </select>

            <div class="row m-2" v-if="addTermFunc != null">
              <div v-for="(point, index) in addTermFunc.points" :key="point" class="col">
                <label :for="'point' + index">Точка №{{ index }}</label>
                <input type="number" :id="'point' + index" v-model="addTermPoints[index]" class="form-control">
              </div>
            </div>

          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
            <button type="button" class="btn btn-primary" @click="addTerm">Добавить</button>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>

import Type from "@/services/Type";
import FuzzificationMethod from "@/services/FuzzificationMethod";
import Term from "@/services/Term";

export default {
  name: "TypesContent",
  props: { parentTypes: Array, termFunctions: Array },
  data() {
    return {
      selectedTerm: Term,
      selectedType: Type,
      selectedFunc: FuzzificationMethod,

      selectedTermFunc: null,
      addType: null,//к чему добавляем тип
      addTermName: null,//что добавляем к термину
      addTermPoints: [],//что добавляем к термину
      addTermFunc: null,//что добавляем к термину
      addTypeName: null,

      editType: null,//к чему относятся изменения

      editTerm: null,//к чему относятся изменения
      editTermFunction: null,//что изменяется у термина
      editTermName: null,//что изменяется у термина
      editTermFunctionPoints: null,//что изменяется у термина

      types: this.parentTypes,
    }
  },
  methods: {
    selectType(type){
      this.selectedType=type;
      this.updateParentTypes();
    },
    deleteTerm(terms, index) {
      terms.splice(index, 1);
      this.updateParentTypes();
    },
    deleteType(index) {
      delete this.types[index]
      this.types.splice(index, 1)
      this.updateParentTypes()
    },
    setSelectValues(term) {
      term.termFunction.name = this.selectedTermFunc.name;
      term.termFunction.points = this.selectedTermFunc.points.slice();
      this.updateParentTypes()
    },
    onEditTypeName(targetType) {
      if (this.editType != null) this.editType = null; else this.editType = targetType;
    },
    onEditTerm(targetTerm) {
      if (this.editTerm != null) {
        this.editTerm = null;
        this.selectedTermFunc = null;
      } else {
        this.editTerm = targetTerm;
        this.selectedTermFunc = targetTerm.termFunction
      }
    },
    updateParentTypes() {
      this.$emit('updateTypes', {
        types: this.types,
        selectedType: this.selectedType,
      })
    },
    addTerm() {
      this.addType.terms.push(
        new Term(this.addTermName, new FuzzificationMethod(this.addTermFunc.name, this.addTermPoints.slice()))
      );
      this.updateParentTypes()
    },
    onAddType() {
      this.types.push(
        new Type(this.addTypeName, [])
      );
      this.updateParentTypes()
    },
  },
  mounted() {
  },
  created() {
  }
}
</script>

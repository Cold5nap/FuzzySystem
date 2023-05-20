<template lang="pug">
#types.col-3.bg-success.bg-opacity-10.p-2
  p.d-flex.justify-content-between
    b Типы
    i.bi.bi-plus-square.text-success.mx-1.h4(
      data-bs-toggle="modal",
      data-bs-target="#typeModal"
    )
  // список типов
  ul.list-group
    li.list-group-item.bg-success.bg-opacity-10.p-1(
      v-for="(type, index) in $store.state.type.types",
      :key="index + 'type'"
    )
      // тип
      .justify-content-between.d-flex
        .d-flex(style="max-width:50%")
          
            | Тип:
            label.mx-1(:for="'type' + index", v-if="editableType !== type") {{ type.name }}
            input.form-control(
                v-if="editableType === type"
                type="text",
                style="max-width:80%"
                @input="setTypeName({index:index, name:$event.target.value})"
                :value="editableType.name"
              )
            input.form-check-input.h4.my-0(
              :id="'type' + index",
              type="radio",
              name="chart",
              style="min-width:24px",
              :value="type",
              @click="showChartByType(type)"
            )
          
            
        div
          i.bi.bi-arrow-down-square.text-muted.h4.me-1(
            data-bs-toggle="collapse",
            :data-bs-target="'#terms'+index"
          )
          i.bi.bi-plus-square.text-success.h4.me-1(
            data-bs-toggle="modal",
            data-bs-target="#termModal",
            @click="addableTypeIndex = index"
          )
          i.bi.bi-dash-square.text-danger.h4.me-1(@click="deleteType(index)")
          i.bi.bi-pencil-square.text-info.h4.me-1(v-if="editableType!=type", @click="editableType = type")
          i.bi.bi-pencil-square.text-info.h4(v-else, @click="editableType = null")
      // список термов в типе
      ul.collapse.list-group(v-if="type.terms.length > 0",:id="'terms'+index")
        li.list-group-item.bg-success.bg-opacity-10(
          v-for="(term, indexTerm) in type.terms"
        )
          // Отображение
          div(v-if="$store.state.editableTerm.term != term")
            .d-flex.justify-content-between
              span Термин: {{ term.name }}
              span
                i.bi.bi-dash-square.text-danger.h4.me-1(
                  @click="deleteTerm({typeIndex:index, termIndex:indexTerm})"
                )
                i.bi.bi-pencil-square.text-info.h4(@click="setEditableTerm(term)")
            div Функция: {{ term.termFunction.name }}
            div(v-if="!term.termFunction.standardDeviation && !term.termFunction.raw") Абсциссы:
              span.mx-1(v-if="term.termFunction.left") {{ term.termFunction.left }},
              span.me-1(v-if="term.termFunction.upperLeft") {{ term.termFunction.upperLeft }},
              span.me-1(v-if="term.termFunction.mean")  {{ term.termFunction.mean }},
              span.me-1(v-if="term.termFunction.upperRight") {{ term.termFunction.upperRight }},
              span.me-1(v-if="term.termFunction.right")  {{ term.termFunction.right }}
              span.me-1(v-if="term.termFunction.x")  {{ term.termFunction.x }}
            div(v-if="term.termFunction.standardDeviation") Среднее: {{ term.termFunction.mean }}
            div(v-if="term.termFunction.standardDeviation") Стандартное отклонение: {{ term.termFunction.standardDeviation }}
            div(v-if="term.termFunction.y") Координаты: {{ term.termFunction.raw }}
          // изменение
          div(v-else)
            .d-flex.justify-content-between
              label(for="TermName") Название
              input#TermName.form-control(
                type="text",
                style="max-width:50%",
                :value="$store.state.editableTerm.term.name",
                @input="setTermName({indexType:index, indexTerm:indexTerm, name:$event.target.value})"
              )
              span
                i.bi.bi-dash-square.text-danger.h4(
                  @click="deleteTerm({typeIndex:index, termIndex:indexTerm})"
                )
                i.bi.bi-pencil-square.text-info.h4(@click="setEditableTerm(null)")
            EditableFunction
      .alert.alert-danger(v-if="type.terms.length === 0") Отсутствуют термины
  // модальное окно для добавления типов
  form#typeModal.modal.fade(
    tabindex="-1",
    aria-labelledby="typeModalLabel",
    aria-hidden="true",
    @submit.prevent="addTypeName(addableTypeName)"
  )
    .modal-dialog
      .modal-content
        .modal-header
          h4#typeModalLabel.modal-title Добавление типа
          button.btn-close(
            type="button",
            data-bs-dismiss="modal",
            aria-label="Close"
          )
        .modal-body
          label(for="nameType") Введите название
          input#nameType.form-control(
            type="text",
            required,
            v-model="addableTypeName",
            placeholder="Название"
          )
        .modal-footer
          button.btn.btn-success.container-fluid Добавить
  // модальное окно для добавления терминов
  form#termModal.modal.fade(
    tabindex="-1",
    aria-labelledby="termModalLabel",
    aria-hidden="true",
    @submit.prevent="addTerm({term:$store.state.addableTerm,typeIndex:addableTypeIndex})"
  )
    .modal-dialog
      .modal-content
        .modal-header
          h4#termModalLabel.modal-title Добавление термина
          button.btn-close(
            type="button",
            data-bs-dismiss="modal",
            aria-label="Close"
          )
        .modal-body
          
            label(for="nameTerm") Введите название
            input#nameTerm.form-control(
              type="text",required,
              :value="$store.state.addableTerm.name",
              @input="setNameAddableTerm($event.target.value)",
              placeholder="Название"
            )
            Function
        .modal-footer
          button.btn.btn-success.container-fluid Добавить
</template>

<script>
import { mapMutations } from "vuex";
import Function from "@/components/AddableFunction.vue";
import EditableFunction from "@/components/EditableFunction.vue";
export default {
  name: "TypesContent",
  data() {
    return {
      folded: [],
      addableTypeName: "",
      addableTypeIndex:'',
      editableTerm: null,
      editableType: null,
    };
  },
  components: {
    Function,
    EditableFunction,
  },
  methods: {
    ...mapMutations([
      "setNameAddableTerm",
      "addTerm",
      "addTypeName",
      "setX",
      "deleteTerm",
      "deleteType",
      "setFunction",
      "setTermName",
      "addTerm",
      "addType",
      "showChartByType",
      "setVisible",
      "setInvisible",
      "setTypeName",
      "setEditableTerm",
    ]),
  },
};
</script>

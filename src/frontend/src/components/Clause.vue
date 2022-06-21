<template>
  <div class="row">
    <div class="col-auto p-0">
      <label for="variable-condition">Переменная</label>
      <select
        class="form-control"
        v-model="selectedVariable"
        id="variable-condition"
        @change="$emit('updateVariable', selectedVariable)"
      >
        <option v-for="variable in this.variables" :key="variable.name" :value="variable">
          {{ variable.name }}
        </option>
      </select>
    </div>

    <div class="col-auto p-0">
      <label for="variable-term-connector">Оператор</label>
      <select
        class="form-control"
        v-model="selectedConnection"
        @change="$emit('updateConnection', selectedConnection)"
        id="variable-term-connector"
      >
        <option>=</option>
        <option>&#8800;</option>
      </select>
    </div>

    <div class="col-auto p-0">
      <label for="term-condition">Термин</label>
      <select disabled class="form-control" v-if="selectedVariable == null" id="term-condition">
        <option selected>Переменная не выбрана</option>
      </select>
      <select
        class="form-control"
        v-model="selectedTerm"
        @change="$emit('updateTerm', selectedTerm)"
        id="term-condition"
        v-if="selectedVariable != null"
      >
        <option v-for="term in selectedVariable.type.terms" :key="term.name" :value="term">
          {{ term.name }}
        </option>
      </select>
    </div>
  </div>
</template>

<script>
export default {
  name: "ClauseComponent",
  props: ["variables", "clause"],
  data() {
    return {
      selectedVariable: this.clause.variable,
      selectedTerm: this.clause.term,
      selectedConnection: this.clause.connection,
    };
  },
};
</script>

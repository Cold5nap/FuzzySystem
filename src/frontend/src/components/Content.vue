<template>
  <div class="container border rounded p-2">
    <div class="my-2">
      <input type="file" hidden class="form-file-input" id="customFile" />
      <label
        class="form-file-label btn-sm btn-success"
        for="customFile"
        style="cursor: pointer"
      >
        Загрузить файл формата fcl
      </label>
    </div>
    <div class="row m-0">
      <VariablesContent
        :methods="defuzzificationMethods"
        :types="types"
        :parentInputVariables="inputVariables"
        :parentOutputVariables="outputVariables"
        @updateParentInputVariables="updateInputVariables"
        @updateParentOutputVariables="updateOutputVariables"
      ></VariablesContent>
      <ChartContent :selectedType="selectedType" ref="chart"></ChartContent>
      <TypesContent
        :termFunctions="termFunctions"
        :parentTypes="types"
        @updateTypes="updateTypes"
      ></TypesContent>
    </div>
    <RulesContent></RulesContent>

    <div id="buttons" class="row row-cols-auto my-2">
      <div class="col">
        <button class="btn btn-sm btn-success" @click="getResult">
          Получить итоговый график
        </button>
      </div>
      <div class="col">
        <button class="btn btn-sm btn-success">
          Экспортировать файл FCL построенной системы.
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TypesContent from "@/components/TypesContent";
import ChartContent from "@/components/ChartContent";
import VariablesContent from "@/components/VariablesContent";
import RulesContent from "@/components/RulesContent";
import FuzzificationMethod from "../services/FuzzificationMethod";
import Type from "../services/Type";
import InputVariable from "../services/InputVariable";
import OutputVariable from "../services/OutputVariable";
import Term from "../services/Term";
import DefuzzificationMethod from "@/services/DefuzzificationMethod";

export default {
  name: "ContentComponent",
  components: {
    ChartContent,
    TypesContent,
    VariablesContent,
    RulesContent,
  },
  data() {
    return {
      termFunctions: [
        new FuzzificationMethod("Треугольная", [1, 2, 3]),
        new FuzzificationMethod("Прямоугольная", [1, 3]),
        new FuzzificationMethod("Трапецеидальная", [1, 2, 3, 4]),
      ],
      defuzzificationMethods: [
        new DefuzzificationMethod("COG"),
        new DefuzzificationMethod("COGS"),
        new DefuzzificationMethod("LM"),
        new DefuzzificationMethod("RM"),
      ],
      types: null,
      inputVariables: null,
      outputVariables: null,
      selectedType: null,
      rules: null,
    };
  },
  created() {
    this.types = [
      new Type("point", [
        new Term("bsas", new FuzzificationMethod("Прямоугольная", [1, 3])),
        new Term(
          "other",
          new FuzzificationMethod("Трапецеидальная", [1, 2, 3, 4])
        ),
      ]),
      new Type("point2", [
        new Term("bsas_2", new FuzzificationMethod("Треугольная", [1, 2, 3])),
        new Term("other_2", new FuzzificationMethod("Прямоугольная", [1, 3])),
      ]),
    ];
    this.inputVariables = [
      new InputVariable("city", this.types[0]),
      new InputVariable("scoring", this.types[0]),
      new InputVariable("occupation_type", this.types[0]),
      new InputVariable("sel", this.types[1]),
      new InputVariable("scoring_partner", this.types[1]),
    ];
    this.outputVariables = [
      new OutputVariable(
        "credLimMul",
        this.types[1],
        1,
        this.defuzzificationMethods[0]
      ),
      new OutputVariable(
        "qualify",
        this.types[1],
        1,
        this.defuzzificationMethods[0]
      ),
    ];
    this.rules = [
      {
        name: "first rule",
        conditions: [
          {
            variable: this.inputVariables[0],
            connection: "это",
            term: this.inputVariables[0].type.terms[0],
            conditionConnector: null,
          },
        ],
        actions: [
          {
            variable: this.outputVariables[0],
            connection: "это",
            term: this.outputVariables[0].type.terms[0],
          },
        ],
        weight: 1,
      },
      {
        name: "second rule",
        conditions: [
          {
            variable: this.inputVariables[0],
            connection: "это",
            term: this.inputVariables[0].type.terms[0],
            conditionConnector: null,
          },
          {
            variable: this.inputVariables[1],
            connection: "это",
            term: this.inputVariables[1].type.terms[0],
            conditionConnector: "и",
          },
        ],
        actions: [
          {
            variable: this.outputVariables[0],
            connection: "это",
            term: this.outputVariables[0].type.terms[0],
          },
        ],
        weight: 1,
      },
    ];

    this.$store.commit("setRules", this.rules);
    this.$store.commit("setInputVariables", this.inputVariables);
    this.$store.commit("setOutputVariables", this.outputVariables);
  },
  methods: {
    updateOutputVariables(outputVariables) {
      this.outputVariables = outputVariables;
      console.log(this.outputVariables);
    },
    updateInputVariables(inputVariables) {
      this.inputVariables = inputVariables;
      console.log(this.inputVariables);
    },
    updateTypes(types) {
      this.types = types.types;
      this.selectedType = types.selectedType;
      this.$refs.chart.showChartByType(this.selectedType);
      console.log(this.selectedType.name);
    },
    sendInputFile() {
      let fd = new FormData();
      fd.append("input_file", this.$refs.inputFile.files[0]);
      axios
        .post("http://localhost:8080/api/input_file", fd, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getResult() {
      axios
        .post("http://localhost:8080/api/input_file", 0, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
  },
  computed() {},
  mounted() {
    this.$store.actions.getGraphicFromApi();
  },
};
</script>

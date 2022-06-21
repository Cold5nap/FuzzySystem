package cs.vsu.ru.FuzzySystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuzzySystem {
    private RuleInput[] rules;
    private InputVariable[] inputVariables;
    private OutputVariable[] outputVariables;
    private String operator;
    private String algorithm;
    private String activator;
    private String accumulation;

}

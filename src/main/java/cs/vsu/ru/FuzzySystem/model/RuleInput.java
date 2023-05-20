package cs.vsu.ru.FuzzySystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleInput {
    private Condition[] conditions;
    private Action[] actions;
    private String name;
    private double weight;
}

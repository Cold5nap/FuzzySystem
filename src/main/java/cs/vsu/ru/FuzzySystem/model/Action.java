package cs.vsu.ru.FuzzySystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    private Term term;
    private String connection;
    private OutputVariable variable;
}

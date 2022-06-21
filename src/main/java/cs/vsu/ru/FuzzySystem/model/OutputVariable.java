package cs.vsu.ru.FuzzySystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputVariable {
    private double def;
    private Method method;
    private String name;
    private Type type;
}

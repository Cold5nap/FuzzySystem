package cs.vsu.ru.FuzzySystem.model;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputVariable extends CommonVariable {
    private double def;
    private Method method;
}

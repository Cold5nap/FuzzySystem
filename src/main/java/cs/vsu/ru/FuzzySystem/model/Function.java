package cs.vsu.ru.FuzzySystem.model;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function {
	private String name;
		// @Nullable
    private double[] x;
    private double[] y;
    private String raw;
    private double mean;
    private double left;
    private double right;
    private double upperRight;
    private double upperLeft;
    private double standardDeviation;
}

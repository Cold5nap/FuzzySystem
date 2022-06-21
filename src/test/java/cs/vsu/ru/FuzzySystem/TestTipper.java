package cs.vsu.ru.FuzzySystem;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

/**
 * Test parsing an FCL file
 * 
 * @author pcingola@users.sourceforge.net
 */
public class TestTipper {

	public static void main(String[] args) throws Exception {
		// Load from 'FCL' file
		String fileName = "src/test/java/cs/vsu/ru/FuzzySystem/tipper.fcl";
		FIS fis = FIS.load(fileName, true);
		if (fis == null) { // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}

		// Show ruleset
		FunctionBlock functionBlock = fis.getFunctionBlock(null);

		// Set inputs
		functionBlock.setVariable("service", 2);
		functionBlock.setVariable("food", 7);

		// Evaluate
		functionBlock.evaluate();

		// Show output variable's chart
		JFuzzyChart.get().chart(fis);
		System.out.println(functionBlock.getVariable("tip").toString());

	}
}

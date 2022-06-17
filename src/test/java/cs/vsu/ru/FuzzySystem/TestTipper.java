package cs.vsu.ru.FuzzySystem;

import java.util.Iterator;
import java.util.Map;

import org.assertj.core.data.MapEntry;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

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
		JFuzzyChart.get().chart(functionBlock);

		// Set inputs
		functionBlock.setVariable("service", 2);
		functionBlock.setVariable("food", 7);

		// Evaluate
		functionBlock.evaluate();

		// Show output variable's chart
		Variable tip = functionBlock.getVariable("tip");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		Iterator it = functionBlock.getVariable("service").getLinguisticTerms().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry p = (Map.Entry) it.next();
			System.out.println(functionBlock.getVariable("service").getMembership((String) p.getKey()));
		}
		System.out.println();
		Gpr.debug("poor[service]: " + functionBlock.getVariable("service").getMembership("poor"));

	}
}

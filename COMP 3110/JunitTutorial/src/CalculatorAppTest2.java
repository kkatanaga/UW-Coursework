import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalculatorAppTest2 {
	private CalculatorApp calculatorApp;
	@Before
	public void setUp() {
		calculatorApp = new CalculatorApp();
	}
	
	@After
	public void tearDown() {
		calculatorApp = null;
	}
	
	@Test
	public void testAdd() {
		assertEquals(calculatorApp.add(3, 2), 5);
	}
}

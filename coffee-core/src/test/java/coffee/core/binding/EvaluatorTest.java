package coffee.core.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import coffee.core.binding.Evaluator;

public class EvaluatorTest {

	private static final String HELLO_WORLD = "#{hello.world}";

	@Test
	public void testFindAttributesRegExp(){
		String expression = HELLO_WORLD;
		assertTrue(expression.matches(Evaluator.RE_IS_VALID_BINDABLE_EXPRESSION));
		
		Matcher matcher = Pattern.compile(Evaluator.RE_FIND_ATTR).matcher(expression);
		assertNotNull(matcher);
		assertTrue(matcher.find());
		assertEquals("hello", matcher.group());
		assertTrue(matcher.find());
		assertEquals("world", matcher.group());
	}

	@Test
	public void grantThatItRetrievesTheAttribute() {
		Evaluator evaluator = Evaluator.eval(new Hello(), HELLO_WORLD);
		Object value = evaluator.getValue();
		assertEquals("Earth", value);
	}

	@Test
	public void grantThatItSentsTheAttribute() {
		Hello hello = new Hello();
		Evaluator evaluator = Evaluator.eval(hello, HELLO_WORLD);
		String expected = "Jupter";
		evaluator.setValue(expected);
		assertEquals(hello.getWorld(), expected);
	}
	
	public class Hello {
		private String world = "Earth";

		public void setWorld(String world) {
			this.world = world;
		}

		public String getWorld() {
			return world;
		}
	}
}

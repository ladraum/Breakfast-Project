package breakfast.coffee.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class EvaluatorTest {

	private static final String HELLO_WORLD = "#{hello.world}";
	private static final String WORLD_SIZE = "#{hello.size}";
	private static final String HELLO_REAL_WORLD = "#{hello.realworld.name}";

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
		Hello target = new Hello();
		target.setWorld("Earth");

		Evaluator evaluator = Evaluator.eval(target, HELLO_WORLD);
		Object value = evaluator.getValue();
		assertEquals("Earth", value);
	}

	@Test
	public void grantThatItSentsTheAttribute() {
		Hello hello = new Hello();
		Evaluator evaluator = Evaluator.eval(hello, HELLO_WORLD);
		String expected = "Jupter";
		evaluator.setValue(expected);
		assertEquals(expected, hello.getWorld());
	}

	@Test
	public void grantThatretrievesThreeLevelAttributes() {
		String originalName = "Mercury";
		World realworld = new World();
		realworld.setName(originalName);
		Hello hello = new Hello();
		hello.setRealworld(realworld);
		
		Evaluator evaluator = Evaluator.eval(hello, HELLO_REAL_WORLD);
		
		Object value = evaluator.getValue();
		assertEquals(originalName, value);
		
		String expected = "Jupter";
		evaluator.setValue(expected);
		assertEquals(expected, hello.getRealworld().getName());
	}
	
	@Test
	public void grantThatDefaultFieldParserConvertToInteger(){
		Hello hello = new Hello();
		Evaluator evaluator = Evaluator.eval(hello, WORLD_SIZE);
		Integer size = 123456;
		evaluator.setValue(size);
		assertEquals(size, hello.getSize());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void grantReflectionCanExtractGenericClass() throws SecurityException, NoSuchFieldException {
		Hello hello = new Hello();
		Field field = hello.getClass().getDeclaredField("countries");
		ParameterizedType ptype = (ParameterizedType) field.getGenericType();
		assertEquals(String.class, (Class)ptype.getActualTypeArguments()[0]);
	}

	public class Hello {
		private String world; // = "Earth"
		private World realworld;
		private Integer size;
		private ArrayList<String> countries;

		public void setWorld(String world) {
			this.world = world;
		}

		public String getWorld() {
			return world;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public Integer getSize() {
			return size;
		}

		public void setCountries(ArrayList<String> countries) {
			this.countries = countries;
		}

		public ArrayList<String> getCountries() {
			return countries;
		}

		public void setRealworld(World realworld) {
			this.realworld = realworld;
		}

		public World getRealworld() {
			return realworld;
		}
	}
	
	public class World {
		private String name;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}
}

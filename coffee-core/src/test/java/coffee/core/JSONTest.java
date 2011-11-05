package coffee.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import coffee.core.util.json.JSON;
import coffee.sample.Animal;
import coffee.sample.SampleEntity;

public class JSONTest {

	private SampleEntity entity;

	@Before
	public void setup() {
		ArrayList<Animal> list = new ArrayList<Animal>();
		list.add(new Animal("Dog"));
		list.add(new Animal("Cat"));

		entity = new SampleEntity();
		entity.setId(12);
		entity.setName("Joseph Smith");
		entity.setAnimals(list);
	}

	@Test
	public void testSerialization() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException
	{
		String expected = "{\"name\":\"Joseph Smith\",\"animals\":[{\"name\":\"Dog\"},{\"name\":\"Cat\"}],\"id\":\"12\"}";
		StringBuilder serialized = new JSON().serialize(entity);

		if (!expected.equals(serialized.toString())) {
			System.out.println("'" + expected + "'");
			System.out.println("'" + serialized + "'");
		}
		Assert.assertEquals(expected, serialized.toString());
	}
}

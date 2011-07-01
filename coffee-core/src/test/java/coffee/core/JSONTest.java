package coffee.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import coffee.core.util.JSON;
import coffee.sample.SampleEntity;

public class JSONTest {

	private SampleEntity entity;

	@Before
	public void setup() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Dog");
		list.add("Cat");

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
		String expected = "{\"id\":\"12\",\"name\":\"Joseph Smith\",\"animals\":[\"Dog\",\"Cat\"]}";
		StringBuilder serialized = JSON.serialize(entity);
		
		if (!expected.equals(serialized.toString())) {
			System.out.println("'" + expected + "'");
			System.out.println("'" + serialized + "'");
		}
		Assert.assertEquals(expected, serialized.toString());
	}
}

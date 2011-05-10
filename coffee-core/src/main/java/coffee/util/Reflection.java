package coffee.util;

import java.lang.reflect.Method;

public class Reflection {

	public static Method extractGetterFor(String attribute, Object target)
			throws SecurityException, NoSuchMethodException {
		String getter = String.format("get%s%s", attribute.substring(0, 1)
				.toUpperCase(), attribute.substring(1));
		return target.getClass().getMethod(getter);
	}

	public static Method extractSetterFor(String attribute, Object target, Object value)
			throws SecurityException, NoSuchMethodException {
		String setter = String.format("set%s%s", attribute.substring(0, 1)
				.toUpperCase(), attribute.substring(1));
		return target.getClass().getMethod(setter, value.getClass());
	}
}

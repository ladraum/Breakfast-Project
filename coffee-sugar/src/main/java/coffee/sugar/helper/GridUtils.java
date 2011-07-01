package coffee.sugar.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import coffee.core.util.Reflection;

public class GridUtils {

/**
 * Creates a Simple (Single Level) JavaScript Object Notation string. Note
 * that is a specific implementation, and it isn't a fully complaint JSON
 * implementation. It was designed to be used with Coffee components.
 * Use this method by yourself, and if you really know what are you doing.
 * 
 * @return
 * @throws NoSuchMethodException
 * @throws SecurityException
 * @throws InvocationTargetException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 */
	public static StringBuilder serializeItem(Object target)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		for (Field field : target.getClass().getDeclaredFields()) {
			Method getter = Reflection.extractGetterFor(field.getName(), target);
			Object object = getter.invoke(target);
			buffer.append(field.getName())
				  .append(":\"")
				  .append(object.toString())
				  .append("\",");
		}
		buffer.append('}');
		return buffer;
	}

	public static Collection<?> unserializeGridData(String string, Class<?> clazz)
		 throws InstantiationException,
					IllegalAccessException, SecurityException, NoSuchMethodException,
					IllegalArgumentException, InvocationTargetException {
		Collection<Object> gridData = new ArrayList<Object>();
		
		for (String line : string.split("?")) {
			Object instance = clazz.newInstance();
			for (String tuple : line.split("&")) {
				String[] tupleValues = tuple.split("=");
				Method setter = Reflection.extractSetterFor(tupleValues[0], instance, tupleValues[1]);
				setter.invoke(instance, tupleValues[1]);
			}
			gridData.add(instance);
		}
		
		return gridData;
	}
}

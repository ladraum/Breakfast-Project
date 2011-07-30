package coffee.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class JSON {

	@SuppressWarnings("unchecked")
	public static StringBuilder serialize (Object target) {
		StringBuilder buffer = new StringBuilder();
		String name = target.getClass().getPackage().getName();
		
		if (Collection.class.isInstance(target)) {
			buffer.append(
				serializeArrayOfObjects((Collection<Object>)target));
		}
		else if (name.startsWith("java.lang")) {
			buffer.append('"')
				  .append(target.toString())
				  .append('"');
		}
		else {
			buffer.append(serializeObject (target));
		}
		
		return buffer;
	}

	public static StringBuilder serializeObject(Object target) {
		StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		
		boolean isFirst = true;
		Class<? extends Object> clazz = target.getClass();
		while (!clazz.equals(Object.class)) {
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				try {
					Method getter = Reflection.extractGetterFor(field.getName(), target);
					Object object = getter.invoke(target);
					
					if (Util.isNull(object))
						continue;
	
					if (!isFirst) buffer.append(',');
					isFirst = false;
	
					buffer.append('"')
						.append(field.getName())
						.append("\":");
	
					buffer.append(serialize(object));
				} catch (Exception e) {
					// XXX: ignoring non changeable fields
				}
			}
			
			clazz = clazz.getSuperclass();
		}

		buffer.append('}');
		return buffer;
	}

	public static StringBuilder serializeArrayOfObjects(Collection<?> arrayOfobjects) {
		StringBuilder buffer = new StringBuilder();

		boolean isFirstItem = true;
		buffer.append('[');

		for (Object item : arrayOfobjects) {
			if (!isFirstItem) buffer.append(',');
			buffer.append(serialize(item));
			isFirstItem = false;
		}

		buffer.append(']');

		return buffer;
	}
}

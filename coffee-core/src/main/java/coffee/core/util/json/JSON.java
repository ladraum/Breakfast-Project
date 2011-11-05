package coffee.core.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import coffee.core.util.Reflection;
import coffee.core.util.Util;

public class JSON {
	
	private Map<Class<? extends Object>, IFieldSerializer> listeners;

	public JSON() {
		listeners = new HashMap<Class<? extends Object>, IFieldSerializer>();
	}

	@SuppressWarnings("unchecked")
	public StringBuilder serialize (Object target) {
		StringBuilder buffer = new StringBuilder();
		String name = target.getClass().getPackage().getName();
		
		if (Collection.class.isInstance(target)) {
			buffer.append(
				serializeArrayOfObjects((Collection<Object>)target));
		}
		else if (listeners.containsKey(target.getClass())) {
			IFieldSerializer fieldSerializer = listeners.get(target.getClass());
			buffer.append(
				fieldSerializer.serialize(target));
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

	public StringBuilder serializeObject(Object target) {
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

	public StringBuilder serializeArrayOfObjects(Collection<?> arrayOfobjects) {
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

	public JSON register(Class<? extends Object> clazz, IFieldSerializer serializer) {
		listeners.put(clazz, serializer);
		return this;
	}

	public void setListeners(Map<Class<? extends Object>, IFieldSerializer> listeners) {
		this.listeners = listeners;
	}

	public Map<Class<? extends Object>, IFieldSerializer> getListeners() {
		return listeners;
	}
}

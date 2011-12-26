package breakfast.coffee.binding;

import java.lang.reflect.Type;

public class DefaultParser implements IParser {

	@Override
	public Object parseValue(Object serializedData, Class<?> type, Type[] genericTypes) {
		if (!String.class.isInstance(serializedData))
			return serializedData;

		if (type.isAssignableFrom(Double.class))
			return Double.parseDouble((String)serializedData);
		else if (type.isAssignableFrom(Float.class))
			return Float.parseFloat((String)serializedData);
		else if (type.isAssignableFrom(Boolean.class))
			return Boolean.parseBoolean((String)serializedData);
		else if (type.isAssignableFrom(Byte.class))
			return Byte.parseByte((String)serializedData);
		else if (type.isAssignableFrom(Integer.class))
			return Integer.parseInt((String)serializedData);
		else if (type.isAssignableFrom(Short.class))
			return Short.parseShort((String)serializedData);

		return serializedData;
	}

	@Override
	public void configure(Object ...context) {}

}

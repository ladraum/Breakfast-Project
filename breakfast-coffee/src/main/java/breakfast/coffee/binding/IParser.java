package breakfast.coffee.binding;

import java.lang.reflect.Type;

public interface IParser {

	void configure(Object ...params);

	Object parseValue(Object value, Class<?> type, Type[] genericTypes);

}

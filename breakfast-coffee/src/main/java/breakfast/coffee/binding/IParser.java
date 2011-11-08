package breakfast.coffee.binding;

import java.lang.reflect.Type;

import breakfast.coffee.CoffeeContext;

public interface IParser {

	void configure(CoffeeContext context);

	Object parseValue(Object value, Class<?> type, Type[] genericTypes);

}

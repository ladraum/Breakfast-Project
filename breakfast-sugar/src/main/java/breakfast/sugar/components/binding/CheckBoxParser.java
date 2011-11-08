package breakfast.sugar.components.binding;

import java.lang.reflect.Type;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.IParser;
import breakfast.coffee.util.StringUtil;

public class CheckBoxParser implements IParser {

	@Override
	public void configure(CoffeeContext component) {}

	@Override
	public Object parseValue(Object value, Class<?> type,
			Type[] genericTypes) {
		return Boolean.parseBoolean(
				!StringUtil.isEmpty((String)value)
					&& value.equals("on") ? "true" : "false");
	}
}

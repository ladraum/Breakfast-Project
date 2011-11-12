package breakfast.sugar.components.binding;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.IParser;
import breakfast.coffee.util.StringUtil;
import breakfast.sugar.components.TextInput;

public class TextInputParser implements IParser {

	private TextInput textInput;

	@Override
	public void configure(CoffeeContext context) {
		textInput = (TextInput)context.get(CoffeeContext.COFFEE_CURRENT_PARSED_COMPONENT);
	}

	@Override
	public Object parseValue(Object value, Class<?> type, Type[] genericTypes) {
		try {
			if (StringUtil.isEmpty((String)value))
				return value;
			return new SimpleDateFormat(textInput.getMask()).parse((String)value);
		} catch (ParseException e) {
			e.printStackTrace();
			return value;
		}
	}

	public void setTextInput(TextInput textInput) {
		this.textInput = textInput;
	}

	public TextInput getTextInput() {
		return textInput;
	}

}

package breakfast.sugar.components;

import java.io.IOException;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.util.Util;
import breakfast.coffee.util.json.JSON;
import breakfast.sugar.Component;


public class ObjectComponent extends Component {

	private String value;

	@Override
	public void configure() {
		
	}

	@Override
	public void render() throws IOException {
		Object value = getValue();

		if (Util.isNull(value) || Util.isNull(getId()))
			return;
		
		getWriter()
			.append("<script>application.registerObject(\"")
				.append(getId())
				.append("\",")
				.append(new JSON().serialize(value))
				.append(");</script>");
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getValue() {
		return CoffeeBinder.getValue(value, getCoffeeContext());
	}
}

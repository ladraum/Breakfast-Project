package coffee.sugar.components;

import java.io.IOException;

import coffee.core.binding.CoffeeBinder;
import coffee.core.util.JSON;
import coffee.core.util.Util;
import coffee.sugar.Component;

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
				.append(JSON.serialize(value))
				.append(");</script>");
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getValue() {
		return CoffeeBinder.getValue(value, getCoffeeContext());
	}
}

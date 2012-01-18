package breakfast.coffee.components.template;

import java.io.IOException;
import java.util.Collection;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;
import breakfast.coffee.util.Util;


public class Foreach extends AbstractComponent {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		String string = getAttribute("var").toString();

		Object collection = CoffeeBinder.getValue(
					getAttribute("value").toString(), getCoffeeContext());

		Collection<?> values = (Collection<?>)collection;
		if (!Util.isNull(values))
			for (Object value : values) {
				coffeeContext.put(string, value);
				for (IComponent child : getChildren()) {
					child.setCoffeeContext(coffeeContext);
					child.render();
				}
			}
	}
}

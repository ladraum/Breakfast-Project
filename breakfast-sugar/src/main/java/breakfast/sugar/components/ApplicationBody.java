package breakfast.sugar.components;

import java.io.IOException;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;


public class ApplicationBody extends AbstractComponent {

	public static final String SUGAR_APPLICATION_BODY = CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + "ApplicationBody";

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		Object definedValue = getCoffeeContext().get(SUGAR_APPLICATION_BODY);

		if (IComponent.class.isInstance(definedValue)) {
			((IComponent)definedValue).render();
			return;
		}
	}

}

package sugar.components;

import java.io.IOException;

import coffee.components.AbstractComponent;
import coffee.components.IComponent;
import coffee.core.CoffeeContext;

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

package coffee.components.template;

import java.io.IOException;

import coffee.components.AbstractComponent;
import coffee.components.IComponent;

public class TemplateComponent extends AbstractComponent {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		for (IComponent child : getChildren()) {
			child.setCoffeeContext(getCoffeeContext());
			child.render();
		}
	}

}

package breakfast.coffee.components.template;

import java.io.IOException;

import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;

public class TemplateBasedComponentChildren extends AbstractComponent {
	
	public static final String CHILDREN = "TemplateBasedComponentChildren.CHILDREN";

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		IComponent children = (IComponent)getCoffeeContext().get(CHILDREN);
		children.setCoffeeContext(getCoffeeContext());
		children.render();
	}

}

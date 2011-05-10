package coffee.components.template;

import coffee.components.AbstractComponentFactory;

/**
 * Core components' factory.
 * 
 * @author Miere Liniel Teixeira
 * @since Coffee 1.0
 */
public class ComponentFactory extends AbstractComponentFactory {

	@Override
	public void configure() {
		register("var", Var.class);
		register("define", Define.class);
		register("template", Template.class);
	}

}

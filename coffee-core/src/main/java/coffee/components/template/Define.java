package coffee.components.template;

import java.io.IOException;

import coffee.binding.CoffeeBinder;
import coffee.components.AbstractComponent;
import coffee.core.CoffeeContext;

public class Define extends AbstractComponent {

	private String name;
	private String value;

	@Override
	public void configure() {
		coffeeContext.put(CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + name, getDefinedValue());
	}

	@Override
	public void render() throws IOException {}
	
	public Object getDefinedValue() {
		if (getNumChildren() == 0)
			return CoffeeBinder.getValue(getValue(), coffeeContext);

		TemplateComponent component = new TemplateComponent();
		component.setCoffeeContext(getCoffeeContext());
		component.setChildren(getChildren());
		return component;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}

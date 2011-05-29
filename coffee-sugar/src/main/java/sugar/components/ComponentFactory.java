package sugar.components;

import coffee.components.AbstractComponentFactory;

public class ComponentFactory extends AbstractComponentFactory {

	@Override
	public void configure() {
		register("ApplicationResources", ApplicationResources.class);
		register("ApplicationBody", ApplicationBody.class);
		register("Application", Application.class);
		register("Button", Button.class);
		register("CheckBox", CheckBox.class);
		register("ComboBox", ComboBox.class);
		register("ComboItem", ComboItem.class);
		register("FormItem", FormItem.class);
		register("Label", Label.class);
		register("RadioGroup", RadioGroup.class);
		register("RadioItem", RadioItem.class);
		register("Script", Script.class);
		register("TextInput", TextInput.class);
	}

}

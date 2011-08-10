package coffee.sugar;

import coffee.core.components.template.TemplateComponentFactory;
import coffee.sugar.components.Application;
import coffee.sugar.components.ApplicationBody;
import coffee.sugar.components.ApplicationResources;
import coffee.sugar.components.Box;
import coffee.sugar.components.Button;
import coffee.sugar.components.ButtonBar;
import coffee.sugar.components.CheckBox;
import coffee.sugar.components.ComboBox;
import coffee.sugar.components.ComboItem;
import coffee.sugar.components.Dialog;
import coffee.sugar.components.FormItem;
import coffee.sugar.components.Grid;
import coffee.sugar.components.GridColumn;
import coffee.sugar.components.Image;
import coffee.sugar.components.ObjectComponent;
import coffee.sugar.components.Text;
import coffee.sugar.components.Panel;
import coffee.sugar.components.RadioGroup;
import coffee.sugar.components.RadioItem;
import coffee.sugar.components.Script;
import coffee.sugar.components.TextInput;
import coffee.sugar.components.Tree;
import coffee.sugar.components.TreeItem;

public class ComponentFactory extends TemplateComponentFactory {

	@Override
	public void configure() {
		super.configure();
		register("ApplicationResources", ApplicationResources.class);
		register("ApplicationBody", ApplicationBody.class);
		register("Application", Application.class);
		register("Box", Box.class);
		register("Button", Button.class);
		register("ButtonBar", ButtonBar.class);
		register("CheckBox", CheckBox.class);
		register("ComboBox", ComboBox.class);
		register("ComboItem", ComboItem.class);
		register("Dialog", Dialog.class);
		register("FormItem", FormItem.class);
		register("Grid", Grid.class);
		register("GridColumn", GridColumn.class);
		register("Image", Image.class);
		register("Object", ObjectComponent.class);
		register("Panel", Panel.class);
		register("RadioGroup", RadioGroup.class);
		register("RadioItem", RadioItem.class);
		register("Script", Script.class);
		register("Text", Text.class);
		register("TextInput", TextInput.class);
		register("Tree", Tree.class);
		register("TreeItem", TreeItem.class);
	}

}

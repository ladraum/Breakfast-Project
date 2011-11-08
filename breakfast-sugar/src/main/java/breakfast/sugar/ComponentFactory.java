package breakfast.sugar;

import breakfast.coffee.components.template.TemplateComponentFactory;
import breakfast.sugar.components.Application;
import breakfast.sugar.components.ApplicationBody;
import breakfast.sugar.components.ApplicationResources;
import breakfast.sugar.components.Box;
import breakfast.sugar.components.Button;
import breakfast.sugar.components.ButtonBar;
import breakfast.sugar.components.CheckBox;
import breakfast.sugar.components.ComboBox;
import breakfast.sugar.components.ComboItem;
import breakfast.sugar.components.Dialog;
import breakfast.sugar.components.FormItem;
import breakfast.sugar.components.Grid;
import breakfast.sugar.components.GridColumn;
import breakfast.sugar.components.Image;
import breakfast.sugar.components.Label;
import breakfast.sugar.components.Link;
import breakfast.sugar.components.ObjectComponent;
import breakfast.sugar.components.Panel;
import breakfast.sugar.components.RadioGroup;
import breakfast.sugar.components.RadioItem;
import breakfast.sugar.components.Script;
import breakfast.sugar.components.Text;
import breakfast.sugar.components.TextArea;
import breakfast.sugar.components.TextInput;
import breakfast.sugar.components.Tree;
import breakfast.sugar.components.TreeItem;

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
		register("Label", Label.class);
		register("Link", Link.class);
		register("Object", ObjectComponent.class);
		register("Panel", Panel.class);
		register("RadioGroup", RadioGroup.class);
		register("RadioItem", RadioItem.class);
		register("Script", Script.class);
		register("Text", Text.class);
		register("TextArea", TextArea.class);
		register("TextInput", TextInput.class);
		register("Tree", Tree.class);
		register("TreeItem", TreeItem.class);
	}

}

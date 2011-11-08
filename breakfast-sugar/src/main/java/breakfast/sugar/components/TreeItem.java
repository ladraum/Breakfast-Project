package breakfast.sugar.components;

import java.io.IOException;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;


/**
 * Represents a Tree Item Component. It's entirely handled by the
 * Tree component and it's data hold by AbstractComponent super class.
 * 
 * @author Miere Liniel Teixeira
 */
public class TreeItem extends AbstractComponent {
	
	private String id;
	private String label;
	private String parentId;
	private String hasChildren;

	@Override
	public void configure() {
	}

	@Override
	public void render() throws IOException {
	}

	public IComponent setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return CoffeeBinder.getValue(
				id, coffeeContext).toString();
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return CoffeeBinder.getValue(
				label, coffeeContext).toString();
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return CoffeeBinder.getValue(
				parentId, coffeeContext).toString();
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getHasChildren() {
		return CoffeeBinder.getValue(
				hasChildren, coffeeContext).toString();
	}

}

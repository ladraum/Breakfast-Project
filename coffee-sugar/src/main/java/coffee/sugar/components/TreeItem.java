package coffee.sugar.components;

import java.io.IOException;

import coffee.core.binding.CoffeeBinder;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;

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
		this.id = CoffeeBinder.getValue(
							id, coffeeContext).toString();
		return this;
	}

	public String getId() {
		return id;
	}

	public void setLabel(String label) {
		this.label = CoffeeBinder.getValue(
							label, coffeeContext).toString();
	}

	public String getLabel() {
		return label;
	}

	public void setParentId(String parentId) {
		this.parentId = CoffeeBinder.getValue(
							parentId, coffeeContext).toString();
	}

	public String getParentId() {
		return parentId;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = CoffeeBinder.getValue(
				hasChildren, coffeeContext).toString();
	}

	public String getHasChildren() {
		return hasChildren;
	}

}

package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import coffee.core.binding.CoffeeBinder;
import coffee.core.components.IComponent;
import coffee.core.util.JSON;
import coffee.core.util.Util;
import coffee.sugar.Widget;

public class Grid extends Widget {
	
	private boolean multipleSelection;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<div id=\"")
			.append(getId())
			.append("\" class=\"Grid\" style=\"")
			.append(getStyleDefinition())
			.append("\"></div>")

			.append("<script>application.addChild(new Grid( {label:\"")
				.append(getAttributeValue("label"))
			.append("\",id:\"")
				.append(getId())
			.append("\",visible:")
				.append(isVisible().toString())
			.append(",multipleSelection:")
				.append(isMultipleSelection().toString());

		if (!Util.isNull(getHeight()))
			writer.append(",height:\"")
				  .append(getHeight())
				  .append("\"");

		if (!Util.isNull(getMinHeight()))
			writer.append(",minHeight:\"")
				  .append(getMinHeight())
				  .append("\"");

		if (!Util.isNull(getMinWidth()))
			writer.append(",minWidth:\"")
				  .append(getMinWidth())
				  .append("\"");

		if (!Util.isNull(getWidth()))
			writer.append(",width:\"")
				  .append(getWidth())
				  .append("\"");

		renderColumns(writer);
		renderValue(writer);

		writer.append("}));</script>");
	}

	@SuppressWarnings("unchecked")
	public void renderValue (PrintWriter writer)
	{
		Object collectionOfValues = CoffeeBinder.getValue(getValue(), getCoffeeContext());
		if ( Util.isNull(collectionOfValues) )
			return;

		Collection<? extends Object> value = (Collection<? extends Object>) collectionOfValues;
		writer
			.append(",value:")
			.append(JSON.serializeArrayOfObjects(value));
	}

	public void renderColumns(PrintWriter buffer) {
		buffer.append(",columns:[");
		int counter = 0;
		int attributeCounter = 0;
		for (IComponent child : getChildren()){
			if (!GridColumn.class.isInstance(child))
				continue;
			GridColumn column = (GridColumn)child;

			if (counter > 0)
				buffer.append(',');
			counter++;

			buffer.append('{');
			
			attributeCounter = 0;
			for (String key : column.getAttributeKeys()) {
				String attribute = column.getAttributeValue(key);
				if (!Util.isNull(attribute)) {
					if (attributeCounter > 0)
						buffer.append(',');
					buffer
						.append(key)
						.append(":\"")
						.append(attribute)
						.append('"');
				}
				attributeCounter++;
			}
			buffer.append("}");
		}
		buffer.append("]");
	}

	public void setMultipleSelection(String multipleSelection) {
		this.multipleSelection = Boolean.parseBoolean(multipleSelection);
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public Boolean isMultipleSelection() {
		return multipleSelection;
	}
}

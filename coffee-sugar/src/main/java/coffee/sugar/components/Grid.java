package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import coffee.core.binding.CoffeeBinder;
import coffee.core.components.IComponent;
import coffee.core.util.Util;
import coffee.sugar.Widget;
import coffee.sugar.helper.GridUtils;

public class Grid extends Widget {

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
			.append("\",");

		if (!Util.isNull(getHeight()))
			writer.append("height:\"")
				  .append(getHeight())
				  .append("\",");

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

		writer.append("value:[");
		for (Object item : value) {
			try {
				StringBuilder serializedItem = GridUtils.serializeItem(item);
				writer.append(serializedItem).append(',');
			} catch (Exception e) {}
		}
		writer.append("],");
	}

	public void renderColumns(PrintWriter buffer) {
		buffer.append("columns:[");
		for (IComponent child : getChildren()){
			if (!GridColumn.class.isInstance(child))
				continue;
			GridColumn column = (GridColumn)child;
			buffer.append('{');
			
			for (String key : column.getAttributeKeys()) {
				String attribute = column.getAttributeValue(key);
				if (!Util.isNull(attribute))
					buffer
						.append(key)
						.append(":\"")
						.append(attribute)
						.append("\",");
			}

			buffer.append("},");
		}
		buffer.append("],");
	}
}

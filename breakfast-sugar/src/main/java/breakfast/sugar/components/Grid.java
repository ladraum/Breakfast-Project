package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.components.IComponent;
import breakfast.coffee.util.Reflection;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;
import breakfast.sugar.Widget;

public class Grid extends Widget {

	private boolean multipleSelection;
	private String dateMask;
	private String timeMask;
	private String timestampMask;
	
	private List<GridColumn> columns;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<div id=\"")
			.append(getId())
			.append("\" class=\"Grid ")
				.append(getSkin())
			.append("\" style=\"")
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

		Collection<? extends Object> values = (Collection<? extends Object>) collectionOfValues;
		writer.append(",value:[");

		boolean isFirstValue = true;
		for (Object target : values) {
			
			if (!isFirstValue)
				writer.append(',');
			isFirstValue = false;
			
			writer.append("{");
			
			Field[] fields = target.getClass().getDeclaredFields();
			boolean isFirst = true;
			for (Field field : fields) {
				try {
					Method getter = Reflection.extractGetterFor(field.getName(), target);
					Object object = getter.invoke(target);
					
					if (Util.isNull(object))
						continue;
	
					if (!isFirst)
						writer.append(',');
					isFirst = false;

					writer.append(serializeColumnValue(field.getName(), object));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			writer.append("}");
		}
		
		writer.append(']');
	}

	public String serializeColumnValue (String columnId, Object value) {
		StringBuilder buffer = new StringBuilder();

		String columnType = getColumnType(columnId);
		if (Util.isNull(columnType))
			columnType = "";

		buffer
			.append('"')
			.append(columnId)
			.append("\":\"");

		if (columnType.equals("time") && Date.class.isInstance(value))
			buffer.append(new SimpleDateFormat(getTimeMask()).format(value));
		else if (columnType.equals("date") && Date.class.isInstance(value))
			buffer.append(new SimpleDateFormat(getDateMask()).format(value));
		else if (columnType.equals("timestamp") && Date.class.isInstance(value))
			buffer.append(new SimpleDateFormat(getTimestampMask()).format(value));
		else if (String.class.isInstance(value))
			buffer.append(StringUtil.escape(value.toString()));
		else
			buffer.append(value.toString());

		return buffer
				.append('"')
				.toString();
	}

	public void renderColumns(PrintWriter buffer) {
		buffer.append(",columns:[");
		int counter = 0;
		int attributeCounter = 0;
		for (GridColumn column : getColumns()){
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

	public void setDateMask(String dateMask) {
		this.dateMask = dateMask;
	}

	public String getDateMask() {
		return dateMask;
	}

	public void setTimestampMask(String timestampMask) {
		this.timestampMask = timestampMask;
	}

	public String getTimestampMask() {
		return timestampMask;
	}

	public void setTimeMask(String timeMask) {
		this.timeMask = timeMask;
	}

	public String getTimeMask() {
		return timeMask;
	}

	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}

	public List<GridColumn> getColumns() {
		if (Util.isNull(columns)) {
			columns = new ArrayList<GridColumn>();
			for (IComponent child : getChildren()){
				if (!GridColumn.class.isInstance(child))
					continue;
				columns.add((GridColumn)child);
			}
		}
		return columns;
	}

	public String getColumnType(String id) {
		for (GridColumn column : getColumns()) {
			if (column.getId().equals(id))
				return column.getAttributeValue("type");
		}
		return null;
	}
}

package breakfast.sugar.components.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.DefaultParser;
import breakfast.coffee.binding.IParser;
import breakfast.coffee.util.Reflection;
import breakfast.coffee.util.StringUtil;
import breakfast.sugar.components.Grid;

public class GridParser implements IParser {
	
	private Grid grid;

	@Override
	public void configure(CoffeeContext context) {
		grid = (Grid)context.get(CoffeeContext.COFFEE_CURRENT_PARSED_COMPONENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parseValue(Object serializedData, Class<?> type, Type[] genericTypes) {
		try {
			
			Collection<Object> gridData = (Collection<Object>)type.newInstance();

			for (String line : ((String)serializedData).split("\\?")) {
				if (StringUtil.isEmpty(line))
					continue;
				Object instance = ((Class<?>)genericTypes[0]).newInstance();
				tupledata:
					for (String tuple : line.split("&")) {
						String[] tupleValues = tuple.split("=");
						if (tupleValues.length < 2)
							continue tupledata;
						
						Object value = parseColumnValue(tupleValues[0], instance, tupleValues[1]);
						Method setter = Reflection.extractSetterFor(tupleValues[0], instance, value);
						setter.invoke(instance, value);
					}
				gridData.add(instance);
			}
			return gridData;
		} catch (Exception e) {
			e.printStackTrace();
			return serializedData;
		}
	}

	public Object parseColumnValue(String attr, Object target, String value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
				   ParseException, SecurityException, NoSuchFieldException
   {
		String columnType = grid.getColumnType(attr);
		if (StringUtil.isEmpty(columnType))
			columnType = "default";

		if (columnType.equals("time"))
			return new SimpleDateFormat(grid.getTimeMask()).parse(value);
		else if (columnType.equals("date"))
			return new SimpleDateFormat(grid.getDateMask()).parse(value);
		else if (columnType.equals("timestamp"))
			return new SimpleDateFormat(grid.getTimestampMask()).parse(value);

		Class<?> returnTypeFor = Reflection.extractReturnTypeFor(attr, target);
		Type[] genericTypes = new Type[]{};

		return new DefaultParser().parseValue(value, returnTypeFor, genericTypes);
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Grid getGrid() {
		return grid;
	}

}

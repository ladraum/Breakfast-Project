package coffee.binding;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coffee.util.Reflection;
import coffee.util.StringUtil;

public class Evaluator {

	public static final String RE_IS_VALID_BINDABLE_EXPRESSION = "^#\\{+?\\w+?\\.[\\w\\.]+?\\}$";
	public static final String RE_IS_VALID_RETRIEVABLE_EXPRESSION = "#\\{\\w+?[\\w\\.]+?\\}";
	public static final String RE_IS_VALID_SINGLE_RETRIEVABLE_EXPRESSION = "^#\\{\\w+?[\\w\\.]+?\\}$";
	public static final String RE_FIND_ATTR = "\\w[\\w\\d]*";

	private Object target;
	private String expression;

	public Evaluator() {
	}

	public Evaluator(Object target, String expression) {
		this.setTarget(target);
		this.setExpression(expression);
	}

	public static boolean isValidBindExpression(String expression) {
		if (StringUtil.isEmpty(expression))
			return false;
		return expression.matches(RE_IS_VALID_BINDABLE_EXPRESSION);
	}

	public static boolean isValidRetrievableExpression(String expression) {
		if (StringUtil.isEmpty(expression))
			return false;
		return expression.matches(RE_IS_VALID_RETRIEVABLE_EXPRESSION);
	}

	public static String extractObjectPlaceholder(String expression) {
		Matcher matcher = Pattern.compile("\\w+").matcher(expression);
		if (matcher.find())
			return matcher.group();
		return null;
	}

/**
 * 
 * @return
 */
	public Object getValue() {
		if (!isValidRetrievableExpression(expression))
			return null;

		Matcher matcher = Pattern.compile(Evaluator.RE_FIND_ATTR).matcher(expression);

		if (!matcher.find())
			return null;

		if (!matcher.find())
			return this.target;

		return getValue(this.target, matcher);
	}

	public Object getValue(Object target, Matcher matcher) {
		String attribute = matcher.group();
		try {
			Method method = Reflection.extractGetterFor(attribute, target);
			Object object = method.invoke(target);
			if (matcher.find())
				getValue(object, matcher);
			return object;
		} catch (Exception e) {
			return null;
		}
	}

	public void setValue(Object value) {
		if (!isValidBindExpression(expression))
			return;

		Matcher matcher = Pattern.compile(Evaluator.RE_FIND_ATTR).matcher(expression);

		if (!matcher.find() || !matcher.find())
			return;

		this.setValue(target, matcher, value);
	}

	public void setValue(Object target, Matcher matcher, Object value) {
		String attribute = matcher.group();
		try {
			if (!matcher.find()) {
				Method setter = Reflection.extractSetterFor(attribute, target, value);
				setter.invoke(target, value);
			} else {
				Method method = Reflection.extractGetterFor(attribute, target);
				target = method.invoke(target);
				setValue(target, matcher, value);
			}
		} catch (Exception e) {
			System.out.println("PUTS, NÂO SETOU VALOR: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	public static Evaluator eval(Object target, String expression) {
		return new Evaluator(target, expression);
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object getTarget() {
		return target;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}
}

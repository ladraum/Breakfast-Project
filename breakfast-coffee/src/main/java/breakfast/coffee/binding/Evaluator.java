/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package breakfast.coffee.binding;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.annotation.Parser;
import breakfast.coffee.util.Reflection;
import breakfast.coffee.util.StringUtil;


public class Evaluator {

	public static final String RE_IS_VALID_BINDABLE_EXPRESSION = "^#\\{+?\\w+?\\.[\\w\\.]+?\\}$";
	public static final String RE_IS_VALID_RETRIEVABLE_EXPRESSION = "#\\{\\w+?[\\w\\.]+?\\}";
	public static final String RE_IS_VALID_SINGLE_RETRIEVABLE_EXPRESSION = "^#\\{\\w+?[\\w\\.]+?\\}$";
	public static final String RE_FIND_ATTR = "\\w[\\w\\d]*";

	private Object target;
	private String expression;
	private CoffeeContext context;

	public Evaluator() {
	}

	public Evaluator(Object target, String expression) {
		this.setTarget(target);
		this.setExpression(expression);
	}

	public Evaluator(Object target, String expression, CoffeeContext context) {
		this.setTarget(target);
		this.setExpression(expression);
		this.setContext(context);
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

		Object result = getValue(this.target, matcher);

		if (String.class.isInstance(result))
			result = StringUtil.escape((String)result);

		return result;
	}

	public Object getValue(Object target, Matcher matcher) {
		String attribute = matcher.group();
		try {
			Method method = Reflection.extractGetterFor(attribute, target);
			Object object = method.invoke(target);
			if (matcher.find())
				return getValue(object, matcher);
			return object;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public boolean setValue(Object value) {
		if (!isValidBindExpression(expression))
			return false;

		Matcher matcher = Pattern.compile(Evaluator.RE_FIND_ATTR).matcher(expression);

		if (!matcher.find() || !matcher.find())
			return false;

		return this.setValue(target, matcher, value);
	}

	public boolean setValue(Object target, Matcher matcher, Object value) {
		String attribute = matcher.group();
		try {
			if (!matcher.find()) {
				value = tryToParseValue(attribute, target, value);
				Method setter = Reflection.extractSetterFor(attribute, target, value);
				setter.invoke(target, value);
				return true;
			} else {
				Method method = Reflection.extractGetterFor(attribute, target);
				target = method.invoke(target);
				return setValue(target, matcher, value);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("WARN: " + e.getMessage());
			return false;
		}
	}

	public Object tryToParseValue(String attribute, Object target, Object value)
			throws NoSuchFieldException, InstantiationException,
			IllegalAccessException {
		Parser parserData = (Parser)Reflection.extractAnnotationFor(attribute, target, Parser.class);
		Class<?> type = Reflection.extractReturnTypeFor(attribute, target);
		Type[] genericTypes = Reflection.extractGenericReturnTypeFor(attribute, target);

		IParser parser = null;
		if (parserData != null) {
			Class<? extends IParser> parserClazz = parserData.value();
			parser = parserClazz.newInstance();
		} else
			parser = new DefaultParser();

		parser.configure(context);
		return parser.parseValue(value, type, genericTypes);
	}

	public static Evaluator eval(Object target, String expression, CoffeeContext context) {
		return new Evaluator(target, expression, context);
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

	public void setContext(CoffeeContext context) {
		this.context = context;
	}

	public CoffeeContext getContext() {
		return context;
	}
}

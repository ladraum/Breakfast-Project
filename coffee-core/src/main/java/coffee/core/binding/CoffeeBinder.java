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
package coffee.core.binding;

import static coffee.core.util.Util.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coffee.core.CoffeeContext;
//import coffee.core.util.StringUtil;
import coffee.core.util.Util;

public class CoffeeBinder {
	
	private static Map<String, Pattern> patternCache;

	public static void setValue (String expression, CoffeeContext coffeeContext, Object value) {
		if (!Evaluator.isValidBindExpression(expression) || Util.isNull(value))
			return;

		String placeholder = Evaluator.extractObjectPlaceholder(expression);
		Object target = coffeeContext.get(placeholder);

		if (Util.isNull(target))
			return;
		
		/* TODO: improve the security by parsing special characters
		 * if (String.class.isInstance(value))
			value = StringUtil.escape((String)value);*/

		Evaluator.eval(target, expression).setValue(value);
	}

	public static Object getValue(String expression, CoffeeContext context) {
		if (Util.isNull(expression))
			return null;

		Matcher matcher = getMatcher(Evaluator.RE_IS_VALID_SINGLE_RETRIEVABLE_EXPRESSION, expression);
		if (matcher.find())
			return evaluateExpressionAsObject(context, matcher);
		return evaluateExpressionAsString(expression, context);
	}

	public static String evaluateExpressionAsString(String expression, CoffeeContext context) {
		ArrayList<String> matchedGroups = new ArrayList<String>();

		Matcher matcher = getMatcher(Evaluator
				.RE_IS_VALID_RETRIEVABLE_EXPRESSION, expression);

		while (matcher.find()) {
			String group = matcher.group();
			if (matchedGroups.contains(group))
				continue;

			Object evaluatedExpression = evaluateExpressionAsObject(context, matcher);
			if (isNull(evaluatedExpression))
				continue;
			expression =  expression.replace(group, evaluatedExpression.toString());
			matchedGroups.add(group);
		}

		return expression;
	}

	public static Object evaluateExpressionAsObject(CoffeeContext context, Matcher matcher) {
		String nextExpression = matcher.group();
		String placeholder = Evaluator.extractObjectPlaceholder(nextExpression);
		Object targetObject = context.get(placeholder);

		if (isNull(targetObject))
			return null;

		Evaluator evaluator = new Evaluator(targetObject, nextExpression);
		return evaluator.getValue();
	}

	public static Matcher getMatcher(String expression, String string) {
		if (Util.isNull(patternCache))
			patternCache = new HashMap<String, Pattern>();
		
		Pattern pattern = patternCache.get(expression);
		if (Util.isNull(pattern)) {
			pattern = Pattern.compile(expression);
			patternCache.put(expression, pattern);
		}
		return pattern.matcher(string);
	}
}

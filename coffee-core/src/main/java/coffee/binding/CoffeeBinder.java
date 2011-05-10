package coffee.binding;

import static coffee.util.Util.isNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coffee.core.CoffeeContext;
import coffee.util.Util;

// TODO: create a pooled matcher
public class CoffeeBinder {
	
	private static Map<String, Pattern> patternCache;

	public static void setValue (String expression, CoffeeContext coffeeContext, Object value) {
		if (!Evaluator.isValidBindExpression(expression) || Util.isNull(value))
			return;

		String placeholder = Evaluator.extractObjectPlaceholder(expression);
		Object target = coffeeContext.get(placeholder);

		if (Util.isNull(target))
			return;

		Evaluator.eval(target, expression).setValue(value);
	}

	public static Object getValue(String expression, CoffeeContext context) {
		Matcher matcher = getMatcher(Evaluator.RE_IS_VALID_SINGLE_RETRIEVABLE_EXPRESSION, expression);
		if (matcher.find())
			return evaluateExpressionAsObject(context, matcher);
		return evaluateExpressionAsString(expression, context);
	}

	public static String evaluateExpressionAsString(String expression, CoffeeContext context) {
		Matcher matcher = getMatcher(Evaluator
				.RE_IS_VALID_RETRIEVABLE_EXPRESSION, expression);
		/*Pattern.compile(Evaluator
								.RE_IS_VALID_RETRIEVABLE_EXPRESSION).matcher(expression)*/;

		while (matcher.find()) {
			Object evaluatedExpression = evaluateExpressionAsObject(context, matcher);
			if (isNull(evaluatedExpression))
				return expression;
			expression = matcher.replaceFirst(evaluatedExpression.toString());
		}
		
		return expression;
	}

	public static Object evaluateExpressionAsObject(CoffeeContext context, Matcher matcher) {
		String nextExpression = matcher.group();
		String placeholder = Evaluator.extractObjectPlaceholder(nextExpression);
		Object targetObject = context.get(placeholder);

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
//		return Pattern.compile(String.format("^%s$", Evaluator
//				.RE_IS_VALID_RETRIEVABLE_EXPRESSION)).matcher(string);
	}
}

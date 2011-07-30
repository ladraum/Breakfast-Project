package coffee.sugar.helper;

import coffee.core.util.StringUtil;

public class CheckBoxUtil {

	public static Boolean parseBoolean(String value) {
		return Boolean.parseBoolean(
				!StringUtil.isEmpty(value)
					&& value.equals("on") ? "true" : "false");
	}
}

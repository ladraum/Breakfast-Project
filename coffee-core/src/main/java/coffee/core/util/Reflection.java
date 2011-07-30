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
package coffee.core.util;

import java.lang.reflect.Method;

public class Reflection {

	/**
	 * @param attribute
	 * @param target
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method extractGetterFor(String attribute, Object target)
			throws SecurityException, NoSuchMethodException {
		String getter = String.format("get%s%s",
				attribute.substring(0, 1).toUpperCase(),
				attribute.substring(1));
		try {
			return target.getClass().getMethod(getter);
		} catch (NoSuchMethodException e) {
			return target.getClass().getMethod(getter.replaceFirst("get", "is"));
		}
	}

	/**
	 * @param attribute
	 * @param target
	 * @param value
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method extractSetterFor(String attribute, Object target,
			Object value) throws SecurityException, NoSuchMethodException {
		String setter = String.format("set%s%s",
				attribute.substring(0, 1).toUpperCase(),
				attribute.substring(1));
		return target.getClass().getMethod(setter, value.getClass());
	}
}

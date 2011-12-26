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
package breakfast.coffee.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import breakfast.coffee.annotation.Parser;
import breakfast.coffee.binding.DefaultParser;
import breakfast.coffee.binding.IParser;

public class Reflection {

	public static Object parseFieldValue(String attribute, Object target, Object value, Object ...optionalParams)
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

		parser.configure(optionalParams);
		return parser.parseValue(value, type, genericTypes);
	}

	public static Annotation[] extractAnnotationsFor(String attribute, Object target)
			throws SecurityException, NoSuchFieldException {
		Field field = extractFieldFor(target, attribute);

		if (Util.isNull(field))
			return new Annotation[]{};

		return field.getAnnotations();
	}

	public static Annotation extractAnnotationFor(String attribute, Object target, Class<? extends Annotation> annotation)
			throws SecurityException {
		Field field = extractFieldFor(target, attribute);

		if (Util.isNull(field))
			return null;

		return field.getAnnotation(annotation);
	}

	public static Field extractFieldFor (Object target, String attribute) {
		Class<? extends Object> clazz = target.getClass();
		return extractFieldFor(attribute, clazz);
	}

	public static Field extractFieldFor (String attribute, Class<?> clazz) {
		try {
			if (clazz.equals(Object.class))
				return null;
			return clazz.getDeclaredField(attribute);
		} catch (NoSuchFieldException e) {
			return extractFieldFor(attribute, clazz.getSuperclass());
		}
	}

	public static Class<?> extractReturnTypeFor (String attribute, Object target) 
			throws SecurityException, NoSuchFieldException {
		Field field = extractFieldFor(target, attribute);
		if (Util.isNull(field))
			return null;
		return field.getType();
	}

	public static Type[] extractGenericReturnTypeFor(String attribute, Object target)
			throws SecurityException {
		Field field = extractFieldFor(target, attribute);
		if (Util.isNull(field))
			return new Type[]{};
		Type type = field.getGenericType();
		if (!(type instanceof ParameterizedType))
			return new Type[]{};
		ParameterizedType ptype = (ParameterizedType) type;
		return ptype.getActualTypeArguments();
	}

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
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException 
	 */
	public static Method extractSetterFor(String attribute, Object target) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		String setter = String.format("set%s%s",
				attribute.substring(0, 1).toUpperCase(),
				attribute.substring(1));
		Class<?> returnTypeFor = extractReturnTypeFor(attribute, target);
		return target.getClass().getMethod(setter, returnTypeFor);
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

	/**
	 * Instantiate the className
	 * @param className
	 * @return
	 */
	public static Object instanceForName (String className) {
		try {
			Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

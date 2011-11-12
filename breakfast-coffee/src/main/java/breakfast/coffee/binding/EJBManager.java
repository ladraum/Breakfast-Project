package breakfast.coffee.binding;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import breakfast.coffee.util.Reflection;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;

/**
 * <p>Utility class that injects {@link EJB} in
 * non managed helper classes that have the 
 * <b>EJB</b> annotation in one or more fields.</p>
 * 
 * <p><b>Note:</b> A pair of <b>getters</b> and
 * <b>setters</b> must be implemented for each
 * annotated variable in the class.</p>
 * 
 * {@link EJBManager}
 */
public class EJBManager {

	private static Context context = null;

	private static Context getInitialContext() throws NamingException {
		if (null == context) {
			context = new InitialContext();
		}
		return context;
	}

	/**
	 * <p>Search for each declared field in the
	 * object and call the {@code ManagedBean.injectEJB()} </b> method.</p>
	 * 
	 * @param bean
	 */
	public static void manageBean(Object bean) {
		try {
			for (Field field : bean.getClass().getDeclaredFields()) {
				injectEJB(field, bean);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>Searches for the {@link EJB} annotation in
	 * the supplied field. If found, try to locate
	 * the <b>EJB</b> <b>no-interface</b> or 
	 * <b>local</b> in the JNDI context. After doing so,
	 * via reflection executes the bean <b>setter</b> and
	 * <b>getter</b> methods, injecting the <b>EJB</b> in
	 * the non managed class.</p>
	 * 
	 * @param field
	 * @param bean
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NamingException
	 * @throws InstantiationException
	 */
	public static void injectEJB(Field field, Object bean)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NamingException, InstantiationException {
		
		Context context = getInitialContext();
		
		Class<?> clazz = field.getType();
		EJB annotation = field.getAnnotation(EJB.class);
		if (Util.isNull(annotation))
			return;

		String lookupName = annotation.name();
		if (StringUtil.isEmpty(lookupName))
			if (!clazz.isInterface())
				lookupName += clazz.getSimpleName() + "/no-interface";
			else
				lookupName += clazz.getSimpleName() + "/local";

		Method getter = Reflection.extractGetterFor(field.getName(), bean);
		if (!Util.isNull(getter.invoke(bean)))
			return;

		Object object = context.lookup(lookupName);
		Method setter = Reflection.extractSetterFor(field.getName(), bean,
				clazz.newInstance());
		setter.invoke(bean, object);
	}
}

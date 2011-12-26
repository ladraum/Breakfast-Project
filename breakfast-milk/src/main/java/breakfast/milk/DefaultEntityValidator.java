package breakfast.milk;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import breakfast.coffee.util.Reflection;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;

public class DefaultEntityValidator<E> implements IValidator<E> {
	
	public static final String MSG_REQUIRED_FIELD_IS_EMPTY = "MSG_REQUIRED_FIELD_IS_EMPTY:";

	@Override
	public void validate(E entity) throws PersistenceException {

		Class<? extends Object> clazz = entity.getClass();
		while (!clazz.equals(Object.class)) {
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				try {
					String fieldName = field.getName();
					Method getter = Reflection.extractGetterFor(fieldName, entity);
					Object object = getter.invoke(entity);

					Validation validation = (Validation)Reflection.extractAnnotationFor(fieldName, entity, Validation.class);
					if (validation.required() && Util.isNull(object))
						throw new PersistenceException(MSG_REQUIRED_FIELD_IS_EMPTY + fieldName);

					else if (String.class.isAssignableFrom(field.getDeclaringClass())
						 && !StringUtil.isEmpty(validation.mask())
						 && !((String)object).matches(validation.mask()))
						throw new PersistenceException(MSG_REQUIRED_FIELD_IS_EMPTY + fieldName);
	
				} catch (PersistenceException e) {
					throw new PersistenceException(e);
				} catch (Exception e) {
					// Ignoring reflection exceptions as its failures are treated at try block above.
				}
			}

			clazz = clazz.getSuperclass();
		}
	}

}

package coffee.core;

/**
 * 
 * @author Miere Liniel Teixeira
 * @since Coffee 1.0
 */
public class CoffeeResource {

	private String pattern;
	private String template;
	private Class<?> clazz;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
/**
 * The resource hook factory. 
 * @param context
 * @return
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 */
	public IResource instantiateHook(CoffeeContext context) throws InstantiationException, IllegalAccessException {
		IResource hook = (IResource)clazz.newInstance();
		hook.configure(context);
		return hook;
	}
}

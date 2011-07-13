package coffee.core.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSystemResourceLoader {

	private ClassLoader classLoader;
	
	public AbstractSystemResourceLoader() {}

	public List<Class<?>> retrieveAvailableClasses () throws IOException, ClassNotFoundException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		List<String> resources = retrieveAvailableResources ();

		for (String resource : resources) {
			if (!resource.endsWith(".class") || resource.contains("$"))
				continue;
			try {
				Class<?> clazz = getClassLoader().loadClass(
						normalizeClassName(resource));
				classes.add(clazz);
			} catch (Throwable e) {
				// Ignoring NoClassDefFound Exception
				// e.printStackTrace();
			}
		}

		return classes;
	}

	public abstract List<String> retrieveAvailableResources() throws IOException;

	public String normalizeClassName (String className) {
		return className
				.replace("/WEB-INF/classes/", "")
				.replace(".class", "")
				.replaceAll("^/+", "")
				.replace("/", ".");
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

}

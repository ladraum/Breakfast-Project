package coffee.core.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;

import coffee.core.util.Util;

public class ServletContextResourceLoader extends AbstractSystemResourceLoader {

	private ServletContext servletContext;
	
	public ServletContextResourceLoader(
			ServletContext servletContext,
			ClassLoader classLoader) {
		setClassLoader(classLoader);
		setServletContext(servletContext);
	}

	public List<String> retrieveAvailableResources () {
		if (Util.isNull(servletContext))
			return new ArrayList<String>();
		return retrieveAvailableResources("/");
	}

	public List<String> retrieveAvailableResources (String root) {
		ArrayList<String> list = new ArrayList<String>();
		if (Util.isNull(servletContext))
			return list;

		Set<String> resourcePaths = servletContext.getResourcePaths(root);
		if (Util.isNull(resourcePaths))
			return list;

		for (String path : resourcePaths) {
		    try {
				if (path.endsWith(".jar")) {
					List<String> jarResources = retrieveResourcesFromJar(path);
					if (!Util.isNull(jarResources))
						list.addAll(jarResources);
				} else if (path.endsWith("/")) {
					List<String> resources = retrieveAvailableResources(path);
					if (!Util.isNull(resources))
						list.addAll(resources);
		    	} else
		    		list.add(path);
		    } catch (MalformedURLException e) {
	    		throw new RuntimeException(e);
		    } catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<String> retrieveResourcesFromJar(String jarName) throws IOException,
			ClassNotFoundException {
		ArrayList<String> list = new ArrayList<String>();

		JarInputStream stream = new JarInputStream
			(servletContext.getResourceAsStream(jarName));

		while(true) {
			JarEntry entry = stream.getNextJarEntry();
			if (!Util.isNull(entry))
				break;
			list.add(entry.getName());
		}

		return list;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}

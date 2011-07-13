/**
 * Based on Tornado's way of registering web resources.
 * Thanks to Richard Kuesters from his suggestion that makes my job easier.
 * Thanks to Victor Tatai from his collaboration at http://snippets.dzone.com/posts/show/4831
 * Thanks to Bill Burke from his his enlightening content at 
 *   http://bill.burkecentral.com/2008/01/14/scanning-java-annotations-at-runtime/
 * 
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
package coffee.core.loader;

import static coffee.core.util.Util.isNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.core.CoffeeContext;
import coffee.core.CoffeeParser;
import coffee.core.CoffeeResource;
import coffee.core.annotation.WebResource;
import coffee.core.components.IComponent;
import coffee.core.util.Util;

public class CoffeeResourceLoader {

	private static CoffeeResourceLoader instance;

	private Map<String, CoffeeResource> registeredResources;
	private HashMap<String, IComponent> resourceCache;
	
	private ServletContext servletContext;
	private boolean cacheEnabled;
	private Boolean populated = false;

	private AbstractSystemResourceLoader[] resourceLoaders;

	private CoffeeResourceLoader() {
		registeredResources = new HashMap<String,CoffeeResource>();
		resourceCache = new HashMap<String, IComponent>();
		cacheEnabled = true;
	}

/**
 * Compiles the resource. For performance improvements it stores earlier compiled
 * resources in a cached factory of components.
 * 
 * @param teplateName
 * @param context 
 * @return IComponent implementation of compiled template
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws CloneNotSupportedException
 */
	public IComponent compile(String templateName, CoffeeContext context) throws IOException,
						ParserConfigurationException, SAXException, CloneNotSupportedException {
		if (isCacheEnabled() && resourceCache.containsKey(templateName))
			return (IComponent)resourceCache.get(templateName).clone(context);

		InputStream template = loadTemplate(templateName);
		if (Util.isNull(template))
			throw new IOException("Can't find template '" + templateName + "'.");

		CoffeeParser parser = new CoffeeParser();
		parser.setContext(context);
		IComponent application = parser.parse(template);
		resourceCache.put(templateName, application);

		return application;
	}

/**
 * Scans all classes accessible from the context class loader which belong to the given package and sub-packages.
 * Based on this Victor's snippet code at http://snippets.dzone.com/posts/show/4831.
 *
 * @param packageName The base package
 * @return The classes
 * @throws ClassNotFoundException
 * @throws IOException
 */
	public void initialize() throws IOException, ClassNotFoundException
    {
		synchronized (populated) {
			if (populated) return;

			resourceLoaders = new AbstractSystemResourceLoader[] {
				new ServletContextResourceLoader(getServletContext(), getClassLoader()),
				new ClassPathResourceLoader(getClassLoader())
			};

			for (AbstractSystemResourceLoader loader : resourceLoaders) {
				for (Class<?> clazz : loader.retrieveAvailableClasses())
					registerResource(clazz);
			}

			populated = true;
		}
    }

	public void registerResource(Class<?> clazz) {
		WebResource webresource = clazz.getAnnotation(WebResource.class);
		if (Util.isNull(webresource))
			return;
		
		String pattern = webresource.pattern();

		CoffeeResource data = new CoffeeResource();
		data.setClazz(clazz);
		data.setPattern(pattern);
		data.setTemplate(webresource.template());

		System.out.println("Registered Resource: " + clazz.getCanonicalName());
		registeredResources.put(pattern, data);
	}

/**
 * Loads a template as InputStream from current class path. The requested resource
 * will be cache for future use.
 * 
 * @param path
 * @return
 * @throws IOException
 */
	public InputStream loadTemplate(String path) throws IOException {
		return getResourceAsStream(path);
	}

/**
 * Instantiate the className
 * @param className
 * @return
 */
	public Object instanceForName (String className) {
		try {
			Class<?> clazz = getClassLoader().loadClass(className);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

/**
 * Retrieves dynamically a resource as Stream from a resourceName.
 * 
 * @param resourceName
 * @return
 * @throws IOException 
 */
	public InputStream getResourceAsStream(String resourceName) throws IOException {
		InputStream stream = null;
		resourceName = resourceName.replaceAll("^/+", "");

		for (AbstractSystemResourceLoader loader: resourceLoaders)
			for (String url : loader.retrieveAvailableResources()){
				String parsedUrl = url.replaceAll("^/+", "");
				if (parsedUrl.equals(resourceName)) {
					stream = openStream(parsedUrl);
					if (!Util.isNull(stream))
						return stream;
				}
			}

		return stream;
	}

	public InputStream openStream(String url) {
		InputStream stream = getClassLoader().getResourceAsStream(url);
		if (Util.isNull(stream) && !Util.isNull(getServletContext()))
			stream = getServletContext().getResourceAsStream("/" + url);
		return stream;
	}

	public CoffeeResource getResource(String uri) {
		for (String resourcePattern : registeredResources.keySet())
			if (uri.matches(resourcePattern))
				return registeredResources.get(resourcePattern);
		return null;
	}

	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void unloadRegisteredResources() {
		this.registeredResources.clear();
		populated = false;
	}

	public void setRegisteredResources(Map<String, CoffeeResource> registeredResources) {
		this.registeredResources = registeredResources;
	}

	public Map<String, CoffeeResource> getRegisteredResources() {
		return registeredResources;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}
	
	public static CoffeeResourceLoader getInstance() {
		if (isNull(instance))
			instance = new CoffeeResourceLoader();
		return instance;
	}

	public void setResourceLoader(ServletContext resourceLoader) {
		this.servletContext = resourceLoader;
	}

	public ServletContext getResourceLoader() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}

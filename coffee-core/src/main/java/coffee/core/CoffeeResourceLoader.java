/**
 * Based on Tornado's way of registering web resources.
 * Thanks to Richard Kuesters from his suggestion that makes my job easier.
 * Thanks to Victor Tatai from his collaboration at http://snippets.dzone.com/posts/show/4831
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
package coffee.core;

import static coffee.util.Util.isNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.components.IComponent;

public class CoffeeResourceLoader {

	private static CoffeeResourceLoader instance;

	private Map<String, CoffeeResource> registeredResources;
	private HashMap<String, IComponent> resourceCache;
	private Object resourceLoader;
	private boolean cacheEnabled;

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
		if (isCacheEnabled() && resourceCache.containsKey(templateName)) {
			IComponent component = (IComponent)resourceCache.get(templateName).clone();
			component.setCoffeeContext(context);
			component.flush();
			return component;
		}
		
		InputStream template = loadTemplate(templateName);
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
	public void loadRegisteredResources(String packageName)
            throws ClassNotFoundException, IOException 
    {
        ClassLoader classLoader = getClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String fileName = resource.getFile();
            String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(fileNameDecoded);
            if (!file.exists())
            		continue;
            this.registeredResources.putAll(findClasses(file, packageName));
        }
    }

/**
 * Recursive method used to find all classes in a given directory and subdirs.
 *
 * @param directory   The base directory
 * @param packageName The package name for classes found inside the base directory
 * @return The classes
 * @throws ClassNotFoundException
 */
	public Map<String,CoffeeResource> findClasses(File directory, String packageName) throws ClassNotFoundException 
	{
		Map<String,CoffeeResource> classes = new HashMap<String,CoffeeResource>();
        for (File file : directory.listFiles()) {
        	String fileName = file.getName();
            if (file.isDirectory()) {
                assert !fileName.contains(".");
            	classes.putAll(findClasses(file, packageName + "." + fileName));
            	return classes;
            }

            if (!fileName.endsWith(".class") || fileName.contains("$"))
            	continue;

			String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
			Class<?> clazz = getClassLoader().loadClass(
					normalizeClassName(className));
			
			WebResource webresource = clazz.getAnnotation(WebResource.class);
			if (isNull(webresource))
				continue;

			String pattern = webresource.pattern();
			CoffeeResource data = new CoffeeResource();
			data.setClazz(clazz);
			data.setPattern(pattern);
			data.setTemplate(webresource.template());

			classes.put(pattern, data);
        }
        return classes;
    }

/**
 * Retrieves a className with a normalized notation (ex. no (back)slashes or dots at
 * beginning, etc.).
 * 
 * @param className
 * @return
 */
	public String normalizeClassName(String className) {
		return className.replaceFirst("^\\.+", "");
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
		InputStream stream = getResourceAsStream(path);
		return stream;
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

	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void unloadRegisteredResources() {
		this.registeredResources.clear();
	}
	
	public static CoffeeResourceLoader getInstance() {
		if (isNull(instance))
			instance = new CoffeeResourceLoader();
		return instance;
	}

	public void setResources(Map<String, CoffeeResource> resources) {
		this.registeredResources = resources;
	}

	public Map<String, CoffeeResource> getResources() {
		return registeredResources;
	}

	public CoffeeResource get(String uri) {
		for (String resourcePattern : registeredResources.keySet())
			if (uri.matches(resourcePattern))
				return registeredResources.get(resourcePattern);
		return null;
	}

	public void setRegisteredResources(Map<String, CoffeeResource> registeredResources) {
		this.registeredResources = registeredResources;
	}

	public Map<String, CoffeeResource> getRegisteredResources() {
		return registeredResources;
	}

/**
 * Retrieves dynamically a resource as Stream from a resourceName.
 * 
 * @param resourceName
 * @return
 */
	public InputStream getResourceAsStream(String resourceName) {
		try {
			Object loader = getCurrentResourceLoader();
			if (isNull(loader))
				loader = getClassLoader();
			Method getResourceMethod = loader.getClass().getMethod("getResourceAsStream", String.class);
			return (InputStream)getResourceMethod.invoke(loader, resourceName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setCurrentResourceLoader(Object resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Object getCurrentResourceLoader() {
		return resourceLoader;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}
}

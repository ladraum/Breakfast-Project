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
package breakfast.coffee;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breakfast.coffee.components.IComponentFactory;


public class CoffeeContext {

	public static final String COFFEE_COMPONENTS_TEMPLATE_VALUE = "COFFEE_COMPONENTS_TEMPLATE_VALUE.";
	public static final String COFFEE_CURRENT_PARSED_COMPONENT = "COFFEE_CURRENT_PARSED_COMPONENT";
	private static Map<String, IComponentFactory> registeredNamespaces = new HashMap<String, IComponentFactory>();
	
	private Map<String, Object> properties;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	private String path;
	private int idComponentCounter = 0;

	public CoffeeContext() {
		properties = new HashMap<String, Object>();
	}

	public static void registerNamespace(String namespace, IComponentFactory factory) {
		registeredNamespaces.put(namespace, factory);
	}
	
	public static IComponentFactory getComponentFactory(String namespace) {
		return registeredNamespaces.get(namespace);
	}
	
	public static boolean isRegisteredNamespace(String namespace) {
		return registeredNamespaces.containsKey(namespace);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(ServletRequest request) {
		this.request = (HttpServletRequest)request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(ServletResponse response) {
		this.response = (HttpServletResponse)response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	public void put(String name, Object property){
		properties.put(name, property);
	}

	public Object get(String name) {
		return properties.get(name);
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}

/** 
 * Set a new URI location relative to the coffeeContext. The '<i>current URI</i>' defines
 * the actual rendered URI. When you change the current URI the Coffee Engine
 * send's a HTTP 302 status and redirect your pages to the new set URI.
 * 
 * @param uri
 * @see CoffeeContext#setPath(String)
 */
	public void setRelativePath(String relativePath) {
		this.path = String.format("%s/%s",
						((HttpServletRequest)this.request).getContextPath(),
						relativePath);
	}

/**
 * Get the location URI relative to the coffeeContext.
 * @return
 */
	public String getRelativePath() {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		return getPath().replaceFirst(httpRequest.getContextPath(), "");
	}

/** 
 * Set a new URI location to the coffeeContext. The '<i>current URI</i>' defines
 * the actual rendered URI. When you change the current URI the Coffee Engine
 * send's a HTTP 302 status and redirect your pages to the new set URI.
 * 
 * @param uri
 */
	public void setPath(String requestURI) {
		this.path = requestURI;
	}

/**
 * Get the location URI. If it is null it tries to retrieves from the
 * current HttpServletContext. Otherwise it will returns null.
 * @return the location URI or null.
 */
	public String getPath() {
		if (path == null)
			path = request.getRequestURI();
		return path;
	}

/**
 * Retrieves the current coffeeContext path. For more info see
 * {@link HttpServletRequest#getContextPath()}
 * @return contextPath
 */
	public String getContextPath() {
		return request.getContextPath();
	}

	public void setIdComponentCounter(int idComponentCounter) {
		this.idComponentCounter = idComponentCounter;
	}
	
	public int getIdComponentCounter() {
		return idComponentCounter;
	}
	
	public String getNextId() {
		return "component" + (idComponentCounter++);
	}
}

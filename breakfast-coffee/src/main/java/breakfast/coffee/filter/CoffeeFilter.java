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
package breakfast.coffee.filter;

import static breakfast.coffee.util.Util.isNull;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import breakfast.coffee.Cafeteria;
import breakfast.coffee.CoffeeContext;
import breakfast.coffee.CoffeeResource;
import breakfast.coffee.IResource;
import breakfast.coffee.components.IComponent;
import breakfast.coffee.loader.CoffeeResourceLoader;


//@WebFilter(urlPatterns={"/*"}, asyncSupported=true)
public class CoffeeFilter implements Filter {

	protected FilterConfig filterConfig;
	protected CoffeeResourceLoader resourceLoader;

	@Override
/**
 * Coffee Filter initialization
 * 
 * @param filterConfig
 * @throws ServletException
 */
	public void init(FilterConfig filterConfig) throws ServletException {
		setFilterConfig(filterConfig);

		try {
			ServletContext servletContext = getServletContext();
			resourceLoader = Cafeteria.getResourceLoader(servletContext.getContextPath());
			resourceLoader.setServletContext(servletContext);
			resourceLoader.initialize();
		} catch (IOException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	@Override
/**
 * Coffee Filter finalization
 */
	public void destroy() {}

	@Override
/**
 * Wraps the request and apply the Coffee life cycle. If the request isn't a
 * valid coffee request it releases the control to the filter chain.
 * 
 * @param request
 * @param response
 * @param chain
 * @throws IOException
 * @throws ServletException
 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		try {
			CoffeeContext coffeeContext = Cafeteria.createContext(request, response, getServletContext());
			CoffeeResource coffeeResource = resourceLoader.getResource(coffeeContext.getRelativePath());

			if (isNull(coffeeResource)) {
				chain.doFilter(request, response);
				return;
			}

			IResource resource = coffeeResource.instantiateResource(coffeeContext);
			IComponent webpage = resourceLoader.compile(coffeeResource.getTemplate(), coffeeContext);
			webpage.setCoffeeContext(coffeeContext);
			resource.process();

			String uri = ((HttpServletRequest)request).getRequestURI();
			if (!coffeeContext.getPath().equals(uri)){
				((HttpServletResponse)response).sendRedirect(coffeeContext.getPath());
				return;
			}

			webpage.render();

		} catch (InstantiationException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (CloneNotSupportedException e) {
			throw new ServletException(e.getMessage(), e);
		}

	}

/**
 * Get the filter current Class Loader
 * @return
 */
	public ClassLoader getClassLoader() {
		return getServletContext().getClass().getClassLoader();
	}

	public ServletContext getServletContext() {
		return filterConfig.getServletContext();
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

}

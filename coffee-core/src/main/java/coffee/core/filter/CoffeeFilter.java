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
package coffee.core.filter;

import static coffee.core.util.Util.isNull;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.core.CoffeeContext;
import coffee.core.Cafeteria;
import coffee.core.CoffeeResource;
import coffee.core.IResource;
import coffee.core.components.IComponent;
import coffee.core.loader.CoffeeResourceLoader;

@WebFilter("/*")
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
			CoffeeContext coffeeContext = createCoffeeContext(request, response, getServletContext());
			CoffeeResource resource = resourceLoader.getResource(coffeeContext.getRelativePath());

			if (isNull(resource)) {
				chain.doFilter(request, response);
				return;
			}

			IResource hook = resource.instantiateHook(coffeeContext);
			IComponent webpage = resourceLoader.compile(resource.getTemplate(), coffeeContext);
			webpage.setCoffeeContext(coffeeContext);
			hook.process();

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
 * 
 * @param request
 * @param response
 * @param servletContext
 * @return
 */
	public CoffeeContext createCoffeeContext(ServletRequest request,
			ServletResponse response, ServletContext servletContext) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		CoffeeContext context = Cafeteria.createContext();
		context.setRequest(request);
		context.setResponse(response);
		context.setServletContext(servletContext);
		context.setPath(httpRequest.getRequestURI());
		context.put("contextPath", httpRequest.getContextPath());
		context.put("path", httpRequest.getRequestURI());
		return context;
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

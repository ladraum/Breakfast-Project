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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import breakfast.coffee.loader.CoffeeResourceLoader;
import breakfast.coffee.util.Util;


public class Cafeteria {
	
	private static final Map<String, CoffeeResourceLoader> resourceLoaders = new HashMap<String, CoffeeResourceLoader>();
	private static boolean cacheEnabled = true;

	public static CoffeeContext createContext(
						ServletRequest request, ServletResponse response, ServletContext servletContext)
	{
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		CoffeeContext context = new CoffeeContext();

		context.setRequest(request);
		context.setResponse(response);
		context.setServletContext(servletContext);
		context.setPath(httpRequest.getRequestURI());
		context.put("contextPath", httpRequest.getContextPath());
		context.put("path", httpRequest.getRequestURI());

		return context;
	}

	public static CoffeeResourceLoader getResourceLoader(CoffeeContext context) throws IOException, ClassNotFoundException {
		CoffeeResourceLoader resourceLoader = getResourceLoader(context.getContextPath());
		resourceLoader.setCacheEnabled(cacheEnabled);
		return resourceLoader;
	}

	public static CoffeeResourceLoader getResourceLoader(String contextPath) throws IOException, ClassNotFoundException {
		CoffeeResourceLoader loader = resourceLoaders.get(contextPath);
		if (Util.isNull(loader)) {
			loader = new CoffeeResourceLoader();
			resourceLoaders.put(contextPath, loader);
		}
		return loader;
	}
	
	public static void disableCache(){
		cacheEnabled = false;
	}
	
	public static void enableCache(){
		cacheEnabled = true;
	}

}

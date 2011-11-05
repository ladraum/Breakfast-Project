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
package coffee.core;

import java.io.IOException;

/**
 * Generic implementation for Web Resource. It is a robust abstract implementation
 * of {@link IResource} interface. It was designed to helps software developer handling
 * parameters, parsing URI, saving resource state to re-use later, etc.
 * 
 * @since Coffee 1.0
 */
public abstract class Resource implements IResource {

	protected CoffeeContext context;

	@Override
	public void configure(CoffeeContext context) throws IOException {
		setContext(context);
		setCharacterEncoding("UTF-8");
		setContentType("text/html");
		configure();
	}

/**
 * @see IResource#configure(CoffeeContext)
 */
	public abstract void configure() throws IOException;

/**
 * Changes the context navigation. Whenever you chance the coffee context navigation
 * the user will be redirected to the new URI.
 * @param uri
 */
	public void redirect (String uri) {
		context.setPath(uri);
	}

	public void redirectToResource(String url) {
		redirect(context.getContextPath() + url);
	}

	public CoffeeContext getContext() {
		return context;
	}

	public void setContext(CoffeeContext context) {
		this.context  = context;
	}

	public void setCharacterEncoding(String encoding) {
		getContext().getResponse().setCharacterEncoding(encoding);
	}

	public void setContentType(String contentType) {
		getContext().getResponse().setContentType(contentType);
	}
	
/**
 * Retrieves a sent parameter from request
 * @param param
 * @return
 */
	public String getParameter(String param) {
		return getContext().getRequest().getParameter(param);
	}

/**
 * @return true if the HTTP request mode is POST
 */
	public boolean isPost() {
		return getHttpMethod().equals("POST");
	}

/**
 * @return true if the HTTP request mode is GET
 */
	public boolean isGet() {
		return getHttpMethod().equals("GET");
	}

/**
 * @return the request HTTP method.
 */
	public String getHttpMethod() {
		return getContext().getRequest().getMethod();
	}
}

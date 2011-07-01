package coffee.core.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import coffee.core.util.JSON;

public abstract class JSONServlet implements Servlet {

	private static final long serialVersionUID = -843898272537461330L;
	private ServletConfig servletConfig;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.servletConfig = config;
	}

	@Override
	public void destroy() {}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String method = httpServletRequest.getMethod().toUpperCase();

		if (!method.equals("GET"))
			throw new ServletException("Method not allowed: " + method);

		StringBuilder serializedObject = JSON.serialize(doGet(httpServletRequest));
		response
			.getWriter()
			.append(serializedObject);
		
		response.setContentType("text/plain");
	}

	public abstract Object doGet(HttpServletRequest request);

	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public String getServletInfo() {
		return getClass().getCanonicalName();
	}

}

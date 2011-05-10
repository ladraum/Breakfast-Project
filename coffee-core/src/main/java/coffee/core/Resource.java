package coffee.core;


/**
 * Generic implementation for Web Resource. It is a robust abstract implementation
 * of {@link IResource} interface. It was designed to helps software developer handling
 * parameters, parsing URI, saving resource state to re-use later, etc.
 * 
 * @author Miere Liniel Teixeira
 * @since Coffee 1.0
 */
public abstract class Resource implements IResource {

	protected CoffeeContext context;

	@Override
	public void configure(CoffeeContext context) {
		setContext(context);
		setCharacterEncoding("UTF-8");
		
		configure();
	}

/**
 * @see IResource#configure(CoffeeContext)
 */
	public abstract void configure();

	public CoffeeContext getContext() {
		return context;
	}

	public void setContext(CoffeeContext context) {
		this.context  = context;
	}

/**
 * Changes the context navigation. Whenever you chance the coffee context navigation
 * the user will be redirected to the new URI.
 * @param uri
 */
	public void redirect (String uri) {
		context.setPath(uri);
	}

/**
 * 
 * @param parameters
 * @return
 */
	public String getResourceURI(CoffeeContext context) {
		StringBuffer buffer = new StringBuffer();

		WebResource resource = getClass().getAnnotation(WebResource.class);

		buffer
			.append(context.getContextPath())
			.append(resource.uri());
		
//		HttpServletRequest request = context.getRequest();

		/*
		 * TODO: melhorar o modelo de getResourceURI
		 * int i=0;
		for (String param : ((Map<String, Object>)request.getParameterMap()).keySet()){
			buffer.append(i==0 ? '?' : '&')
				  .append(param)
				  .append("=")
				  .append(request.getParameter(param));
			i++;
		}*/
		return "";
	}
	
	public void setCharacterEncoding(String encoding) {
		getContext().getResponse().setCharacterEncoding(encoding);
	}
	
	public void setContentType(String contentType) {
		getContext().getResponse().setContentType("applicationx/xhtml+xml");
	}
}

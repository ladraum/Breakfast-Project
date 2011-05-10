package coffee.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Defines a Web Resource and its basic configuration.
 */
public @interface WebResource {
	/** 
	 * <b>uri</b> (required): defines the correct URI to access your resource. The URI
	 * 			  informed should be relative to the current context path.
	 */
	String uri();
	/**
	 * <b>pattern</b> (required): a regular expression pattern to match against
	 * 				  request URI. If the requested URI matches with this pattern then
	 * 				  your web resource will be processed. Remember that the URI patter
	 * 			  	  informed should be relative to the current context path.
	 */
	String pattern();
	/**
	 * <b>template</b> (required): the XHTML template file that will be rendered by
	 * 				   web resource.
	 */
	String template();
}

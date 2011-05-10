package coffee.core;

/**
 * The web resource interface. It defines the basic life cycle of
 * a web resource on the Coffee Framework. Software developers that
 * looks for a robust web resource should use <i>{@link Resource}</i>
 * instead of this interface.
 *  
 * @author Miere Liniel Teixeira
 * @since Coffee 1.0
 */
public interface IResource {

/**
 * Configure the web resource. It allows to feed the context with data
 * that will be handle by the coffee parser (like value expressions, for
 * an example).
 * @param context
 */
	public abstract void configure(CoffeeContext context);

/**
 * The main method from the web resource. This method was designed to make possible
 * to retrieve data that will be rendered on the browser, or persist browser sent data
 * before redirect the user to a success/fail page. 
 */
	public abstract void process();
	
}

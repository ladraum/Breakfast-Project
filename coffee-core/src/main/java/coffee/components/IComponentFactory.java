package coffee.components;

import coffee.components.template.ComponentFactory;
import coffee.core.CoffeeContext;
import coffee.core.CoffeeParser;

public interface IComponentFactory {

/**
 * Creates a new component.<br/>
 * <br/>
 * The default implementation is defined by {@link AbstractComponentFactory} and it's called
 * by the default component parser {@link CoffeeParser}. The parser expects that new component
 * has the context set and configured.<br/>
 * <br/>
 * Note that you should not implement this interface by yourself unless you really knows what are
 * you doing. Otherwise, choose to extends {@link AbstractComponentFactory} or {@link ComponentFactory}
 * classes.
 * 
 * @param name
 * @param context
 * @return
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @see IComponent#setCoffeeContext(CoffeeContext)
 * @see IComponent#configure()
 */
	public IComponent newComponent(String name, CoffeeContext context) throws InstantiationException, IllegalAccessException;

}

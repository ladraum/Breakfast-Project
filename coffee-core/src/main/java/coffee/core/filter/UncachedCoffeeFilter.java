package coffee.core.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class UncachedCoffeeFilter extends CoffeeFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		resourceLoader.setCacheEnabled(false);
	}

}

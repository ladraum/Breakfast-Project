package coffee.core;

public class CoffeeContextFactory implements ICoffeeContextFactory {

	@Override
	public CoffeeContext createContext() {
		return new CoffeeContext();
	}

}

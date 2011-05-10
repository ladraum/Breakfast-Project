package coffee.binding;

import static org.junit.Assert.*;

import org.junit.Test;

import coffee.binding.CoffeeBinder;
import coffee.core.CoffeeContext;

public class ExpressionTest {

	private static final String HELLO = "Olá #{usuario.nome}!";
	private static final String CONTEXT = "#{usuario.context}";
	private static final String NAME = "#{usuario.nome}";

	@Test
	public void evalTest() {
		CoffeeContext context = new CoffeeContext();
		Usuario usuario = new Usuario("Miere", context);
		context.put("usuario", usuario);

		String returnString = CoffeeBinder.getValue(HELLO, context ).toString();
		assertEquals("Olá Miere!", returnString);

		Object evaluatedObject = CoffeeBinder.getValue(CONTEXT, context);
		assertSame(context, evaluatedObject);

		String newName = "Joseh";
		CoffeeBinder.setValue(NAME, context, newName);
		assertEquals(newName, usuario.getNome());
	}

	class Usuario {
		private String nome;
		private CoffeeContext context;
		
		public Usuario(String nome, CoffeeContext context) {
			setContext(context);
			setNome(nome);
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		public void setContext(CoffeeContext context) {
			this.context = context;
		}

		public CoffeeContext getContext() {
			return context;
		}
	}
}

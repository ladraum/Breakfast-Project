package breakfast.coffee.binding;

import static org.junit.Assert.*;

import org.junit.Test;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.CoffeeBinder;


public class ExpressionTest {

	private static final String CONTEXT = "#{usuario.context}";
	private static final String HELLO = "Olá #{usuario.nome}!";
	private static final String NAME = "#{usuario.nome}";
	private static final String TWO_EXPRESSIONS = "#{usuario.nome} will #{usuario.action} till tomorrow.";

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
	
	@Test
	public void evalTwoExpressions() {
		CoffeeContext context = new CoffeeContext();
		Usuario usuario = new Usuario("Miere", context);
		usuario.setAction("run");
		context.put("usuario", usuario);

		String returnString = CoffeeBinder.getValue(TWO_EXPRESSIONS, context ).toString();
		assertEquals("Miere will run till tomorrow.", returnString);
	}

	class Usuario {
		private String nome;
		private String action;
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

		public void setAction(String action) {
			this.action = action;
		}

		public String getAction() {
			return action;
		}
	}
}

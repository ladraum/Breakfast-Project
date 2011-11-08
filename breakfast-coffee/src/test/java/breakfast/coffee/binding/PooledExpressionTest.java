package breakfast.coffee.binding;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Test;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.binding.Evaluator;


public class PooledExpressionTest {

	private static final String CONTEXT = "#{usuario.context}";
	private static final String NAME = "#{usuario.nome}";

	@Test
	public void testSpeedOne() {
		CoffeeContext context = new CoffeeContext();
		context.put("usuario", new Usuario("Miere Teixeira", context));
		
		long prev_time, total_time=0, loop_times=1;
		for (int j=0; j<loop_times; j++) {
		    prev_time = System.currentTimeMillis();
			for (int i=0;i<10000; i++) {
				Object value = CoffeeBinder.getValue(CONTEXT, context);
				assertSame(context, value);
			}
			long time = System.currentTimeMillis() - prev_time;
			total_time+= time;
		}
		System.out.println("Total Time:" + total_time);
		System.out.println("Avarage Time:" + (total_time/loop_times));
	}

	@Test
	public void notSameMatcher() {
		Matcher matcher = CoffeeBinder.getMatcher(Evaluator.RE_IS_VALID_RETRIEVABLE_EXPRESSION, NAME);
		Matcher matcher2 = CoffeeBinder.getMatcher(Evaluator.RE_IS_VALID_RETRIEVABLE_EXPRESSION, NAME);
		assertNotSame(matcher, matcher2);
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

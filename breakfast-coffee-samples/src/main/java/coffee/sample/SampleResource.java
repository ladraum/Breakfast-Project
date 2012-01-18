package coffee.sample;

import breakfast.coffee.Resource;
import breakfast.coffee.annotation.WebResource;
import breakfast.coffee.util.Util;

@WebResource( pattern="/sample/*",
			  template="templates/sample.xhtml")

public class SampleResource extends Resource {

	private Usuario user;

	@Override
	public void configure() {
		user = new Usuario();
		coffeeContext.put("user", user);
	}

	@Override
	public void process() {

		if (Util.isNull(user.getNome()))
			user.setNome("Miere");
		if (Util.isNull(user.getSobrenome()))
			user.setSobrenome("Liniel Teixeira");
		if (Util.isNull(user.getIdade()))
			user.setIdade("27");
		if (Util.isNull(user.getSexo()))
			user.setSexo((short) 0);
		if (Util.isNull(user.getObservacoes()))
			user.setObservacoes("");
		if (Util.isNull(user.getDoador()))
			user.setDoador("false");
	}

	public class Usuario {
		
		private String nome;
		private String sobrenome;
		private String idade;
		private Short sexo;
		private String observacoes;
		private String doador;
		private Boolean active;

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		public void setSobrenome(String sobrenome) {
			this.sobrenome = sobrenome;
		}

		public String getSobrenome() {
			return sobrenome;
		}

		public void setIdade(String idade) {
			this.idade = idade;
		}

		public String getIdade() {
			return idade;
		}

		public void setSexo(Short sexo) {
			this.sexo = sexo;
		}

		public Short getSexo() {
			return sexo;
		}

		public void setObservacoes(String observacoes) {
			this.observacoes = observacoes;
		}

		public String getObservacoes() {
			return observacoes;
		}

		public void setDoador(String doador) {
			this.doador = doador;
		}

		public String getDoador() {
			return doador;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		public Boolean getActive() {
			return active;
		}
		
	}

}

package coffee.sample;

import breakfast.coffee.Resource;
import breakfast.coffee.annotation.WebResource;
import breakfast.coffee.util.Util;

@WebResource( pattern="/sample/*",
			  template="templates/sample.xhtml")

public class SampleResource extends Resource {

	private Usuario usuario;

	@Override
	public void configure() {
		usuario = new Usuario();
		coffeeContext.put("usuario", usuario);
	}

	@Override
	public void process() {
		
		// TODO: incluir a servlet-api:2.5 no pom.xml
		
		if (Util.isNull(usuario.getNome()))
			usuario.setNome("Miere");
		if (Util.isNull(usuario.getSobrenome()))
			usuario.setSobrenome("Liniel Teixeira");
		if (Util.isNull(usuario.getIdade()))
			usuario.setIdade("26");
		if (Util.isNull(usuario.getSexo()))
			usuario.setSexo((short) 0);
		if (Util.isNull(usuario.getObservacoes()))
			usuario.setObservacoes("");
		if (Util.isNull(usuario.getDoador()))
			usuario.setDoador("false");
	}
	
	public class Usuario {
		
		private String nome;
		private String sobrenome;
		private String idade;
		private short sexo;
		private String observacoes;
		private String doador;

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
		
		public void setSexo(String sexo) {
			setSexo(Short.parseShort(sexo));
		}

		public void setSexo(short sexo) {
			this.sexo = sexo;
		}

		public short getSexo() {
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
		
	}

}

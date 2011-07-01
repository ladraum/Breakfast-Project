package sugar.sample;

public class Usuario {
	private String name;
	private String civilstate;
	private String isActiveUser;
	private String doador;

	public void setCivilstate(String civilstate) {
		this.civilstate = civilstate;
	}

	public String getCivilstate() {
		return civilstate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setIsActiveUser(String isActiveUser) {
		this.isActiveUser = isActiveUser;
	}

	public String getIsActiveUser() {
		return isActiveUser;
	}

	public void setDoador(String doador) {
		this.doador = doador;
	}

	public String getDoador() {
		return doador;
	}
}
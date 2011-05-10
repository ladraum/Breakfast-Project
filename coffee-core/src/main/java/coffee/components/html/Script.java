package coffee.components.html;


public class Script extends HtmlComponent {
	
	private String src;
	
	@Override
	public void configure() {
		setComponentName("script");
	}
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String value) {
		if (value.charAt(0) != '/')
			value = getCoffeeContext().getContextPath() + "/" + value;
		this.src = value;
	}

}

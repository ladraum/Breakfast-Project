package coffee.components.html;


public class Image extends HtmlComponent {
	
	private String href;
	
	@Override
	public void configure() {
		setComponentName("img");
		setSelfCloseable(true);
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String value) {
		if (value.charAt(0) != '/')
			value = getCoffeeContext().getContextPath() + "/" + value;
		this.href = value;
	}

}

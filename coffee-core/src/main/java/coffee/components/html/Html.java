package coffee.components.html;


public class Html extends HtmlComponent {
	
	@Override
	public void configure() {
		setComponentName("html");
		setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
	}

}

package coffee.components.html;


public class TextArea extends HtmlComponent {

	private String name;

	@Override
	public void configure() {
		super.configure();
		setComponentName("textarea");
	}

	public void setName(String name) {
		this.name = name;
		setId(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public void setTextContent(String content) {
		super.setTextContent(content);
		holdValue(content);
	}

}

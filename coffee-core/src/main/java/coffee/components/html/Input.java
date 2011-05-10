package coffee.components.html;


public class Input extends HtmlComponent {
	
	private String type;
	private String value;

	@Override
	public void configure() {
		setComponentName("input");
		setSelfCloseable(true);
	}

	public void setName(String name) {
		setId(name);
	}
	
	public void setType(String value) {
		this.type = value;
	}

	public void setValue(String value) {
		if (type.equals("text"))
			holdValue(value);
		this.value = value;
	}

	public String getChecked() {
		String newValue = getAttributeValue("checked");
		if (type.equals("radio") && newValue.equals(value))
			return "checked";
		if (type.equals("checkbox") && newValue.equals("on"))
			return "checked";
		return "";
	}

	public void setChecked(String value) {
		if (type.equals("checkbox")
		||  type.equals("radio"))
			holdValue(value);
	}

}

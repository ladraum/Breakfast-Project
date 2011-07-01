package coffee.sugar;

import java.util.ArrayList;

import coffee.core.util.Util;

public abstract class Widget extends Component {
	
	// Events
	private final static String[] events = new String[] {
		"onblur", "onchange", "onclick",
		"ondblclick", "onfocus", "onmousedown", "onmousemove",
		"onmouseout", "onmouseover", "onmouseup", "onkeydown",
		"onkeypress", "onkeyup", "onselect" };

	private String label;
	private boolean required = false;
	private String value;

	public StringBuilder getEventsDefinition() {

		StringBuilder buffer = new StringBuilder();

		for (String attribute : this.attributes.keySet())
			if (isValidEvent(attribute))
				buffer
					.append(attribute)
					.append("=\"")
					.append(getAttributeValue(attribute))
					.append("\" ");

		return buffer;
	}

/**
 * Based on {@link ArrayList#contains(Object)} implementation.
 * @param object
 * @return
 */
	public boolean isValidEvent(String object) {
		for (int i = 0; i < events.length; i++) {
			if (object.equals(events[i])) {
				return true;
			}
		}
		return false;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Boolean isRequired() {
		return required;
	}

	public void setValue(String value) {
		this.value = value;
		holdValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		if (Util.isNull(label))
			return getId();
		return label;
	}

}

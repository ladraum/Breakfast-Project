package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.Util;
import coffee.sugar.Widget;


public class TextInput extends Widget {
	
	private boolean passwordMask = false;
	private String mask;
	private String maxLength;
	private boolean readonly = false;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		
		PrintWriter writer = getWriter();
		writer
			.append("<input type=\"")
				.append(getType())
			.append("\" name=\"")
				.append(getId())
			.append("\" id=\"")
				.append(getId())
			.append("\" style=\"")
				.append(getStyleDefinition())
			.append("\" class=\"")
				.append(getSkin())
			.append("\" ");
		
		if (isReadonly())
			writer.append(" readonly=\"readonly\" ");
		if (!Util.isNull(maxLength))
			writer
				.append("maxlength=\"")
				.append(getMaxLength())
				.append("\" ");

		String value = getAttributeValue("value");
		if (!Util.isNull(value))
				writer.append("value=\"").append(value).append("\" ");

		writer
			.append(getEventsDefinition())
			.append(" />")
			.append("<script>application.addChild(new TextInput( {required:")
			.append(isRequired().toString())
			.append(",label:\"")
				.append(getLabel())
			.append("\",")
			.append("id:\"")
				.append(getId())
			.append("\", readonly:")
				.append(isReadonly().toString());

		if (!Util.isNull(getMask()))
			writer
				.append(",mask:\"")
					.append(getMask())
				.append("\"");

		writer.append("}));</script>");
	}

	public void setPasswordMask(String passwordMask) {
		this.passwordMask = Boolean.parseBoolean(passwordMask);
	}

	public boolean isPasswordMask() {
		return passwordMask;
	}

	public String getType() {
		return isPasswordMask() ? "password" : "text";
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMask() {
		return mask;
	}

	public void setMaxLength(String maxlength) {
		this.maxLength = maxlength;
	}

	public String getMaxLength() {
		return maxLength;
	}
	
	public void setReadonly(String readonly) {
		this.readonly = Boolean.parseBoolean(readonly);;
	}
	
	public String getReadonly() {
		return isReadonly() ? "readonly" : "";
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public Boolean isReadonly() {
		return readonly;
	}
	
	@Override
	public String getSkin() {
		return new StringBuilder()
				.append(super.getSkin())
				.append(getReadonly())
					.toString();
	}
}

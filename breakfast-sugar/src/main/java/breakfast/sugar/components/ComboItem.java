package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.sugar.Widget;



public class ComboItem extends Widget {
	
	private boolean selected;
	
	@Override
	protected void initialize() {
		super.initialize();
		selected = false;
	}

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<option value=\"")
			.append(getAttributeValue("value"))
			.append("\" ");

		if (isSelected())
			writer.append("selected=\"selected\"");

		writer
			.append('>')
			.append(getAttributeValue("label"))
			.append("</option>");
	}
	
	@Override
	protected void flush() {
		super.flush();
		selected = false;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

}

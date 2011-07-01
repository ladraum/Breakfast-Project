package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.StringUtil;
import coffee.core.util.Util;
import coffee.sugar.Component;

public class Panel extends Component {

	private boolean collapsed = false;
	private boolean collapsible = false;
	private String styleClassNames;

	@Override
	public void configure() {
		setStyleClassNames("Panel");

		registerComponent("new Panel( {" +
								"id:\""+getId()+"\", label:\"" + getLabel() + "\", " +
								"collapsible:"+collapsible+", collapsed:"+collapsed+"} )");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();

		writer.append("<div class=\"")
			.append(getStyleClassNames())
			.append("\" id=\"")
			.append(getId())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\">")
			.append("<div class=\"PanelBar\"><span id=\"")
				.append(getId()).append("Label")
			.append("\">");

		if (!Util.isNull(getLabel()))
			writer.append(getLabel());

		writer
			.append("</span></div><div id=\"")
				.append(getId()).append("Container")
			.append("\" style=\"overflow: auto; height: 100%;\">");
		renderChildren();
		writer.append("</div></div>");
	}
	
	@Override
	public String getId() {
		String id = super.getId();
		if (StringUtil.isEmpty(id)) {
			id = coffeeContext.getNextId();
			setId(id);
		}
		return id;
	}
	
	public void setCollapsed(String collapsed) {
		setCollapsed(Boolean.parseBoolean(collapsed));
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsible(String collapsible) {
		setCollapsible(Boolean.parseBoolean(collapsible));
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setStyleClassNames(String styleClassNames) {
		this.styleClassNames = styleClassNames;
	}

	public String getStyleClassNames() {
		return styleClassNames;
	}

}

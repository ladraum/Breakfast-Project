package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;
import breakfast.sugar.Component;


public class Panel extends Component {
	private String styleClassNames = "Panel";
	private String label;

	@Override
	public void configure() {
		registerComponent("new Panel( {id:\""+
					getId()+"\", label:\"" + getLabel() + "\"} )");
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
			.append("\" class=\"Container\">"); // style=\"overflow: auto; height: 100%;\"

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

	public void setStyleClassNames(String styleClassNames) {
		this.styleClassNames = styleClassNames;
	}

	public String getStyleClassNames() {
			return new StringBuilder()
				.append(styleClassNames)
				.append(' ')
				.append(getSkin())
					.toString();
	}

	@Override
	public String getLabel() {
		String superclassLabel = super.getLabel();
		if (Util.isNull(label) && !Util.isNull(superclassLabel))
			label = (String) CoffeeBinder.getValue(superclassLabel, coffeeContext);
		return label;
	}

}

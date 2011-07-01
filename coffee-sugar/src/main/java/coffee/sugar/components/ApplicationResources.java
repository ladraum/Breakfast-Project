package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.CoffeeContext;
import coffee.core.util.Util;
import coffee.sugar.Component;

public class ApplicationResources extends Component {

	public static final String SUGAR_JS_ON_LOAD_EVENT = CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + "JsOnLoadEvent";

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = coffeeContext.getResponse().getWriter();

		renderStyleInclude (writer, "css/sugar-components.css");
		renderScriptInclude(writer, "js/sugar-core.js");
		renderScriptInclude(writer, "js/sugar-core-components.js");
		renderScriptInclude(writer, "js/sugar-core-application.js");
		renderScriptInclude(writer, "js/sugar-core-json.js");

		for (String url : getRegisteredScriptIncludes())
			renderScriptInclude(writer, url);

		renderMainScriptBlock(writer);

		for (String script : getRegisteredBlocks())
			writer.append(script);
	}

	public void renderMainScriptBlock(PrintWriter writer) {
		writer
			.append("<script>\n")
			.append("var contextPath = \"").append(getCoffeeContext().getContextPath()).append("\";")
			.append("var application = new Application();\n")
			.append("application.onApplicationLoad = function (){\n");

		for (String component : getRegisteredComponents())
			writer.append("\tapplication.addChild(").append(component).append(");\n");

		writer
			.append("\tapplication.addValidator( new RequiredFieldsValidator() );\n");

		String onLoadMethod = getOnLoadMethod();
		if (!Util.isNull(onLoadMethod))
			writer.append("\t").append(onLoadMethod).append(";\n");

		writer
			.append("}\n")
			.append("DomUtil.addEventListener ( window, \"load\", application.getMethod(\"onApplicationLoad\") );\n")
			.append("</script>");
	}

	public void renderScriptInclude (PrintWriter writer, String url) {
		writer.append("<script src=\"");

		if (url.charAt(0) != '/')
			writer.append(getCoffeeContext().getContextPath()).append('/');

		writer.append(url).append("\" type=\"text/javascript\"></script>");
	}

	public void renderStyleInclude(PrintWriter writer, String url) {
		writer.append("<link href=\"");

		if (url.charAt(0) != '/')
			writer.append(getCoffeeContext().getContextPath()).append('/');

		writer.append(url).append("\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" ></link>");
	}

	public String getOnLoadMethod() {
		return (String)getCoffeeContext().get(SUGAR_JS_ON_LOAD_EVENT);
	}

}

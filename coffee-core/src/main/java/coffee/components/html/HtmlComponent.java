package coffee.components.html;

import static coffee.util.StringUtil.isEmpty;
import static coffee.util.Util.isNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import coffee.binding.CoffeeBinder;
import coffee.components.AbstractComponent;
import coffee.components.IComponent;

/**
 * Default implementation for HTML Components.
 */
public class HtmlComponent extends AbstractComponent {
	
	private boolean selfCloseable;
	private List<String> ignoredAttributes;
	
	public HtmlComponent() {
		ignoredAttributes = new ArrayList<String>();
	}

	public HtmlComponent(String name) {
		super();
		setComponentName(name);
	}

	@Override
	public void configure()  {
		setAttribute("id", getId());
	}

	@Override
/**
 * Renders the component to the browser.
 * @throws IOException
 */
	public void render() throws IOException {
		String componentName = getComponentName();
		PrintWriter writer = coffeeContext.getResponse().getWriter();
		writer.append("<").append(componentName);

		for (String attr : attributes.keySet()) {
			if (ignoredAttributes.contains(attr))
				continue;
			Object attributeValue = getAttribute(attr);
			if (!isNull(attributeValue)
			&&  !isEmpty(attributeValue.toString()))
				writer.append(' ')
					  .append(attr)
					  .append("=\"")
					  .append(attributeValue.toString())
					  .append("\"");
		}

		if (isSelfCloseable()) {
			writer.append(" />");
			return;
		} else
			writer.append('>');

		renderChildren();

		String textContent = getTextContent();
		if (!isNull(textContent))
			writer.append(bindTextContent());

		writer.append("</").append(componentName).append('>');
	}

/**
 * Renders the custom child elements. Software developers should
 * use this method to create custom child elements to him components.
 * 
 * The sample wrapper bellow shows how we can wrap some HTML tags into
 * a single custom component.
 * 
 * <code><pre>
 * 
 * class FormItemAndTextInputComponent extends HtmlComponent {
 * 
 * 		public void configure (){
 * 			setSkipRenderChildComponents(true);
 *  	}
 * 
 * 		public void renderChildren() {
 * 			
 * 		}
 * }
 * 
 * </pre></code>
 * @throws IOException 
 */
	public void renderChildren() throws IOException {
		for (IComponent child : childs) {
			child.setCoffeeContext(getCoffeeContext());
			child.render();
		}
	}

	public CharSequence bindTextContent() {
		return CoffeeBinder.getValue(
						getTextContent(), coffeeContext
					).toString();
	}

/**
 * Define if the component is Self Closeable Tag Component or not. 
 * @param selfCloseable
 */
	public void setSelfCloseable(boolean selfCloseable) {
		this.selfCloseable = selfCloseable;
	}

/**
 * @see HtmlComponent#setSelfCloseable(boolean)
 * @return
 */
	public boolean isSelfCloseable() {
		return selfCloseable;
	}
	
	public void ignoreAttribute(String attribute) {
		ignoredAttributes.add(attribute);
	}
}

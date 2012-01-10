/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package breakfast.coffee.components.xhtml;

import static breakfast.coffee.util.StringUtil.isEmpty;
import static breakfast.coffee.util.Util.isNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;


/**
 * Default implementation for HTML Components.
 */
public class XHtmlComponent extends AbstractComponent {
	
	private boolean selfCloseable;
	private List<String> ignoredAttributes;
	private String componentName;
	
	public XHtmlComponent() {}

	public XHtmlComponent(String name) {
		super();
		setComponentName(name);
	}

	@Override
	protected void initialize() {
		super.initialize();
		ignoredAttributes = new ArrayList<String>();
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

		for (String attr : getAttributes().keySet()) {
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
			writer.append(textContent);

		writer.append("</").append(componentName).append('>');
	}

/**
 * Renders the custom child elements. Software developers should
 * use this method to create custom child elements to their components.
 * 
 * @throws IOException 
 */
	public void renderChildren() throws IOException {
		for (IComponent child : children) {
			child.setCoffeeContext(getCoffeeContext());
			child.render();
		}
	}

/**
 * Define if the component is Self Closeable Tag Component or not. 
 * @param selfCloseable
 */
	public void setSelfCloseable(boolean selfCloseable) {
		this.selfCloseable = selfCloseable;
	}

/**
 * @see XHtmlComponent#setSelfCloseable(boolean)
 * @return
 */
	public boolean isSelfCloseable() {
		return selfCloseable;
	}
	
	public void ignoreAttribute(String attribute) {
		ignoredAttributes.add(attribute);
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentName() {
		return componentName;
	}
}

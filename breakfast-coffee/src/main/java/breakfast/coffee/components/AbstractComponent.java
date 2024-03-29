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
package breakfast.coffee.components;

import static breakfast.coffee.util.Util.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;


/**
 * Default implementation for components. Software developers can override
 * the methods for a custom component development. The <b><i>See Also</i></b>
 * section has useful methods JavaDocs to understand the basics for construct
 * your self custom component.
 * 
 * @author Miere Liniel Teixeira
 * @see IComponent#clone()
 * @see IComponent#getChildren()
 * @see IComponent#getId()
 * @see IComponent#getTextContent()
 * @see IComponent#render()
 */
public abstract class AbstractComponent implements IComponent {
	
	private static final String[] invalidAttributeNames = new String[]{ "class", "children", "attributes", "parent", "textContent", "coffeeContext", "heldExpression" };

	protected List<IComponent> children;
	private Map<String, Object> attributes;
	private IComponent parent;
	private String textContent;
	private String id;
	private String heldExpression;
	protected CoffeeContext coffeeContext;

	public AbstractComponent() {
		super();
		children = new ArrayList<IComponent>();
		attributes = new HashMap<String, Object>();
	}

	public abstract void configure();

	public abstract void render() throws IOException;

	@Override
	public void bind() {
		configure();

		if (!isValueHolder())
			return;

		String expression = (String)getHeldExpression();
		String value = coffeeContext.getRequest().getParameter(getId());
		CoffeeBinder.setValue(expression, coffeeContext, value);
	}

	@Override
	public Object clone(CoffeeContext context) throws CloneNotSupportedException {
		flush();

		List<IComponent> children = new ArrayList<IComponent>();
		AbstractComponent clone = (AbstractComponent)super.clone();

		for (IComponent child : getChildren()) {
			child.setParent(clone);
			children.add((IComponent)child.clone(context));
		}

		clone.setChildren(children);
		clone.setTextContent(textContent);
		clone.setCoffeeContext(context);
		clone.bind();

		return clone;
	}

/**
 * Flushes objects that will be never used after component's rendering. Custom components should
 * override this method to clean some variables to avoid memory leaks. Note that you should
 * always call super.flush() to clean the {@link AbstractComponent} internal variables. 
 */
	protected void flush() {
		this.setCoffeeContext(null);
	}

	@Override
	public void addChild(IComponent child) {
		children.add(child);
	}

/**
 * Insert a child on the children list at position defined by <i>index</i> parameter.
 * @param child
 * @param index
 */
	public void addChild(IComponent child, int index) {
		children.add(index, child);
	}

	@Override
	public IComponent setTextContent(String content) {
		this.textContent = content;
		return this;
	}

	public String getTextContent() {
		if (isNull(textContent))
			return null;

		Object content = CoffeeBinder.getValue(textContent, coffeeContext);
		if (isNull(content))
			return null;

		return content.toString();
	}

	@Override
/**
 * Retrieves the component's attribute value as an Object.<br/>
 * <br/>
 * If the component implementation has a setter method with same name of the attribute
 * it will be dispatched and ignoring the value binding. Otherwise, it will try to
 * retrieve the value from a possible defined binding expression.
 * 
 * @param attribute
 */
	public Object getAttribute(String attribute) {
		return attributes.get(attribute);
	}

/**
 * Retrieves the attribute value and, if it is an valid expression, returns
 * the value of the parsed expression
 *  
 * @param attribute
 * @return
 */
	public Object getParsedAttribute(String attribute) {
		Object object = attributes.get(attribute);
		if (isNull(object))
			return null;
		return CoffeeBinder.getValue(object.toString(), getCoffeeContext());
	}

/**
 * Retrieves the component's attribute value as an String.
 * @param attr
 * @return
 */
	public String getAttributeAsString(String attr) {
		Object value = getParsedAttribute(attr);
		if (isNull(value))
			return "";
		return value.toString();
	}

	@Override
/**
 * Sets an attribute value.<br/>
 * <br/>
 * After stores the value, if the component implementation has a setter method with same name of the attribute
 * it will be dispatched too.
 */
	public IComponent setAttribute(String attribute, Object value) {
		this.attributes.put(attribute, value);
		return this;
	}

	public boolean isInvalidAttribute(String attribute) {
		for (String attr : invalidAttributeNames)
			if (attribute.equals(attr))
				return true;
		return false;
	}
	
	public Collection<String> getAttributeKeys() {
		return attributes.keySet();
	}

	@Override
	public IComponent getParent() {
		return parent;
	}

	@Override
	public IComponent setParent(IComponent parent) {
		this.parent = parent;
		return this;
	}

	@Override
	public int getNumChildren() {
		return children.size();
	}

	@Override
	public List<IComponent> getChildren() {
		return children;
	}

	@Override
	public IComponent setChildren(List<IComponent> value) {
		this.children = value;
		return this;
	}

	@Override
	public boolean isValueHolder() {
		return !StringUtil.isEmpty(getHeldExpression()) && !Util.isNull(getId());
	}

	@Override
	public IComponent setId(String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getId() {
		if (Util.isNull(this.id)) {
			String id = getAttributeAsString("id");
			if (Util.isNull(id))
				id = getCoffeeContext().getNextId();
			this.id = id;
		}
		return this.id;
	}

	@Override
	public String getHeldExpression() {
		return heldExpression;
	}

	@Override
	public IComponent holdExpression(String value) {
		this.heldExpression = value;
		return this;
	}
	
	public void setBindableAttribute(String attribute) {
        Object value = getAttribute(attribute);
        if (!Util.isNull(value))
        	holdExpression(value.toString());
	}

	@Override
	public IComponent setCoffeeContext(CoffeeContext context) {
		this.coffeeContext = context;
		return this;
	}

	@Override
	public CoffeeContext getCoffeeContext() {
		return this.coffeeContext;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

}
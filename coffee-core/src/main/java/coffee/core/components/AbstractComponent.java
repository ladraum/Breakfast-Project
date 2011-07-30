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
package coffee.core.components;

import static coffee.core.util.Util.isNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coffee.core.CoffeeContext;
import coffee.core.binding.CoffeeBinder;
import coffee.core.util.Reflection;
import coffee.core.util.Util;

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
	
	private static final String[] invalidAttributeNames = new String[]{ "class", "children", "attributes", "parent", "textContent", "coffeeContext" };

	protected List<IComponent> children;
	protected Map<String, Object> attributes;
	private IComponent parent;
	private String textContent;
	private String id;
	private Object value;
	protected CoffeeContext coffeeContext;

	public AbstractComponent() {
		super();
		initialize();
	}

	protected void initialize() {
		children = new ArrayList<IComponent>();
		attributes = new HashMap<String, Object>();
	}

	@Override
	public abstract void configure();

	public abstract void render() throws IOException;

	@Override
	public void bind() {
		configure();

		if (!isValueHolder())
			return;

		String expression = (String)getHeldValue();
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
		return CoffeeBinder.getValue(
				textContent, coffeeContext).toString();
	}

	@Override
/**
 * Retrieves the attribute value from component as Object.<br/>
 * <br/>
 * If the component implementation has a setter method with same name of the attribute
 * it will be dispatched and ignoring the value binding. Otherwise, it will try to
 * retrieve the value from a possible defined binding expression.
 * 
 * @param attribute
 */
	public Object getAttribute(String attribute) {
		try{
			if (isInvalidAttribute(attribute))
				return getAttributeValue(attribute);
			Method getter = Reflection.extractGetterFor(attribute, this);
			return getter.invoke(this);
		} catch (Exception e) {
			Object object = attributes.get(attribute);
			if (isNull(object))
				return null;
			return CoffeeBinder.getValue(object.toString(), getCoffeeContext());
		}
	}

/**
 * 
 * @param attr
 * @return
 */
	public String getAttributeValue(String attr) {
		Object object = attributes.get(attr);
		if (isNull(object))
			return null;

		Object value = CoffeeBinder.getValue(object.toString(), getCoffeeContext());
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
		try {
			Method setter = Reflection.extractSetterFor(attribute, this, value);
			setter.invoke(this, value);
			return this;
		} catch (Exception e) {
			return this;
		}
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
		return !Util.isNull(getHeldValue()) && !Util.isNull(getId());
	}

	@Override
	public IComponent setId(String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getHeldValue() {
		return value;
	}

	@Override
	public IComponent holdValue(String value) {
		this.value = value;
		return this;
	};

	@Override
	public IComponent setCoffeeContext(CoffeeContext context) {
		this.coffeeContext = context;
		return this;
	}

	@Override
	public CoffeeContext getCoffeeContext() {
		return this.coffeeContext;
	}

}
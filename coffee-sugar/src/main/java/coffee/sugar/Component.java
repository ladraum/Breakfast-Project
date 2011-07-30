package coffee.sugar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import coffee.core.CoffeeContext;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;
import coffee.core.util.Util;

public abstract class Component extends AbstractComponent {

	public static String SUGAR_REGISTERED_BLOCKS = CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + "SugarScriptRegisteredBlocks";
	public static String SUGAR_REGISTERED_COMPONENT = CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + "SugarScriptRegisteredComponent";
	public static String SUGAR_REGISTERED_INCLUDES = CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + "SugarScriptRegisteredIncludes";

	private String label;
	private String align;
	private String width;
	private String height;
	private String maxWidth;
	private String maxHeight;
	private String minWidth;
	private String minHeight;
	private String margin;
	private String marginTop;
	private String marginLeft;
	private String marginRight;
	private String marginBottom;
	private String padding;
	private String paddingTop;
	private String paddingLeft;
	private String paddingRight;
	private String paddingBottom;
	private String border;
	private String skin;
	private boolean visible = true;
	private boolean floating;

	public void renderChildren() throws IOException {
		for (IComponent child : getChildren()) {
			child.setCoffeeContext(getCoffeeContext());
			child.render();
		}
	}

	public void addChild(IComponent component, int index) {
		children.add(index, component);
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getRegisteredComponents() {
		Collection<String> registeredComponents = (Collection<String>)coffeeContext.get(SUGAR_REGISTERED_COMPONENT);
		if (registeredComponents == null) {
			registeredComponents = new ArrayList<String>();
			coffeeContext.put(SUGAR_REGISTERED_COMPONENT, registeredComponents);
		}
		return registeredComponents;
	}

	public void registerComponent(String component) {
		this.getRegisteredComponents().add(component);
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getRegisteredBlocks() {
		Collection<String> registeredScriptBlocks = (Collection<String>)coffeeContext.get(SUGAR_REGISTERED_BLOCKS);
		if (registeredScriptBlocks == null) {
			registeredScriptBlocks = new ArrayList<String>();
			coffeeContext.put(SUGAR_REGISTERED_BLOCKS, registeredScriptBlocks);
		}
		return registeredScriptBlocks;
	}

	public void registerScriptBlock(String script) {
		this.getRegisteredBlocks().add(
			new StringBuilder()
					.append("<script>")
					.append(script)
					.append("</script>")
						.toString());
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getRegisteredScriptIncludes() {
		Collection<String> registeredScriptIncludes = (Collection<String>)coffeeContext.get(SUGAR_REGISTERED_INCLUDES);
		if (registeredScriptIncludes == null) {
			registeredScriptIncludes = new ArrayList<String>();
			coffeeContext.put(SUGAR_REGISTERED_INCLUDES, registeredScriptIncludes);
		}
		return registeredScriptIncludes;
	}

	public void registerScriptInclude(String url) {
		this.getRegisteredScriptIncludes().add(url);
	}

	public PrintWriter getWriter() throws IOException {
		return getCoffeeContext().getResponse().getWriter();
	}

	public StringBuilder getStyleDefinition () {
		StringBuilder buffer = new StringBuilder();

		if (!Util.isNull(height))
			buffer.append("height: ").append(height).append(';');
		if (!Util.isNull(width))
			buffer.append("width: ").append(width).append(';');
		if (!Util.isNull(maxHeight))
			buffer.append("max-height: ").append(maxHeight).append(';');
		if (!Util.isNull(maxWidth))
			buffer.append("max-width: ").append(maxWidth).append(';');
		if (!Util.isNull(minHeight))
			buffer.append("min-height: ").append(minHeight).append(';');
		if (!Util.isNull(minWidth))
			buffer.append("min-width: ").append(minWidth).append(';');

		if (!Util.isNull(margin))
			buffer.append("margin: ").append(margin).append(';');
		if (!Util.isNull(marginTop))
			buffer.append("margin-top: ").append(marginTop).append(';');
		if (!Util.isNull(marginBottom))
			buffer.append("margin-bottom: ").append(marginBottom).append(';');
		if (!Util.isNull(marginLeft))
			buffer.append("margin-left: ").append(marginLeft).append(';');
		if (!Util.isNull(marginRight))
			buffer.append("margin-right: ").append(marginRight).append(';');

		if (!Util.isNull(padding))
			buffer.append("padding: ").append(padding).append(';');
		if (!Util.isNull(paddingTop))
			buffer.append("padding-top: ").append(paddingTop).append(';');
		if (!Util.isNull(paddingBottom))
			buffer.append("padding-bottom: ").append(paddingBottom).append(';');
		if (!Util.isNull(paddingLeft))
			buffer.append("padding-left: ").append(paddingLeft).append(';');
		if (!Util.isNull(paddingRight))
			buffer.append("padding-right: ").append(paddingRight).append(';');
		
		if (!Util.isNull(align))
			buffer.append("text-align: ").append(align).append(';');
		if (!Util.isNull(border))
			buffer.append("border: ").append(border).append(';');

		if (isFloating())
			buffer.append("float: left;");
		if (!isVisible())
			buffer.append("display: none;");

		return buffer;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getWidth() {
		return width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeight() {
		return height;
	}

	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	public String getMaxWidth() {
		return maxWidth;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMinWidth(String minWidth) {
		this.minWidth = minWidth;
	}

	public String getMinWidth() {
		return minWidth;
	}

	public void setMinHeight(String minHeight) {
		this.minHeight = minHeight;
	}

	public String getMinHeight() {
		return minHeight;
	}

	public void setFloating(String floating) {
		setFloating(Boolean.parseBoolean(floating));
	}

	public void setFloating(boolean floating) {
		this.floating = floating;
	}

	public boolean isFloating() {
		return floating;
	}

	public String getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(String marginTop) {
		this.marginTop = marginTop;
	}

	public String getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(String marginLeft) {
		this.marginLeft = marginLeft;
	}

	public String getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(String marginRight) {
		this.marginRight = marginRight;
	}

	public String getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(String marginBottom) {
		this.marginBottom = marginBottom;
	}

	public String getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(String paddingTop) {
		this.paddingTop = paddingTop;
	}

	public String getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(String paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public String getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(String paddingRight) {
		this.paddingRight = paddingRight;
	}

	public String getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(String paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public void setPadding(String padding) {
		this.padding = padding;
	}

	public String getPadding() {
		return padding;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getMargin() {
		return margin;
	}

	public void setVisible(String visible) {
		setVisible(Boolean.parseBoolean(visible));
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Boolean isVisible() {
		return visible;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getBorder() {
		return border;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getAlign() {
		return align;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getSkin() {
		if (Util.isNull(skin))
			skin = "";
		return skin;
	}
}

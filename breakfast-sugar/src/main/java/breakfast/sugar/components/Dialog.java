package breakfast.sugar.components;

import breakfast.coffee.util.StringUtil;

public class Dialog extends Panel {

	private boolean closable = true;
	private boolean visible = false;
	private String onclose;

	@Override
	public void configure() {
		setStyleClassNames("Panel DialogPanel");

		registerComponent("new Dialog( {modal:true,id:\""+getId()+"\",label:\""+getLabel()+
								"\", closable:"+isClosable()+",width:\""+getWidth()+
								"\", height:\""+getHeight()+"\",visible:"+isVisible()+"," +
									"onclose:\""+ getOnclose() +"\"} )");
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public Boolean isVisible() {
		return visible;
	}

	public void setClosable(String closable) {
		setClosable(Boolean.parseBoolean(closable));
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public boolean isClosable() {
		return closable;
	}
	
	@Override
	public String getWidth() {
		String width = super.getWidth();
		if (StringUtil.isEmpty(width)) {
			width = "400px";
			super.setWidth(width);
		}
		return width;
	}

	public void setOnclose(String onclose) {
		this.onclose = onclose;
	}

	public String getOnclose() {
		return onclose;
	}

}

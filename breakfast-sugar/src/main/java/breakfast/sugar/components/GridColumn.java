package breakfast.sugar.components;

import java.io.IOException;

import breakfast.sugar.Component;



public class GridColumn extends Component {
	
	private String label;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}

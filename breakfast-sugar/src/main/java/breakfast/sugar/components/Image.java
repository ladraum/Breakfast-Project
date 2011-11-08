package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.binding.CoffeeBinder;
import breakfast.sugar.Component;


public class Image extends Component {
	
	private String click;
	private String location;

	@Override
	public void configure() {
		registerComponent(
			new StringBuilder()
					.append("new Image({location:\"")
						.append(getLocation())
					.append("\"})").toString());
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<img src=\"")
				.append(getLocation())
			.append("\" onclick=\"")
				.append(getClick())
			.append("\" style=\"")
				.append(getStyleDefinition())
			.append("\" />");
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getClick() {
		return click;
	}

	public void setLocation(String location) {
		if (location.matches("^[a-z0-9].*") && !location.contains(":"))
			location = getCoffeeContext().getContextPath() + "/" + location;
		this.location = location;
	}

	public String getLocation() {
		return (String) CoffeeBinder.getValue(location, getCoffeeContext());
	}

}

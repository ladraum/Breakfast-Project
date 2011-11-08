package breakfast.coffee.sample;

import breakfast.coffee.Resource;
import breakfast.coffee.annotation.WebResource;

@WebResource(
	pattern="/hello/*",
	template="templates/hello.xhtml" )

public class HelloWorld extends Resource {

	@Override
	public void configure() {
	}

	@Override
	public void process() {
	}
}

package coffee.sample;

import coffee.core.Resource;
import coffee.core.annotation.WebResource;

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

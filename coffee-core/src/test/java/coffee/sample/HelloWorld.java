package coffee.sample;

import coffee.core.Resource;
import coffee.core.WebResource;

@WebResource(
	uri="/hello/",
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

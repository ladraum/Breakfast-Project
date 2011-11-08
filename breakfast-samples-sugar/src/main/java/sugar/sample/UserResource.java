package sugar.sample;

import breakfast.coffee.XHtmlResource;
import breakfast.coffee.annotation.WebResource;

@WebResource(
		pattern="/user/.*",
		template="templates/User.xhtml" )
public class UserResource extends XHtmlResource {

	private User user;

	@Override
	public void configure() {
		user = new User();
		context.put("user", user);
	}

	@Override
	public void process() {
	}

}

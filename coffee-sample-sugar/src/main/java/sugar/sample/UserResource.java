package sugar.sample;

import coffee.core.XHtmlResource;
import coffee.core.annotation.WebResource;

@WebResource(
		pattern="/usuario/([0-9]+)",
		template="templates/User.xhtml" )
public class UserResource extends XHtmlResource {

	private Usuario user;

	@Override
	public void configure() {
		user = new Usuario();
		context.put("user", user);
	}

	@Override
	public void process() {
		// try {
		// h.newSession
		// u = new UserService ( session )
		// u.createUser ( user )
		// h.commit ()
		// } catch (BusinessErroFail failure) {
		// context.put ("error", failure.getMessage());
		// }
	}

}

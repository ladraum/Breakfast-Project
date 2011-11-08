package breakfast.coffee;

import java.io.IOException;

public abstract class XHtmlResource extends Resource {

	public static final String XHTMLDOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
												"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";
	protected String doctype;

	@Override
	public void configure(CoffeeContext context) throws IOException {
		super.configure(context);
		// FIXME: setContentType("applicationx/xhtml+xml");
		// For more information about XHTML mime types take a look at:
		// http://www.w3.org/TR/xhtml-media-types/#media-types
		context.getResponse().getWriter().append( getDoctype() );
	}

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDoctype() {
        if (this.doctype == null)
            this.doctype = XHTMLDOCTYPE;
        return this.doctype;
    }

}

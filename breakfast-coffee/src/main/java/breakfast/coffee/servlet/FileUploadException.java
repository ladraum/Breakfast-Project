package breakfast.coffee.servlet;

public class FileUploadException extends Exception {

	private static final long serialVersionUID = 3796370909366600044L;

	public static final String MAX_FILE_SIZE_REACHED = "MAX_FILE_SIZE_REACHED";
	
	public FileUploadException() {
		super();
	}
	
	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}
}

package breakfast.milk;

/**
 * <p>Class that extends {@link Exception}.
 * This should be throw when a business logic
 * error occurs or when a validation exception
 * occurs.</br>
 * The current database transaction will be
 * rolled back.</p>
 * 
 * {@link PersistenceException}
 */
public class PersistenceException extends Exception {

	private static final long serialVersionUID = 3785782248769803001L;
	
	/**
	 * <p>Default constructor.
	 * 
	 * @see {@link Exception}</p>
	 */
	public PersistenceException(){
		super();
	}
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified detail message.
	 * 
	 * @see {@link Exception}</p>
	 */
	public PersistenceException(String message){
		super(message);
	}
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified detail message and throwable.
	 * 
	 * @see {@link Exception}</p>
	 */
	public PersistenceException(String message, Throwable throwable){
		super(message, throwable);
	}
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified throwable.
	 * 
	 * @see {@link Exception}</p>
	 */
	public PersistenceException(Throwable throwable){
		super(throwable);
	}
}

package breakfast.milk;

/**
 * <p>Default validator.</br>
 * A validator is an object that, as the name says,
 * validate something. This kind of object will
 * be called before you do a database operation,
 * it could be said that this class acts like a
 * business rule.<br>
 * You can easily add or remove validators to the
 * logic core of your product.</p>
 * 
 * <p><b>Note:</b> Select operations do not invoke
 * this objects.</p>
 * 
 * {@link IValidator}
 *
 * @param <E>
 */
public interface IValidator<E> {

	public String INSERT = "insert";
	public String UPDATE = "update";
	public String CREATE_NEW_VERSION = "createNewVersion";
	public String DELETE = "delete";
	public String PHYSICAL_DELETE = "physicalDelete";
	public String ALL = "all";

	/**
	 * <p>Executes the validator.</p>
	 * 
	 * @param entity
	 * @throws PersistenceException
	 */
	public void validate(E entity) throws PersistenceException;
}

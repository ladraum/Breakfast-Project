package breakfast.milk;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import breakfast.coffee.util.Util;

/**
 * <p>Abstract class that implements all the persistence
 * operation for the product.</br>
 * All methods that call the persistence logic will get
 * validated by the validation chain.</p>
 * 
 * <p>{@link AbstractPersistence}</p>
 *
 * @param <E>
 */
@Stateless
public abstract class AbstractPersistence<E extends AbstractEntity> {

	@PersistenceContext
	protected EntityManager entityManager;
	private Map<String, List<IValidator<E>>> validationChain;

	/**
	 * <p>Add an {@link IValidator} to the validation chain
	 * of the persistence class {@link IEntity}. You also
	 * need to pass which methods the validator should be
	 * called.</p>
	 * 
	 * <p>Ex.: addValidator(new CustomValidator(), IValidator.INSERT);</br>
	 * This validator will be called when the {@code AbstractPersistence.insert()} method of
	 * the persistence class is invoked.</p>
	 * 
	 * <p>Ex.: addValidator(new CustomValidator(), IValidator.INSERT, 
	 * IValidator.UPDATE);</br>
	 * This validator will be called when the {@code AbstractPersistence.insert()} and 
	 * {@code AbstractPersistence.update()} methods of
	 * the persistence class are invoked.</p>
	 * 
	 * @param validator
	 * @param methodsNames
	 */
	public void addValidator(IValidator<E> validator, String... methodsNames) {
		Map<String, List<IValidator<E>>> chain = getValidationChain();
		for (String methodName : methodsNames) {
			List<IValidator<E>> validators = chain.get(methodName);

			if (validators == null)
				validators = new ArrayList<IValidator<E>>();

			validators.add(validator);
			validators.add(new DefaultEntityValidator<E>());
			chain.put(methodName, validators);
		}
	}

	/**
	 * <p>Executes all the validators for the informed method.</p>
	 * 
	 * @param entity
	 * @param methodName
	 * @throws PersistenceException
	 */
	protected void validate(E entity, String methodName)
			throws PersistenceException {
		Map<String, List<IValidator<E>>> chain = getValidationChain();
		List<IValidator<E>> validators = chain.get(methodName);
		
		List<IValidator<E>> list = chain.get(IValidator.ALL);
		if (list != null)
			validators.addAll(list);
		
		if (validators != null)
			for (IValidator<E> validator : validators)
				validator.validate(entity);
	}

	/**
	 * <p>Inserts a new register in the database. All
	 * the validators in the validation chain for the
	 * {@code AbstractPersistence.insert()} method
	 * are called before the database operation.</p>
	 * 
	 * <p>The entity will assume some default values
	 * like, not removed, version number one, and
	 * will be the actual version.</p>
	 * 
	 * @param entity
	 * @return
	 * @throws PersistenceException
	 */
	public E create(E entity) throws PersistenceException {
		try {
			this.validate(entity, IValidator.INSERT);
			getEntityManager().persist(entity);
			return entity;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * <p>Updates a register in the database. All the 
	 * validators in the validation chain for the
	 * {@code AbstractPersistence.update()} method 
	 * are called before the database operation.</p>
	 * 
	 * <p>The entity will assume some default values
	 * like, not removed, actual version number
	 * <b>plus</b> one, and will be the actual version.</p>
	 * 
	 * @param entity
	 * @return
	 * @throws PersistenceException
	 */
	public E update(E entity) throws PersistenceException {
		try{
			this.validate(entity, IValidator.UPDATE);
			getEntityManager().merge(entity);
			return entity;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * <p>Physically removes a register in the database.
	 * All the validators in the validation chain for the
	 * {@code AbstractPersistence.physicalDelete()} 
	 * method are called before the database operation.</p>
	 * 
	 * @param entity
	 * @throws PersistenceException
	 */
	public void delete(E entity) throws PersistenceException {
		try{
			this.validate(entity, IValidator.PHYSICAL_DELETE);
			getEntityManager().remove(entity);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * <p>Search and returns the entity with the
	 * corresponding <b>id</b>.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param id
	 * @return
	 */
	public E retrieveById(Long id) {
		List<E> entityList = this.retrieve(eq("id", id));
		
		if(Util.isNull(entityList) || entityList.size() == 0)
			return null;

		return entityList.get(0);
	}		

	/**
	 * <p>Search and returns a list of all entities.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @return
	 */
	public List<E> retrieve() {
		return retrieve(null, null, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Integer limit, Integer startAt) {
		return retrieve(null, null, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful for making filters.
	 * </p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @return
	 */
	public List<E> retrieve(Criterion... criterions) {
		return retrieve(criterions, null, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful to sort the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @return
	 */
	public List<E> retrieve(Order... orders) {
		return retrieve(null, orders, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful for making filters. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Integer limit) {
		return retrieve (criterions, null, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} and an
	 * array of {@link Order} objects. Useful to 
	 * filter and sort the query. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param orders
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Order[] orders,  Integer limit) {
		return retrieve (criterions, orders, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful to sort the query. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Order[] orders,  Integer limit) {
		return retrieve (null, orders, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful for making filters.</br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Order[] orders,  Integer limit, Integer startAt) {
		return retrieve (null, orders, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful to sort the query.<br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Integer limit, Integer startAt) {
		return retrieve (criterions, null, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} and an
	 * array of {@link Order} objects. Useful to 
	 * filter and sort the query.
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param orders
	 * @param limit
	 * @param startAt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> retrieve(Criterion[] criterions, Order[] orders, Integer limit, Integer startAt) {
		Session session = (Session) getEntityManager().getDelegate();

		Criteria criteria = session.createCriteria(this.getGenericClass());

		if (criterions != null)
			for (Criterion criterion : criterions)
				criteria.add(criterion);

		if (orders != null)
			for (Order order : orders)
				criteria.addOrder(order);

		if (!Util.isNull(limit) && limit > 0)
			criteria.setMaxResults(limit);

		if (!Util.isNull(startAt) && startAt > 0)
			criteria.setFirstResult(startAt);

		return (List<E>) criteria.list();
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion eq(String propertyName, Object value) {
		return Restrictions.eq(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Not Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion neq(String propertyName, Object value) {
		return Restrictions.ne(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Or.
	 * Receives two {@link Criterion} objects.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Criterion or(Criterion first, Criterion second) {
		return Restrictions.or(first, second);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion gt(String propertyName, Object value) {
		return Restrictions.gt(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Lesser Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion lt(String propertyName, Object value) {
		return Restrictions.lt(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than or Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion ge(String propertyName, Object value) {
		return Restrictions.ge(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Lesser Than or Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion le(String propertyName, Object value) {
		return Restrictions.le(propertyName, value);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search anywhere in the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion contains(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.ANYWHERE);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search only at the beginning
	 * of the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion beginsWith(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.START);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search only at the end of
	 * the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion endsWith(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.END);
	}

	/**
	 * <p>Enclosures the {@link Criterion}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param criterion
	 * @return
	 */
	public static Criterion[] where(Criterion... criterion) {
		return criterion;
	}

	/**
	 * <p>See {@code AbstractPersistence.asc()} or 
	 * {@code AbstractPersistence.desc()}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param fieldName
	 * @return
	 */
	public static Order[] orderby(String fieldName) {
		return orderby(asc(fieldName));
	}

	/**
	 * <p>See {@code AbstractPersistence.asc()} or 
	 * {@code AbstractPersistence.desc()}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param fieldName
	 * @return
	 */
	public static Order[] orderby(Order... order) {
		return order;
	}

	/**
	 * <p>Enclosures the {@link Order}. This orders
	 * the query in an ascending order.
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @return
	 */
	public static Order asc(String propertyName) {
		return Order.asc(propertyName);
	}

	/**
	 * <p>Enclosures the {@link Order}. This orders
	 * the query in an descending order.
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @return
	 */
	public static Order desc(String propertyName) {
		return Order.desc(propertyName);
	}

	/**
	 * <p>Retrieves the type of the generic super class.</p>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<E> getGenericClass() {
		return (Class<E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * <p>Replaces the actual validation chain
	 * with a new one.</p>
	 * 
	 * @param validationChain
	 */
	public void setValidationChain(
			Map<String, List<IValidator<E>>> validationChain) {
		this.validationChain = validationChain;
	}

	/**
	 * <p>Returns the actual validation chain.</br>
	 * If the chain is null, a new chain will be
	 * created also calling the {@code registerValidators()}</b>
	 * method from the class.</p>
	 * 
	 * @return
	 */
	public Map<String, List<IValidator<E>>> getValidationChain() {
		if (validationChain == null) {
			validationChain = new HashMap<String, List<IValidator<E>>>();
			registerValidators();
		}
		return validationChain;
	}

	/**
	 * <p>You can use this method to add all the 
	 * {@link IValidator} objects. This method will
	 * be called when the validation chain start or
	 * when you call the {@code getValidationChain()}.</p>
	 */
	public abstract void registerValidators();

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}

package breakfast.milk;

import javax.ejb.EJB;

import breakfast.coffee.binding.EJBManager;

/**
 * <p>Abstract class that automatically injects 
 * the {@link EJB} in the {@link IValidator} classes.</br>
 * This class uses the {@link EJBManager} to inject
 * the <b>EJB</b> dependencies in the annotated 
 * variables.</p> 
 * 
 * <p><b>Note:</b> A pair of <b>getters</b> and
 * <b>setters</b> must be implemented for each
 * annotated variable in the validator.</p>
 * 
 * @param <E>
 * @see EJBManager
 */
public abstract class AbstractValidator<E> implements IValidator<E> {

	public AbstractValidator() {
		EJBManager.manageBean(this);
	}
}

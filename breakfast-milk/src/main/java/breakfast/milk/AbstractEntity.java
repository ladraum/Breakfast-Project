package breakfast.milk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * <p>Abstract class that implements a GLOBAL ID policy
 * for all entities that extends this class.</p>
 * 
 * <p>An database sequence "SEQ_DEFAULT" will be created.</p>
 * 
 * <p>{@link AbstractEntity}</p>
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 4766876679916533274L;
	private Long id;
	
	@Id
	@GenericGenerator(
		name = "SEQ_DEFAULT",
		strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		parameters={
			@Parameter(name="sequence_name", value="SEQ_GLOBAL")})
	@GeneratedValue(generator = "SEQ_DEFAULT")
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = Long.parseLong(id);
	}
}

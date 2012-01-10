package breakfast.coffee;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import breakfast.coffee.util.Reflection;

public class ReflectionTest {

	@Test
	public void retrieveAnnotatedFields() {
		User user = new AnotherUser();
		List<Field> fields = Reflection.extractAnnotatedFieldsFor(Deprecated.class, user);
		assertEquals(2, fields.size());
		assertEquals("active", fields.get(0).getName());
		assertEquals("user", fields.get(1).getName());
	}
	
	public class User {
		@Deprecated
		private String user;

		public void setUser(String user) {
			this.user = user;
		}

		public String getUser() {
			return user;
		}
	}
	
	public class AnotherUser extends User {
		@Deprecated
		private Boolean active;

		public void setActive(Boolean active) {
			this.active = active;
		}

		public Boolean getActive() {
			return active;
		}
	}
}

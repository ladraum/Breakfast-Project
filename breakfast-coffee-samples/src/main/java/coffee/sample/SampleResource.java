package coffee.sample;

import breakfast.coffee.Resource;
import breakfast.coffee.annotation.WebResource;

@WebResource(pattern = "/user/*",
			 template = "templates/user.xhtml")
public class SampleResource extends Resource {

	private User user;

	@Override
	public void configure() {
		user = new User();
		coffeeContext.put("user", user);
	}

	@Override
	public void doGet() throws Exception {
	}

	public class User {

		private String firstName;
		private String lastName;
		private String age;
		private Short gender;
		private String obs;
		private String profileName;
		private Boolean active;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public Short getGender() {
			return gender;
		}

		public void setGender(Short gender) {
			this.gender = gender;
		}

		public String getObs() {
			return obs;
		}

		public void setObs(String obs) {
			this.obs = obs;
		}

		public String getProfileName() {
			return profileName;
		}

		public void setProfileName(String profileName) {
			this.profileName = profileName;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

	}

}

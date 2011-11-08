package breakfast.coffee.sample;

import java.util.List;

public class SampleEntity extends GenericEntity {

	private String name;
	private List<Animal> animals;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	public List<Animal> getAnimals() {
		return animals;
	}
}

package coffee.sample;

import java.util.List;

public class SampleEntity {
	private Integer id;
	private String name;
	private List<String> animals; 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAnimals(List<String> animals) {
		this.animals = animals;
	}

	public List<String> getAnimals() {
		return animals;
	}
}

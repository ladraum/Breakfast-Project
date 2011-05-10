package coffee.stress;

public class RequestData {

	private String location;
	private int elapsedTime;
	private int numberOfRequests;
	
	public RequestData(String url) {
		location= url;
		elapsedTime = 0;
		numberOfRequests = 0;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void increaseNumberOfRequestsDones() {
		this.numberOfRequests++;
	}
	
	public void increaseElapsedTime(int value) {
		this.elapsedTime+= value;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public int getNumberOfRequests() {
		return numberOfRequests;
	}

	public void setNumberOfRequests(int numberOfRequests) {
		this.numberOfRequests = numberOfRequests;
	}

}

import java.util.Scanner;

public class Drone {
	Scanner sc = new Scanner(System.in);
	private String Location = new String(); // 현재 위치
	private String destination = new String(); // 도착 위치
	// Calculation calculator = new Calculator();
	LimitedArea limitarea = new LimitedArea();

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void Search() {
		// this.calculator(this.getLocation(), this.getDestination());
	}

}

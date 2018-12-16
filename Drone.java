import java.util.Scanner;

public class Drone {
	Scanner sc = new Scanner(System.in);
	private String Location = new String(); // 현재 위치
	private String destination = new String(); // 도착 위치
	// Calculation calculator = new Calculator();
	LimitedArea limitarea = new LimitedArea();
	long startTime = 0;
	long arrivalTime = 0;

	public String getLocation() { // 현재 위치 접근자
		return Location;
	}

	public void setLocation(String location) { // 현재 위치 설정자
		Location = location;
	}

	public String getDestination() { // 도착 위치 접근자
		return destination;
	}

	public void setDestination(String destination) { // 도착 위치 설정자
		this.destination = destination;
	}

	public void Search(String start, String destination) { // 최단거리 탐색 메소드
		// this.calculator(this.getLocation(), this.getDestination());
	}



}

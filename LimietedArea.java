import java.io.*;
import java.util.Scanner;

public class LimitedArea {
	Scanner sc = new Scanner(System.in);
	String location; // 주소
	private double longitude = 0.0; // 경도
	private double latitude = 0.0; // 위도
	private double radius = 0.0;
	private static int size = 10;
	private int used = 0;
	Map map = new Map();
	LimitedArea[] limitarea = new LimitedArea[size];

	public void LoadLimitedArea() {
		try {
			File file = new File("LimitedArea.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
			String line = br.readLine();
			String[] line_split;
			int index = 0;
			for (int i = 0; i < size; ++i)
				limitarea[i] = new LimitedArea();

			while (line != null) {
				if (used == size)
					break;

				line = line.toLowerCase();
				line_split = line.split(",");
				limitarea[index].setLocation(line_split[0]);
				limitarea[index].setLongitude(this.map.get_longitude(limitarea[index].getLocation()));
				limitarea[index].setLatitude(this.map.get_latitude(limitarea[index].getLocation()));
				limitarea[index].setRadius(Double.parseDouble(line_split[1]));
				++index;

				line = br.readLine();
			}

			used = index;
		} catch (IOException e) {
			System.out.println("예외 처리");
		}
	}

	public double getLongitude() { // 경도 접근자
		return longitude;
	}

	public void setLongitude(double longitude) { // 경도 설정자
		this.longitude = longitude;
	}

	public double getLatitude() { // 위도 접근자
		return latitude;
	}

	public void setLatitude(double latitude) { // 위도 설정자
		this.latitude = latitude;
	}

	public double getRadius() { // 반지름 접근자
		return radius;
	}

	public void setRadius(double radius) { // 반지름 설정자
		this.radius = radius;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean AddLimitedArea(String limitLocation, double radius) {
		if (used == size)
			return false;

		for (int i = 0; i < used; ++i) {
			if (limitLocation.equals(limitarea[i].getLocation())) // 이미 지정된 장소인 경우
				return false;
		}

		limitarea[used].setLocation(limitLocation);
		limitarea[used].setLongitude(this.map.get_longitude(limitLocation));
		limitarea[used].setLatitude(this.map.get_latitude(limitLocation));
		limitarea[used].setRadius(radius);
		++used;
		return true;
	}

	public boolean DeleteLimitedArea() {
		int deleteNumber = 0;
		File file = new File("LimitedArea.txt");
		while (true) {
			this.PrintLimitedArea();
			System.out.println("> 삭제할 비행 금지구역을 선택하세요 : ");
			deleteNumber = sc.nextInt();
			sc.nextLine();
			if (0 < deleteNumber && deleteNumber < used + 1)
				break;

			System.out.println("@ [안내] 잘못입력했습니다.");
		}

		System.arraycopy(limitarea, deleteNumber, limitarea, deleteNumber - 1, (size - deleteNumber - 1));
		limitarea[size-1].setLocation(null);
		limitarea[size-1].setLongitude(0.0);
		limitarea[size-1].setLatitude(0.0);
		limitarea[size-1].setRadius(0.0);
		--used;
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "euc-kr"));
			for (int i = 0; i < size; ++i) {
				if (limitarea[i].getLocation() != null) {
					bw.write(limitarea[i].getLocation() + "," + Double.toString(limitarea[i].getRadius()));
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("예외 처리");
		}

		return true;
	}

	public void PrintLimitedArea() {
		this.LoadLimitedArea();
		for (int i = 0; i < this.size; ++i) {
			System.out.print("[" + (i + 1) + "]" + " 주소 : " + limitarea[i].getLocation() + ", 경도 : "
					+ limitarea[i].getLongitude());
			System.out.println(", 위도 : " + limitarea[i].getLatitude() + ", 반지름 : " + limitarea[i].getRadius() + "(m)");
		}
		System.out.println("");
	}

}

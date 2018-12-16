import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LimitedArea {
	Scanner sc = new Scanner(System.in);
	String location; // 주소
	private double longitude = 0.0; // 경도
	private double latitude = 0.0; // 위도
	private double radius = 0.0; // 비행금지구역 반지름(단위:m)
	private static int size = 10; // 비행금지구역 최대개수
	private int used = 0; //
	Map map = new Map();
	LimitedArea[] limitarea = new LimitedArea[size];

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

	public String getLocation() { // 장소 접근자
		return location;
	}

	public void setLocation(String location) { // 장소 설정자
		this.location = location;
	}

	// 드론 비행 금지구역 추가 메소드
	public boolean AddLimitedArea(String limitLocation, double radius) throws SQLException {
		if (used == size) { // 비행 금지구역 목록이 꽉 찬 경우
			System.out.println("@ [안내] 목록이 이미 꽉 차서 더 이상 비행 금지구역을 추가할 수 없습니다.");
			return false;
		}
		// DB 연결 부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB 내부 테이블 연결 부분
		ResultSet result = stat.executeQuery("select * from LimitedArea;");
		while (result.next()) {
			// 이미 추가된 금지구역인지 확인
			if (limitLocation.equals(result.getString("location"))) {
				// 이미 추가된 금지구역인 경우 비행 금지구역 추가 실패
				System.out.println("@ [안내] 이미 금지구역 목록에 있는 장소입니다.");
				result.close();
				connect.close();
				return false;
			}
		}
		double longitude = this.map.get_longitude(limitLocation); // 경도 반환 받음
		double latitude = this.map.get_longitude(limitLocation); // 위도 반환 받음
		stat.executeUpdate("insert into LimitedArea(location,longitude,latitude,radius) " + "values('" + limitLocation
				+ "'," + longitude + "," + latitude + ", " + radius + ")"); // DB에 비행 금지구역 추가
		System.out.println("> 금지구역 목록 업데이트를 성공하였습니다!!"); // 성공 출력문
		++used; // 비행 금지구역 목록 개수 증가
		result.close();
		connect.close();
		//DB연결 종료
		return true;
	}

	// 비행 금지구역 삭제 메소드
	public boolean DeleteLimitedArea() throws SQLException {
		// 비행 금지구역 목록 출력후 주소를 입력받아 삭제하는 방식으로 실행한다
		this.PrintLimitedArea(); // 비행 금지구역 목록 출력
		// DB 연결 부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB 내부 테이블 연결 부분
		ResultSet result = stat.executeQuery("select * from LimitedArea;");
		System.out.print("> 삭제할 비행 금지구역의 주소를 입력하세요 (작업을 취소하고 싶으시면 '-1'을 입력해주세요) : ");
		String deleteLocation = sc.nextLine();
		if(deleteLocation.equals("-1")) // '-1' 입력시 비행 금지구역 삭제 ㅈ취소
			return false;
		stat.executeUpdate("delete from LimitedArea where (location=='" + deleteLocation + "')");
		// DB에서 해당 주소에 대한 비행 금지구역 삭제
		result.close();
		connect.close();
		// DB 연결 종료
		return true;
	}

	public void PrintLimitedArea() throws SQLException {
		// DB 연결 부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB 내부 테이블 연결 부분
		ResultSet result = stat.executeQuery("select * from LimitedArea;");

		// 비행 금지구역 목록을 출력한다(10개까지 출력 // 없는 경우에는 null과 0.0을 대신 출력)
		for (int i = 1; i < 11; ++i) {
			if (result.next()) {
				// 비행 금지구역 목록을  출력
				System.out.println("[" + i + "] 주소 : " + result.getString("location") + ", 경도 : "
						+ result.getString("longitude") + ", 위도 : " + result.getString("latitude") + ", 반지름 : "
						+ result.getString("radius") + "(m)");
			} else {
				// 비행 금지구역이 더 이상 없는 경우 해당 출력문을 출력
				System.out.println(
						"[" + i + "] 주소 : " + null + ", 경도 : " + 0.0 + ", 위도 : " + 0.0 + ", 반지름 : " + 0.0 + "(m)");
			}
		}
		System.out.println(""); // 줄바꿈

		result.close();
		connect.close();
		// DB 연결 종료
	}

}

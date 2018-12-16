import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
	String name; // 이름
	String id; // ID
	String password; // Password
	int authorization; // 권한
	Drone drone = new Drone(); // 사용자마다 가지고 있는 드론

	// 로그인 실행 메소드
	public int login(String id, String pw) throws SQLException {
		// DB 연결부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB내의 테이블 선택
		ResultSet result = stat.executeQuery("select * from information;");

		// 입력 ID와 PW를 DB에서 탐색하여 일치시 로그인을 일치하지 않을시 로그인 실패
		while (result.next()) {
			if (id.equals(result.getString("id")) && pw.equals(result.getString("password"))) {
				System.out.println("로그인 완료되었습니다.");
				int authorization = Integer.parseInt(result.getString("rights"));
				result.close();
				connect.close();
				return authorization;
			}
		}
		result.close();
		connect.close();
		// DB연결 종료부분
		return -1;
	}

	// 회원가입 메소드
	public boolean SignUp() throws SQLException {
		// 회원가입시 항상 관리자권한이 아닌 일반권한으로만 가입된다

		// DB 연결부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB 내부에서의 테이블 연결부분
		ResultSet result = stat.executeQuery("select * from information;");

		Scanner sc = new Scanner(System.in);
		String name = new String(); // 이름
		String id = new String(); // ID
		String pw = new String(); // Password
		int cmd = 0; // 명령 변수
		try {
			while (true) { // 계정 생성
				System.out.println("> 계정을 생성하시겠습니까?? [-1] : 취소 , [1] : 생성");
				cmd = sc.nextInt();
				sc.nextLine();
				if (cmd == 1 || cmd == -1)
					break;

				System.out.println("@ [안내] 잘못된 입력입니다!");
			}

			if (cmd == 1) {
				// 계정 생성 진행
				System.out.println("> 이름을 입력하세요 : ");
				name = sc.nextLine();
				System.out.println("> 생성할 ID를 입력하세요 : ");
				id = sc.nextLine();
				System.out.println("> 설정할 Password를 입력하세요 : ");
				pw = sc.nextLine();
				while (result.next()) {
					if (id.equals(result.getString("id"))) { // ID 중복으로 인한 생성 실패
						System.out.println("@ [안내] 사용할 수 없는 ID입니다.");
						return false;
					}
				}
			} else
				return true; // 계정 생성 취소

			stat.executeUpdate("insert into information(name,ID,password,rights) values('" + name + "','" + id + "','"
					+ pw + "', '2')");
			// 계정 생성 완료 (DB에 회원정보 추가)
			System.out.println("> 계정 생성 완료!!");

			result.close();
			connect.close();
			// DB연결 종료
		} catch (InputMismatchException e) {
			// 잘못된 입력을 받았을 시의 예외처리
			System.out.println("[Error] 잘못된 입력입니다. 계정생성을 취소합니다.");
			sc.nextLine(); // 버퍼 비움
		}
		return true;
	}

	// 작업 실행 결정 메소드(관리자 권한으로 작업을 실행할 것인지 일반권한으로 실행할 것인지 결정후 작업 실행)
	public void work(int authorization) throws SQLException {
		if (authorization == 1) // 관리자 권한 실행
			this.AdministratorWork();
		else // 일반 권한 실행
			this.NormalUserWork();
	}

	private void AdministratorWork() throws SQLException { // 관리자 권한으로 작업 실행
		Scanner sc = new Scanner(System.in);
		// 명령, 주소, 반지름은 입력을 받기위한 변수
		int cmd = 0; // 명령 입력 받는 변수
		String setAddress = new String(); // 드론 현재 주소 입력 받는 변수
		String destinationAddress = new String(); // 드론 도착 장소 주소 입력 받는 변수
		String limitArea = new String(); // 비행 제한구역 주소 입력 받는 변수
		double radius = 0.0; // 반지름 입력 받는 변숨

		// 작업 실행('-1'입력 전까지 계속 반복 실행)
		while (true) {
			System.out.print("[1] : 드론 위치 설정 , [2] : 최단거리 조회 , [3] : 드론 비행 금지구역 목록 조회, [4] : 드론 비행 금지구역 설정 , ");
			System.out.println("[5] : 회원 목록 조회 , [-1] 로그아웃");
			System.out.print("> 수행할 작업을 선택하세요 : ");
			cmd = sc.nextInt();
			sc.nextLine();

			switch (cmd) {
			case 1:
				// 드론 현재 위치 설정 메소드 실행
				System.out.println("===== 드론 위치를 설정합니다. =====");
				System.out.print("> 현재 위치의 주소를 입력하세요 : ");
				setAddress = sc.nextLine();
				this.drone.setLocation(setAddress);
				break;

			case 2:
				// 드론 도착지 입력후 최단거리, 소요시간 등을 안내받는 메소드 실행
				if (this.drone.getLocation() == null) {
					System.out.println("@ [안내] 드론 현재 위치부터 설정하세요!");
					break;
				}
				System.out.println("===== 현재 위치로부터 목적지까지 최단거리와 소요 시간을 탐색합니다. =====");
				System.out.print("> 목적지 주소를 입력하세요 : ");
				destinationAddress = sc.nextLine();
				this.drone.setDestination(destinationAddress); // 도착 주소 설정
				this.drone.Search(this.drone.getLocation(), this.drone.getDestination()); // 최단거리, 소요시간 안내 메소드 실행
				break;
			case 3:
				// 비행 금지구역 목록 출력 메소드
				this.drone.limitarea.PrintLimitedArea();
				break;
			case 4:
				// 비행 금지구역 설정 메소드 실행
				System.out.println("===== 드론 비행 금지구역을 설정합니다. =====");
				while (true) {
					System.out.println("[1] : 추가 , [2] : 삭제 , [-1] : 취소");
					System.out.print("> 수행할 작업을 선택하세요 : ");
					cmd = sc.nextInt();
					sc.nextLine();
					if (cmd == -1 || cmd == 1 || cmd == 2)
						break;
					System.out.println("@ [안내] 잘못된입력입니다! 다시 입력하세요.");
				}
				if (cmd == 1) {
					// 드론 비행 금지구역 추가
					System.out.print("> 비행 금지구역으로 추가할 장소에 대한 주소를 입력하세요(작업 취소를 원할시 '-1'을 입력하세요)  : ");
					limitArea = sc.nextLine();
					if (limitArea.equals("-1")) // 실행 취소
						break;

					while (true) {
						// 비행 금지구역 추가를 위한 반지름 입력
						System.out.print("> 지정할 범위의 반지름을 입력하세요  : ");
						radius = sc.nextDouble();
						sc.nextLine();
						if (radius < 0.0) {
							// 반지름 입력 오류 알림
							System.out.println("[Error] 반지름이 잘못 입력되었습니다.");
						} else {
							boolean addState = this.drone.limitarea.AddLimitedArea(limitArea, radius);
							if (addState) // 정상 추가
								break;
							else {
								// 금지구역 추가 입력주소가 이미 목록에 있는 경우 실패를 알림
								System.out.println("[Error] 추가에 실패하였습니다. 이미 설정된 금지구역입니다.");
								break;
							}
						}

					}
				} else if (cmd == 2)
					// 드론 비행 금지구역 삭제 메소드 실행
					this.drone.limitarea.DeleteLimitedArea();
				else // 비행 금지구역 설정 메소드 실행 취소
					break;

				break;
			case 5:
				// 회원 목록 조회 메소드 실행
				this.UserList();
				break;
			case -1:
				// 로그아웃
				return;
			default:
				// 잘못 입력된 경우
				break;
			}
		}
	}

	// 회원 목록 조회 메소드
	private void UserList() throws SQLException {
		System.out.println("@ [안내] 권한 1 : 관리자 , 권한 2 : 일반사용자");
		// DB 연결부분
		Connection connect = DriverManager.getConnection("jdbc:sqlite:information.db");
		Statement stat = connect.createStatement();
		// DB 내부 테이블 연결 부분
		ResultSet result = stat.executeQuery("select * from information;");

		int i = 1; // 출력문을 위한 변수
		while (result.next()) { // 출력문 변수
			System.out
					.print("[ " + i + " ] " + " 이름 : " + result.getString("name") + ", ID : " + result.getString("ID"));
			System.out.println(", Password : " + result.getString("password") + ", 권한 : " + result.getString("rights"));
			++i;
		}
		result.close();
		connect.close();
		// DB 연결 종료
		System.out.println(""); // 줄 바꿈
	}

	// 관리자 권한이 아닌 상태로 일반작업을 실행하는 메소드
	private void NormalUserWork() throws SQLException {
		Scanner sc = new Scanner(System.in);
		int cmd = 0; // 명령어  변수
		String setAddress = new String(); //
		String destinationAddress = new String();

		while (true) {
			// 수행할 작업 입력 받음
			System.out.println("[1] : 드론 위치 설정 , [2] : 최단거리 조회 , [3] : 드론 비행 금지구역 목록 조회 , [-1] : 로그아웃 ");
			System.out.println("> 수행할 작업을 선택하세요 : ");
			cmd = sc.nextInt();
			sc.nextLine();

			switch (cmd) {
			case 1:
				// 드론 위치 설정 메소드 실행
				System.out.println("===== 드론 위치를 설정합니다. =====");
				System.out.print("> 현재 위치의 주소를 입력하세요 : ");
				setAddress = sc.nextLine();
				this.drone.setLocation(setAddress);
				break;

			case 2:
				// 드론 내비게이션 실행
				System.out.println("===== 현재 위치로부터 목적지까지 최단거리와 소요 시간을 탐색합니다. =====");
				System.out.print("> 목적지 주소를 입력하세요 : ");
				destinationAddress = sc.nextLine();
				this.drone.setDestination(destinationAddress);
				// this.drone.Search();
				break;
			case 3:
				// 비행 금지구역 목록 조회
				this.drone.limitarea.PrintLimitedArea();
				break;
			case -1:
				// 로그아웃
				return;
			default:
				// 잘못 입력된 경우
				break;
			}
		}
	}
}

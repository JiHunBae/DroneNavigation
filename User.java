import java.io.*;
import java.util.Scanner;

public class User {
	String name;
	String id;
	String password;
	int authorization;
	Drone drone = new Drone();
	private final static int size = 10;
	User[] user = new User[size];

	public int login(String id, String pw) {
		this.LoadUserInformation();

		for (int i = 0; user[i].id != null && i < this.size; i++) {
			if (id.equals(user[i].id) && pw.equals(user[i].password)) {
				System.out.println("로그인 완료되었습니다.");
				return user[i].authorization; // 권한 반환
			}
		}
		System.out.println("로그인에 실패하였습니다.");
		return -1;
	}

	private void LoadUserInformation() {
		try {
			File file = new File("User.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
			String line = br.readLine();
			line = line.toLowerCase();
			String[] line_split;
			int index = 0;
			int temp_authorization = 0;

			for (int a = 0; a < size; a++)
				user[a] = new User();

			while (line != null) {
				line_split = line.split(",");
				user[index].name = line_split[0];
				user[index].id = line_split[1];
				user[index].password = line_split[2];
				user[index].authorization = Integer.parseInt(line_split[3]);
				line = br.readLine();
				++index;
			}
		} catch (IOException e) {
			System.out.println("예외 처리");
		}
	}

	/*
	 * public boolean SignUp() { this.LoadUserInformation();
	 *
	 * return true; }
	 */

	public void work(int authorization) { // 관리자 권한으로 작업을 실행할 것인지 일반권한으로 실행할 것인지 결정후 작업 실행
		if (authorization == 1)
			this.AdministratorWork();
		else
			this.NormalUserWork();
	}

	private void AdministratorWork() { // 관리자 권한으로 작업 실행
		this.drone.limitarea.LoadLimitedArea();
		Scanner sc = new Scanner(System.in);
		int cmd = 0;
		String setAddress = new String();
		String destinationAddress = new String();
		String limitArea = new String();
		double radius = 0.0;

		while (true) {
			System.out.print("[1] : 드론 위치 설정 , [2] : 최단거리 조회 , [3] : 드론 비행 금지구역 목록 조회, [4] : 드론 비행 금지구역 설정 , ");
			System.out.println("[5] : 회원 목록 조회 , [-1] 로그아웃");
			System.out.print("> 수행할 작업을 선택하세요 : ");
			cmd = sc.nextInt();
			sc.nextLine();

			switch (cmd) {
			case 1:
				System.out.println("===== 드론 위치를 설정합니다. =====");
				System.out.print("> 현재 위치의 주소를 입력하세요 : ");
				setAddress = sc.nextLine();
				this.drone.setLocation(setAddress);
				break;

			case 2:
				if(this.drone.getLocation() == null) {
					System.out.println("@ [안내] 드론 현재 위치부터 설정하세요!");
					break;
				}
				System.out.println("===== 현재 위치로부터 목적지까지 최단거리와 소요 시간을 탐색합니다. =====");
				System.out.print("> 목적지 주소를 입력하세요 : ");
				destinationAddress = sc.nextLine();
				this.drone.setDestination(destinationAddress);
				this.drone.Search();
				break;
			case 3:
				this.drone.limitarea.PrintLimitedArea();
				break;
			case 4:
				System.out.println("===== 드론 비행 금지구역을 설정합니다. =====");
				while (true) {
					System.out.println("[1] : 추가 , [2] : 삭제");
					cmd = sc.nextInt();
					sc.nextLine();
					if (cmd == -1 || cmd == 1 || cmd == 2)
						break;
					System.out.println("@ [안내] 잘못된입력입니다! 다시 입력하세요.");
				}
				if (cmd == 1) {
					System.out.print("> 비행 금지구역으로 추가할 장소에 대한 주소를 입력하세요 : ");
					limitArea = sc.nextLine();
					while (true) {
						System.out.print("> 지정할 범위의 반지름을 입력하세요 : ");
						radius = sc.nextDouble();
						sc.nextLine();
						boolean addState = this.drone.limitarea.AddLimitedArea(limitArea, radius);

						if (radius > 0.0 && addState)
							break;

						System.out.println("[Error] 추가에 실패하였습니다. 반지름 또는 이미 설정된 금지구역이 아닌지 확인하세요!");
					}
				} else if (cmd == 2)
					this.drone.limitarea.DeleteLimitedArea();
				else
					break;

				break;
			case 5:
				this.LoadUserInformation();
				this.UserList();
				break;
			case -1:
				return;
			default:
				break;
			}
		}
	}

	private void UserList() {
		System.out.println("@ [안내] 권한 1 : 관리자 , 권한 2 : 일반사용자");
		for (int i = 0; user[i].id != null; ++i) {
			System.out.print("[" + (i + 1) + "]" + " Name : " + user[i].name + ", ID : " + user[i].id);
			System.out.println(", PW : " + user[i].password + ", 권한 : " + user[i].authorization);
		}
		System.out.println("");
	}

	private void NormalUserWork() {
		Scanner sc = new Scanner(System.in);
		int cmd = 0;
		String setAddress = new String();
		String destinationAddress = new String();

		while (true) {
			System.out.println("[1] : 드론 위치 설정 , [2] : 최단거리 조회 , [3] : 드론 비행 금지구역 목록 조회 , [-1] : 로그아웃 ");
			System.out.println("> 수행할 작업을 선택하세요 : ");
			cmd = sc.nextInt();
			sc.nextLine();

			switch (cmd) {
			case 1:
				System.out.println("===== 드론 위치를 설정합니다. =====");
				System.out.print("> 현재 위치의 주소를 입력하세요 : ");
				setAddress = sc.nextLine();
				// this.drone.setLocation(setAddress);
				break;

			case 2:
				System.out.println("===== 현재 위치로부터 목적지까지 최단거리와 소요 시간을 탐색합니다. =====");
				System.out.print("> 목적지 주소를 입력하세요 : ");
				destinationAddress = sc.nextLine();
				// this.drone.setDestination(destinationAddress);
				// this.drone.Search();
				break;
			case 3:
				this.drone.limitarea.PrintLimitedArea();
				break;
			case -1:
				return;
			default:
				break;
			}
		}
	}
}

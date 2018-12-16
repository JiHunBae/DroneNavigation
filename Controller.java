import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner 생성
		int cmd = 0; // 명령을 받을 변수
		String id = new String(); // Login ID
		String pw = new String(); // Login Password
		User user = new User(); // User 객체 생성
		int authorization = 0; // 권한

		while (true) {
			while (true) {
				try {
					System.out.println("[1] : 로그인 , [2] : 회원가입 , [-1] : 종료"); // 작업 선택 출력문
					System.out.print("> 수행할 작업을 선택하세요  : "); // 작업 선택 입력 받음
					int end = 0; // 기능 수행 도중 수행 취소시를 위한 변수
					cmd = sc.nextInt(); // 명령을 저장
					sc.nextLine(); // Buffer 비우기
					switch (cmd) { // 명령에 따른 실행 분기
					case 1:
						/*
						 * 로그인을 실행하는 부분 사용자에게 ID와 Password를 입력받고, 입력받은 ID와 Password에 따라 로그인 된다. 로그인시 각
						 * ID(사용자)마다 Authorization(권한)이 있는데, 권한에 따라 작업할 수 있는 메뉴가 달라진다. 관리자(권한 == 1)는
						 * 사용자의 기능 외에 추가적인 기능 사용 가능.
						 */
						while (authorization < 1) {
							System.out.println("===== 로그인을 시작합니다.(로그인 취소를 원할시 입력칸에 '-1'을 입력하세요.) =====");
							System.out.print("> ID를 입력하세요  : "); // ID입력 요청문
							id = sc.nextLine();
							if(id.equals("-1")) { // 로그인 취소
								end = 1; // 로그인 취소 상태로 세팅
								break;
							}
							System.out.print("> Password를 입력하세요  : "); // Password입력 요청문
							pw = sc.nextLine();
							if(pw.equals("-1")) { // 로그인 취소
								end = 1; // 로그인 취소 상태로 세팅
								break;
							}
							authorization = user.login(id, pw); // 입력받은 ID와 Password를 사용자 목록의 정보와 비교해 로그인 시도

							if (authorization == -1) // 로그인 실패
								System.out.println("> ID와 PW를 다시 입력하세요.");
						}
						if(end == 1) { // 로그인 취소를 처리
							end = 0; // 로그인 취소 변수 정상 세팅
							break;
						}
						user.work(authorization); // 로그인 성공시 권한에 따른 작업 수행
						authorization = 0; // 후에 로그아웃시 권한을 초기화시켜준다.(권한 초기화 하지 않을 시, 오류가 생길 수 있으므로 오류를 방지한다.)
						break;
					case 2:
						// 회원가입을 실행한다.
						user.SignUp();
						break;
					case -1:
						// 시스템을 종료
						System.out.println("@ [안내] 시스템을 종료합니다.");
						return;
					default:
						// 목록 이외의 값 입력시 Error문구 출력
						System.out.println("[Error] 잘못된 입력입니다.");
						break;
					}
				} catch (InputMismatchException e) {// 잘못된 입력시 예외 처리
					System.out.println("[Error] 잘못된 입력입니다.");
					sc.nextLine(); // Buffer 비우기
					break;
				}

			}

		}
	}
}
